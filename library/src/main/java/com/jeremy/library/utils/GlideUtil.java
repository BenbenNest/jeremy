package com.jeremy.library.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.jeremy.library.R;


/**
 * Created by benbennest on 16/8/25.
 */
public class GlideUtil {
    private static final String TAG = "GlideUtil";

    public static void display(ImageView imageView, String url) {
        displayUrl(imageView, url, R.drawable.img_default_gray);
    }

    public static void displayUrl(final ImageView imageView, String url, @DrawableRes int defaultImage) {
        if (imageView == null) {
            Log.e(TAG, "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = imageView.getContext();
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        try {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(defaultImage)
                    .crossFade()
                    .centerCrop()
                    .into(imageView)
                    .getSize(new SizeReadyCallback() {
                        @Override
                        public void onSizeReady(int width, int height) {
                            if (!imageView.isShown()) {
                                imageView.setVisibility(View.VISIBLE);
                            }
                        }
                    });


//                    .getSize((width, height) -> {
//                        if (!imageView.isShown()) {
//                            imageView.setVisibility(View.VISIBLE);
//                        }
//                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayNative(final ImageView imageView, @DrawableRes int resId) {
//        if (imageView == null) {
//            Log.e(TAG, "GlideUtils -> display -> imageView is null");
//            return;
//        }
//        Context context = imageView.getContext();
//        if (context instanceof Activity) {
//            if (((Activity) context).isFinishing()) {
//                return;
//            }
//        }
//        try {
//            Glide.with(context)
//                    .load(resId)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .crossFade()
//                    .centerCrop()
//                    .into(imageView)
//                    .getSize((width, height) -> {
//                        if (!imageView.isShown()) {
//                            imageView.setVisibility(View.VISIBLE);
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    public static

}


//
//    public static void displayCircleHeader(ImageView view, @DrawableRes int res) {
//        // 不能崩
//        if (view == null) {
//            Logger.e("GlideUtils -> display -> imageView is null");
//            return;
//        }
//        Context context = view.getContext();
//        // View你还活着吗？
//        if (context instanceof Activity) {
//            if (((Activity) context).isFinishing()) {
//                return;
//            }
//        }
//
//        try {
//            Glide.with(context)
//                    .load(res)
//                    .centerCrop()
//                    .placeholder(R.mipmap.img_default_gray)
//                    .bitmapTransform(new GlideCircleTransform(context))
//                    .crossFade()
//                    .into(view);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }