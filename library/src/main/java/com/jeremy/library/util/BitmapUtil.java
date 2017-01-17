package com.jeremy.library.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by benbennest on 16/12/14.
 */

public class BitmapUtil {
    /**
     * Bitmap缩放
     */
    private static Bitmap scale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }


}
