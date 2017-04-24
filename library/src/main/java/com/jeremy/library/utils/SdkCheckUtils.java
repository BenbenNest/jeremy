package com.jeremy.library.utils;

import android.os.Build;

/**
 * Created by changqing.zhao on 2017/4/23.
 */
public class SdkCheckUtils {

    public static boolean isAndroid70() {
        return Build.VERSION.SDK_INT > 23;
    }

    public static boolean isAndroid60() {
        return Build.VERSION.SDK_INT > 22;
    }

    public static boolean isAndroid50() {
        return Build.VERSION.SDK_INT > 20;
    }

    public static boolean isAndroid44() {
        return Build.VERSION.SDK_INT > 19;
    }

}
