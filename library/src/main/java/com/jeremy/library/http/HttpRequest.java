package com.jeremy.library.http;

import java.util.Map;

public interface HttpRequest {
    void get(String url, Map<String,String> params,ICallback callback);

    void post(String url, Map<String,String> params, ICallback callback);
}
