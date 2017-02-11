package com.jeremy.lychee.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by zhangqiang-s on 2014/7/14.
 */
public class GetTouchEventFrameLayout extends FrameLayout {
    private OnTouchListener delegatedTouchListener = null;
//    private int mMotionY = 0;

    public GetTouchEventFrameLayout(Context context) {
        super(context);
    }

    public GetTouchEventFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GetTouchEventFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (delegatedTouchListener != null) {
            delegatedTouchListener.onTouch(this, ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setDelegatedTouchListener(OnTouchListener delegatedTouchListener) {
        this.delegatedTouchListener = delegatedTouchListener;
    }
}
