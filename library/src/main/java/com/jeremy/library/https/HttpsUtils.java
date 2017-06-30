package com.jeremy.library.https;

import android.content.Context;
import android.util.Config;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by benbennest on 16/10/13.

 (1)SSLContext: 此类的实例表示安全套接字协议的实现， 它是SSLSocketFactory、SSLServerSocketFactory和SSLEngine的工厂。

 (2)SSLSocket: 扩展自Socket

 (3)SSLServerSocket: 扩展自ServerSocket

 (4)SSLSocketFactory: 抽象类，扩展自SocketFactory, SSLSocket的工厂

 (5)SSLServerSocketFactory: 抽象类，扩展自ServerSocketFactory, SSLServerSocket的工厂

 (6)KeyStore: 表示密钥和证书的存储设施

 (7)KeyManager: 接口，JSSE密钥管理器

 (8)TrustManager: 接口，信任管理器(?翻译得很拗口)

 (9)X590TrustedManager: TrustManager的子接口，管理X509证书，验证远程安全套接字

 */
public class HttpsUtils {
    public static final String TAG = "HttpsUtils";
    private static final String TYPE = "X.509";
    private static final String PROTOCOL = "TLS";
    private String encoder = "utf-8";

    /**
     * HttpUrlConnection 方式，支持指定load-der.crt证书验证，此种方式Android官方建议
     */
    public static String runWithHttpsUrlConnection(Context context, String url) throws CertificateException, IOException, KeyStoreException,
            NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream in = context.getAssets().open("sogou.cer");//或者sogou.crt
        Certificate cartificate = cf.generateCertificate(in);

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(null, null);
        keystore.setCertificateEntry("trust", cartificate);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        URL Url = new URL(url);
        HttpsURLConnection urlConnection = (HttpsURLConnection) Url.openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

        InputStream input = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        if (Config.DEBUG) {
            Log.d(TAG, "runWithHttpsUrlConnection() called with: context = [" + context + "], url = [" + url + "]");
        }
        return result.toString();
    }

    /**
     * HttpUrlConnection支持所有Https免验证，不建议使用
     */
    public static String runAllWithHttpsUrlConnection(String url) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        URL Url = new URL(url);
        SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
        sslContext.init(null, new TrustManager[]{new TrustAllManager()}, null);
        HttpsURLConnection urlConnection = (HttpsURLConnection) Url.openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

        InputStream input = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = "";
        StringBuffer result = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    /**
     * HttpClient方式实现，支持所有Https免验证方式链接
     */
    public static String runAllWithHttpClient(String url) throws IOException {
        int timeOut = 30 * 1000;
        HttpParams param = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(param, timeOut);
        HttpConnectionParams.setSoTimeout(param, timeOut);
        HttpConnectionParams.setTcpNoDelay(param, true);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", TrustAllSSLSocketFactory.getDefault(), 443));
        ClientConnectionManager manager = new ThreadSafeClientConnManager(param, registry);
        DefaultHttpClient client = new DefaultHttpClient(manager, param);

        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    /**
     * HttpClient方式实现，支持验证指定证书
     */
    public static String runWithHttpClient(Context context, String url) throws IOException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance(TYPE);
            InputStream in = context.getAssets().open("alipay.cer");
            Certificate cartificate = cf.generateCertificate(in);

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("cer", cartificate);

            SSLSocketFactory socketFactory = new SSLSocketFactory(keystore);

            int timeOut = 30 * 1000;
            HttpParams param = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(param, timeOut);
            HttpConnectionParams.setSoTimeout(param, timeOut);
            HttpConnectionParams.setTcpNoDelay(param, true);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", socketFactory, 443));

            ClientConnectionManager manager = new ThreadSafeClientConnManager(param, registry);
            DefaultHttpClient client = new DefaultHttpClient(manager, param);

            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }


}
