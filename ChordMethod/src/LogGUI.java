import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

public class LogGUI implements Runnable{
    private final int REFRESH_TIME = 500;
    JFrame frame;
    static public JTextArea logArea = newTextArea("log"),
            curNetwork = newTextArea("network"),
            state = newTextArea("state"),
            error = newTextArea("error"),
            entries = newTextArea("entries"),
            files = newTextArea("files");

    static JPanel buildStringForIp() {
        JPanel stringForIp = new JPanel();
        stringForIp.setLayout(new FlowLayout());
        JTextField ipField = new JTextField();
        ipField.setPreferredSize(new Dimension(180, 24));
        JTextField genedString = new JTextField();
        genedString.setPreferredSize(new Dimension(180, 24));
        genedString.setEditable(false);
        stringForIp.add(new JLabel("Gen key for given IP"));
        stringForIp.add(ipField);
        JButton genBtn = new JButton("gen");
        genBtn.setPreferredSize(new Dimension(100, 24));
        stringForIp.add(genBtn);
        stringForIp.add(genedString);
        genBtn.addActionListener(e -> {
            try {
                InetAddress cur = InetAddress.getByName(ipField.getText());
                InetAddress pred = Packets.getPredecessor(cur);
                int l = Utils.sha1(pred);
                int r = Utils.sha1(cur);
                String res = Utils.getStringInRange(l, r);
                if (res == null)
                    res = "can't generate";
                genedString.setText(res);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return stringForIp;
    }

    JPanel buildAddEntry() {
        JPanel addEntry = new JPanel();
        addEntry.setLayout(new FlowLayout());
        JTextField keyField = new JTextField();
        JTextField valueField = new JTextField();
        keyField.setPreferredSize(new Dimension(180, 24));
        valueField.setPreferredSize(new Dimension(180, 24));
        addEntry.add(new JLabel("Add new entry"));
        addEntry.add(keyField);
        addEntry.add(valueField);
        JButton addEntryBtn = new JButton("add");
        addEntryBtn.setPreferredSize(new Dimension(100, 24));
        addEntryBtn.addActionListener(e -> {
            String key = keyField.getText();
            String value = valueField.getText();
            try {
                NetworkManager.addEntry(key, value);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        addEntry.add(addEntryBtn);
        return addEntry;
    }

    JPanel buildGetData() {
        JPanel getData = new JPanel();
        getData.setLayout(new FlowLayout());
        JTextField keyField = new JTextField();
        keyField.setPreferredSize(new Dimension(180, 24));
        getData.add(new JLabel("Get data by key"));
        getData.add(keyField);

        JButton getDataBtn = new JButton("get");
        getDataBtn.setPreferredSize(new Dimension(100, 24));
        getData.add(getDataBtn);

        JTextField dataField = new JTextField();
        dataField.setPreferredSize(new Dimension(180, 24));
        dataField.setEditable(false);
        getData.add(dataField);

        JButton exitBtn = new JButton("exit");
        exitBtn.setPreferredSize(new Dimension(100, 24));
        getData.add(exitBtn);

        exitBtn.addActionListener(e -> {
            for (Map.Entry<Integer, InetAddress> entry : NetworkManager.keepNode.entrySet()) {
                Packets.sendAddEntry(NetworkManager.succ, entry.getKey(), entry.getValue().getAddress());
            }
            System.exit(0);
        });

        getDataBtn.addActionListener(e -> {
            String key = keyField.getText();
            int keyHash = Utils.sha1(key);
            try {
                InetAddress keepNode = NetworkManager.getKeepNode(keyHash);
                InetAddress addr = Packets.getKeepNodeOfValue(keepNode, keyHash);
                for (int i = 0; i < Settings.MAX_TRY_GET_DATA; i++) {
                    String data = Packets.getData(addr, keyHash);
                    if (data != null) {
                        dataField.setText(data);
                        return;
                    }
                }
                dataField.setText("data not found");
                Packets.sendDeleteEntry(keepNode, keyHash);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        return getData;
    }

    public LogGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Log");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        GridLayout grid = new GridLayout(3, 2);
        frame.setLayout(grid);

        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollLog = new JScrollPane(logArea);


        frame.add(buildAddEntry());
        frame.add(scrollLog);
        frame.add(curNetwork);
        frame.add(state);
        frame.add(error);
        frame.add(entries);
        frame.add(files);
        frame.add(buildStringForIp());
        frame.add(buildGetData());
    }

    static public void log(String s) {
        logArea.append(s + "\n");
       // logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    static public void error(String s) {
        error.append(s + "\n");
        error.setCaretPosition(error.getDocument().getLength());
    }

    static public JTextArea newTextArea(String name) {
        JTextArea res = new JTextArea();
        res.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        res.setEditable(false);

        return res;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (Settings.SHOW_NETWORK) {
                    ArrayList<InetAddress> addrs = Utils.getNetwork();
                    curNetwork.setText("");
                    for (InetAddress addr : addrs) {
                        curNetwork.append(addr + "\n");
                    }
                }

                state.setText("");
                state.append("pred " + NetworkManager.pred + "\n");
                state.append("cur " + NetworkManager.me + "\n");
                state.append("succ " + NetworkManager.succ + "\n");
                state.append("succ2 " + NetworkManager.succ2 + "\n");

                entries.setText("Entries\n");
                for (Map.Entry<Integer, InetAddress> entry : NetworkManager.keepNode.entrySet()) {
                    int key = entry.getKey();
                    InetAddress addr = entry.getValue();
                    entries.append(key + " :: " + addr + "\n");
                }
                if (NetworkManager.backupKeepNode.size() > 0) {
                    entries.append("Backup\n");
                    for (Map.Entry<Integer, InetAddress> entry : NetworkManager.backupKeepNode.entrySet()) {
                        int key = entry.getKey();
                        InetAddress addr = entry.getValue();
                        entries.append(key + " :: " + addr + "\n");
                    }
                }

                files.setText("Strings\n");
                for (Map.Entry<Integer, String> entry : FileManager.fileKeys.entrySet()) {
                    Integer keyHash = entry.getKey();
                    String key = entry.getValue();
                    String value = FileManager.files.get(key);
                    files.append("key: " + key + ", value: " + value + ", sha1: " + keyHash + "\n");
                }
                Thread.sleep(REFRESH_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
