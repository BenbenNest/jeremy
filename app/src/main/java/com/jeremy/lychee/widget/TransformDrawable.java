package com.jeremy.lychee.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

/**
 * Created by wangp on 15-11-26.
 */
public class TransformDrawable extends Drawable {

    public static final int TRANSFORM_ROUNDRECT = 0;
    public static final int TRANSFORM_CIRCLE = 1;

    private Paint mPaint;
    private Bitmap mBitmap;

    private RectF rectF;
    private int mRadius;
    private int mType = TRANSFORM_ROUNDRECT;

    public TransformDrawable(Bitmap bitmap, int radius, int type) {
        mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP,
                TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        mRadius = radius;
        mType = type;
    }

    public TransformDrawable(Drawable drawable, int radius, int type) {
        this(drawableToBitmap(drawable) ,radius , type);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE
                        ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        switch (mType) {
            case TRANSFORM_ROUNDRECT:
                canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
                break;
            case TRANSFORM_CIRCLE:
                canvas.drawCircle(rectF.width() / 2, rectF.width() / 2, rectF.width() / 2, mPaint);
                break;
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}  