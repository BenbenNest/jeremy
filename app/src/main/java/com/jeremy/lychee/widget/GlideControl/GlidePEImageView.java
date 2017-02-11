package com.jeremy.lychee.widget.GlideControl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.fmsirvent.ParallaxEverywhere.PEWImageView;

public class GlidePEImageView extends PEWImageView {

    public static final int PARALLAX_VERTICAL = 0;
    public static final int PARALLAX_HORIZONTAL = 1;
    private GlideImageLoader mLoader;
    private int mOrientation;
    private float mScale = Float.parseFloat(
            getContext().getResources().getString(com.jeremy.lychee.R.string.parallax_img_scale));

    /**
     * PEWImageView正常显示需以下条件：
     * 1.设置的img的大小必须>(=也不行)ImageView
     * 2.img的长宽比必须>(=也不行)ImageView的长宽比
     * 以上条件若不满足，即使设置CenterCrop也无法正常显示（无法铺满ImageView）
     * 故无法保证以上条件满足时，可以通过以下变量的设置，可以强制满足
     */
    private int mWidth = -1, mHeight = -1;

    public GlidePEImageView setImageLoadSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public GlidePEImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLoader = new GlideImageLoader(this);
        setReverseX(true);
        setReverseY(true);
        setInterpolator(new AccelerateDecelerateInterpolator());
        setScaleType(ScaleType.CENTER_CROP);
    }

    @Deprecated
    public GlidePEImageView setParallaxEnabled(boolean val){
        /**
         * 看项目源码blockParallaxX这blockParallaxY貌似没有被使用，
         * 所以此方法可能无效。
         * 此方法暂时也不会调用，将来需要调用时，需要修改项目源码
         */
        setBlockParallaxX(!val);
        setBlockParallaxY(!val);
        return this;
    }

    public GlidePEImageView setParallaxOrientation(int val){
        setBlockParallaxX(false);
        setBlockParallaxY(false);
        if (val == PARALLAX_HORIZONTAL) {
            setBlockParallaxY(true);
        } else {
            setBlockParallaxX(true);
        }
        mOrientation = val;
        return this;
    }

    public GlidePEImageView setParallaxScale(float val) {
        if (val >= 1.0f) {
            mScale = val;
        }
        return this;
    }

    public boolean loadImage(String url, GlideImageLoader.OnGlideLoadListener listener) {
        mLoader.loadImage(url, listener);
        return true;
    }

    public boolean loadImage(String url) {
        if (mOrientation == PARALLAX_VERTICAL) {
            return mLoader.loadImage(url, (req, v) -> req
                    .crossFade()
//                    .override(getOverrideWidth(), (int) (getOverrideHeight() * mScale))
                    .into(v));
        } else {
            return mLoader.loadImage(url, (req, v) -> req
                    .crossFade()
//                    .override((int) (getOverrideWidth() * mScale), getOverrideHeight())
                    .into(v));
        }

    }
//
//    private int getOverrideWidth () {
//        return mWidth == -1 ? mLoader.getLoadWidth() : mWidth;
//    }
//    private int getOverrideHeight() {
//        return mHeight == -1 ? mLoader.getLoadHeight() : mHeight;
//    }


}
