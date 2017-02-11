package com.jeremy.lychee.preference;

import com.jeremy.lychee.base.ContentApplication;

import net.grandcentrix.tray.TrayPreferences;

/**
 * Created by daibangwen-xy on 15/12/24.
 */
public class TestAddressPreference extends TrayPreferences {
    private static final int VERSION = 1;
    private volatile static TestAddressPreference mInstance;
    public static final String PREF_TEST_ADDRESS_DATA = "testaddress_data";
    public static TestAddressPreference getInstance(){
        if(mInstance == null){
            mInstance = new TestAddressPreference();
        }
        return mInstance;
    }

    public TestAddressPreference() {
        super(ContentApplication.getInstance(), PREF_TEST_ADDRESS_DATA, VERSION);
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
    public Boolean getIsTestAddress(){
        return getBooleanValue("testaddress");
    }
    public void setIsTestAddress(boolean isTestAddress){
        saveBooleanValue("testaddress",isTestAddress);
    }
}
