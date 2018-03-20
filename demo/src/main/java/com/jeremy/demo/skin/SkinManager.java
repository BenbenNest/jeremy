package com.jeremy.demo.skin;

import android.app.Application;

/**
 * Created by changqing on 2018/3/20.
 */

public class SkinManager {

    private static SkinManager instance;

    private SkinManager(Application application) {

    }

    public static SkinManager getInstance(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
        return instance;
    }

    public static void init(Application application) {
        instance = getInstance(application);
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycleCallback());
    }

}
