package com.jeremy.demo.net;

import android.os.Build;

import com.jeremy.demo.app.PackageUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;

abstract public class ServiceInfo {
    abstract public String getBaseUrl();

    abstract public Map<String, String> getCommonParameters();

    abstract public Map<String, String> getExtraParameters(Request request);

    abstract public Interceptor getExtraInterceptor();

//    public Converter.Factory getConverterFactory() {
//        return GsonConverterFactory.create();
//    }

    protected static Map<String, String> getBaseCommonParameters() {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("ver", String.valueOf(PackageUtils.getVersionCode()));
        mapParams.put("os", Build.DISPLAY);
        mapParams.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        mapParams.put("os_type", "Android");
//        mapParams.put("carrier", NetworkUtil.getCarrierName());
//        mapParams.put("token", DeviceUtil.getDeviceToken());
//        mapParams.put("sid", Session.getSid());
//        mapParams.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
//        mapParams.put("src", "lx_android");
//        mapParams.put("net", NetworkUtil.getNetworkTypeName());
        return mapParams;
    }

}
