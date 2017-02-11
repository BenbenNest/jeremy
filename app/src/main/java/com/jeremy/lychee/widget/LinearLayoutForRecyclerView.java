
package com.jeremy.lychee.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jeremy.lychee.R;


public class LinearLayoutForRecyclerView extends LinearLayout {
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private Drawable mDivider;
    private final static int DIVIDER_HEIGHT = 1;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public LinearLayoutForRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 构造函数 用于实例化
    public LinearLayoutForRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 构造函数 用于XML注册组件
    public LinearLayoutForRecyclerView(Context context) {
        super(context);
    }

    /**
     * 绑定布局
     */
    private void bindLinearLayout() {
        int count = adapter.getItemCount();
        if (count == 0) {
            return;
        }
        LayoutParams params;
        if (getOrientation() == LinearLayout.VERTICAL) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        } else {
            // params = new LayoutParams(LayoutParams.WRAP_CONTENT,
            // LayoutParams.MATCH_PARENT);
            params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        }
        for (int i = 0; i < count; i++) {
            int type = adapter.getItemViewType(i);
            RecyclerView.ViewHolder holder = adapter.createViewHolder(this, type);
            adapter.onBindViewHolder(holder, i);
            View v = holder.itemView;
            if (v != null) {
                addView(v, params);
            }
        }
    }


    /**
     * 设置数据
     *
     * @param adpater
     */
    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adpater) {
        this.adapter = adpater;
        removeAllViews();
        bindLinearLayout();
    }

    public void drawVertical(Canvas c) {

        int left = getPaddingLeft();
        int right = getWidth() - getPaddingRight();

        int childCount = getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + DIVIDER_HEIGHT;

            if(mDivider==null){
                mDivider = new ColorDrawable(getResources().getColor(R.color.list_divider));
            }
            mDivider.setBounds(left, top, right, bottom);
            c.save();
            c.clipRect(left, top, right, bottom);
            mDivider.draw(c);
            c.restore();
        }

    }

    public void drawHorizontal(Canvas c) {
        int top = getPaddingTop();
        int bottom = getHeight() - getPaddingBottom();

        int childCount = getChildCount();
        for (int i = 0; i < childCount-1; i++) {
            View child = getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + DIVIDER_HEIGHT;

            if(mDivider==null){
                mDivider = new ColorDrawable(getResources().getColor(R.color.list_divider));
            }
            mDivider.setBounds(left, top, right, bottom);

            c.save();
            c.clipRect(left, top, right, bottom);
            mDivider.draw(c);
            c.restore();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (getOrientation() == LinearLayout.VERTICAL) {
            drawVertical(canvas);
        } else {
            drawHorizontal(canvas);
        }
    }
}
