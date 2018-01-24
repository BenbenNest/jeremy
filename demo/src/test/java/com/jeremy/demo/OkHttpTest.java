package com.jeremy.demo;

import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by changqing on 2018/1/22.
 */

public class OkHttpTest {

    @Test
    public void test() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("").build();


    }


}
