package com.jeremy.demo.skin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author jeremy
 */

public class SkinPreference {
    private static final String SKIN_SHARED = "skins";

    private static final String KEY_SKIN_PATH = "skin-path";
    private static SkinPreference instance;
    private final SharedPreferences mPref;

    public synchronized static void init(Context context) {
        if (instance == null) {
            instance = new SkinPreference(context.getApplicationContext());
        }
    }

    public static SkinPreference getInstance() {
        return instance;
    }

    private SkinPreference(Context context) {
        mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
    }

    public void setSkin(String skinPath) {
        mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply();
    }

    public String getSkin() {
        return mPref.getString(KEY_SKIN_PATH, null);
    }

}
