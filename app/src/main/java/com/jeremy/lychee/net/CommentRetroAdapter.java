package com.jeremy.lychee.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jeremy.lychee.net.fixedgson.FixedGsonConverterFactory;
import com.jeremy.lychee.utils.SystemInfo;

import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class CommentRetroAdapter {
    private static OldRetroApiService service = null;
    protected static final Object monitor = new Object();
    private static final String host = "http://gcs.so.com/";
//    private static final String host = "http://test.bjtime.so.com/";
//    private static final String host = "http://jy.comment.haosou.com/";

    public static OldRetroApiService getCommentService() {
        synchronized (monitor) {
            if (service == null) {
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                clientBuilder.addInterceptor(chain -> {
                    HttpUrl.Builder builder = chain.request().url().newBuilder();
                    Map<String, String> params = getUserCenterCommonParasMap();
                    if(params!=null){
                        for(Map.Entry<String, String> entry:params.entrySet()){
                            builder.addQueryParameter(entry.getKey(), entry.getValue());
                        }
                    }
                    HttpUrl url = builder.build();
                    Request request = chain.request().newBuilder().url(url).build();
                    return chain.proceed(request);
                });

                if (SystemInfo.isDebugMode()) {
                    HttpLoggingInterceptorWithReq logging = new HttpLoggingInterceptorWithReq();
                    logging.setLevel(HttpLoggingInterceptorWithReq.Level.BODY);
                    clientBuilder.addInterceptor(logging);

                    clientBuilder.addNetworkInterceptor(new StethoInterceptor());
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .client(clientBuilder.build())
                        .baseUrl(host)
                        .addConverterFactory(FixedGsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                service = retrofit.create(OldRetroApiService.class);
            }
            return service;
        }
    }

    /**
     *
     * @return 评论中心系统公共参数
     */
    public static Map<String, String> getUserCenterCommonParasMap() {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "25");
        map.put("src", "lx_android");
        return map;
    }

}
