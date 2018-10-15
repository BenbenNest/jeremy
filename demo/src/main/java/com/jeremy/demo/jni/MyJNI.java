package com.jeremy.demo.jni;

/**
 * Created by changqing on 2017/12/29.
 */

public class MyJNI {

    static {
        try {
            //加载打包完毕的 so类库
            System.loadLibrary("myjni");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static native String getStringFromC();

    public static native String helloworld();

}
