package com.jeremy.lychee.activity.function;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jeremy.lychee.R;
import com.jeremy.lychee.core.BaseToolbarActivity;
import com.jeremy.lychee.model.ModelBase;
import com.jeremy.lychee.model.function.impl.FunctionListModel;
import com.jeremy.lychee.model.news.NewsDetailContent;
import com.jeremy.lychee.net.RetroAdapter;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.FileUtil;
import com.jeremy.lychee.utils.ToastHelper;

import java.io.File;
import java.io.ObjectInputStream;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by benbennest on 16/6/5.
 */
public class NewsDetailActivity extends BaseToolbarActivity {
    @Bind(R.id.webview)
    WebView mWebView;
    private String fileName = "NewsDetail.json";
    private String html_url = "file:///android_asset/html/news_detail.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("新闻详情");
        initWebView(mWebView);
        mWebView.loadUrl(html_url);
//        saveNewsDetail();
        getNewsDetail();
//        getObject();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initWebView(WebView webView) {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (TextUtils.isEmpty(url)) {
                    return true;
                }
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    Intent intent = new Intent(getApplicationContext(), FunctionListModel.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        };
        mWebView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient = new WebChromeClient();
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.addJavascriptInterface(this, "$_news");
    }

    private void getObject() {
        if (AppUtil.isMounted()) {
            String dir = AppUtil.getAppSdRootPath();
            File Dir = new File(dir);
            if (!Dir.exists()) {
                Dir.mkdirs();
            }
            File file = new File(Dir, fileName);
            try {
//                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                ObjectInputStream objectInputStream = new ObjectInputStream(getResources().getAssets().open("cache/" + fileName));
                NewsDetailContent object = (NewsDetailContent) objectInputStream.readObject();
                objectInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    private void getNewsDetail() {

        NewsDetailContent detail = (NewsDetailContent) FileUtil.getObjectFromAssertFile(this, "cache/" + fileName);

    }

    private void saveNewsDetail() {
        RetroAdapter.getService().getNewsDetail()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ModelBase<NewsDetailContent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastHelper.getInstance(NewsDetailActivity.this).toast("获取新闻详情失败！");
                    }

                    @Override
                    public void onNext(ModelBase<NewsDetailContent> result) {
                        if (result.getErrno() == 0) {
                            NewsDetailContent detail = result.getData();
                            if (detail != null) {
                                FileUtil.saveObject2File(detail, fileName);
                            }
                        } else {
                            ToastHelper.getInstance(NewsDetailActivity.this).toast("获取新闻详情失败！");
                        }
                    }
                });


    }
}
