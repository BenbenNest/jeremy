package com.jeremy.lychee.widget.GlideTransforms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideBorderCircleTransform extends BitmapTransformation {
    private int mBorderColor;
    private float mBorder;

    public GlideBorderCircleTransform(Context context, int color, float border) {
        super(context);
        mBorderColor = color;
        mBorder = border;
    }

    @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);

        //draw border
        Paint borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setColor(mBorderColor);
        float r = size / 2f;
        canvas.drawCircle(r,  r,  r, borderPaint);

        //draw content
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        borderPaint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        float cr = (size - mBorder) / 2f;
        canvas.drawCircle(r, r, cr, paint);

        return result;
    }

    @Override public String getId() {
        return getClass().getName();
    }
}
