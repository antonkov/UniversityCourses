import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Packets {
    static DatagramPacket buildInit() throws IOException {
        return Utils.genPacket(out -> {
            out.writeByte(Codes.INIT);
            out.write(Settings.myIP);
        }, InetAddress.getByName("255.255.255.255"));
    }

    static HashMap<Integer, InetAddress> getBackup(InetAddress addr) {
        HashMap<Integer, InetAddress> map = new HashMap<>();
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.GET_BACKUP);

                int res = in.read();
                if (res == Codes.ERROR) {
                    LogGUI.error("getBackup error");
                    map.clear();
                    return;
                }

                int dataLen = in.readInt();
                for (int i = 0; i < dataLen / 8; i++) {
                    int keyHash = in.readInt();
                    byte[] ipBytes = Utils.readBytes(in, 4);
                    map.put(keyHash, InetAddress.getByAddress(ipBytes));
                }
            }, addr, Settings.PORT);
        } catch (Exception e) {
            map.clear();
        }
        return map;
    }

    static InetAddress getPredecessor(InetAddress addr) throws IOException {
        byte[] ipBytes = new byte[4];
        boolean[] error = new boolean[1];
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.GET_PREDECESSOR);
                int res = in.read();
                if (res == Codes.ERROR) {
                    System.err.println("Get predecessor error");
                    error[0] = true;
                    return;
                }
                in.readFully(ipBytes);
            }, addr, Settings.PORT);
        } catch (Exception e) {
            error[0] = true;
        }
        if (error[0])
            return null;
        return InetAddress.getByAddress(ipBytes);
    }

    static InetAddress getKeepNodeOfValue(InetAddress keepNode, int keyHash) throws UnknownHostException {
        boolean[] error = new boolean[1];
        byte[] ipBytes = new byte[4];
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.GET_IP);
                out.writeInt(keyHash);

                int res = in.read();
                if (res == Codes.ERROR) {
                    LogGUI.error("getKeepNodeOfValue error");
                    error[0] = true;
                    return;
                }
                in.readFully(ipBytes);
            }, keepNode, Settings.PORT);
        } catch (Exception e) {
            error[0] = true;
        }
        if (error[0])
            return null;
        return InetAddress.getByAddress(ipBytes);
    }

    static String getData(InetAddress addr, int keyHash) {
        boolean[] error = new boolean[1];
        String[] s = new String[1];
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.GET_DATA);
                out.writeInt(keyHash);

                int result = in.read();
                if (result == Codes.ERROR) {
                    LogGUI.error("get data error");
                    error[0] = true;
                    return;
                }
                int dataLen = in.readInt();
                byte[] data = new byte[dataLen];
                in.readFully(data);
                s[0] = new String(data);
            }, addr, Settings.PORT);
        } catch (Exception e) {
            error[0] = true;
        }
        if (error[0])
            return null;
        return s[0];
    }

    static void sendDeleteEntry(InetAddress keepNode, int keyHash) {
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.DELETE_ENTRY);
                out.writeInt(keyHash);
            }, keepNode, Settings.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean sendDeleteFromBackup(InetAddress addr, int keyHash) {
        boolean[] error = new boolean[1];
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.DELETE_FROM_BACKUP);
                out.writeInt(keyHash);

                int res = in.read();
                if (res == Codes.ERROR) {
                    System.err.println("delete from backup error");
                    error[0] = true;
                    return;
                }
            }, addr, Settings.PORT);
        } catch (Exception e) {
            error[0] = true;
        }
        return !error[0];
    }

    static boolean sendAddToBackup(InetAddress addr, int keyHash, InetAddress keepNode) {
        boolean[] error = new boolean[1];
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.ADD_TO_BACKUP);
                out.writeInt(keyHash);
                out.write(keepNode.getAddress());

                int res = in.read();
                if (res == Codes.ERROR) {
                    LogGUI.error("add to backup error");
                    error[0] = true;
                    return;
                }
            }, addr, Settings.PORT);
        } catch (Exception e) {
            error[0] = true;
        }
        return !error[0];
    }

    static boolean sendAddEntry(InetAddress addr, int hash, byte[] keepIp) {
        boolean[] error = new boolean[1];
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.ADD_ENTRY);
                out.writeInt(hash);
                out.write(keepIp);

                int res = in.read();
                if (res == Codes.ERROR) {
                    System.err.println("add entry error");
                    error[0] = true;
                    return;
                }
                if (res == Codes.COLLISION) {
                    System.err.println("add entry collision");
                    error[0] = true;
                    return;
                }
            }, addr, Settings.PORT);
        } catch (Exception e) {
            error[0] = true;
        }
        if (error[0])
            return false;
        return true;
    }

    static InetAddress sendFindSuccessor(InetAddress addr, int hash) throws IOException {
        byte[] ipBytes = new byte[4];
        boolean[] error = new boolean[1];
        try {
            Utils.socketIO((in, out) -> {
                out.writeByte(Codes.FIND_SUCCESSOR);
                out.writeInt(hash);
                int res = in.read();
                if (res == Codes.ERROR) {
                    LogGUI.error("Find successor error");
                    error[0] = true;
                    return;
                }
                in.readFully(ipBytes);
            }, addr, Settings.PORT);
        } catch (Exception e) {
            error[0] = true;
        }
        if (error[0])
            return null;
        return InetAddress.getByAddress(ipBytes);
    }

    static void sendNotify(InetAddress addr) throws IOException {
        Utils.socketIO((in, out) -> {
            out.writeByte(Codes.NOTIFY);
            out.write(Settings.myIP);
        }, addr, Settings.PORT);
    }

    static void sendPredFailed(InetAddress addr) throws IOException {
        Utils.socketIO((in, out) -> {
            out.writeByte(Codes.PRED_FAILED);
            out.write(Settings.myIP);
        }, addr, Settings.PORT);
    }

    static void sendPickUp(InetAddress addr) throws IOException {
        Utils.socketIO((in, out) -> {
            out.writeByte(Codes.PICK_UP);
            if (addr.equals(NetworkManager.succ)) // RETHINK
                out.write(NetworkManager.me.getAddress());
            else
                out.write(NetworkManager.succ.getAddress());
        }, addr, Settings.PORT);
    }

    static DatagramPacket buildKeepAlive(InetAddress addr) throws IOException {
        return Utils.genPacket(dos -> {
            dos.writeByte(Codes.KEEP_ALIVE);
        }, addr);
    }
}
