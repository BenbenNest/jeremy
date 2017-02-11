package com.jeremy.lychee.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import com.anupcowkur.reservoir.Reservoir;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jeremy.lychee.db.DaoMaster;
import com.jeremy.lychee.db.DaoSession;
import com.jeremy.lychee.gank.GankApi;
import com.jeremy.lychee.manager.FontManager;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.utils.SystemInfo;
import com.jeremy.lychee.utils.logger.LogLevel;
import com.jeremy.lychee.utils.logger.Logger;
import com.qihoo.kd.push.KDPushSDK;
import com.qihoo.livecloud.LiveCloudRecorder;
import com.qihoo.sdk.report.QHStatAgent;
import com.squareup.leakcanary.LeakCanary;

import java.lang.reflect.Field;

/**
 * Basic application functionality that should be shared among all browser applications
 * based on the content layer.
 */
public class ContentApplication extends BaseApplication {
    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = ONE_KB * 1024L;
    public static final long CACHE_DATA_MAX_SIZE = ONE_MB * 3L;
    public Gson gson;
    public static Typeface TypeFaceFZLTHK;

    public void onCreate() {
        super.onCreate();
        this.initGson();
        this.initReservoir();
        if (SystemInfo.isDebugMode()) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build()
            );

        }

        LeakCanary.install(this);
        installLeakCanary();

        LiveCloudRecorder.init();// 初始化环境，静态初始化，可以在程序一开始调用

        Logger.init("lianxian").logLevel(SystemInfo.isDebugMode() ? LogLevel.FULL : LogLevel.NONE).methodCount(4);
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
        });
        Session.restore(this);

        //替换系统字体(serif)为fzlthk
        TypeFaceFZLTHK = FontManager.getHtTypeface();
        try
        {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, TypeFaceFZLTHK);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        startPush();

        //QDAS init
        QHStatAgent.init(this);
        QHStatAgent.onError(this);
        QHStatAgent.openActivityDurationTrack(this, false);//禁止使用自动的方式统计页面路径
        QHStatAgent.setDefaultReportPolicy(this, 1);  //手动设置上传策略(0为下次启动时，1为按间隔)，注意此设置可能会被云控参数覆盖
    }

    private void initGson() {
        this.gson = new GsonBuilder()
                .setDateFormat(GankApi.GANK_DATA_FORMAT)
                .create();
    }

    private void initReservoir() {
        try {
            Reservoir.init(this, CACHE_DATA_MAX_SIZE, this.gson);
        } catch (Exception e) {
            //failure
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private volatile static DaoMaster sDaoMaster;
    private volatile static DaoSession sDaoSession;

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    private static DaoMaster getDaoMaster(Context context) {
        if (sDaoMaster == null) {
            synchronized (ContentApplication.class) {
                if (sDaoMaster == null) {
                    DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, "lianxian_db", null);
                    sDaoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return sDaoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @return
     */
    public static DaoSession getDaoSession() {
        if (sDaoSession == null) {
            synchronized (ContentApplication.class) {
                if (sDaoMaster == null) {
                    sDaoMaster = getDaoMaster(sInstance.getApplicationContext());
                }
            }
            sDaoSession = sDaoMaster.newSession();
        }
        return sDaoSession;
    }

    private static ContentApplication sInstance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sInstance = this;
    }

    public static ContentApplication getInstance() {
        return sInstance;
    }

    private void startPush() {
        KDPushSDK.start(this);
        KDPushSDK.setLogEnable(false);
    }

}
