package com.jeremy.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jeremy.library.R;

/**
 * Created by zhaochangqing-pd on 2015/10/12.
 */
public class CircleAvatarView extends ImageView {
    private Context context;

    private Paint paint;

    private PorterDuffXfermode xfermode;

    private int diameter;

    private Bitmap bitmap;

    public CircleAvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        setDiameter(100); // 默认100dip
        bitmap = decodeBitmap(R.drawable.default_avatar); // 默认头像

        paint = new Paint();
        paint.setAntiAlias(true);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap == null) {
            return;
        }
        int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, diameter, diameter, null, saveFlags);
        paint.setColor(0xFF000000);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        paint.setXfermode(null);
        canvas.restore();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(diameter, diameter);
    }

    private Bitmap decodeBitmap(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm == null) return;
        if (bm.getWidth() != diameter) {
            int newWidth = diameter;
            int bmWidth = bm.getWidth();
            int bmHeight = bm.getHeight();

            float scaleWidth = ((float) newWidth) / bmWidth;
            float scaleHeight = ((float) newWidth) / bmHeight;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight, bmWidth / 2, bmHeight / 2);

            try {
                this.bitmap = Bitmap.createBitmap(bm, 0, 0, bmWidth, bmHeight,
                        matrix, true);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            this.bitmap = bm;
        }
        invalidate();
    }

    @Override
    public void setImageResource(int resId) {
        Bitmap bitmap = decodeBitmap(resId);
        setImageBitmap(bitmap);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        setImageBitmap(drawableToBitmap(drawable));
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable != null) {
            bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

    /**
     * * 设置头像直径
     *
     * @param diameterDip
     */
    public void setDiameter(int diameterDip) {
        this.diameter = dip2px(diameterDip);
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
