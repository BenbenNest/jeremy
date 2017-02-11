package com.jeremy.lychee.preference;

import com.jeremy.lychee.base.ContentApplication;


import net.grandcentrix.tray.TrayPreferences;

/**
 * 简单说下使用,如果一组业务请求中有很多数据要存储,建议继承 TrayPreferences,
 * 如果简单点直接用 CommonPreferences,不建议把所有的的数据都放在一个文件中,所以拆开.
 */
public class NewsChannelPreference extends TrayPreferences {
    private static final int VERSION = 1;

    public static final String PREF_SAVED_NEWS_CHANNELS = "saved_channels";

    public NewsChannelPreference() {
        super(ContentApplication.getInstance(), PREF_SAVED_NEWS_CHANNELS, VERSION);
    }

    public void saveNewsChannelCids(String channels) {
        put(PREF_SAVED_NEWS_CHANNELS, channels);
    }

    public String getNewsChannelCids() {
        return getString(PREF_SAVED_NEWS_CHANNELS, "");
    }

    public void saveStringValue(String key, String channels) {
        put(key, channels);
    }

    public String getStringValue(String key) {
        return getString(key, "");
    }

}
