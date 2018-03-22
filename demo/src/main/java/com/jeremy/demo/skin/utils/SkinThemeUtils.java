package com.jeremy.demo.skin.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by changqing on 2018/3/22.
 */

public class SkinThemeUtils {

    public static int[] getResId(Context context, int[] attrs) {
        int[] resIds=new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIds;
    }


}
