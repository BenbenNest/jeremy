package com.jeremy.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;

public class LargeImageView extends View implements GestureDetector.OnGestureListener {
    private final String TAG = this.getClass().getSimpleName();

    private BitmapRegionDecoder mDecoder;

    //绘制的区域
    private volatile Rect mRect = new Rect();

    private int mScaledTouchSlop;

    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    //图片的宽度和高度
    private int mImageWidth, mImageHeight;
    //手势控制器
    private GestureDetector mGestureDetector;
    private BitmapFactory.Options options;

    public LargeImageView(Context context) {
        super(context);
        init(context, null);
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LargeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //设置显示图片的参数，如果对图片质量有要求，就选择ARGB_8888模式
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;


        mScaledTouchSlop = ViewConfiguration.get(getContext())
                .getScaledTouchSlop();
        Log.d(TAG, "sts:" + mScaledTouchSlop);
        //初始化手势控制器
        mGestureDetector = new GestureDetector(context, this);

        //获取图片的宽高
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open("timg.jpg");
            //初始化BitmapRegionDecode，并用它来显示图片
            mDecoder = BitmapRegionDecoder
                    .newInstance(is, false);
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // 设置为true则只获取图片的宽高等信息，不加载进内存
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把触摸事件交给手势控制器处理
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mLastX = (int) e.getRawX();
        mLastY = (int) e.getRawY();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int x = (int) e2.getRawX();
        int y = (int) e2.getRawY();
        move(x, y);
        return true;
    }

    /**
     * 移动的时候更新图片显示的区域
     *
     * @param x
     * @param y
     */
    private void move(int x, int y) {
        int deltaX = x - mLastX;
        int deltaY = y - mLastY;
        Log.d(TAG, "move, deltaX:" + deltaX + " deltaY:" + deltaY);
        //如果图片宽度大于屏幕宽度
        if (mImageWidth > getWidth()) {
            //移动rect区域
            mRect.offset(-deltaX, 0);
            //检查是否到达图片最右端
            if (mRect.right > mImageWidth) {
                mRect.right = mImageWidth;
                mRect.left = mImageWidth - getWidth();
            }

            //检查左端
            if (mRect.left < 0) {
                mRect.left = 0;
                mRect.right = getWidth();
            }
            invalidate();
        }
        //如果图片高度大于屏幕高度
        if (mImageHeight > getHeight()) {
            mRect.offset(0, -deltaY);

            //是否到达最底部
            if (mRect.bottom > mImageHeight) {
                mRect.bottom = mImageHeight;
                mRect.top = mImageHeight - getHeight();
            }

            if (mRect.top < 0) {
                mRect.top = 0;
                mRect.bottom = getHeight();
            }
            //重绘
            invalidate();
        }
        mLastX = x;
        mLastY = y;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        mLastX = (int) e.getRawX();
        mLastY = (int) e.getRawY();Glide.with(null).load(null).into(null).
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int x = (int) e2.getRawX();
        int y = (int) e2.getRawY();
        move(x, y);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //显示图片
        Bitmap bm = mDecoder.decodeRegion(mRect, options);
        canvas.drawBitmap(bm, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认显示图片的中心区域，开发者可自行选择
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
    }
}
