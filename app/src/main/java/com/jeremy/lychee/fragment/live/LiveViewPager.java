package com.jeremy.lychee.fragment.live;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by chengyajun on 2016/1/12.
 */
public class LiveViewPager extends ViewPager {

    private boolean isCanScroll = true;

    public LiveViewPager(Context context) {
        super(context);
    }

    public LiveViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return isCanScroll;
    }


}

