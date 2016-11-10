import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by antonkov on 31.10.14.
 */
public class FileShareClient implements Runnable {
    private ShareManager share;
    private int port = 7777;

    public FileShareClient(ShareManager share) {
        this.share = share;
    }

    byte[] ipFromString(String s) {
        String[] numbers = s.split(".");
        byte[] res = new byte[numbers.length];
        for (int i = 0; i < res.length; ++i)
            res[i] = Byte.parseByte(numbers[i]);
        return res;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        try {
            while (true) {
                String s = in.nextLine();
                String[] args = s.split(" ");
                String command = args[0];
                if (command.equals("list")) {
                    String ip = args[1];
                    Socket socket = new Socket(ip, port);
                    OutputStream os = socket.getOutputStream();
                    os.write(Utils.typeBytes(Utils.LIST_REQUEST));
                    InputStream is = socket.getInputStream();
                    int commandResultCode = is.read();
                    if (commandResultCode == Utils.LIST_RESULT) {
                        int countFiles = Utils.readInt(is);
                        for (int i = 0; i < countFiles; ++i) {
                            //TODO: minus before md5
                            System.out.println(Utils.MD5ToString(Utils.getMD5FromStream(is)) + " " + Utils.readNullTermString(is));
                        }
                    }
                    socket.close();
                } else if (command.equals("get")) {
                    String ip = args[1];
                    String filename = args[2];
                    Socket socket = new Socket(ip, port);
                    OutputStream os = socket.getOutputStream();
                    os.write(Utils.typeBytes(Utils.GET_REQUEST));
                    os.write((filename + '\000').getBytes());
                    InputStream is = socket.getInputStream();
                    int commandResultCode = is.read();
                    if (commandResultCode == Utils.GET_RESULT) {
                        long fileSize = Utils.readLong(is);
                        byte[] md5 = Utils.getMD5FromStream(is);
                        byte[] fileBuffer = new byte[(int) fileSize];
                        is.read(fileBuffer);
                        share.writeFile(filename, fileBuffer);
                    }
                    socket.close();
                } else if (command.equals("put")) {
                    String ip = args[1];
                    String filename = args[2];
                    Socket socket = new Socket(ip, port);
                    OutputStream os = socket.getOutputStream();
                    os.write(Utils.typeBytes(Utils.PUT_REQUEST));
                    byte[] fileBytes = share.readFile(filename);
                    os.write((filename + '\000').getBytes());
                    Utils.writeLong(os, fileBytes.length);
                    os.write(fileBytes);
                    socket.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
