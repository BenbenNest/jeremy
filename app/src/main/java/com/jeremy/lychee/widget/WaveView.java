package com.jeremy.lychee.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by benbennest on 16/7/28.
 */
public class WaveView extends View {
    private String TAG = getClass().getSimpleName();
    static final int LEFT = 0, RIGHT = 1;
    private Paint mPaint, mTextPaint;
    private Path mPath;
    private int mWidth, mHeight;
    private int mControlX, mControlY;
    private int mDirection = LEFT;
    private String text = "text change with the wave !";
    private int mTextPos;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        // 实例化画笔并设置参数
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0xFFA2D6AE);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(40);
        // 实例化路径对象
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.v(TAG, "onSizeChanged");
        mWidth = w;
        mHeight = h;
        mControlX = 0;
        mTextPos = (int) (mWidth - mTextPaint.measureText(text)) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.v(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.v(TAG, "onLayout");
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        Log.v(TAG, "layout");
        super.layout(l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v(TAG, "onDraw");
        super.onDraw(canvas);
        mPath.moveTo(-mWidth / 4, mHeight / 16 * 15);
        mPath.quadTo(mControlX, mHeight / 4 * 3, mWidth + mWidth / 4, mHeight / 16 * 15);
        canvas.drawTextOnPath(text, mPath, mTextPos + mWidth / 4, 0, mTextPaint);

        mPath.lineTo(mWidth + mWidth / 4, mHeight);
        mPath.lineTo(-mWidth / 4, mHeight);
        canvas.drawPath(mPath, mPaint);

        if (mDirection == LEFT) {
            if (mControlX + 5 < mWidth + mWidth / 4) {
                mControlX += 5;
            } else {
                mDirection = RIGHT;
                mControlX -= 5;
            }
        } else {
            if (mControlX > -mWidth / 4 + 5) {
                mControlX -= 5;
            } else {
                mDirection = LEFT;
                mControlX += 5;
            }
        }
        mPath.reset();
        invalidate();
    }


}
