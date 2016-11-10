import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Created by antonkov on 26.03.15.
 */
public class FileManager {
    static public final Path dbPath = FileSystems.getDefault().getPath("db");
    static public final File dbDir = new File(dbPath.toString());

    static public final Path filesPath = FileSystems.getDefault().getPath("files");
    static public final File filesDir = new File(filesPath.toString());

    static HashMap<Integer, String> fileKeys;
    static HashMap<String, String> files;

    static void init() {
        fileKeys = new HashMap<>();
        files = new HashMap<>();
    }
}
