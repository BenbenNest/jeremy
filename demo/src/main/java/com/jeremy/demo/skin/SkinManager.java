package com.jeremy.demo.skin;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.jeremy.demo.skin.utils.SkinPreference;

import java.lang.reflect.Method;

/**
 * Created by changqing on 2018/3/20.
 */

public class SkinManager {

    private static SkinManager instance;
    private static Application application;

    private SkinManager(Application application) {
        this.application = application;
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
//        instance = getInstance(application);
        SkinPreference.init(application);
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycleCallback());
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    /**
     * 加载皮肤包
     *
     * @param skin
     */
    private static void loadSkin(String skin) {
        //还原默认皮肤包
        if (TextUtils.isEmpty(skin)) {
            SkinPreference.getInstance().setSkin("");
        } else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, skin);
                Resources resources = application.getResources();
                Resources skinResources = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
