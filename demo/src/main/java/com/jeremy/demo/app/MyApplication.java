package com.jeremy.demo.app;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.jeremy.library.utils.CrashHandler;
import com.morgoo.droidplugin.PluginHelper;

/**
 * Created by benbennest on 17/2/11.
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        //DroidPlugin在Android7.0有兼容性问题，所以需要判断版本
        if (Build.VERSION.SDK_INT < 24) {
            PluginHelper.getInstance().applicationAttachBaseContext(base);
        }
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance(getApplicationContext()).init();
        if (Build.VERSION.SDK_INT < 24) {
            PluginHelper.getInstance().applicationOnCreate(getBaseContext());
        }
    }


}
