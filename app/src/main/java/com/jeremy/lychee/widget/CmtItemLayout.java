package com.jeremy.lychee.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class CmtItemLayout extends RelativeLayout {
    public CmtItemLayout(Context context) {
        super(context);
    }

    public CmtItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CmtItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float lastX;
    private float lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                lastX = event.getX();
                lastY = event.getY();
                break;
        }
        return super.onTouchEvent(event);
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastY() {
        return lastY;
    }
}
