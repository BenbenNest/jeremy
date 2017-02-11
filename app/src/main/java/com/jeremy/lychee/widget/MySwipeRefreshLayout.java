package com.jeremy.lychee.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.jeremy.lychee.utils.DensityUtils;

/**
 * Created by zhaozuotong on 2015/11/25.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    public MySwipeRefreshLayout(Context context) {
        super(context);
        initUI(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
    }
    private void initUI(Context context){
        setColorSchemeResources(com.jeremy.lychee.R.color.swipe_refresh_color);
    }

    /**
     * TODO:临时对策：
     * 用户中心首页的RecyclerView的顶部缩进问题
     */
    public void setRefreshBarOffset(int dp){
        getChildAt(0).setY(DensityUtils.dip2px(getContext(), dp));
    }
}
