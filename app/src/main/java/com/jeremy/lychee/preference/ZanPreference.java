package com.jeremy.lychee.preference;

import com.jeremy.lychee.base.ContentApplication;

import net.grandcentrix.tray.TrayPreferences;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class ZanPreference extends TrayPreferences {
    private static final int VERSION = 1;
    private volatile static ZanPreference mInstance;
    public static final String PREF_SAVED_ZAN_DATA = "zan_data";
    public static ZanPreference getInstance(){
        if(mInstance == null){
            mInstance = new ZanPreference();
        }
        return mInstance;
    }

    public ZanPreference() {
        super(ContentApplication.getInstance(), PREF_SAVED_ZAN_DATA, VERSION);
    }
    public void saveLongValue(String key, Long value) {
        put(key, value);
    }

    public Long getLongValue(String key) {
        return getLong(key, 0L);
    }

    public void saveStringValue(String key, String value) {
        put(key, value);
    }

    public String getStringValue(String key) {
        return getString(key, "");
    }

    public void saveBooleanValue(String key, Boolean value) {
        try {
            put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean getBooleanValue(String key) {
        return getBoolean(key, false);
    }
}
