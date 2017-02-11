package com.jeremy.lychee.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 添加可以设置drawable大小的功能
 * */
public class ImageTextView extends TextView {

    // 需要从xml中读取的各个方向图片的宽和高
    private int leftHeight = -1;
    private int leftWidth = -1;
    private int rightHeight = -1;
    private int rightWidth = -1;
    private int topHeight = -1;
    private int topWidth = -1;
    private int bottomHeight = -1;
    private int bottomWidth = -1;

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // TypeArray中含有我们需要使用的参数
        TypedArray a = context.obtainStyledAttributes(attrs,
                com.jeremy.lychee.R.styleable.ImageTextView, defStyle, 0);
        if (a != null) {
            for (int i = 0; i < a.getIndexCount(); i++) {
                int index = a.getIndex(i);
                switch (index) {
                    case com.jeremy.lychee.R.styleable.ImageTextView_bottom_height:
                        bottomHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case com.jeremy.lychee.R.styleable.ImageTextView_bottom_width:
                        bottomWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case com.jeremy.lychee.R.styleable.ImageTextView_left_height:
                        leftHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case com.jeremy.lychee.R.styleable.ImageTextView_left_width:
                        leftWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case com.jeremy.lychee.R.styleable.ImageTextView_right_height:
                        rightHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case com.jeremy.lychee.R.styleable.ImageTextView_right_width:
                        rightWidth = a.getDimensionPixelSize(index, -1);
                        break;
                    case com.jeremy.lychee.R.styleable.ImageTextView_top_height:
                        topHeight = a.getDimensionPixelSize(index, -1);
                        break;
                    case com.jeremy.lychee.R.styleable.ImageTextView_top_width:
                        topWidth = a.getDimensionPixelSize(index, -1);
                        break;
                }
            }

            // 获取各个方向的图片，按照：左-上-右-下 的顺序存于数组中
            Drawable[] drawables = getCompoundDrawables();
            int dir = 0;
            // 0-left; 1-top; 2-right; 3-bottom;
            for (Drawable drawable : drawables) {
                setImageSize(drawable, dir++);
            }
            setCompoundDrawables(drawables[0], drawables[1], drawables[2],
                    drawables[3]);

        }

    }

    /**
     * 设定图片的大小
     * */
    private void setImageSize(Drawable d, int dir) {
        if (d == null) {
            return;
        }

        int height = -1;
        int width = -1;
        // 根据方向给宽和高赋值
        switch (dir) {
            case 0:
                // left
                height = leftHeight;
                width = leftWidth;
                break;
            case 1:
                // top
                height = topHeight;
                width = topWidth;
                break;
            case 2:
                // right
                height = rightHeight;
                width = rightWidth;
                break;
            case 3:
                // bottom
                height = bottomHeight;
                width = bottomWidth;
                break;
        }
        // 如果有某个方向的宽或者高没有设定值，则不去设定图片大小
        if (width != -1 && height != -1) {
            d.setBounds(0, 0, width, height);
        }
    }
}
