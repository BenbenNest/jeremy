package com.jeremy.demo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.jeremy.demo.R;
import com.jeremy.library.https.HttpsUtils;

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
import javax.net.ssl.TrustManagerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HttpsActivity extends AppCompatActivity {

    @BindView(R.id.https1)
    Button https1;
    @BindView(R.id.https2)
    Button https2;
    @BindView(R.id.https3)
    Button https3;
    @BindView(R.id.webview)
    WebView webview;

    private static final String TYPE = "X.509";
    private final String mSogouUrl = "https://account.sogou.com/web/webLogin";
    private final String mAlipayUrl = "https://www.alipay.com/";
    private final String m12306Url = "https://kyfw.12306.cn/otn/";
    private static final String PROTOCOL = "TLS";

    private String encoder = "utf-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_https);
        ButterKnife.bind(this);
        initToolbar();
        initWebView();
    }

    private void initToolbar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initWebView() {
        webview.getSettings().setDefaultTextEncodingName(encoder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.https1, R.id.https2, R.id.https3})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.https1:
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            return HttpsUtils.runWithHttpsUrlConnection(HttpsActivity.this, mSogouUrl);
//                            return HttpsUtils.runAllWithHttpsUrlConnection(mSogouUrl);
                        } catch (Exception e) {
                            return e.toString();
                        }

                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
//                        webview.loadData(s, "text/html", encoder);
                        webview.loadDataWithBaseURL("", s, "text/html", "UTF-8", "");
//                        tvContent.setText(Html.fromHtml(s));
                    }
                }.execute();
                break;
            case R.id.https2:
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
//                            return HttpsUtils.runWithHttpClient(HttpsActivity.this, mAlipayUrl);
                            return HttpsUtils.runAllWithHttpClient(mAlipayUrl);
                        } catch (Exception e) {
                            return e.toString();
                        }

                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
//                        webview.loadData(s, "text/html", encoder);
                        webview.loadDataWithBaseURL("", s, "text/html", "UTF-8", "");


//                        tvContent.setText(Html.fromHtml(s));
                    }
                }.execute();
                break;
            case R.id.https3:
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            return visit12306();
                        } catch (Exception e) {
                            return e.toString();
                        }
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
//                        webview.loadData(s, "text/html", encoder);
                        webview.loadDataWithBaseURL("", s, "text/html", "UTF-8", "");
//                        tvContent.setText(Html.fromHtml(s));
                    }
                }.execute();
                break;
        }
    }

    /**
     * HttpUrlConnection 方式，支持指定load-der.crt证书验证，此种方式Android官方建议
     */
    public String visit12306() throws CertificateException, IOException, KeyStoreException,
            NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance(TYPE);
        InputStream in = getAssets().open("srca.cer");
        Certificate cartificate = cf.generateCertificate(in);

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(null, null);
        keystore.setCertificateEntry("trust", cartificate);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);

        SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        URL Url = new URL(m12306Url);
        HttpsURLConnection urlConnection = (HttpsURLConnection) Url.openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

        InputStream input = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }


}
