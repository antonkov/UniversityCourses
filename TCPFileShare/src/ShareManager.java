import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by antonkov on 31.10.14.
 */
public class ShareManager {
    public final Path sharePath = FileSystems.getDefault().getPath("share_directory");
    public final File shareDir = new File(sharePath.toString());
    public final String shareName = "antonkovShare";

    long lastChangeTimestamp() {
        return shareDir.lastModified();
    }

    class FileInfo {
        String filename;
        byte[] md5;
    }

    byte[] getMD5ByFilename(String filename) {
        try {
            String path = FileSystems.getDefault().getPath(sharePath.toString(), filename).toString();
            return Utils.getMD5FromStream(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ArrayList<FileInfo> getFiles() {
        ArrayList<FileInfo> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sharePath)) {
            for (Path entry: stream) {
                FileInfo info = new FileInfo();
                info.filename = entry.getFileName().toString();
                info.md5 = Utils.calcMD5OfFile(new FileInputStream(entry.toString()));
                files.add(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    int countFiles() {
        int count = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sharePath)) {
            for (Path entry: stream) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    byte[] readFile(String filename) {
        Path path = FileSystems.getDefault().getPath(sharePath.toString(), filename);
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(path.toString())))
        {
            byte[] bytes = new byte[input.available()];
            input.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void writeFile(String filename, byte[] bytes) {
        Path path = FileSystems.getDefault().getPath(sharePath.toString(), filename);
        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(path.toString())))
        {
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ShareManager() {
    }
}
