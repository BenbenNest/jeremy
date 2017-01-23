package com.jeremy.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by benbennest on 17/1/22.
 */

public class SpringBackLayout extends LinearLayout {
    private int mMove;
    private int yDown, yMove;
    private int i = 0;

    public SpringBackLayout(Context context) {
        super(context);
    }

    public SpringBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpringBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yDown = y;
                break;
            case MotionEvent.ACTION_MOVE:
                yMove = y;
                mMove = yMove - yDown;
                i += mMove/2;
                layout(getLeft(), getTop() + mMove/2, getRight(), getBottom() + mMove/2);
                break;
            case MotionEvent.ACTION_UP:
                layout(getLeft(), getTop() - i, getRight(), getBottom() - i);
                i = 0;
                break;
        }

        return true;
    }
}
