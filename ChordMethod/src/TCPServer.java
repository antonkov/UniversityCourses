import com.sun.xml.internal.ws.api.message.Packet;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

class TCPServer implements Runnable {
    public TCPServer() {
    }

    class EventHandler implements Runnable {
        Socket socket;

        public EventHandler(Socket socket) {
            LogGUI.log(socket.getInetAddress().toString());
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                int commandCode = input.read();
                switch (commandCode) {
                    case Codes.PICK_UP: {
                        //System.err.println("received pickup");
                        if (NetworkManager.udpSender.initMode) {
                            NetworkManager.disableInit();
                            InetAddress senderIp = InetAddress.getByAddress(Utils.readBytes(input, 4));
                            NetworkManager.join(senderIp);
                        }
                        break;
                    }
                    case Codes.FIND_SUCCESSOR: {
                        int hash = input.readInt();
                        InetAddress res = NetworkManager.getKeepNode(hash);
                        LogGUI.log("rec fs " + hash + " " + res);
                        if (res == null) {
                            output.writeByte(Codes.ERROR);
                        } else {
                            output.writeByte(Codes.OK);
                            output.write(res.getAddress());
                        }
                        break;
                    }
                    case Codes.NOTIFY: {
                        InetAddress senderIp = InetAddress.getByAddress(Utils.readBytes(input, 4));
                        if (Intervals.ExEx(Utils.sha1(NetworkManager.pred),
                                Utils.sha1(senderIp), Utils.sha1(NetworkManager.me))) {
                            InetAddress oldPred = NetworkManager.pred;
                            NetworkManager.pred = senderIp;
                            NetworkManager.shareMap(oldPred);
                            NetworkManager.cleanMap(oldPred);
                        }
                        break;
                    }
                    case Codes.GET_PREDECESSOR: {
                        if (NetworkManager.pred == null) {
                            output.writeByte(Codes.ERROR);
                        } else {
                            output.writeByte(Codes.OK);
                            output.write(NetworkManager.pred.getAddress());
                        }
                        break;
                    }
                    case Codes.ADD_ENTRY: {
                        int keyHash = input.readInt();
                        InetAddress keepNode = InetAddress.getByAddress(Utils.readBytes(input, 4));
                        if (!Intervals.InEx(Utils.sha1(NetworkManager.pred), keyHash, Utils.sha1(NetworkManager.me))) {
                            output.writeByte(Codes.ERROR);
                            break;
                        }
                        if (NetworkManager.keepNode.containsKey(keyHash)) {
                            output.writeByte(Codes.COLLISION);
                            break;
                        }
                        boolean success = Packets.sendAddToBackup(NetworkManager.succ, keyHash, keepNode);
                        if (success) {
                            NetworkManager.keepNode.put(keyHash, keepNode);
                            output.writeByte(Codes.OK);
                        } else {
                            output.writeByte(Codes.ERROR);
                        }
                        break;
                    }
                    case Codes.ADD_TO_BACKUP: {
                        int keyHash = input.readInt();
                        InetAddress keepNode = InetAddress.getByAddress(Utils.readBytes(input, 4));
                        NetworkManager.backupKeepNode.put(keyHash, keepNode);
                        output.writeByte(Codes.OK);
                        break;
                    }
                    case Codes.DELETE_FROM_BACKUP: {
                        int keyHash = input.readInt();
                        NetworkManager.backupKeepNode.remove(keyHash);
                        output.writeByte(Codes.OK);
                        break;
                    }
                    case Codes.GET_IP: {
                        int keyHash = input.readInt();
                        if (NetworkManager.keepNode.containsKey(keyHash)) {
                            output.writeByte(Codes.OK);
                            output.write(NetworkManager.keepNode.get(keyHash).getAddress());
                        } else {
                            output.writeByte(Codes.ERROR);
                        }
                        break;
                    }
                    case Codes.GET_DATA: {
                        int keyHash = input.readInt();
                        if (FileManager.fileKeys.containsKey(keyHash)) {
                            output.writeByte(Codes.OK);
                            String data = FileManager.files.get(FileManager.fileKeys.get(keyHash));
                            byte[] bytes = data.getBytes();
                            output.writeInt(bytes.length);
                            output.write(bytes);
                        } else {
                            output.writeByte(Codes.ERROR);
                        }
                        break;
                    }
                    case Codes.DELETE_ENTRY: {
                        int keyHash = input.readInt();
                        NetworkManager.keepNode.remove(keyHash);
                        Packets.sendDeleteFromBackup(NetworkManager.succ, keyHash);
                        break;
                    }
                    case Codes.PRED_FAILED: {
                        InetAddress newPred = InetAddress.getByAddress(Utils.readBytes(input, 4));
                        for (Map.Entry<Integer, InetAddress> entry : NetworkManager.backupKeepNode.entrySet()) {
                            int keyHash = entry.getKey();
                            InetAddress keepNode = entry.getValue();
                            for (int i = 0; i < Settings.MAX_TRY_SAVE_BACKUP; i++) {
                                boolean result = Packets.sendAddToBackup(NetworkManager.succ, keyHash, keepNode);
                                if (result)
                                    break;
                            }
                            NetworkManager.keepNode.put(keyHash, keepNode);
                        }
                        NetworkManager.pred = newPred;
                        NetworkManager.backupKeepNode.clear();
                        NetworkManager.backupKeepNode = Packets.getBackup(NetworkManager.pred);
                        break;
                    }
                    case Codes.GET_BACKUP: {
                        int size = NetworkManager.backupKeepNode.size() * 8;
                        output.writeByte(Codes.OK);
                        output.writeInt(size);
                        for (Map.Entry<Integer, InetAddress> entry : NetworkManager.backupKeepNode.entrySet()) {
                            output.writeInt(entry.getKey());
                            output.write(entry.getValue().getAddress());
                        }
                        break;
                    }
                    default:
                        LogGUI.error("!!!!1Unsupported oper " + commandCode);
                        break;
                }
                socket.close();
            } catch (SocketException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(Settings.PORT);
            LogGUI.log("TCPServer is started");
            while (true) {
                new Thread(new EventHandler(server.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}