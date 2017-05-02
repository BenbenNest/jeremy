package com.jeremy.demo.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.facebook.stetho.Stetho;
import com.jeremy.library.utils.CrashHandler;
import com.morgoo.droidplugin.PluginHelper;

/**
 * Created by benbennest on 17/2/11.
 */
public class MyApplication extends Application {
    public static Context sContext;

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
        sContext = this;
        Stetho.initializeWithDefaults(this);
        CrashHandler.getInstance(getApplicationContext()).init();
        if (Build.VERSION.SDK_INT < 24) {
            PluginHelper.getInstance().applicationOnCreate(getBaseContext());
        }
        mainProcessInit();
    }

    private void mainProcessInit() {
        if (isMainProcess()) {

        }
    }

    private boolean isMainProcess() {
        try {
            String processName = getProcessName();
            if (processName == null) {
                return true;
            }
            return processName.equals(getPackageName());
        } catch (Throwable e) {
            e.printStackTrace();
            return true;
        }
    }

    private String getProcessName() {
        try {
            int currentPid = android.os.Process.myPid();
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
                if (processInfo.pid == currentPid) {
                    return processInfo.processName;
                }
            }
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}
