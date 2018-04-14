package com.jeremy.library.utils.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by changqing on 2018/4/14.
 */

public class EncryptUtils {
    //SUN提供的常用的算法: MD2 MD5 SHA-1 SHA-256 SHA-384 SHA-512
    public static final String KEY_SHA = "SHA-1";
    public static final String KEY_MD5 = "MD5";

    //public void update(byte[] input) 使用指定的 byte 数组更新摘要。
    //public byte[] digest() 通过执行诸如填充之类的最终操作完成哈希计算。在调用此方法之后，摘要被重置。
    //public static boolean isEqual(byte[] digesta,byte[] digestb) 比较两个摘要的相等性。做简单的字节比较。

    /**
     * 获取一个文件的md5值(可处理大文件)
     *
     * @return md5 value
     */
    public static String getMD5(File file) {
        FileInputStream fis = null;
        BigInteger bi = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance(KEY_MD5);
            fis = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            byte[] digest = MD5.digest();
            bi = new BigInteger(1, digest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bi.toString(16);
    }

    public static byte[] getMD5(byte[] bytes) {
        byte[] digest = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance(KEY_MD5);
            MD5.update(bytes);
            digest = MD5.digest();
        } catch (Exception e) {

        }
        return digest;
    }


}
