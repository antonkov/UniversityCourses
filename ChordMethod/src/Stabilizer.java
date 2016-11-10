import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by root on 27.03.15.
 */
public class Stabilizer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    //NetworkManager.printCurrent();
                    NetworkManager.stabilize();
                    Thread.sleep(Settings.SEND_INTERVAL);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
