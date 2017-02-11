package com.jeremy.lychee.net;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jeremy.lychee.utils.DeviceUtil;
import com.jeremy.lychee.utils.logger.Logger;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.SystemInfo;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.schedulers.Schedulers;

public class CustomGlideModule implements GlideModule {

    //retry constants
    private final static int CONNECT_TIMEOUT_MILLIS = 5000;
    private final static int READ_TIMEOUT_MILLIS = 5000;
    private final static int MAX_RETRY_COUNT = 3;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true);

        /**
         * stetho
         */
        if (SystemInfo.isDebugMode()) {
            clientBuilder.addNetworkInterceptor(new StethoInterceptor());
            clientBuilder.addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request requestWithUserAgent = originalRequest.newBuilder()
                        .header("User-Agent", "bjtime")
                        .build();
                return chain.proceed(requestWithUserAgent);
            });
        }

        /**
         * load failed retry
         */
        clientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            Response response = null;
            boolean responseOK = false;
            int tryCount = 0;

            long ts = 0;
            if (SystemInfo.isDebugMode()) {
                ts = System.currentTimeMillis();
            }

            while (!responseOK && tryCount <= MAX_RETRY_COUNT) {
                try {
                    response = chain.proceed(request);
                    responseOK = response.isSuccessful();
                } catch (Exception e) {
                    Logger.t("CustomGlideModule")
                            .e("img load retry(" + tryCount + "):" + request.url().toString());
                } finally {
                    tryCount++;
                }
            }

            //统计图片请求时长
            if (SystemInfo.isDebugMode() && response != null) {
                StringBuilder sb = new StringBuilder();
                //时长
                sb.append(System.currentTimeMillis() - ts);
                sb.append(",");
                //size
                sb.append(response.body().contentLength());
                sb.append(",");
                //url
                sb.append(request.url().toString());
                sb.append(",");
                //网络类型
                sb.append(AppUtil.isNetTypeMobile(context) ? "mobile" : "wifi");
                sb.append(",");
                //网络详细
                sb.append(AppUtil.isNetTypeMobile(context) ? DeviceUtil.getProvidersName(context) : DeviceUtil.getWifiSSID(context));
                sb.append(",");
                //IP
                sb.append(AppUtil.isNetTypeMobile(context) ? DeviceUtil.getCarrierIpAddress() : DeviceUtil.getWifiIpAddress(context));
                OldRetroAdapter.getService()
                        .appLog("img_req_time_cost", sb.toString())
                        .subscribeOn(Schedulers.io())
                        .subscribe(s -> {
                                },
                                Throwable::printStackTrace);
            }

            // otherwise just pass the original response on
            return response;
        });

        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(clientBuilder.build()));
    }

}