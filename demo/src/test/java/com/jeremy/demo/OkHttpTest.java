package com.jeremy.demo;

import org.junit.Test;

/**
 * Created by changqing on 2018/1/22.
 */

public class OkHttpTest {

    @Test
    public void testdd(){

        String str1 = "\u6237";
        try {
            String str2 = new String(str1.getBytes("utf-8"));
            String ss = "\u6570\u636e\u672a\u5f55\u5165";
            String s = "\345\257\274\350\210\252\345\274\200\345\247\213\n";
            System.out.print(s+"");
            System.out.println(ss+"--");
            System.out.print(str2);
        }catch (Exception e){

        }

    }

//    @Test
//    public void test() {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url("").build();
//
//
//    }


}
