package com.jeremy.demo.net;

public class CommonServiceInfo extends ServiceInfo {
//    public static final String BASE_URL = "http://gank.io/api/";
//
//    @Override
//    public String getBaseUrl() {
//        return BASE_URL;
//    }
//
//    @Override
//    public Map<String, String> getCommonParameters() {
//        return ServiceInfo.getBaseCommonParameters();
//    }
//
//    @Override
//    public Map<String, String> getExtraParameters(Request request) {
//        if (request == null) {
//            return null;
//        }
//
//        TreeMap<String, String> paramsMap = new TreeMap<>();
//        if (request.body() != null && request.body() instanceof FormBody) {
//            FormBody formBody = (FormBody) request.body();
//            for (int i = 0; i < formBody.size(); i++) {
//                paramsMap.put(URLDecoder.decode(formBody.encodedName(i)), URLDecoder.decode(formBody.encodedValue(i)));
//            }
//        }
//        if (request.url() != null) {
//            for (int i = 0; i < request.url().querySize(); i++) {
//                paramsMap.put(request.url().queryParameterName(i), request.url().queryParameterValue(i));
//            }
//        }
//
//        StringBuilder stringBuilder = new StringBuilder();
//        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
//            stringBuilder.append(entry.getKey());
//            stringBuilder.append("=");
//            stringBuilder.append(entry.getValue() != null ? entry.getValue() : "");
//            stringBuilder.append("&");
//        }
//        if (stringBuilder.length() > 0) {
//            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        }
//        stringBuilder.append("shi!@#$%^&*[xian!@#]*");
////        String md5Str = CryptoUtil.MD5Hash(stringBuilder.toString());
//
//        Map<String, String> resultMap = new HashMap<>();
////        resultMap.put("sign", md5Str != null ? md5Str.substring(3, 10) : "");
//        return resultMap;
//    }
//
//    @Override
//    public Interceptor getExtraInterceptor() {
//        return null;
//    }
}
