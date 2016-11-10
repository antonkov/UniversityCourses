import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

/**
 * Created by antonkov on 31.10.14.
 */
public class UDPAnnounceReceiver implements Runnable {
    private final int port = 7777;

    class PeerInfo implements Comparable<PeerInfo> {
        byte[] ip = new byte[4];
        String name;
        int countFiles;
        long lastChangeTimestamp;

        @Override
        public String toString() {
            String ipString = ipToString(ip);
            Date date = new Date(lastChangeTimestamp);
            DateFormat format = new SimpleDateFormat("\"yyyy.MM.dd G 'at' HH:mm:ss z\"");
            return  "ip=" + ipString +
                    ", name=" + name +
                    ", countFiles=" + countFiles +
                    ", lastChangeTimestamp=" + format.format(date);
        }

        @Override
        public int compareTo(PeerInfo peerInfo) {
            return ipToString(ip).compareTo(ipToString(peerInfo.ip));
        }
    }

    TreeSet<PeerInfo> peers = new TreeSet<>();

    String ipToString(byte[] ip) {
        try {
            return InetAddress.getByAddress(ip).toString().split("/")[1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(port,
                    InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (true) {
                byte[] bytes = new byte[64*1024];
                DatagramPacket packet = new DatagramPacket(bytes,
                        bytes.length);
                socket.receive(packet);

                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                DataInputStream dis = new DataInputStream(bis);
                PeerInfo info = new PeerInfo();
                dis.readFully(info.ip);
                info.countFiles = dis.readInt();
                info.lastChangeTimestamp = dis.readLong();
                byte[] nameBytes = new byte[dis.available()];
                dis.readFully(nameBytes);
                info.name = new String(nameBytes);
                synchronized (peers) {
                    peers.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
