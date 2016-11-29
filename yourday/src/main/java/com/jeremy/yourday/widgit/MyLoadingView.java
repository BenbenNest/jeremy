package com.jeremy.yourday.widgit;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jeremy.yourday.R;

/**
 * Created by benbennest on 16/8/16.
 */
public class MyLoadingView extends ImageView {

    public MyLoadingView(Context context) {
        super(context);
        setImageResource(R.drawable.anim_loading);
        setScaleType(ScaleType.FIT_XY);
    }

    public MyLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.anim_loading);
        setScaleType(ScaleType.FIT_XY);
    }

    public MyLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setImageResource(R.drawable.anim_loading);
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        startLoading();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        stopLoading();
    }

    public void startLoading() {
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.start();
    }

    public void stopLoading() {
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.stop();
    }
}
