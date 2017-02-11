/**
 *
 */

package com.jeremy.lychee.activity.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jeremy.lychee.customview.news.NewsDetailWebView;
import com.jeremy.lychee.R;
import com.jeremy.lychee.model.news.NewsListDataWrapper;
import com.jeremy.lychee.widget.slidingactivity.SlidingActivity;

public class WebViewActivity extends SlidingActivity {
    private static final String BLANK_URL = "about:blank";
    private ViewGroup mViewGroup;
    private WebView mWebView;
    private ProgressBar mWebLoadingProgressBar;
    private View mWebNetError;
    private boolean mLoadError;

    protected int getLayoutId(){
        return R.layout.activity_search_detail;
    }

    protected WebView createWebView(){
        return new NewsDetailWebView(this);
    }
    @Override
    protected void setContentView() {
        setContentView(getLayoutId());
        initToolbar();

        mViewGroup = (ViewGroup) findViewById(R.id.search_detail_container);
        mWebNetError = findViewById(R.id.web_net_error);
        mWebView = createWebView();
        mWebLoadingProgressBar = (ProgressBar) findViewById(R.id.web_load_progress);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewGroup.addView(mWebView, 0, params);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

        WebSettings setting = mWebView.getSettings();
        String ua = getPackageName();
        setting.setUserAgentString(setting.getUserAgentString() + " " + ua);
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(true);
        setting.setDisplayZoomControls(false);
        setting.setSupportZoom(false);

        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        setting.setDomStorageEnabled(true);

        try {
            WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if(TextUtils.isEmpty(url)){
            NewsListDataWrapper mNewsEntity = getIntent().getParcelableExtra(SceneDirectSeedingActivity.NEW_ENTITY);
            url = mNewsEntity.getUrl();
        }
        mWebView.loadUrl(url);
    }


    private Toolbar mToolbar;

    public Toolbar configToolbar(int menuId, int titleId, int navigationIconId) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (menuId != 0) {
            toolbar.inflateMenu(menuId);
        }
        if (titleId != 0) {
            toolbar.setTitle(titleId);
        }
        if (navigationIconId != 0) {
            toolbar.setNavigationIcon(navigationIconId);
        }
        return toolbar;
    }
    private void initToolbar(){
//        int menu_id = R.menu.menu_webpage;
        mToolbar = configToolbar(0,0,R.mipmap.ic_nav_back);
        mToolbar.setNavigationOnClickListener(v -> finish());
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.article_icon_logo);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mToolbar.addView(imageView,params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
        	 try {
     			WebIconDatabase.getInstance().close();
     		} catch (Exception e) {
     			e.printStackTrace();
     		}
            mViewGroup.removeAllViews();
            mWebView.stopLoading();
            mWebView.clearCache(false);
            mWebView.destroyDrawingCache();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebNetError!= null && mWebNetError.getVisibility() == View.VISIBLE) {
            mWebNetError.setVisibility(View.GONE);
        }
        if(mWebView.canGoBack()){
            mWebView.goBack();
        } else {
            finish();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mWebNetError!=null && mWebNetError.getVisibility() == View.VISIBLE && !mLoadError) {
                mWebNetError.setVisibility(View.GONE);
            }
        }
		@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

        	if(url.toLowerCase().startsWith("http") || url.toLowerCase().startsWith("https") || url.toLowerCase().startsWith("file")){
                return false;
        	} else {
                try {
					Uri uri = Uri.parse(url);
					 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					 startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
                return true;
        	}
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            float height = mToolbar.getHeight();
//            float d = AppUtil.getDensity(WebViewActivity.this);
//            height = height/d-1;
//            mWebView.loadUrl("javascript:document.body.style.marginTop=\"" + height + "px\"; void 0");

            super.onPageFinished(view, url);
        }

		@Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        	if(failingUrl.toLowerCase().startsWith("http") || failingUrl.toLowerCase().startsWith("https") || failingUrl.toLowerCase().startsWith("file")){
        		mLoadError = true;
        		super.onReceivedError(view, errorCode, description, failingUrl);
        		view.stopLoading();
        		view.clearView();
        		view.loadUrl(BLANK_URL);
        		if (mWebNetError!=null && mWebNetError.getVisibility() == View.GONE) {
        			mWebNetError.setVisibility(View.VISIBLE);
        		}
        	}
        }

        @Override
        public void onLoadResource(WebView view, String url) {
        	super.onLoadResource(view, url);
        }


        @Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
            if(handler!=null){
                handler.proceed(); // Ignore SSL certificate errors
            }
		}


    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mWebLoadingProgressBar.setProgress(newProgress);
            if (newProgress >= 90 && mWebLoadingProgressBar.getVisibility() == View.VISIBLE) {
                mWebLoadingProgressBar.setVisibility(View.GONE);
            }
        }


    }

    private void onNewNetAction() {
        mLoadError = false;
        if (mWebNetError.getVisibility() == View.VISIBLE) {
            mWebNetError.setVisibility(View.GONE);
        }
    }

    public WebView getWebView(){
        return mWebView;
    }
}
