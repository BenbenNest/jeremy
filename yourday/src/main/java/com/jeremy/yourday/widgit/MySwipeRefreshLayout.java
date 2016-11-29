package com.jeremy.yourday.widgit;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.jeremy.yourday.R;
import com.jeremy.yourday.util.DensityUtils;

/**
 * Created by benbennest on 16/8/16.
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

    private void initUI(Context context) {
        setColorSchemeResources(R.color.swipe_refresh_color);
    }

    /**
     * TODO:临时对策：
     * 用户中心首页的RecyclerView的顶部缩进问题
     */
    public void setRefreshBarOffset(int dp) {
        getChildAt(0).setY(DensityUtils.dip2px(getContext(), dp));
    }
}
