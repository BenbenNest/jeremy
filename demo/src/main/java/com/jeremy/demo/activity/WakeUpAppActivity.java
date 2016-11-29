package com.jeremy.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jeremy.demo.R;

public class WakeUpAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup_app);

        loadHtml();

    }

    public void loadHtml()
    {
        WebView webview = new WebView(this);
        WebSettings wSet = webview.getSettings();
        wSet.setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/a.html");
        setContentView(webview);
    }



}
