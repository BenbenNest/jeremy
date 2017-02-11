package com.jeremy.lychee.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.jeremy.lychee.widget.GlideControl.GlideImageView;

public class CustomRatioImageView extends GlideImageView {

    float heightRatio = -1;
    float widthRatio = -1;

    public CustomRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, com.jeremy.lychee.R.styleable.CustomRatioImageView);
        heightRatio = typedArray.getFloat(com.jeremy.lychee.R.styleable.CustomRatioImageView_height_ratio, -1f);
        widthRatio = typedArray.getFloat(com.jeremy.lychee.R.styleable.CustomRatioImageView_width_ratio, -1);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthRatio < 1 || heightRatio < 1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        try {
            int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int)(measuredWidth * heightRatio / widthRatio);
            setMeasuredDimension(measuredWidth, height);
        } catch (Exception e) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
