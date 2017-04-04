package com.jeremy.library.utils;

import android.os.Handler;
import android.os.Looper;

public class RunOnUIUtils {
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static void runOnUI(final Runnable runnable) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }

    public static void runOnUI(final Runnable runnable, long delay) {
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay);
    }

}
