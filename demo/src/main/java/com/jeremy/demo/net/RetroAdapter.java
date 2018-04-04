package com.jeremy.demo.net;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;


/**
 * 官方文档说明，安卓开发应避免使用Enum（枚举类），因为相比于静态常量Enum会花费两倍以上的内存。
 * 参 http://developer.android.com/training/articles/memory.html#Overhead
 * 那么如果需要使用Enum应该怎么做呢？
 */
public class RetroAdapter {
    public final static int CONNECT_TIMEOUT_MILLIS = 10;
    public final static int READ_TIMEOUT_MILLIS = 10;

    @IntDef({ServiceType.GENERAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ServiceType {
        int GENERAL = 1;
    }

    private static final Object lock = new Object();
    private static final HashMap<Integer, RetroApiService> serviceMap = new HashMap<>();
    private static final HashMap<Integer, ServiceInfo> serviceInfoMap = new HashMap<>();

    static {
        registerServiceInfo(ServiceType.GENERAL, new CommonServiceInfo());

//        QEventBus.getEventBus().register(new Object() {
//            @SuppressWarnings("unused")
//            public void onEventMainThread(DebugNetworkConfig.OnTestServerPreferenceChanged event) {
//                synchronized (lock) {
//                    serviceMap.clear();
//                }
//            }
//        });
    }

    public static void registerServiceInfo(@ServiceType int serviceType, ServiceInfo serviceInfo) {
        synchronized (lock) {
            serviceInfoMap.put(serviceType, serviceInfo);
        }
    }

    public static ServiceInfo getServiceInfo(@ServiceType int serviceType) {
        synchronized (lock) {
            return serviceInfoMap.get(serviceType);
        }
    }

//    public static RetroApiService getService() {
//        return getService(ServiceType.GENERAL);
//    }
//
//    public static RetroApiService getService(@ServiceType int type) {
//        synchronized (lock) {
//            if (serviceMap.get(type) == null) {
//                serviceMap.put(type, createService(type));
//            }
//            return serviceMap.get(type);
//        }
//    }
//
//    private static RetroApiService createService(@ServiceType final int type) {
//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
//        clientBuilder.addNetworkInterceptor(new StethoInterceptor());
//        clientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Request.Builder builder = request.newBuilder();
//                HttpUrl.Builder urlBuilder = request.url().newBuilder();
//                Headers.Builder headersBuilder = request.headers().newBuilder();
//                Map<String, String> params = getServiceInfo(type).getCommonParameters();
//                if (params != null) {
//                    for (Map.Entry<String, String> entry : params.entrySet()) {
//                        urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
//                    }
//                }
//                builder = builder.url(urlBuilder.build());
//                return chain.proceed(builder.build());
//            }
//        });
//        clientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Request.Builder builder = request.newBuilder();
//                HttpUrl.Builder urlBuilder = request.url().newBuilder();
//                Headers.Builder headersBuilder = request.headers().newBuilder();
//                Map<String, String> extraParams = getServiceInfo(type).getExtraParameters(chain.request());
//                if (extraParams != null) {
//                    for (Map.Entry<String, String> entry : extraParams.entrySet()) {
//                        urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
//                    }
//                }
//                builder = builder.url(urlBuilder.build());
//                return chain.proceed(builder.build());
//            }
//        });
//
//        Interceptor extraInterceptor = getServiceInfo(type).getExtraInterceptor();
//        if (extraInterceptor != null) {
//            clientBuilder.addInterceptor(extraInterceptor);
//        }
////        if (BuildConfig.DEBUG) {
////            Interceptor debugHostInterceptor = DebugNetworkConfig.getInstance().addHostHeader(getServiceInfo(type).getBaseUrl());
//
////        }
//
////        if (SystemInfo.isDebugMode()) {
////            Interceptor debugHostInterceptor = DebugNetworkConfig.getInstance().addHostHeader(getServiceInfo(type).getBaseUrl());
////            if (debugHostInterceptor != null) {
////                clientBuilder.addInterceptor(debugHostInterceptor);
////            }
////
////            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
////            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
////            clientBuilder.addInterceptor(loggingInterceptor);
////            if (ContentApplicationLike.getLikeInstance().getStethoInterceptor() != null) {
////                clientBuilder.addNetworkInterceptor(ContentApplicationLike.getLikeInstance().getStethoInterceptor());
////            }
////        }
////
////        clientBuilder.cookieJar(BTimeCookieJar.getInstance());
//        clientBuilder.connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.SECONDS);
//        clientBuilder.readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.SECONDS);
//
//        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
//                .client(clientBuilder.build())
//                .baseUrl(getServiceInfo(type).getBaseUrl())
////                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
//
////        Converter.Factory converterFactory = getServiceInfo(type).getConverterFactory();
////        if (converterFactory != null) {
////            retrofitBuilder.addConverterFactory(converterFactory);
////        }
////
//        return retrofitBuilder.build().create(RetroApiService.class);
//    }

//    public static String getRequestUrl(@ServiceType int type) {
//        if (SystemInfo.isDebugMode() || QEventBus.getEventBus().getStickyEvent(ApplicationEvents.DebugMode.class) != null) {
//            return DebugNetworkConfig.getInstance().patchDomain(getServiceInfo(type).getBaseUrl());
//        } else {
//            return getServiceInfo(type).getBaseUrl();
//        }
//    }

//    public static String getHostHeader(@ServiceType int type) {
//        return DebugNetworkConfig.getInstance().getHostHeader(getServiceInfo(type).getBaseUrl());
//    }

//    private static Response proceedRequest(Interceptor.Chain chain, Func1<Interceptor.Chain, HttpUrl> cb) {
//        HttpUrl url = cb.call(chain);
//        Request request = chain.request().newBuilder().url(url).build();
//        Response response = null;
//        try {
//            response = chain.proceed(request);
//        } catch (Throwable ignored) {
//        }
//        return response;
//    }
}
