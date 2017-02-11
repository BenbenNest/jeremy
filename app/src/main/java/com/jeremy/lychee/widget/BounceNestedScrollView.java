package com.jeremy.lychee.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wangp on 15-11-20.
 */
public class BounceNestedScrollView extends StableNestedScrollView {
    public BounceNestedScrollView(Context context) {
        super(context);
    }

    private static final int size = 4;
    private View inner;
    private float y;
    private Rect normal = new Rect();

    private static final boolean ANIM_ENABLE = true;

    public BounceNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMinimumHeight(800);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        inner = getChildAt(0);
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                /**
                 * size=4 表示 拖动的距离为屏幕的高度的1/4
                 */
                int deltaY = (int) (preY - nowY) / size;

                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                        return;
                    }
                    int yy = inner.getTop() - deltaY;

                    inner.layout(inner.getLeft(), yy, inner.getRight(),
                            inner.getBottom() - deltaY);
                }
                break;
            default:
                break;
        }
    }

    public void animation() {
        int start = inner.getTop();
        int end = normal.top;
        ObjectAnimator.ofFloat(inner, "translationY", start, end).setDuration(150).start();
        // 设置回到正常的布局位置
        if(!normal.isEmpty()){
            inner.layout(normal.left, normal.top, normal.right, normal.bottom);
            normal.setEmpty();
        }

//        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
//                normal.top);
//        ta.setDuration(180);
//        inner.startAnimation(ta);
//        // 设置回到正常的布局位置
//        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
//        normal.setEmpty();

    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        if (ANIM_ENABLE) {
            int offset = inner.getMeasuredHeight() - getHeight();
            int scrollY = getScrollY();
            if (scrollY == 0 || scrollY == offset) {
                return true;
            }
        }
        return false;
    }

}
