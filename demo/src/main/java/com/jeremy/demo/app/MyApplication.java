package com.jeremy.demo.app;

import android.app.Application;

import com.jeremy.library.utils.CrashHandler;

/**
 * Created by benbennest on 17/2/11.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance(getApplicationContext()).init();
    }

}
