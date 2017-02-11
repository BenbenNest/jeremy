package com.jeremy.lychee.widget.BookReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jeremy.lychee.R;
import com.jeremy.lychee.utils.ToastUtils;

import java.util.Date;

/**
 * Created by benbennest on 16/7/2.
 */
public class PageWidget extends View {
    private static final String TAG = "PageWidget";

    private Context mContext;
    private int mWidth, mHeight;

    private int mCornerX = 0; // 拖拽点对应的页脚
    private int mCornerY = 0;
    private Path mPath0;
    private Path mPath1;
    Bitmap mCurPageBackBitmap = null;

    PointF mTouch = new PointF(); // 拖拽点
    PointF mBezierStart1 = new PointF(); // 贝塞尔曲线起始点
    PointF mBezierControl1 = new PointF(); // 贝塞尔曲线控制点
    PointF mBeziervertex1 = new PointF(); // 贝塞尔曲线顶点
    PointF mBezierEnd1 = new PointF(); // 贝塞尔曲线结束点

    PointF mBezierStart2 = new PointF(); // 另一条贝塞尔曲线
    PointF mBezierControl2 = new PointF();
    PointF mBeziervertex2 = new PointF();
    PointF mBezierEnd2 = new PointF();

    float mMiddleX;
    float mMiddleY;
    float mDegrees;
    float mTouchToCornerDis;

    // for test
    float mMaxLength;
    int[] mBackShadowColors;
    int[] mFrontShadowColors;
    GradientDrawable mBackShadowDrawableLR;
    GradientDrawable mBackShadowDrawableRL;
    GradientDrawable mFolderShadowDrawableLR;
    GradientDrawable mFolderShadowDrawableRL;

    GradientDrawable mFrontShadowDrawableHBT;
    GradientDrawable mFrontShadowDrawableHTB;
    GradientDrawable mFrontShadowDrawableVLR;
    GradientDrawable mFrontShadowDrawableVRL;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;

    private PageFactory mPageFactory;
    public Bitmap mPrePageBitmap, mCurPageBitmap, mNextPageBitmap;
    private Corner mCorner;

    public PageWidget(Context context, int width, int height) {
        super(context);
        mContext = context;
        mWidth = width;
        mHeight = height;
        mMaxLength = (float) Math.hypot(mWidth, mHeight);
        mPath0 = new Path();
        mPath1 = new Path();
        createDrawable();
        // ---------------------------------------
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    }

    private void calcCornerXY(float x, float y) {
        if (x <= mWidth / 2)
            mCornerX = 0;
        else
            mCornerX = mWidth;
        if (y <= mHeight / 2)
            mCornerY = 0;
        else
            mCornerY = mHeight;

        if (mCornerX == 0 && mCornerY == 0) {
            mCorner = Corner.LeftTop;
        } else if (mCornerX == 0 && mCornerY > 0) {
            mCorner = Corner.LeftBottom;
        } else if (mCornerX > 0 && mCornerY == 0) {
            mCorner = Corner.RightTop;
        } else {
            mCorner = Corner.RightBottom;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mCanvas.drawColor(0xFFAAAAAA);
            mTouch.x = event.getX();
            mTouch.y = event.getY();
            this.postInvalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mCanvas.drawColor(0xFFAAAAAA);
            mTouch.x = event.getX();
            mTouch.y = event.getY();
            calcCornerXY(mTouch.x, mTouch.y);
            this.postInvalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mCanvas.drawColor(0xFFAAAAAA);
            mTouch.x = mCornerX;
            mTouch.y = mCornerY;
            if (event.getX() < mWidth / 2) {
                if (mPageFactory.getCurrentPage() == 0) {
                    ToastUtils.toast(mContext.getResources().getString(R.string.tip_first_page));
                    return true;
                } else {
                    mPageFactory.goPrePage();
//                    mPageFactory.onDraw(mPageFactory.getCurrentPage() - 1);
                }
            } else {
                if (mPageFactory.isLastPage()) {
                    ToastUtils.toast(mContext.getResources().getString(R.string.tip_last_page));
                    return true;
                } else {
                    mPageFactory.goNextPage();
//                    mPageFactory.onDraw(mPageFactory.getCurrentPage() + 1);
                }
            }
            setBitmaps(mPageFactory.mPrePageBitmap, mPageFactory.mCurPageBitmap, mPageFactory.mNextPageBitmap);
            this.postInvalidate();

        }
        // return super.onTouchEvent(event);
        return true;
    }

    public void setPageFactory(PageFactory pageFactory) {
        this.mPageFactory = pageFactory;
    }

    /**
     * Author :  hmg25
     * Version:  1.0
     * Description : 求解直线P1P2和直线P3P4的交点坐标
     */
    public PointF getCross(PointF P1, PointF P2, PointF P3, PointF P4) {
        PointF CrossP = new PointF();
        // 二元函数通式： y=ax+b
        float a1 = (P2.y - P1.y) / (P2.x - P1.x);
        float b1 = ((P1.x * P2.y) - (P2.x * P1.y)) / (P1.x - P2.x);

        float a2 = (P4.y - P3.y) / (P4.x - P3.x);
        float b2 = ((P3.x * P4.y) - (P4.x * P3.y)) / (P3.x - P4.x);
        CrossP.x = (b2 - b1) / (a1 - a2);
        CrossP.y = a1 * CrossP.x + b1;
        return CrossP;
    }

    private void calcPoints() {

        mMiddleX = (mTouch.x + mCornerX) / 2;
        mMiddleY = (mTouch.y + mCornerY) / 2;

        mBezierControl1.x = mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX);
        mBezierControl1.y = mCornerY;
        mBezierControl2.x = mCornerX;
        mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY);

        Log.i(TAG, "mTouchX  " + mTouch.x + "  mTouchY  " + mTouch.y);
        Log.i(TAG, "mBezierControl1.x  " + mBezierControl1.x + "  mBezierControl1.y  " + mBezierControl1.y);
        Log.i(TAG, "mBezierControl2.x  " + mBezierControl2.x + "  mBezierControl2.y  " + mBezierControl2.y);

        mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2;
        Log.v("start1.x", mBezierStart1.x + "");
        mBezierStart1.y = mCornerY;

        mBezierStart2.x = mCornerX;
        Log.v("start1.x", mBezierStart2.x + "");

        mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y)
                / 2;

        Log.i(TAG, "mBezierStart1.x  " + mBezierStart1.x + "  mBezierStart1.y  " + mBezierStart1.y);
        Log.i(TAG, "mBezierStart2.x  " + mBezierStart2.x + "  mBezierStart2.y  " + mBezierStart2.y);

        mTouchToCornerDis = (float) Math.hypot((mTouch.x - mCornerX), (mTouch.y - mCornerY));

        mBezierEnd1 = getCross(mTouch, mBezierControl1, mBezierStart1, mBezierStart2);
        mBezierEnd2 = getCross(mTouch, mBezierControl2, mBezierStart1, mBezierStart2);

        Log.i(TAG, "mBezierEnd1.x  " + mBezierEnd1.x + "  mBezierEnd1.y  " + mBezierEnd1.y);
        Log.i(TAG, "mBezierEnd2.x  " + mBezierEnd2.x + "  mBezierEnd2.y  " + mBezierEnd2.y);

	/*
     * mBeziervertex1.x 推导
	 * ((mBezierStart1.x+mBezierEnd1.x)/2+mBezierControl1.x)/2 化简等价于
	 * (mBezierStart1.x+ 2*mBezierControl1.x+mBezierEnd1.x) / 4
	 */
        mBeziervertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4;
        mBeziervertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4;
        mBeziervertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4;
        mBeziervertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4;

        Log.i(TAG, "mBeziervertex1.x  " + mBeziervertex1.x + "  mBeziervertex1.y  " + mBeziervertex1.y);
        Log.i(TAG, "mBeziervertex2.x  " + mBeziervertex2.x + "  mBeziervertex2.y  " + mBeziervertex2.y);

    }

    private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
        mPath0.reset();
        mPath0.moveTo(mBezierStart1.x, mBezierStart1.y);
        mPath0.quadTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x, mBezierEnd1.y);
        mPath0.lineTo(mTouch.x, mTouch.y);
        mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y);
        mPath0.quadTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x, mBezierStart2.y);
        mPath0.lineTo(mCornerX, mCornerY);
        mPath0.close();

        canvas.save();
        canvas.clipPath(path, Region.Op.XOR);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();
    }

    private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
        mPath1.reset();
        mPath1.moveTo(mBezierStart1.x, mBezierStart1.y);
        mPath1.lineTo(mBeziervertex1.x, mBeziervertex1.y);
        mPath1.lineTo(mBeziervertex2.x, mBeziervertex2.y);
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
        mPath1.lineTo(mCornerX, mCornerY);
        mPath1.close();

        mDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl1.x
                - mCornerX, mBezierControl2.y - mCornerY));
        float f5 = mTouchToCornerDis / 4;
        int leftx;
        int rightx;
        GradientDrawable mBackShadowDrawable;
        if (mCorner == Corner.LeftTop) {
            leftx = (int) (mBezierStart1.x - 1);
            rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4 + 1);
            mBackShadowDrawable = mBackShadowDrawableLR;
        } else if (mCorner == Corner.LeftBottom) {
            leftx = (int) (mBezierStart1.x - 1);
            rightx = (int) (mBezierStart1.x + mTouchToCornerDis / 4 + 1);
            mBackShadowDrawable = mBackShadowDrawableLR;
        } else if (mCorner == Corner.RightTop) {
            leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4 - 1);
            rightx = (int) mBezierStart1.x + 1;
            mBackShadowDrawable = mBackShadowDrawableRL;
        } else {
            leftx = (int) (mBezierStart1.x - mTouchToCornerDis / 4 - 1);
            rightx = (int) mBezierStart1.x + 1;
            mBackShadowDrawable = mBackShadowDrawableRL;
        }

        Log.i(TAG, "leftx  " + leftx + "   rightx  " + rightx);
        canvas.save();
        canvas.clipPath(mPath0);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.rotate(mDegrees, mBezierStart1.x, mBezierStart1.y);
        mBackShadowDrawable.setBounds(leftx,
                (int) mBezierStart1.y, rightx,
                (int) (mMaxLength + mBezierStart1.y));
        mBackShadowDrawable.draw(canvas);
        canvas.restore();
    }

    public void setBitmaps(Bitmap pre, Bitmap cur, Bitmap next) {

        mCurPageBitmap = cur;
//        mCurPageBackBitmap = bm2;
        mNextPageBitmap = next;
    }

    public void setCurrentBitmap(Bitmap curBitmap) {
        mCurPageBitmap = curBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0x00AAAAAA);
        calcPoints();
        drawCurrentPageArea(mCanvas, mCurPageBitmap, mPath0);
        drawNextPageAreaAndShadow(mCanvas, mNextPageBitmap);
//        drawCurrentPageShadow(mCanvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
    }


    /**
     * Author :  hmg25
     * Version:  1.0
     * Description : 创建阴影的GradientDrawable
     */
    private void createDrawable() {
        int[] color = {0x333333, 0xB0333333};
        mFolderShadowDrawableRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, color);
        mFolderShadowDrawableRL
                .setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFolderShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, color);
        mFolderShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowColors = new int[]{0xFF111111, 0x111111};
        mBackShadowDrawableRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
        mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mBackShadowDrawableLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowColors = new int[]{0x00888888, 0x888888};
        mFrontShadowDrawableVLR = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
        mFrontShadowDrawableVLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mFrontShadowDrawableVRL = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
        mFrontShadowDrawableVRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableHTB = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
        mFrontShadowDrawableHTB.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        mFrontShadowDrawableHBT = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
        mFrontShadowDrawableHBT.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }


    public void drawCurrentPageShadow(Canvas canvas) {

        //注：拖拽顶点的阴影，定位有问题，待修改~~
        double d1 = Math.atan2(mBezierControl1.y - mTouch.y, mTouch.x - mBezierControl1.x);
        // double d3 =(float)Math.cos(d1) *  25*1.414f;
        double d3 = (float) 25 / Math.cos(d1);
        //  float x = (float)(mTouch.x-d3);
        //   float y = mTouch.y -(float)Math.sin(d1) * 25*1.414f;
        float x = (float) (mTouch.x - d3);
        float y = (float) (mTouch.y - d3);
        mPath1.reset();
        mPath1.moveTo(x, y);
        mPath1.lineTo(mTouch.x, mTouch.y);
        mPath1.lineTo(mBezierControl1.x, mBezierControl1.y);
        mPath1.lineTo(mBezierStart1.x, mBezierStart1.y);
        mPath1.close();
        float rotateDegrees;
        canvas.save();

        canvas.clipPath(mPath0, Region.Op.XOR);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);
        int leftx;
        int rightx;
        GradientDrawable mCurrentPageShadow;
        if (mCorner == Corner.LeftTop) {
            leftx = (int) (mBezierControl1.x);
            rightx = (int) mBezierControl1.x + 25;
            mCurrentPageShadow = mFrontShadowDrawableVLR;

        } else if (mCorner == Corner.LeftBottom) {
            leftx = (int) (mBezierControl1.x);
            rightx = (int) mBezierControl1.x + 25;
            mCurrentPageShadow = mFrontShadowDrawableVLR;
        } else if (mCorner == Corner.RightTop) {
            leftx = (int) (mBezierControl1.x - 25);
            rightx = (int) mBezierControl1.x;
            mCurrentPageShadow = mFrontShadowDrawableVRL;
        } else {
            leftx = (int) (mBezierControl1.x - 25);
            rightx = (int) mBezierControl1.x;
            mCurrentPageShadow = mFrontShadowDrawableVRL;
        }
        rotateDegrees = (float) Math.toDegrees(Math.atan2(mTouch.x - mBezierControl1.x, mBezierControl1.y - mTouch.y));
        canvas.rotate(rotateDegrees, mBezierControl1.x, mBezierControl1.y);

        mCurrentPageShadow.setBounds(leftx, (int) (mBezierControl1.y - 500), rightx, (int) (mBezierControl1.y));
        mCurrentPageShadow.draw(canvas);
        canvas.restore();

        mPath1.reset();
        mPath1.moveTo(x, y);
        mPath1.lineTo(mTouch.x, mTouch.y);
        mPath1.lineTo(mBezierControl2.x, mBezierControl2.y);
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y);
        mPath1.close();
        canvas.save();
        canvas.clipPath(mPath0, Region.Op.XOR);
        canvas.clipPath(mPath1, Region.Op.INTERSECT);

        if (mCorner == Corner.LeftTop) {
            leftx = (int) (mBezierControl2.y);
            rightx = (int) (mBezierControl2.y + 25);
            mCurrentPageShadow = mFrontShadowDrawableHTB;
        } else if (mCorner == Corner.LeftBottom) {
            leftx = (int) (mBezierControl2.y);
            rightx = (int) (mBezierControl2.y + 25);
            mCurrentPageShadow = mFrontShadowDrawableHTB;
        } else if (mCorner == Corner.RightTop) {
            leftx = (int) (mBezierControl2.y - 25);
            rightx = (int) (mBezierControl2.y);
            mCurrentPageShadow = mFrontShadowDrawableHBT;
        } else {
            leftx = (int) (mBezierControl2.y - 25);
            rightx = (int) (mBezierControl2.y);
            mCurrentPageShadow = mFrontShadowDrawableHBT;
        }

        rotateDegrees = (float) Math.toDegrees(Math.atan2(mBezierControl2.y - mTouch.y, mBezierControl2.x - mTouch.x));
        canvas.rotate(rotateDegrees, mBezierControl2.x, mBezierControl2.y);
        mCurrentPageShadow.setBounds((int) (mBezierControl2.x - 500), leftx, (int) (mBezierControl2.x), rightx);
        mCurrentPageShadow.draw(canvas);
        canvas.restore();
    }


    private BookState mState;
    private Corner mSelectCorner;
    private Point aniStartPos;
    private Point aniStopPos;
    private Date aniStartTime;
    private long aniTime = 800;
    private long timeOffset = 10;
    private final int clickCornerLen = 250 * 250; // 50dip
    private static boolean closeBook = false;
    private float scrollX = 0, scrollY = 0;


    class BookOnGestureListener implements GestureDetector.OnGestureListener {
        public boolean onDown(MotionEvent event) {
            if (mState == BookState.ANIMATING)
                return false;
            float x = event.getX(), y = event.getY();
            int w = mWidth, h = mHeight;
            if (x * x + y * y < clickCornerLen) {
                if (mPageFactory.getCurrentPage() > 0) {
                    mSelectCorner = Corner.LeftTop;
                    aniStartPos = new Point(0, 0);
                }
            } else if ((x - w) * (x - w) + y * y < clickCornerLen) {
                if (mPageFactory.getCurrentPage() < 99) {
                    mSelectCorner = Corner.RightTop;
                    aniStartPos = new Point(mWidth, 0);
                }
            } else if (x * x + (y - h) * (y - h) < clickCornerLen) {
                if (mPageFactory.getCurrentPage() > 0) {
                    mSelectCorner = Corner.LeftBottom;
                    aniStartPos = new Point(0, mWidth);
                }
            } else if ((x - w) * (x - w) + (y - h) * (y - h) < clickCornerLen) {
                if (mPageFactory.getCurrentPage() < 99) {
                    mSelectCorner = Corner.RightBottom;
                    aniStartPos = new Point(mWidth, mHeight);
                }
            }
            if (mSelectCorner != Corner.None) {
                aniStopPos = new Point((int) x, (int) y);
                aniTime = 800;
                mState = BookState.ABOUT_TO_ANIMATE;
                closeBook = false;
                aniStartTime = new Date();
            }
            return false;
        }


        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling velocityX:" + velocityX + " velocityY:" + velocityY);
            if (mSelectCorner != Corner.None) {
                if (mSelectCorner == Corner.LeftTop) {
                    if (velocityX < 0) {
                        aniStopPos = new Point(0, 0);
                    } else {
                        aniStopPos = new Point(2 * mWidth, 0);
                    }
                } else if (mSelectCorner == Corner.RightTop) {
                    if (velocityX < 0) {
                        aniStopPos = new Point(-mWidth, 0);
                    } else {
                        aniStopPos = new Point(mWidth, 0);
                    }
                } else if (mSelectCorner == Corner.LeftBottom) {
                    if (velocityX < 0) {
                        aniStopPos = new Point(0, mHeight);
                    } else {
                        aniStopPos = new Point(2 * mWidth, mHeight);
                    }
                } else if (mSelectCorner == Corner.RightBottom) {
                    if (velocityX < 0) {
                        aniStopPos = new Point(-mWidth, mHeight);
                    } else {
                        aniStopPos = new Point(mWidth, mHeight);
                    }
                }
                Log.d(TAG, "onFling animate");
                aniStartPos = new Point((int) scrollX, (int) scrollY);
                aniTime = 1000;
                mState = BookState.ABOUT_TO_ANIMATE;
                closeBook = true;
                aniStartTime = new Date();
            }
            return false;
        }

        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress");
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mState = BookState.TRACKING;
            if (mSelectCorner != Corner.None) {
                scrollX = e2.getX();
                scrollY = e2.getY();
            }
            return false;
        }

        public void onShowPress(MotionEvent e) {
            Log.d(TAG, "onShowPress");
        }

        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp");

            if (mSelectCorner != Corner.None) {
                if (mSelectCorner == Corner.LeftTop) {
                    if (scrollX < mWidth / 2) {
                        aniStopPos = new Point(0, 0);
                    } else {
                        aniStopPos = new Point(2 * mWidth, 0);
                    }
                } else if (mSelectCorner == Corner.RightTop) {
                    if (scrollX < mWidth / 2) {
                        aniStopPos = new Point(-mWidth, 0);
                    } else {
                        aniStopPos = new Point(mWidth, 0);
                    }
                } else if (mSelectCorner == Corner.LeftBottom) {
                    if (scrollX < mWidth / 2) {
                        aniStopPos = new Point(0, mHeight);
                    } else {
                        aniStopPos = new Point(2 * mWidth, mHeight);
                    }
                } else if (mSelectCorner == Corner.RightBottom) {
                    if (scrollX < mWidth / 2) {
                        aniStopPos = new Point(-mWidth, mHeight);
                    } else {
                        aniStopPos = new Point(mWidth, mHeight);
                    }
                }
                aniStartPos = new Point((int) scrollX, (int) scrollY);
                aniTime = 800;
                mState = BookState.ABOUT_TO_ANIMATE;
                closeBook = true;
                aniStartTime = new Date();
            }
            return false;
        }


    }

}
