import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by root on 27.03.15.
 */
public class Client implements Runnable {

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("Enter command: ");
            String s = in.nextLine();
            String[] args = s.split(" ");
            String command = args[0];
            try {
                switch (command) {
                    case "printnet": {
                        ArrayList<InetAddress> net = Utils.getNetwork();
                        for (InetAddress addr : net) {
                            System.out.println(addr);
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
