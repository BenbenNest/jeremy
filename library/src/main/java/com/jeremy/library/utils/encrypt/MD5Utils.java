package com.jeremy.library.utils.encrypt;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class MD5Utils {
    private static String KEY_MD5 = "MD5";

    public static String getMd5Hex(String message) {
        return TextUtils.isEmpty(message) ? "" : getMd5Hex(message.getBytes());
    }

    public static String getMd5Hex(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        String digest = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance(KEY_MD5);
            algorithm.reset();
            algorithm.update(bytes);
            digest = toHexString(algorithm.digest());
        } catch (Exception e) {

        }
        return digest;
    }

    //转为16进制
    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b & 0xff));
        }
        return hexString.toString();
    }

    public static boolean checkMD5(String md5, File file) {
        if (TextUtils.isEmpty(md5) || file == null || !file.canRead()) {
            return false;
        }

        String digest = getFileMd5Sum(file);
        if (digest == null) {
            return false;
        }
        return digest.equalsIgnoreCase(md5);
    }

    public static String getFileMd5Sum(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);

            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);

            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);

            // Fill to 32 chars
            value = String.format("%32s", value).replace(' ', '0');

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
