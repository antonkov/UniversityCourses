public class Main {
    public static void main(String[] args) {
        try {
            FileManager.init();

            NetworkManager.init();

            Daemon daemon = new Daemon();

            TCPServer server = new TCPServer();
            new Thread(server).start();

            new Thread(new LogGUI()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
