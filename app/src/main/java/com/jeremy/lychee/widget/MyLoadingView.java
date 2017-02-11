package com.jeremy.lychee.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.R;

/**
 * Created by zhaozuotong on 2015/11/24.
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
        Logger.t("loading").d("start");
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.start();
    }

    public void stopLoading() {
        Logger.t("loading").d("stop");
        AnimationDrawable animationDrawable = (AnimationDrawable) getDrawable();
        animationDrawable.stop();
    }
}
