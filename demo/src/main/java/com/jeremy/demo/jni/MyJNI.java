package com.jeremy.demo.jni;

import com.morgoo.helper.Log;

/**
 * Created by changqing on 2017/12/29.
 */

public class MyJNI {

    static {
        try {
            //加载打包完毕的 so类库
            Log.v("jeremy_jni", "System.loadLibrary(\"myjni\")");
            System.loadLibrary("myjni");
            Log.v("jeremy_jni", "System.loadLibrary(\"myjni\")");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static native String getStringFromC();

}
