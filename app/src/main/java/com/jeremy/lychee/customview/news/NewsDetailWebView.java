package com.jeremy.lychee.customview.news;

import android.content.Context;
import android.util.AttributeSet;

import com.jeremy.lychee.customview.webview.SafeWebView;

public class NewsDetailWebView extends SafeWebView {
    public NewsDetailWebView(Context context) {
        super(context);
    }

    public NewsDetailWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
