package com.jeremy.lychee.preference;

public class UserPreference {
    public static final String PREF_MOBILE_NET_ENABLED = "mobile_net_enabled";
    public static final String PREF_MOBILE_PUSH_ENABLED = "mobile_push_enabled";
    public static final String PREF_MOBILE_WIFI_AUTO_LOAD_VEDIO_ENABLED = "mobile_wifi_auto_load_vedio_enabled";
    private volatile static UserPreference mInstance;
    public static UserPreference getInstance(){
        if(mInstance == null){
            mInstance = new UserPreference();
        }
        return mInstance;
    }

    public void setUseMobileNetEnabled(boolean val) {
        CommonPreferenceUtil.getAppPreferences().put(PREF_MOBILE_NET_ENABLED, val);
    }

    public boolean getUseMobileNetEnabled() {
        return CommonPreferenceUtil.getAppPreferences().getBoolean(PREF_MOBILE_NET_ENABLED, false);
    }

    public void setUseMobilePushEnabled(boolean val) {
        CommonPreferenceUtil.getAppPreferences().put(PREF_MOBILE_PUSH_ENABLED, val);
    }

    public boolean getUseMobilePushEnabled() {
        return CommonPreferenceUtil.getAppPreferences().getBoolean(PREF_MOBILE_PUSH_ENABLED, true);
    }
}
