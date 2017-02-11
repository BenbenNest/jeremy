package com.jeremy.lychee.widget.GlideControl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GlideImageView extends ImageView {

    private GlideImageLoader mLoader;

    public GlideImageView(Context context) {
        super(context);
        mLoader = new GlideImageLoader(this);
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLoader = new GlideImageLoader(this);
    }

    public boolean loadImage(String url, GlideImageLoader.OnGlideLoadListener listener) {
        return mLoader.loadImage(url, listener);
    }

    public boolean loadImageOriginalSize(String url, GlideImageLoader.OnGlideLoadListener listener) {
        return mLoader.loadImage(url, listener, false);
    }

    public boolean loadImage(String url) {
        return  mLoader.loadImage(url);
    }


}
