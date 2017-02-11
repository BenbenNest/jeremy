package com.jeremy.lychee.base;

import android.os.Bundle;

import com.jeremy.lychee.utils.hitLog.HitLogHelper;
import com.qihoo.sdk.report.QHStatAgent;
import com.trello.rxlifecycle.components.support.RxFragment;
import android.os.Handler;
import android.os.Looper;

public abstract class BaseFragment extends RxFragment {

    private boolean isCreated = false, isVisible = false, isEnter = false, isLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isVisible) {
            onLeave();
        }
        //上传现阶段打点信息
        HitLogHelper.getHitLogHelper().postLogToNet();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) {
            onEnter();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated) {
            return;
        }
        isVisible = isVisibleToUser;
        if (isVisible) {
            onEnter();
            if (!isLoaded)
                onLoad();
        } else {
            onLeave();
        }
    }

    private void onEnter() {
        if (!isEnter && getContext() != null) {
            QHStatAgent.onPageStart(getContext(), this.getClass().getName());
            isEnter = true;
        }
    }

    private void onLeave() {
        if (isEnter && getContext() != null) {
            QHStatAgent.onPageEnd(getContext(), this.getClass().getName());
            isEnter = false;
        }
    }

    private void onLoad() {
        isLoaded = true;
        new Handler(Looper.getMainLooper()).post(this::lazyLoad);
    }
    protected void lazyLoad(){};

    public boolean onBackPressed() {return false;}
}

