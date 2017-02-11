package com.jeremy.lychee.customview.live;

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LoadingStateDelegator {
    @IntDef({LOADING_STATE.STATE_LOADING, LOADING_STATE.STATE_LOAD_SUCCEED, LOADING_STATE.STATE_LOAD_FAILED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LOADING_STATE {
        int STATE_LOADING       = 0;
        int STATE_LOAD_SUCCEED  = 1;
        int STATE_LOAD_FAILED   = 2;
    }

    private View viewHolder[] = new View[3];
    public LoadingStateDelegator(View normalView, View loadingView, View errorView) {
        viewHolder[0] = loadingView;
        viewHolder[1] = normalView;
        viewHolder[2] = errorView;
    }

    public void setViewState(@LOADING_STATE int viewState) {
        if (viewState<0 || viewState>=viewHolder.length) {
            return;
        }

        for (View v : viewHolder) {
            v.setVisibility(View.GONE);
        }

        viewHolder[viewState].setVisibility(View.VISIBLE);
    }
}
