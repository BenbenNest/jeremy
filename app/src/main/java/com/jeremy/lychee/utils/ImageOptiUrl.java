package com.jeremy.lychee.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageOptiUrl {
    public static float screenDensityFactor = 1.0f;

    // 这个函数很高概率会获取View的宽、高失败，尽量不要使用，目前留在这里仅用于兼容旧代码，后续会删除
    // 请使用GlideImageView来处理类似情况
    @Deprecated static public String get(String originalUrl, View view) {
        if (view == null || TextUtils.isEmpty(originalUrl)) {
            return originalUrl;
        }

        int width = view.getWidth();
        int height = view.getHeight();

        return get(originalUrl, width, height);
    }

    private static Pattern patternImgUrl = Pattern.compile("^(https?://[0-9a-zA-Z]+?.qhimg.com/dmfd)(/__[0-9]+?)?(/.*?)(\\.png|\\.jpg|\\.webp)(.*)$(.?)");
    static public String get(String url, int width, int height) {
        if (width <= 0 || height <= 0 || TextUtils.isEmpty(url)) {
            return url;
        }

        try {
            Matcher matcher = patternImgUrl.matcher(url);
            if (!matcher.find()) {
                return url;
            }

            Uri uri = Uri.parse(url);
            float zoomFactor = 1.0f;
            String zoomFactorString = uri.getQueryParameter("zoom_out");
            if (!TextUtils.isEmpty(zoomFactorString)) {
                try {
                    zoomFactor = Integer.parseInt(zoomFactorString) / 100.0f;
                } catch (Throwable e) {
                    e.printStackTrace();
                    zoomFactor = 1.0f;
                }
            }
            zoomFactor *= screenDensityFactor;
            width *= zoomFactor;
            height *= zoomFactor;

            String host = matcher.group(1);
            String quality = matcher.group(2);
            if (quality != null) {
                quality = quality.replace("/__", "/" + width + "_" + height + "_");
            } else {
                quality = "/" + width + "_" + height + "_70";
            }
            String fileName = matcher.group(3);
            String params = matcher.group(5);

            return host + quality + fileName + ".webp" + params;
        } catch (Throwable e) {
            e.printStackTrace();
            return url;
        }
    }
}
