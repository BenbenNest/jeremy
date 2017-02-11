package com.jeremy.lychee.net;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.net.fixedgson.FixedGsonConverterFactory;
import com.jeremy.lychee.utils.SystemInfo;
import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.preference.TestAddressPreference;
import com.jeremy.lychee.utils.AppUtil;

import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


public class OldRetroAdapter {
    private static OldRetroApiService service = null;
    protected static final Object monitor = new Object();
    public  static String HOST_IOS = "http://cmsapi.kandian.360.cn/";

    /**
     * 公共参数
     * 参数名      类型    是否必要     描述
     * ver        string   必填    版本号 默认是1
     * channel    string   必填    渠道号
     * os         string   必填    操作系统如iphone
     * os_ver     string   必填    操作系统版本
     * os_type    string   必填    Android或者ios
     * carrier    string   必填    网络运营商
     * token      string   必填    手机标识码
     * sid        string  非必填   登录后产生的分配的sid,这个以前没有，需要加上
     */

    public static OldRetroApiService getService() {
        synchronized (monitor) {
            if (service == null) {
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                clientBuilder.addInterceptor(chain -> {
                    HttpUrl url = chain.request().url()
                            .newBuilder()
                            .addQueryParameter("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())))
                            .addQueryParameter("channel", "cid")
                            .addQueryParameter("os", Build.DISPLAY)
                            .addQueryParameter("os_ver", String.valueOf(Build.VERSION.SDK_INT))
                            .addQueryParameter("os_type", "Android")
                            .addQueryParameter("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName())
                            .addQueryParameter("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()))
                            .addQueryParameter("sid", Session.getSid())
                            .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()/1000L))
                            .build();
                    Request request = chain.request().newBuilder().url(url).build();
                    return chain.proceed(request);
                });

                if (SystemInfo.isDebugMode()) {
                    HttpLoggingInterceptorWithReq logging = new HttpLoggingInterceptorWithReq();
                    logging.setLevel(HttpLoggingInterceptorWithReq.Level.BODY);
                    clientBuilder.addInterceptor(logging);
                    clientBuilder.addNetworkInterceptor(new StethoInterceptor());
                    clientBuilder.connectTimeout(2, TimeUnit.MINUTES);
                    clientBuilder.readTimeout(2, TimeUnit.MINUTES);
                }
                if (TestAddressPreference.getInstance().getIsTestAddress()){
                    HOST_IOS = "http://10.117.83.51:8080/";
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .client(clientBuilder.build())
                        .baseUrl(HOST_IOS)
//                        .baseUrl("http://10.117.83.51:8001/")
                        .addConverterFactory(FixedGsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                service = retrofit.create(OldRetroApiService.class);
            }
            return service;
        }
    }
}
