import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by antonkov on 26.03.15.
 */
public class Utils {
    static byte[] getIpAddress() {
        try {
            System.out.println("Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            for (; n.hasMoreElements();)
            {
                NetworkInterface e = n.nextElement();
                System.out.println("Interface: " + e.getName());
                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements();)
                {
                    InetAddress addr = a.nextElement();
                    System.out.println("  " + addr.getHostAddress() + " " + Arrays.toString(addr.getAddress()));
                    if (e.getName().equals("wlan0"))
                        if (addr instanceof Inet4Address) {
                            Inet4Address cur = (Inet4Address) addr;
                            return cur.getAddress();
                        }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String ipToString(byte[] ip) {
        try {
            return InetAddress.getByAddress(ip).toString().split("/")[1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    static int sha1(byte[] bytes) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(bytes);
            byte[] sha1hash = md.digest();
            final int countBytes = 4;
            byte[] res = new byte[countBytes];
            for (int i = 0; i < countBytes; i++)
                res[i] = sha1hash[sha1hash.length - countBytes + i];
            return ByteBuffer.wrap(res).getInt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int sha1(InetAddress addr) {
        return sha1(addr.getAddress());
    }

    static int sha1(String s) {
        try {
            return sha1(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public interface PacketGenerator {
        void gen(DataOutputStream dos) throws IOException;
    }

    public interface PacketReader {
        void read(DataInputStream dis) throws IOException;
    }

    static DatagramPacket genPacket(PacketGenerator pg, InetAddress addr) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        pg.gen(dos);
        dos.flush();
        byte[] bytes = bos.toByteArray();
        DatagramPacket packet = new DatagramPacket(bytes,
                bytes.length, addr, Settings.PORT);
        return packet;
    }

    public interface IOCommands {
        void go(DataInputStream in, DataOutputStream out) throws IOException;
    }

    static void socketIO(IOCommands ioCommands, InetAddress addr, int port) throws IOException {
        Socket socket = new Socket(addr, Settings.PORT);
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        DataInputStream dis = new DataInputStream(in);
        DataOutputStream dos = new DataOutputStream(out);
        ioCommands.go(dis, dos);
        out.flush();
        socket.close();
    }

    static byte[] readBytes(InputStream is, int cnt) throws IOException {
        byte[] ipBytes = new byte[cnt];
        is.read(ipBytes);
        return ipBytes;
    }

    static String getStringInRange(int l, int r) {
        SecureRandom random = new SecureRandom();
        int countIter = 1000;
        for (int iter = 0; iter < countIter; iter++) {
            int cnt = 20;
            if (iter > countIter / 2)
                cnt = 70;
            String s = new BigInteger(cnt, random).toString(32);
            int m = sha1(s);
            if (Intervals.InEx(l, m, r))
                return s;
        }
        return null;
    }

    static ArrayList<InetAddress> getNetwork() throws IOException {
        ArrayList<InetAddress> result = new ArrayList<>();
        result.add(NetworkManager.me);
        InetAddress cur = NetworkManager.succ;
        while (!NetworkManager.me.equals(cur)) {
            result.add(cur);
            if (cur == null)
                break;
            cur = Packets.sendFindSuccessor(cur, Utils.sha1(cur));
        }
        return result;
    }

    static int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    static byte[] intToBytes(int x) {
        return ByteBuffer.allocate(4).putInt(x).array();
    }
}
