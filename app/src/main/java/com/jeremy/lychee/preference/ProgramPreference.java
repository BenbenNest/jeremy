package com.jeremy.lychee.preference;

import com.jeremy.lychee.base.ContentApplication;

import net.grandcentrix.tray.TrayPreferences;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class ProgramPreference extends TrayPreferences {
    private static final int VERSION = 1;
    private volatile static ProgramPreference mInstance;
    public static final String PREF_SAVED_PROGRAM_LIST_DATA = "program_list_data";
    public static ProgramPreference getInstance(){
        if(mInstance == null){
            mInstance = new ProgramPreference();
        }
        return mInstance;
    }

    public ProgramPreference() {
        super(ContentApplication.getInstance(), PREF_SAVED_PROGRAM_LIST_DATA, VERSION);
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
