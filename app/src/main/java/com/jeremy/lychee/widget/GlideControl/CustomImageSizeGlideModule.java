package com.jeremy.lychee.widget.GlideControl;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.module.GlideModule;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.ImageOptiUrl;

import java.io.File;
import java.io.InputStream;

public class CustomImageSizeGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        int cacheSize500MegaBytes = 524288000;
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSize500MegaBytes));
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, AppUtil.getAppSdRootPath() + "cache" + File.separator, cacheSize500MegaBytes));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(CustomImageSizeModel.class, InputStream.class, new CustomImageSizeModelFactory());
    }


    static private class CustomImageSizeModelFactory implements ModelLoaderFactory<CustomImageSizeModel, InputStream> {
        @Override
        public ModelLoader<CustomImageSizeModel, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new CustomImageSizeUrlLoader(context);
        }

        @Override
        public void teardown() {

        }
    }

    public interface CustomImageSizeModel {
        String requestCustomSizeUrl(int width, int height);
    }

    static public class CustomImageSizeUrlLoader extends BaseGlideUrlLoader<CustomImageSizeModel> {
        public CustomImageSizeUrlLoader(Context context) {
            super(context);
        }

        @Override
        protected String getUrl(CustomImageSizeModel model, int width, int height) {
            return model.requestCustomSizeUrl(width, height);
        }
    }

    static public class CustomImageSizeModelFutureStudio implements CustomImageSizeModel {
        String baseImageUrl;

        public CustomImageSizeModelFutureStudio(String baseImageUrl) {
            this.baseImageUrl = baseImageUrl;
        }

        @Override
        public String requestCustomSizeUrl(int width, int height) {
            return ImageOptiUrl.get(baseImageUrl, width, height);
        }
    }
}
