/**
 * Created by antonkov on 31.10.14.
 */
public class Main {
    public static void main(String[] args) {
        ShareManager share = new ShareManager();
        new Thread(new FileShareClient(share)).start();
        new Thread(new FileShareServer(share)).start();
        new Thread(new UDPAnnouncer(share)).start();
        UDPAnnounceReceiver announceReceiver = new UDPAnnounceReceiver();
        new Thread(announceReceiver).start();
        new Thread(new PeersGUI(announceReceiver)).start();
    }
}
