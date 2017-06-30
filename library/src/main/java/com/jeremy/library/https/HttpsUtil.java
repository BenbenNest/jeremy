package com.jeremy.library.https;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by didi on 2017/6/4.

 java 是java j2sdk 中的类库，也就是Java Development kit 。 它提供也一些基础的东西，如io库、桌面程序的类库，如awt。集合库（如Collection、List、Map）。等这些最基础的类库

 javax是java的扩展包,如j2ee 中的类库，包括servlet，jsp，ejb，数据库相关的一些东西，xml的等。

 你可以参考下面的链接，一个是j2sdk API、一个是J2EE API  一看，你就能更明白了。

 http://java.sun.com/j2se/1.4.2/docs/api/
 http://java.sun.com/j2ee/1.4/docs/api/

 java和javax都是Java的API包，java是核心包，javax的x是extension的意思，也就是扩展包，它和java各有各的用处
 */

public class HttpsUtil {
    private HttpsUtil(){}

    /**
     * 生成SSLSocketFactory
     * @param certificates  证书，不可为空
     * @return  {@link SSLSocketFactory}
     */
    public static SSLSocketFactory getSslSocketFactory(InputStream certificates) {

        return getSslSocketFactory(certificates,null,null);
    }

    /**
     * 生成SSLSocketFactory
     * @param certificates  证书，不可为空
     * @param key           keystore
     * @param keyPassword   keystore密码
     * @return  {@link SSLSocketFactory}
     */
    public static SSLSocketFactory getSslSocketFactory
    (InputStream certificates, InputStream key, String keyPassword)
    {
        SSLContext sslContext = null;

        Certificate ca = getCertificate(certificates);
        if(ca == null){return null;}

        try
        {
            TrustManagerFactory tmf = getTrustManagerFactory(ca);

            KeyManagerFactory kmf = null;
            if(key != null && keyPassword != null)
            {
                kmf = getKeyManagerFactory(key,keyPassword);
            }

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf == null ? null:kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return sslContext != null? sslContext.getSocketFactory():null;
    }

    /**
     * 获得证书实体
     * @param certificate 证书
     * @return  {@link Certificate},如果输入流错误就返回null
     */
    private static Certificate getCertificate(InputStream certificate)
    {
        Certificate ca = null;
        try
        {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            try {
                ca = certificateFactory.generateCertificate(certificate);
            } finally {
                certificate.close();
            }
        }
        catch (CertificateException | IOException e)
        {
            e.printStackTrace();
        }

        return ca;
    }

    /**
     * 获取{@link TrustManagerFactory}（信任证书管理）
     * @param ca 证书实体
     * @return {@link TrustManagerFactory},如果输入流错误就返回null
     */
    private static TrustManagerFactory getTrustManagerFactory(Certificate ca)
    {
        TrustManagerFactory tmf = null;
        try {
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null,null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmf;
    }

    /**
     * 获取{@link KeyManagerFactory}（Keystote管理）
     * @param key           keystore文件流（.bks）
     * @param keyPassword   keystore密码
     * @return  {@link KeyManagerFactory},如果输入流错误或密码错误就返回null
     */
    private static KeyManagerFactory getKeyManagerFactory(InputStream key,String keyPassword)
    {
        KeyManagerFactory kmf = null;

        try {
            String keyStoreType = "BKS";
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(key, keyPassword.toCharArray());

            String kmfAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
            kmf = KeyManagerFactory.getInstance(kmfAlgorithm);
            kmf.init(keyStore,keyPassword.toCharArray());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return kmf;
    }
}
