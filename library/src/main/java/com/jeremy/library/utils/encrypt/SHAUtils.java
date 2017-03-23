package com.jeremy.library.utils.encrypt;

import java.security.MessageDigest;

/**
 * Created by changqing.zhao on 2017/3/23.
 */
public class SHAUtils {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

}

