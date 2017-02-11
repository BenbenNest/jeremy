package com.jeremy.lychee.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by wangp on 15-11-26.
 */
public class ViewPagerDotIndicator extends LinearLayout implements ViewPager.OnPageChangeListener{

    private Paint paint = new Paint();
    private ViewPager mPager;
    private static final int SCROLL_STATE_IDLE = 0;
    private static final int SCROLL_STATE_DRAGGING = 1;
    private static final int SCROLL_STATE_SETTLING = 2;
    public void setViewPager(ViewPager pager){
        mPager = pager;
        pager.addOnPageChangeListener(this);
        setVisibility(GONE);
    }


    private Animator mAnimator;

    public ViewPagerDotIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //height 25dp
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                (int) (25 * context.getResources().getDisplayMetrics().density + 0.5f)));

        mAnimator
                = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).setDuration(1000);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (mState != SCROLL_STATE_IDLE) {
                    mAnimator.cancel();
                    setAlpha(1f);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (mState != SCROLL_STATE_IDLE) {
                    setAlpha(1f);
                } else {
                    setVisibility(GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
    });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawDot(canvas);
    }

    private void drawDot(Canvas canvas) {
        canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        int count = 0;
        if (mPager.getAdapter() != null) {
            count = mPager.getAdapter().getCount();
        }
        int select = mPager.getCurrentItem();
        float density = getContext().getResources().getDisplayMetrics().density;
        int itemWidth = (int) (11 * density);
        int itemHeight = itemWidth / 2;
        int x = (getWidth() - count * itemWidth * 2) / 2;
        int y = itemWidth;
        int minItemHeight = (int) ((float) itemHeight * 0.8F);
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < count; i++) {
            if (select == i) {
                paint.setColor(0xFFFFFFFF);
                canvas.drawCircle(x + itemWidth * i * 2 + itemWidth / 2, y, minItemHeight, paint);
            } else {
                paint.setColor(0x60000000);
                canvas.drawCircle(x + itemWidth * i * 2 + itemWidth / 2, y, minItemHeight, paint);
            }
        }
        canvas.restore();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
            setAlpha(1f);
        }
    }

    @Override
    public void onPageSelected(int position) {
        hide();
    }

    private int mState;
    @Override
    public void onPageScrollStateChanged(int state) {
        mState = state;
        switch (state) {
            case SCROLL_STATE_DRAGGING:
            case SCROLL_STATE_SETTLING:
                reDraw();
                break;
            case SCROLL_STATE_IDLE:
                hide();
                break;
        }
    }

    private void hide(){
        if (!mAnimator.isRunning() && getVisibility() == VISIBLE) {
            mAnimator.start();
        }
    }

    private void reDraw() {
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        setAlpha(1f);
        setVisibility(VISIBLE);
        postInvalidate();
    }
}
