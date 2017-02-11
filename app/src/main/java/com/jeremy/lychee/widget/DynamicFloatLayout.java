package com.jeremy.lychee.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.jeremy.lychee.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by benbennest on 16/8/18.
 */
public class DynamicFloatLayout extends FrameLayout {

    private ArrayList<CirclePosition> positionList = new ArrayList<CirclePosition>();
    private int spacing = 30;

    public DynamicFloatLayout(Context context) {
        super(context);
    }

    public DynamicFloatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicFloatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = DeviceUtil.getScreenWidth(getContext());
        int height = DeviceUtil.getScreenHeight(getContext());
        CirclePosition position = new CirclePosition();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int l, t, r, b;
            if (i == 0) {
                l = right / 2;
                t = bottom / 2;
                r = right / 2 + child.getMeasuredWidth();
                b = bottom / 2 + child.getMeasuredHeight();
                position.setLeft(l);
                position.setTop(t);
                position.setRight(r);
                position.setBottom(b);
                child.layout(l, t, r, b);
            } else {
                position = getPosition(new Random().nextInt(width), new Random().nextInt(height), child);
                child.layout(position.getLeft(), position.getTop(), position.getRight(), position.getBottom());
            }
        }

    }

//    private boolean checkOverLay(CirclePosition position) {
//        for (CirclePosition p : positionList) {
////            简单判断
//            if(position.getLeft()>p.getLeft()&&position.getLeft()<)
//        }
//        return false;
//    }

//    private boolean checkPoint(int)

    private CirclePosition getPosition(int x, int y, View view) {
        CirclePosition position = new CirclePosition();
        position.setLeft(x - view.getMeasuredWidth() / 2);
        position.setTop(y - view.getMeasuredHeight() / 2);
        position.setRight(x + view.getMeasuredWidth() / 2);
        position.setBottom(y + view.getMeasuredHeight() / 2);
        return position;
    }

    static class CirclePosition {
        private int left;
        private int top;
        private int right;
        private int bottom;

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getRight() {
            return right;
        }

        public void setRight(int right) {
            this.right = right;
        }

        public int getBottom() {
            return bottom;
        }

        public void setBottom(int bottom) {
            this.bottom = bottom;
        }
    }
}
