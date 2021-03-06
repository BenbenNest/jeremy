package com.jeremy.demo.app;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.Nullable;

import com.github.moduth.blockcanary.BlockCanary;
import com.jeremy.demo.skin.SkinManager;
import com.jeremy.library.utils.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by benbennest on 17/2/11.
 */
public class MyApplication extends BaseApplication {
    public static Context sContext;
    private static RefWatcher refWatcher;//LeakCanary只能够检测Activity的内存泄漏，需要使用RefWatcher来进行监控其他类的内存泄漏

    @Override
    protected void attachBaseContext(Context base) {
        //DroidPlugin在Android7.0有兼容性问题，所以需要判断版本
//        if (Build.VERSION.SDK_INT < 24) {
//            PluginHelper.getInstance().applicationAttachBaseContext(base);
//        }
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
//        Stetho.initializeWithDefaults(this);
        CrashHandler.getInstance(getApplicationContext()).init();
//        if (Build.VERSION.SDK_INT < 24) {
//            PluginHelper.getInstance().applicationOnCreate(getBaseContext());
//        }
        SkinManager.init(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            refWatcher = RefWatcher.DISABLED;
            return;
        }
        refWatcher = LeakCanary.install(this);
        mainProcessInit();
    }

    private void mainProcessInit() {
        if (isMainProcess()) {
            // Do it on main process
            BlockCanary.install(this, new AppBlockCanaryContext()).start();


            //友盟
//            MobclickAgent.setDebugMode(true);
//            MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
//            MobclickAgent.setCatchUncaughtExceptions(true);

//            //微信
//            PlatformConfig.setWeixin("xxx", "xxx");
//            //新浪
//            PlatformConfig.setSinaWeibo("xxx", "xxx");
//            //qq qqzone
//            PlatformConfig.setQQZone("xxx", "xxx");
        }
    }

    private boolean isMainProcess() {
        try {
            String processName = getProcessName();
            if (processName == null) {
                return true;
            }
            return processName.equals(getPackageName());
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    //下面这个方法是通过非静态变量的方式引用
    public static RefWatcher getRefWatcher(@Nullable Context context) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();
        return myApplication.refWatcher;
    }

}
