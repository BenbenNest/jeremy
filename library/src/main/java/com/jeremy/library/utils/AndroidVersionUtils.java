package com.jeremy.library.utils;

import android.os.Build;

/**
 * Created by changqing.zhao on 2017/3/23.
 */

public class AndroidVersionUtils {

    /**
     * 判断android SDK 版本是否大于等于5.0
     *
     * @return
     */
    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


}
