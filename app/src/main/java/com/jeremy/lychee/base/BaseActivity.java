package com.jeremy.lychee.base;

import android.os.Bundle;

import com.jeremy.lychee.utils.hitLog.HitLogHelper;
import com.jeremy.lychee.init.AsyncInitializationActivity;
import com.qihoo.sdk.report.QHStatAgent;

import android.os.Handler;

public abstract class BaseActivity extends AsyncInitializationActivity{

    /**
     * 记录APP运行时长打点信息
     */
    public volatile static long sAppForgroundDuration;
    /**
     * 记录APP切换到前台后的时间戳，推到后台时设置为0
     */
    public volatile static long sAppResumeTimeStamp;

    @Override
    protected void onPause() {
        super.onPause();
        QHStatAgent.onPause(this);
        QHStatAgent.onPageEnd(this, this.getClass().getName());
        //上传现阶段打点信息
        HitLogHelper.getHitLogHelper().postLogToNet();
        new StaticHandler().postDelayed(
                () -> {
                    if (ApplicationStatus.getForgroundActivities().size() == 0) {
                        //app被切换到后台
                        sAppForgroundDuration += System.currentTimeMillis() / 1000L - sAppResumeTimeStamp;
                        sAppResumeTimeStamp = 0;
                    }
                }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onInitializeStart() {

    }

    @Override
    public void onInitializeComplete() {

    }

    @Override
    public void onInitializeFailure() {

    }

    @Override
    public void initializeAsync() {

    }

    @Override
    protected void onPreInflation() {

    }

    @Override
    protected void onPostInflation() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        QHStatAgent.onResume(this);
        QHStatAgent.onPageStart(this, this.getClass().getName());
        QHStatAgent.setDebugMode(this,true);
        if (sAppResumeTimeStamp == 0) {
            //APP被切换到前台
            sAppResumeTimeStamp = System.currentTimeMillis() / 1000L;
        }
    }

    private static class StaticHandler extends Handler {}
}
