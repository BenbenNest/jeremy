package com.jeremy.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jeremy.library.utils.encrypt.SignUtils;

/**
 * Created by changqing.zhao on 2017/3/24.
 */

public class DigtalSignActivity extends Activity {

    private String password = "123456";
    private String alias = "test";
    private String certificatePath = "test.crt";
    private String keyStorePath = "test.keystore";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String inputStr = "Ceritifcate";
        byte[] data = inputStr.getBytes();

        byte[] encrypt = SignUtils.encryptByPublicKey(DigtalSignActivity.this, data, certificatePath);

        byte[] decrypt = SignUtils.decryptByPrivateKey(DigtalSignActivity.this, encrypt, keyStorePath, alias, password);
        String outputStr = new String(decrypt);

        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

        if (data.equals(decrypt)) {

        }

        // 验证数据一致
//        assertArrayEquals(data, decrypt);

        // 验证证书有效
//        assertTrue(SignUtils.verifyCertificate(certificatePath));

    }

    public void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");

        String inputStr = "sign";
        byte[] data = inputStr.getBytes();

        byte[] encodedData = SignUtils.encryptByPrivateKey(data,
                keyStorePath, alias, password);

        byte[] decodedData = SignUtils.decryptByPublicKey(encodedData,
                certificatePath);

        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
//        assertEquals(inputStr, outputStr);

        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = SignUtils.sign(encodedData, keyStorePath, alias,
                password);
        System.err.println("签名:\r" + sign);

        // 验证签名
        boolean status = SignUtils.verify(encodedData, sign,
                certificatePath);
        System.err.println("状态:\r" + status);
//        assertTrue(status);

    }


}
