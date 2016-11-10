import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NetworkManager {
    static public InetAddress pred, me, succ, succ2;
    static final public int countFingers = 32;
    static public int[] start;
    static public InetAddress[] finger;
    static public int myHash;
    static public long lastTimeKeepAlive;
    static public HashMap<Integer, InetAddress> keepNode;
    static public HashMap<Integer, InetAddress> backupKeepNode;
    static public Daemon.UDPSender udpSender;

    static void shareMap(InetAddress oldPred) {
        for (int keyHash : keepNode.keySet()) {
            if (Intervals.InEx(Utils.sha1(oldPred), keyHash, Utils.sha1(pred))) {
                Packets.sendAddEntry(pred, keyHash, keepNode.get(keyHash).getAddress());
            }
        }
    }

    static void disableInit() {
        lastTimeKeepAlive = System.currentTimeMillis();
        udpSender.initMode = false;
    }

    static void cleanMap(InetAddress oldPred) {
        try {
            ArrayList<Integer> toRemove = new ArrayList<>();
            for (int keyHash : keepNode.keySet()) {
                if (Intervals.InEx(Utils.sha1(oldPred), keyHash, Utils.sha1(pred))) {
                    while (!Packets.sendDeleteFromBackup(NetworkManager.succ, keyHash)) {
                        Thread.sleep(50);
                    }
                    toRemove.add(keyHash);
                }
            }
            for (int x : toRemove)
                keepNode.keySet().remove(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void init() throws UnknownHostException {
        keepNode = new HashMap<>();
        backupKeepNode = new HashMap<>();

        me = pred = succ = succ2 = InetAddress.getByAddress(Settings.myIP);
        finger = new InetAddress[countFingers];
        Arrays.fill(finger, me);
        start = new int[countFingers];
        for (int i = 0; i < countFingers; i++) {
            start[i] = myHash + (1 << i) - 1;
        }
        myHash = Utils.sha1(Settings.myIP);
    }

    static void printCurrent() {
        System.out.println("pred " + pred);
        System.out.println("cur " + me);
        System.out.println("succ " + succ);
        System.out.println("succ2 " + succ2);
        System.out.println("------------------");
    }

    static void stabilize() throws IOException {
        InetAddress x = Packets.getPredecessor(succ);
        if (x == null)
            return;
        if (Intervals.ExEx(myHash, Utils.sha1(x), Utils.sha1(succ))) {
            finger[0] = succ = x;
        }
        Packets.sendNotify(succ);
        succ2 = Packets.sendFindSuccessor(succ, Utils.sha1(succ));
    }

    static void updateFinger(int i) throws IOException {
        finger[i] = getKeepNode(start[i]);
    }



    static void successorDead() {
        try {
            if (succ2 != null) {
                Packets.sendPredFailed(succ2);
                finger[0] = succ = succ2;
                succ2 = Packets.sendFindSuccessor(succ, Utils.sha1(succ));
            } else {
                me = pred = succ = succ2 = InetAddress.getByAddress(Settings.myIP);
            }
            if (me.equals(succ)) {
                NetworkManager.udpSender.initMode = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void join(InetAddress senderIp) throws IOException {
        //pred = senderIp;
        succ = Packets.sendFindSuccessor(senderIp, myHash);
        finger[0] = succ;
        succ2 = Packets.sendFindSuccessor(succ, Utils.sha1(succ));
        if (succ.equals(succ2)) {
            succ2 = me; // we are second client in net
        }
    }

    static boolean addEntry(String key, String value) throws IOException {
        int hash = Utils.sha1(key);
        InetAddress keepNode = getKeepNode(hash);
        boolean added = Packets.sendAddEntry(keepNode, hash, Settings.myIP);
        if (added) {
            FileManager.fileKeys.put(Utils.sha1(key), key);
            FileManager.files.put(key, value);
        }
        return added;
    }

    static InetAddress getKeepNode(int hash) throws IOException {
        int predHash = Utils.sha1(pred);
        if (Intervals.InEx(predHash, hash, myHash)) {
            LogGUI.log("send fs me");
            return NetworkManager.me;
        }
        for (int i = countFingers - 1; i >= 0; i--) {
            int cur = Utils.sha1(finger[i]);
            if (Intervals.ExEx(NetworkManager.myHash, cur, hash)) {
                InetAddress succIp = null;
                if (!me.equals(finger[i])) {
                    LogGUI.log("send fs " + i + " " + finger[i] + " " + hash);
                    succIp = Packets.sendFindSuccessor(finger[i], hash);
                }
                if (succIp != null)
                    return succIp;
            }
        }
        return succ;
    }
}
