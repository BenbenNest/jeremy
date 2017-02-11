package com.jeremy.lychee.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.jeremy.lychee.R;
import com.jeremy.lychee.utils.BlurUtil;

public class BlurBgLayout extends FrameLayout {

    private Bitmap mBgBmp;
    private float mTranslateY;

    public BlurBgLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //重写的ViewGroup必须设置一个background，否则onDraw不会被调用
        setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBgBmp == null) return;
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBgBmp, 0f, -mTranslateY, paint);
        canvas.drawARGB(0xd0, 0xa2, 0x9b, 0x98);//高斯模糊后二次加深
    }

    public void setBlurBg(Bitmap bmp) {
        try {
            mBgBmp = BlurUtil.blur(getContext(), bmp);
        } catch (OutOfMemoryError err) {
            Bitmap b = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas();
            c.setBitmap(b);
            c.drawColor(getResources().getColor(R.color.news_pop_win_bg));
            mBgBmp = b;
        }
    }

    public void setTranslateY(float val) {
        mTranslateY = val;
        invalidate();
    }
}
