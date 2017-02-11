package com.jeremy.lychee.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.preference.NewsListPreference;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.utils.ThreadUtils;
import com.jeremy.lychee.utils.ToastHelper;
import com.jeremy.lychee.utils.dialog.EditTextDialog;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

import net.grandcentrix.tray.core.ItemNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateArticleActivity extends SlidingActivity implements View.OnFocusChangeListener {
    private static String CONTENT_CACHE_KEY = "ARTICLE_CONTENT_CACHE_KEY";
    private static String TITLE_CACHE_KEY = "ARTICLE_TITLE_CACHE_KEY";
    private boolean hasLoadFinished;//确保数据不会丢失
    @Bind(com.jeremy.lychee.R.id.toolbar)
    Toolbar mToolbar;

    @Bind(com.jeremy.lychee.R.id.options_lay)
    View options_lay;

    @Bind(com.jeremy.lychee.R.id.options_lay_divider)
    View options_lay_divider;

    @Bind(com.jeremy.lychee.R.id.title_et)
    EditText title_et;

    @Bind(com.jeremy.lychee.R.id.content_web)
    WebView content_web;

    private boolean isGoPublish;
    private boolean isSaveAndFinish;
    private boolean isSave;

    @Override
    protected void setContentView() {
        setContentView(com.jeremy.lychee.R.layout.activity_create_article);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        ButterKnife.bind(this);

        mToolbar.setTitle("创作文章");
        mToolbar.inflateMenu(com.jeremy.lychee.R.menu.menu_create_article);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        String editerUrl = "file:///android_asset/editor/ueditor/editor.html";
//        String editerUrl = "http://cmsapi.kandian.360.cn/static/app/html/editor.html";
        content_web.loadUrl(editerUrl);
        initWebView(content_web);

        title_et.setOnFocusChangeListener(this);
        content_web.setOnFocusChangeListener(this);
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initWebView(WebView mWebView) {
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                new Handler().postDelayed(() -> {
                    try {
                        //延迟防止数据加载失败
                        String title = NewsListPreference.getInstance().getString(TITLE_CACHE_KEY);
                        setTitle(title);
                    } catch (ItemNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        String value = NewsListPreference.getInstance().getString(CONTENT_CACHE_KEY);
                        setContent(value);
                    } catch (ItemNotFoundException e) {
                        e.printStackTrace();
                    }
                    hasLoadFinished = true;
                },100);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hasLoadFinished = true;
            }
        };
        mWebView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient = new WebChromeClient();
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.addJavascriptInterface(this, "$_editor");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 点击段落 btn
     */
    public void onClickBlock(View view) {
        String method = "javascript:noToolbar.execCommand( 'blockquote' );";
        exeJsMethod(method);
    }

    public void onClickLink(View view) {
        String title = getString(com.jeremy.lychee.R.string.link_title);
        String hint = getString(com.jeremy.lychee.R.string.link_hint);
        EditTextDialog dialog = new EditTextDialog(this);
        TextView titleView = dialog.getTitleView();
        titleView.setGravity(Gravity.CENTER);
        EditText contentView = dialog.getContentView();
        contentView.setGravity(Gravity.CENTER);

        dialog.setCustomTitle(title);
        dialog.setHint(hint);
        dialog.setSingleLine(true);
        dialog.setMaxLength(250);
        dialog.setOnConfirmListener(v -> {
                    String content = dialog.getContent();
                    String method = "javascript:noToolbar.execCommand( " +
                            "'link',{" +
                            "'href':'"+content+"'," +
                            "'textValue':'"+content+"'," +
                            "'target':'_self'" +
                            "});";
                    exeJsMethod(method);
                    dialog.dismiss();
                }
        )
                .setOnCancleListener(v -> dialog.dismiss())
                .show();

    }
    public void onClickLine(View view) {
        String method = "javascript:noToolbar.execCommand( 'horizontal');";
        exeJsMethod(method);
    }
    public void onClickDecimal(View view) {
        String method = "javascript:noToolbar.execCommand( 'insertunorderedlist','decimal');";
        exeJsMethod(method);
    }
    public void onClickDot(View view) {
        String method = "javascript:noToolbar.execCommand( 'insertunorderedlist','dot');";
        exeJsMethod(method);
    }
    public void onClickSave(MenuItem item) {
        if(hasLoadFinished){
            isSave = true;
            String method = "javascript:onSaveEditor();";
            exeJsMethod(method);
        }
    }

    public void onClickPublish(MenuItem item) {
        if(hasLoadFinished){
            isGoPublish = true;
            String method = "javascript:onSaveEditor();";
            exeJsMethod(method);
        }

    }

    private void setContent(String vlaue) {
        String method = "javascript:noToolbar.setContent('" + vlaue + "');";
        exeJsMethod(method);
    }
    private void setTitle(String title) {
        if(title_et==null||TextUtils.isEmpty(title)){
            return;
        }
        title_et.setText(title);
        title_et.setSelection(title.length());
    }



    private void exeJsMethod(String arg){
        if(content_web!=null){
            Logger.e("exeJsMethod: " + arg);
            content_web.loadUrl(arg);
        }
    }

    /**
     * 保存数据  并判断是否关闭界面或发布文章
     */
    @JavascriptInterface
    public void OnSendContent(String arg){
        if(isFinishing()){
            return;
        }
        Logger.e("OnSendContent: " + arg);
        saveArticleCache(CONTENT_CACHE_KEY, arg);
        String title = title_et.getText().toString();
        saveArticleCache(TITLE_CACHE_KEY, title);

        if(isGoPublish){
            isGoPublish = false;

            if(TextUtils.isEmpty(title)){
                ThreadUtils.runOnUiThread(() -> {
                    ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.title_must_not_null);
                });
                return;
            }
            if(TextUtils.isEmpty(arg)){
                ThreadUtils.runOnUiThread(() -> {
                    ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.article_must_not_null);
                });
                return;
            }
            Intent intent = new Intent(getApplicationContext(), PublishActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("article", arg);
            startActivityForResult(intent, 1);
        }

        if(isSaveAndFinish){
            isSaveAndFinish = false;
            finish();
        }

        if(isSave){
            isSave = false;
            ThreadUtils.runOnUiThread(() -> {
                ToastHelper.getInstance(getApplicationContext()).toast(com.jeremy.lychee.R.string.save_ok);
            });
        }
    }

    private void saveArticleCache(String cache_key, String value) {
        NewsListPreference.getInstance().saveStringValue(cache_key, value);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case com.jeremy.lychee.R.id.content_web:
                    options_lay.setVisibility(View.VISIBLE);
                    options_lay_divider.setVisibility(View.VISIBLE);
                    break;
                case com.jeremy.lychee.R.id.title_et:
                    options_lay.setVisibility(View.GONE);
                    options_lay_divider.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){
            saveArticleCache(CONTENT_CACHE_KEY, "");
            saveArticleCache(TITLE_CACHE_KEY, "");
            finish();
            Intent intent = new Intent(CreateArticleActivity.this, WeMediaChannelActivity.class);
            intent.putExtra("USER_ID", Session.getSession().getReal_uid());
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if(hasLoadFinished){
            isSaveAndFinish = true;
            String method = "javascript:onSaveEditor();";
            exeJsMethod(method);
            new Handler().postDelayed(() -> {
                if(!isFinishing()){
                    finish();
                }
            }, 500);
        } else {
            super.onBackPressed();
            Logger.e("OnSendContent: didn't load finished");
        }
    }
}
