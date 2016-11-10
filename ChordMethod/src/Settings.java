/**
 * Created by antonkov on 26.03.15.
 */
public class Settings {
    public static final int PORT = 7777;
    public static final int SEND_INTERVAL = 2 * 1000;
    public static final int INIT_SEND_INTERVAL = 3 * 1000;
    public static final int KEEP_ALIVE_SEND_INTERVAL = 1000;
    public static final int FIX_FINGERS_INTERVAL = 1000;
    public static final int CHECK_KEEP_ALIVE_INTERVAL = 1000;
    public static final int KEEP_ALIVE_TIMEOUT = 5 * 1000;
    public static byte[] myIP = Utils.getIpAddress();

    public static boolean useBackup = false;
    public static int MAX_TRY_GET_DATA = 1;
    public static int MAX_TRY_SAVE_BACKUP = 1;

    public static boolean SHOW_NETWORK = false;
}
