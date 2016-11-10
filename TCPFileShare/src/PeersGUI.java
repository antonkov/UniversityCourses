import javax.swing.*;

/**
 * Created by antonkov on 31.10.14.
 */
public class PeersGUI implements Runnable {
    private final int REFRESH_TIME = 100;
    JFrame frame;
    UDPAnnounceReceiver announceReceiver;

    public PeersGUI(UDPAnnounceReceiver announceReceiver) {
        this.announceReceiver = announceReceiver;
    }

    @Override
    public void run() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Available Peers");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JTextArea textArea = new JTextArea();
        frame.add(textArea);
        textArea.setEditable(false);
        while (true) {
            synchronized (announceReceiver.peers) {
                String text = "";
                for (UDPAnnounceReceiver.PeerInfo info : announceReceiver.peers) {
                    if (text.length() != 0)
                        text += "\n";
                    text += info.toString();
                }
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
