import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    final int WAIT = 2000;

    class Entry implements Comparable<Entry> {
        String[] s;
        long lastTime;
        ArrayDeque<Long> times;
        boolean fresh;

        int countMissed() {
            long currentTime = System.currentTimeMillis();
            int cnt;
            if (fresh) {
                int should = (int) Math.ceil(1.0 * (currentTime - times.getFirst()) / WAIT);
                cnt = should - times.size();
            } else {
                cnt = 10 - times.size();
            }
            return Math.max(0, cnt);
        }

        @Override
        public String toString() {
            long currentTime = System.currentTimeMillis();
            return s[0] + " " + s[1] + " " + s[2] + " " + (countMissed() + " " + (currentTime - lastTime));
        }

        public Entry(String[] s) {
            super();
            this.s = s;
            times = new ArrayDeque<>();
            fresh = true;
        }

        @Override
        public int compareTo(Entry o) {
            int cm0 = countMissed();
            int cm1 = o.countMissed();
            if (cm0 == cm1)
                return s[0].compareTo(o.s[0]);
            return Integer.compare(cm0, cm1);
        }
    }

    TreeMap<String, Entry> table = new TreeMap<String, Entry>();

    long startProgramTime;

    class SendData {
        byte[] mac;
        byte[] ip;
        byte[] name;

        byte[] getBytes() {
            byte[] res = new byte[mac.length + ip.length + name.length];
            for (int i = 0; i < ip.length; ++i)
                res[i] = ip[i];
            for (int i = 0; i < mac.length; ++i)
                res[i + ip.length] = mac[i];
            for (int i = 0; i < name.length; ++i)
                res[i + ip.length + mac.length] = name[i];
            return res;
        }
    }

    SendData myData = new SendData();
    String myAddress;

    public class Client implements Runnable {
        private String hostname = "localhost";
        private int port = 7777;
        private InetAddress host;
        private DatagramSocket socket;
        DatagramPacket packet;
        private int WAIT_TIME = 2000;

        @Override
        public void run() {
            while (true) {
                try {
                    host = getIpAddress();
                    socket = new DatagramSocket(null);
                    byte[] res = myData.getBytes();
                    packet = new DatagramPacket(res,
                            res.length,
                            InetAddress.getByName("255.255.255.255"), port);
                    socket.send(packet);
                    socket.close();
                    Thread.sleep(WAIT_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Server implements Runnable {
        DatagramSocket socket;

        @Override
        public void run() {
            try {
                socket = new DatagramSocket(7777,
                        InetAddress.getByName("0.0.0.0"));
                socket.setBroadcast(true);

                while (true) {
                    byte[] recvBuf = new byte[15000];
                    DatagramPacket packet = new DatagramPacket(recvBuf,
                            recvBuf.length);
                    socket.receive(packet);

                    byte[] data = packet.getData();
                    String ipString = ipToString(Arrays.copyOfRange(data, 0, 4));
                    String macString = macToString(Arrays.copyOfRange(data, 4, 10));
                    int last = 10;
                    while (last < data.length && data[last] != 0)
                        last++;
                    String nameString = nameToString(Arrays.copyOfRange(data, 10, last));
//System.out.println(ipString + "   " + macString + "   " + nameString);

                    long currentTime = System.currentTimeMillis();
                    {
                        String[] s = new String[3];
                        s[0] = ipString;
                        s[1] = macString;
                        s[2] = nameString;
                        Entry e = new Entry(s);
                        if (table.containsKey(ipString)) {
                            e = table.get(ipString);
                        } else {
                            table.put(ipString, e);
                        }
                        e.lastTime = currentTime;
                        e.times.addLast(currentTime);
                    }

                }
            } catch (IOException ex) {
            }
        }
    }

    String nameToString(byte[] name) {
        return new String(name);
    }

    String ipToString(byte[] ip) {
        try {
            return InetAddress.getByAddress(ip).toString().split("/")[1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    String macToString(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i],
                    (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }

    public static byte[] getMACAddress() {
        try {
            InetAddress ip = InetAddress.getLocalHost();

            Enumeration<NetworkInterface> networks = NetworkInterface
                    .getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();

                if (mac != null) {
                    return mac;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    class GUI implements Runnable {
        private final int REFRESH_TIME = 100;
        JFrame frame;

        @Override
        public void run() {
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Upd Example");
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            JTextArea textArea = new JTextArea();
            frame.add(textArea);
            textArea.setEditable(false);
            while (true) {
                synchronized (table) {
                    long currentTime = System.currentTimeMillis();
                    String text = "";
                    ArrayList<String> toRemove = new ArrayList<String>();
                    ArrayList<Entry> output = new ArrayList<>();
                    for (String ip : table.keySet()) {
                        Entry entry = table.get(ip);
                        long expireTime = currentTime - 20 * 1000;
                        while (entry.times.size() > 0 && entry.times.getFirst() < expireTime) {
                            entry.fresh = false;
                            entry.times.removeFirst();
                        }
                        if (entry.times.size() == 0) {
                            toRemove.add(ip);
                            continue;
                        }
                        output.add(entry);
                    }
                    for (String s : toRemove) {
                        table.remove(s);
                    }
                    Collections.sort(output);
                    for (Entry entry : output)
                        text += entry.toString() + "\n";
                    textArea.setText(text);
                }
                try {
                    Thread.sleep(REFRESH_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    InetAddress getIpAddress() {
        try {
            //System.out.println("Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
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
                    if (e.getName().equals("tun0"))
                        if (addr instanceof Inet4Address) {
                            return addr;
                        }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    void init() {
        myData.name = "Anton Kovsharov".getBytes();
        startProgramTime = System.currentTimeMillis();
        InetAddress ip;
        try {

            ip = getIpAddress();
            System.out.println("Current IP address : " + ip.getHostAddress());

            myData.ip = ip.getAddress();

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            myData.mac = getMACAddress();

            System.out
                    .print("Current MAC address : " + macToString(myData.mac));

        } catch (SocketException e) {

            e.printStackTrace();

        }
    }



    void run() {
        init();

        new Thread(new Client()).start();
        new Thread(new Server()).start();
        new GUI().run();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
