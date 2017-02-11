package com.jeremy.lychee.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by benbennest on 16/6/22.
 */
public class ScreenUtil {

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, height = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            height = context.getResources().getDimensionPixelSize(x);
            return height;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return height;
    }

}
