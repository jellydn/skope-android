package vn.dev.Skope.apis;

import vn.dev.Skope.utils.Helpers;

/**
 * Created by tientun on 3/4/15.
 */

public class TCryptUtil {
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
    public static String padString(String source, int size) {
        char paddingChar = ' ';
        int x = source.length() % size;
        int padLength = size - x;
        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }
        return source;
    }
    public static byte[] getKeyBytes(String keyString){
        String hash = Helpers.md5(keyString);
        return hexToBytes(hash);
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }
        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++) {
            if ((data[i] & 0xFF) < 16)
                str = str + "0" + Integer.toHexString(data[i] & 0xFF);
            else
                str = str + Integer.toHexString(data[i] & 0xFF);
        }
        return str;
    }
}
