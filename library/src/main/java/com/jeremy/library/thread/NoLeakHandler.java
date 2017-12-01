package com.jeremy.library.thread;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by changqing on 2017/12/1
 * 这只是代码Demo，一般情况下该Handler要定义在Activity内部，并且为静态内部类，
 * 并且在Activity 的onDestroy方法中添加removeCallbacksAndMessages(null);
 * 这样的话能及时清除消息队列
 */
public class NoLeakHandler extends Handler {
    private WeakReference<Activity> mActivity;

    public NoLeakHandler(Activity activity) {
        mActivity = new WeakReference<>(activity);
        removeCallbacksAndMessages(null);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
}