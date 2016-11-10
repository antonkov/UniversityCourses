import sun.rmi.runtime.Log;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Random;

public class Daemon {
    UDPReceiver udpReceiver;
    Stabilizer stabilizer;
    FixFingers fixFingers;

    class CheckKeepAlive implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (!NetworkManager.udpSender.initMode) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - NetworkManager.lastTimeKeepAlive >= Settings.KEEP_ALIVE_TIMEOUT) {
                        NetworkManager.successorDead();
                    }
                }
                try {
                    Thread.sleep(Settings.CHECK_KEEP_ALIVE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class UDPSender implements Runnable {
        volatile Boolean initMode;

        public UDPSender() {
            initMode = true;
        }

        @Override
        public void run() {
            while (true) {
                while (initMode) {
                    try {
                        //System.out.println("sent Init");
                        DatagramSocket socket = new DatagramSocket(null);
                        socket.send(Packets.buildInit());
                        socket.close();
                        Thread.sleep(Settings.INIT_SEND_INTERVAL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while (!initMode) {
                    try {
                        //System.out.println("sent Keep Alive");
                        if (NetworkManager.pred != null) {
                            DatagramSocket socket = new DatagramSocket(null);
                            socket.send(Packets.buildKeepAlive(NetworkManager.pred));
                            socket.close();
                        }
                        Thread.sleep(Settings.KEEP_ALIVE_SEND_INTERVAL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class UDPReceiver implements Runnable {

        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(Settings.PORT,
                        InetAddress.getByName("0.0.0.0"));

                while (true) {
                    byte[] bytes = new byte[64*1024];
                    DatagramPacket packet = new DatagramPacket(bytes,
                            bytes.length);
                    socket.receive(packet);
                    //System.err.println("Init received");

                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    DataInputStream dis = new DataInputStream(bis);
                    //LogGUI.log("UDP " + socket.getInetAddress());
                    switch (dis.readByte()) {
                        case Codes.INIT:
                            byte[] recIpBytes = new byte[4];
                            dis.readFully(recIpBytes);
                            boolean our = Arrays.equals(recIpBytes, Settings.myIP);
                            if (our)
                                break;
                            //System.err.println(Utils.ipToString(recIpBytes) + " " + Utils.sha1(recIpBytes));
                            InetAddress senderIp = InetAddress.getByAddress(recIpBytes);
                            int senderHash = Utils.sha1(senderIp);
                            int succHash = Utils.sha1(NetworkManager.succ);
                            if (Intervals.ExIn(NetworkManager.myHash, senderHash, succHash)) {
                                System.err.println("Sent pickup");
                                NetworkManager.disableInit();
                                Packets.sendPickUp(senderIp);
                            }
                            break;
                        case Codes.KEEP_ALIVE:
                            NetworkManager.lastTimeKeepAlive = System.currentTimeMillis();
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class FixFingers implements Runnable {
        Random rng;

        public FixFingers() {
            rng = new Random(19);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(Settings.FIX_FINGERS_INTERVAL);
                    int i = rng.nextInt(NetworkManager.countFingers);
                    NetworkManager.updateFinger(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Daemon()
    {
        udpReceiver = new UDPReceiver();
        NetworkManager.udpSender = new UDPSender();
        stabilizer = new Stabilizer();
        fixFingers = new FixFingers();
        new Thread(udpReceiver).start();
        new Thread(NetworkManager.udpSender).start();
        new Thread(stabilizer).start();
        //TODO new Thread(fixFingers).start();
        new Thread(new CheckKeepAlive()).start();
    }
}