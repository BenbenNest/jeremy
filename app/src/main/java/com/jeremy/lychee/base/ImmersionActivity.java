package com.jeremy.lychee.base;

import android.view.View;

import com.jeremy.lychee.utils.statusbar.StatusBarUtil;
import com.jeremy.lychee.utils.statusbar.SystemBarTintManager;

public abstract class ImmersionActivity extends BaseActivity {

    private View mFitWindowView;
    private SystemBarTintManager mManager;

    abstract public View onGetFitWindowView();

    protected void setStatusBarColor(int color) {
        StatusBarUtil.setStatusBarTintViewColor(this, mManager, color);
    }

    protected void setFitsSystemWindows(boolean enabled) {
        if(mFitWindowView == null){
            mFitWindowView = onGetFitWindowView();
        }
        mFitWindowView.setFitsSystemWindows(enabled);
    }

    @Override
    protected void onPostInflation() {
        super.onPostInflation();
        mManager = StatusBarUtil.getSystemBarTintManager(this);
    }

    protected void setStatusBarAlpha(float val) {
        mManager.setStatusBarAlpha(val);
    }

    protected void setStatusBarTransparent(boolean val){
        if (val) {
            setStatusBarAlpha(0f);
        } else {
            setStatusBarAlpha(1f);
        }
    }

}
