package com.jeremy.lychee.widget.GlideControl;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class GlideParallaxView extends ImageView {

    private GlideImageLoader mLoader;

    public GlideParallaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLoader = new GlideImageLoader(this);
        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                applyParallax();
            }
        };

        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                applyParallax();
            }
        };
    }

    public boolean loadImage(String url, GlideImageLoader.OnGlideLoadListener listener) {
        mLoader.loadImage(url, listener);
        return true;
    }

    public boolean loadImage(String url) {
        return mLoader.loadImage(url);
    }

    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = null;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = null;

    private void parallaxAnimation() {
        applyParallax();
    }

    private void applyParallax() {
        if (getParent() != null) {
            float offset = ((View) getParent()).getY();
            if (offset < 0) {
                setTranslationY((int) (-offset / 2));
            } else if (offset > 0) {
                setTranslationY(0);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);
        viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        parallaxAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        } else {
            viewTreeObserver.removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        }
        super.onDetachedFromWindow();
    }

}
