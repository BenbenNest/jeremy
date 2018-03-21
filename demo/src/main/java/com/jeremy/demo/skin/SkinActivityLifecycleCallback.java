package com.jeremy.demo.skin;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.lang.reflect.Field;

/**
 * Created by changqing on 2018/3/20.
 */

public class SkinActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        try {
            //Andrid布局加载器使用mFactorySet标记是否设置过factory
            //如果设置过则抛出异常
            //这里需要把mFactorySet设置为false
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        layoutInflater.setFactory2(new SkinLayoutFactory() );
//        LayoutInflaterCompat.setFactory2(layoutInflater,new SkinLayoutFactory());

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
