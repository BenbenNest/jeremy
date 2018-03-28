package com.jeremy.demo.skin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.jeremy.demo.skin.utils.SkinPreference;
import com.jeremy.demo.skin.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

/**
 * Created by changqing on 2018/3/20.
 */

public class SkinManager extends Observable {

    private static SkinManager instance;
    private static Application application;

    private SkinManager() {

    }

    private SkinManager(Application application) {
        this.application = application;
    }

    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }

    public void init(Application application) {
//        instance = getInstance(application);
        this.application = application;
        SkinPreference.init(application);
        SkinResources.init(application);
        application.registerActivityLifecycleCallbacks(new SkinActivityLifecycleCallback());
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    /**
     * 加载皮肤包
     *
     * @param skin
     */
    private void loadSkin(String skin) {
        //还原默认皮肤包
        if (TextUtils.isEmpty(skin)) {
            SkinPreference.getInstance().setSkin("");
            SkinResources.getInstance().reset();
        } else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, skin);
                Resources resources = application.getResources();
                Resources skinResources = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
                //获取外部皮肤包包名
                PackageManager packageManager = application.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(skin, PackageManager.GET_ACTIVITIES);
                String packageName = packageInfo.packageName;
                SkinResources.getInstance().applySkin(skinResources, packageName);
                SkinPreference.getInstance().setSkin(skin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //应用皮肤包
        setChanged();
        notifyObservers();
    }
}
