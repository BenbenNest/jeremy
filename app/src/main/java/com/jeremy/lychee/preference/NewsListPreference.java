package com.jeremy.lychee.preference;

import com.jeremy.lychee.base.ContentApplication;

import net.grandcentrix.tray.TrayPreferences;

/**
 * Created by zhaozuotong on 2015/11/22.
 */

public class NewsListPreference extends TrayPreferences {
    private static final int VERSION = 1;
    private volatile static NewsListPreference mInstance;
    public static final String PREF_SAVED_NEWS_LIST_DATA = "saved_list_data";

    public static NewsListPreference getInstance(){
        if(mInstance == null){
            mInstance = new NewsListPreference();
        }
        return mInstance;
    }
    public NewsListPreference() {
        super(ContentApplication.getInstance(), PREF_SAVED_NEWS_LIST_DATA, VERSION);
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
        put(key, value);
    }

    public Boolean getBooleanValue(String key) {
        return getBoolean(key, true);
    }
}