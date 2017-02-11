package com.jeremy.lychee.widget.GlideControl;

import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.jeremy.lychee.R;

import java.lang.ref.WeakReference;

public class GlideImageLoader {
    private boolean needCompress = true;
    private WeakReference<ImageView> mImageView;

    public GlideImageLoader(ImageView view) {
        mImageView = new WeakReference<>(view);
    }

    public interface OnGlideLoadListener {
        void onLoad(DrawableTypeRequest request, ImageView view);
    }

    public boolean loadImage(String url, OnGlideLoadListener listener, boolean compress) {
        this.needCompress = compress;
        return loadImage(url, listener);
    }

    public boolean loadImage(String url, OnGlideLoadListener listener) {
        if (mImageView == null || mImageView.get() == null) {
            return false;
        }

        if (listener == null) {
            return loadImage(url);
        }

        DrawableTypeRequest builder = null;
        if (needCompress) {
            builder = Glide
                    .with(mImageView.get().getContext())
                    .load(new CustomImageSizeGlideModule.CustomImageSizeModelFutureStudio(url));
        } else {
            builder = Glide
                    .with(mImageView.get().getContext())
                    .load(url);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //TODO:
            // glide在android6.0下使用crossFade会出现图片不能铺满ImageView的问题
            //6.0暂时禁用crossFade，今后解决
            builder.dontAnimate();
        } else {
            builder.crossFade();
        }
        listener.onLoad(builder, mImageView.get());
        return true;
    }

    public boolean loadImage(String url) {
        return loadImage(url, (req, v) -> {
            if (v == null || req == null) {
                return;
            }
            v.setScaleType(ImageView.ScaleType.CENTER);
            DrawableRequestBuilder builder = req
//                    .placeholder(v.getContext().getResources().getDrawable(R.drawable.placeholder_16_9))
                    .placeholder(R.color.image_default_color)
                    .centerCrop();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //TODO:
                // glide在android6.0下使用crossFade会出现图片不能铺满ImageView的问题
                //6.0暂时禁用crossFade，今后解决
                builder.dontAnimate();
            } else {
                builder.crossFade();
            }
            builder.into(v);
        });
    }

}
