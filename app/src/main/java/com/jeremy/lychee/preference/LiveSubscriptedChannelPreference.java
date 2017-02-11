package com.jeremy.lychee.preference;

import com.jeremy.lychee.base.ContentApplication;

import net.grandcentrix.tray.TrayPreferences;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class LiveSubscriptedChannelPreference extends TrayPreferences {
    private static final int VERSION = 1;
    private volatile static LiveSubscriptedChannelPreference mInstance;
    public static final String PREF_SUBSCRIPT_CHANNEL_LIST_DATA = "subcripted_channel_list_data";
    public static LiveSubscriptedChannelPreference getInstance(){
        if(mInstance == null){
            mInstance = new LiveSubscriptedChannelPreference();
        }
        return mInstance;
    }

    public LiveSubscriptedChannelPreference() {
        super(ContentApplication.getInstance(), PREF_SUBSCRIPT_CHANNEL_LIST_DATA, VERSION);
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
        return getBoolean(key, false);
    }
}
