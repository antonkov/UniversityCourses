import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by antonkov on 31.10.14.
 */
public class FileShareServer implements Runnable {
    private ShareManager share;
    private final int port = 7777;

    public FileShareServer(ShareManager share) {
        this.share = share;
    }

    class EventHandler implements Runnable {
        Socket socket;

        EventHandler(Socket socket) {
            this.socket = socket;
        }

        void sendList(OutputStream os) throws IOException {
            ArrayList<ShareManager.FileInfo> files = share.getFiles();
            Utils.writeInt(os, files.size());
            for (ShareManager.FileInfo info : files) {
                os.write(info.md5);
                os.write((info.filename + '\000').getBytes());
            }
        }

        void sendFile(OutputStream os, String filename) throws IOException {
            byte[] bytes = share.readFile(filename);
            Utils.writeLong(os, (long)bytes.length);
            os.write(share.getMD5ByFilename(filename));
            os.write(bytes);
        }

        void writeFile(OutputStream os, String filename, byte[] fileBuffer) {
            //TODO:
        }

        @Override
        public void run() {
            try {
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                int commandCode = input.read();
                switch (commandCode) {
                    case Utils.LIST_REQUEST: {
                        output.write(Utils.typeBytes(Utils.LIST_RESULT));
                        sendList(output);
                        break;
                    }
                    case Utils.GET_REQUEST: {
                        String filename = Utils.readNullTermString(input);
                        output.write(Utils.typeBytes(Utils.GET_RESULT));
                        sendFile(output, filename);
                        break;
                    }
                    case Utils.PUT_REQUEST: {
                        String filename = Utils.readNullTermString(input);
                        long fileSize = Utils.readLong(input);
                        byte[] fileBuffer = new byte[(int) fileSize];
                        input.read(fileBuffer);
                        share.writeFile(filename, fileBuffer);
                        break;
                    }
                    case Utils.LIST_RESULT: {
                        int countFiles = Utils.readInt(input);
                        for (int i = 0; i < countFiles; ++i) {
                            System.out.println(Utils.getMD5FromStream(input) + " " + Utils.readNullTermString(input));
                        }
                        break;
                    }
                    case Utils.ERROR: {
                        int errorCode = input.read();
                        switch (errorCode) {
                            case Utils.FILE_NOT_FOUND:
                                System.err.println("File not found");
                                break;
                            case Utils.TOO_MANY_CONNECTIONS:
                                System.err.println("Too many connections");
                                break;
                            case Utils.MALFORMED_MESSAGE:
                                System.err.println("Malformed message");
                                break;
                            case Utils.INTERNAL_SERVER_ERROR:
                                System.err.println("Internal server error");
                                break;
                        }
                        break;
                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server is started");
            while (true) {
                new Thread(new EventHandler(server.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
