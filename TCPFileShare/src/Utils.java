import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by antonkov on 01.11.14.
 */
public class Utils {
    static final int LIST_REQUEST = 0x01;
    static final int GET_REQUEST = 0x02;
    static final int PUT_REQUEST = 0x03;
    static final int LIST_RESULT = 0x04;
    static final int GET_RESULT = 0x05;
    static final int ERROR = 0xFF;

    static final int FILE_NOT_FOUND = 0x01;
    static final int TOO_MANY_CONNECTIONS = 0x02;
    static final int MALFORMED_MESSAGE = 0x03;
    static final int INTERNAL_SERVER_ERROR = 0xFF;

    static String readNullTermString(InputStream input) throws IOException {
        ArrayList<Byte> bytes = new ArrayList<>();
        int c;
        while ((c = input.read()) != 0)
            bytes.add((byte)c);
        byte[] rawArray = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); ++i)
            rawArray[i] = bytes.get(i);
        return new String(rawArray);
    }

    static byte[] typeBytes(int type) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte)type;
        return bytes;
    }

    static public String MD5ToString(byte[] bytes) {
        byte[] b = new byte[bytes.length + 1];
        for (int i = 0; i < bytes.length; ++i)
            b[i + 1] = bytes[i];
        BigInteger number = new BigInteger(b);
        return number.toString(16);
    }

    static public byte[] getMD5FromStream(InputStream input) throws IOException {
        byte[] bytes = new byte[16];
        input.read(bytes);
        return bytes;
    }

    static int readInt(InputStream is) {
        int x = 0;
        try {
            byte[] fileSizeBytes = new byte[4];
            is.read(fileSizeBytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(fileSizeBytes);
            DataInputStream dis = new DataInputStream(bis);
            x = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return x;
    }

    static long readLong(InputStream is) {
        long x = 0;
        try {
            byte[] fileSizeBytes = new byte[8];
            is.read(fileSizeBytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(fileSizeBytes);
            DataInputStream dis = new DataInputStream(bis);
            x = dis.readLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return x;
    }

    static void writeInt(OutputStream os, int x) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeInt(x);
            os.write(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void writeLong(OutputStream os, long x) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeLong(x);
            os.write(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static byte[] calcMD5OfFile(InputStream input) {
        try {
            return DigestUtils.md5(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
