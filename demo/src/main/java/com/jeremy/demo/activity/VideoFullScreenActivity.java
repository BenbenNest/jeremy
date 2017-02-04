package com.jeremy.demo.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jeremy.demo.R;
import com.jeremy.library.jsobject.BrowserJsInject;

public class VideoFullScreenActivity extends AppCompatActivity {
    private WebView my_web;
    xWebChromeClient xwebchromeclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full_screen);
        my_web = (WebView) findViewById(R.id.my_web);
        initWebView();
        my_web.loadUrl("http://v.qq.com/iframe/player.html?vid=o0318tp1ddw&tiny=0&auto=0");
//        my_web.loadUrl("file:///android_asset/nio_content.html");

        my_web.addJavascriptInterface(new JsObject(VideoFullScreenActivity.this), "console");
//my_web.loadData(s, "text/html; charset=UTF-8", null);
    }

    private void initWebView() {
        WebSettings ws = my_web.getSettings();
/**
 * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
 * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
 * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
 * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
 * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
 * setSupportZoom 设置是否支持变焦
 * */
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setPluginState(WebSettings.PluginState.ON);
// settings.setPluginsEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setLoadWithOverviewMode(true);
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        my_web.setSaveEnabled(false);
        ws.setSaveFormData(false);
// 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(false);
        xwebchromeclient = new xWebChromeClient();
//setWebChromeClient主要处理解析，渲染网页等浏览器做的事情
//这个方法必须有，就算类中没有函数也可以，不然视频播放不了
        my_web.setWebChromeClient(xwebchromeclient);
//WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
        my_web.setWebViewClient(new xWebViewClientent());
    }

    /**
     * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
     *
     * @author
     */
    public class xWebChromeClient extends WebChromeClient {
    }

    /**
     * 设置监听事件
     * 处理各种通知、请求等事件
     *
     * @author
     */
    public class JsObject {
        Context mContext;

        JsObject(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void full() {
            System.out.println("返回结果");
            setFullScreen();
        }
    }

    /**
     * 设置全屏
     */
    private void setFullScreen() {
        Log.i("视频全屏-->", "竖屏切换到横屏");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
// 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

// 全屏下的状态码：1098974464
// 窗口下的状态吗：1098973440
    }

    public class xWebViewClientent extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {

            view.loadUrl(BrowserJsInject.fullScreenByJs(url));
            System.out.println("url1:" + BrowserJsInject.fullScreenByJs(url));
            System.out.println("url2" + url);

        }


    }


}
