package com.jeremy.lychee.net;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.net.fixedgson.FixedGsonConverterFactory;
import com.jeremy.lychee.preference.AppConstant;
import com.jeremy.lychee.utils.AppUtil;
import com.jeremy.lychee.utils.SystemInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class UserCenterRetroAdapter {
    private static OldRetroApiService service = null;
    protected static final Object monitor = new Object();

    public static OldRetroApiService getUserCenterService() {
        synchronized (monitor) {
            if (service == null) {
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                clientBuilder.addInterceptor(chain -> {
                    HttpUrl.Builder builder = chain.request().url().newBuilder();
                    Map<String, String> params = getUserCenterCommonParasMap();
                    if (params != null) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            builder.addQueryParameter(entry.getKey(), entry.getValue());
                        }
                    }
                    HttpUrl url = builder.build();
                    Request request = chain.request().newBuilder().url(url).build();
                    return chain.proceed(request);
                }).build();

                if (SystemInfo.isDebugMode()) {
                    HttpLoggingInterceptorWithReq logging = new HttpLoggingInterceptorWithReq();
                    logging.setLevel(HttpLoggingInterceptorWithReq.Level.BODY);
                    clientBuilder.addInterceptor(logging);

                    clientBuilder.addNetworkInterceptor(new StethoInterceptor());
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .client(clientBuilder.build())
                        .baseUrl(OldRetroAdapter.HOST_IOS)
                        .addConverterFactory(FixedGsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                service = retrofit.create(OldRetroApiService.class);
            }
            return service;
        }
    }

    /**
     * @return 用户中心系统公共参数
     */
    public static Map<String, String> getUserCenterCommonParasMap() {
        Map<String, String> map = new HashMap<>();
        map.put("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        map.put("channel", "cid");
        map.put("os", Build.DISPLAY);
        map.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        map.put("os_type", "Android");
        map.put("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
        map.put("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        map.put("src", "lx_android");

        String time = String.valueOf(System.currentTimeMillis() / 1000L);
        map.put("u_time", time);
        return map;
    }

    public static String GetSign(Map<String, String> params) {
        Map<String, String> commonParams = getUserCenterCommonParasMap();
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.putAll(commonParams);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                treeMap.put(entry.getKey(), entry.getValue());
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("&");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("&");
        stringBuilder.append(AppConstant.APP_SECRET);
        String str = stringBuilder.toString();
        return AppUtil.getMD5code(str);
    }
}
