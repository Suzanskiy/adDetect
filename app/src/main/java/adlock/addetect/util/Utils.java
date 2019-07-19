package krow.dev.addetector.util;

import android.support.v4.view.MotionEventCompat;
import java.io.ByteArrayInputStream;

public class Utils {
    private static final String[] hexStr = new String[]{"A", "B", "C", "D", "E", "F"};
    public static boolean isProduction = false;

    public static String toHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        String hex = "";
        for (byte b : bytes) {
            int num = b & MotionEventCompat.ACTION_MASK;
            int div = num / 16;
            int rem = num % 16;
            if (div > 9) {
                hex = new StringBuilder(String.valueOf(hex)).append(" 0x").append(hexStr[div - 10]).toString();
            } else {
                hex = new StringBuilder(String.valueOf(hex)).append(" 0x").append(div).toString();
            }
            if (rem > 9) {
                hex = new StringBuilder(String.valueOf(hex)).append(hexStr[rem - 10]).toString();
            } else {
                hex = new StringBuilder(String.valueOf(hex)).append(rem).toString();
            }
        }
        return hex;
    }

    public static int toInt(byte[] bytes, boolean isBigEndian) {
        int x = 0;
        int numOfBytes = bytes.length;
        if (numOfBytes > 4) {
            numOfBytes = 4;
        }
        for (int i = 0; i < numOfBytes; i++) {
            if (i == 0) {
                if (isBigEndian) {
                    x = bytes[i] & MotionEventCompat.ACTION_MASK;
                } else {
                    x = bytes[numOfBytes - 1] & MotionEventCompat.ACTION_MASK;
                }
            } else if (isBigEndian) {
                x = (x << 8) | (bytes[i] & MotionEventCompat.ACTION_MASK);
            } else {
                x = (x << 8) | (bytes[(numOfBytes - 1) - i] & MotionEventCompat.ACTION_MASK);
            }
        }
        return x;
    }

    public static long toLong(byte[] bytes, boolean isBigEndian) {
        long x = 0;
        int numOfBytes = bytes.length;
        if (numOfBytes > 8) {
            numOfBytes = 8;
        }
        for (int i = 0; i < numOfBytes; i++) {
            if (i == 0) {
                if (isBigEndian) {
                    x = (long) (bytes[i] & MotionEventCompat.ACTION_MASK);
                } else {
                    x = (long) (bytes[numOfBytes - 1] & MotionEventCompat.ACTION_MASK);
                }
            } else if (isBigEndian) {
                x = (x << 8) | ((long) (bytes[i] & MotionEventCompat.ACTION_MASK));
            } else {
                x = (x << 8) | ((long) (bytes[(numOfBytes - 1) - i] & MotionEventCompat.ACTION_MASK));
            }
        }
        return x;
    }

    public static String toString(byte[] charBuf, boolean isBigEndian) throws Exception {
        StringBuffer strBuf = new StringBuffer();
        byte[] buf_2 = new byte[2];
        ByteArrayInputStream in = new ByteArrayInputStream(charBuf);
        while (in.read(buf_2) != -1) {
            int code = toInt(buf_2, isBigEndian);
            if (code == 0) {
                break;
            }
            strBuf.append((char) code);
        }
        return strBuf.toString();
    }
}
