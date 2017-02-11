package com.jeremy.lychee.preference;

import com.jeremy.lychee.base.ContentApplication;

import net.grandcentrix.tray.AppPreferences;

/**
 * 这里面的数据在一个文件中
 */
public class CommonPreferenceUtil implements PreferenceKey {

    public static AppPreferences getAppPreferences() {
        return appPreferences;
    }

    private static final AppPreferences appPreferences = new AppPreferences(ContentApplication.getInstance());

    public static void SaveNewsChannelMd5(String news_channel_md5) {
        appPreferences.put(NEWS_CHANNEL_MD5, news_channel_md5);
    }

    public static String GetNewsChannelMd5() {
        return appPreferences.getString(NEWS_CHANNEL_MD5, "");
    }

    public static int GetNewsLayoutRate() {
        return appPreferences.getInt(NEWS_LAYOUT_RATE, 1);
    }

    public static void SetNewsLayoutRate(int rate) {
        appPreferences.put(NEWS_LAYOUT_RATE, rate);
    }


    public static Boolean GetIsFirstOpen() {
        return appPreferences.getBoolean(APP_FIRST_OPEN, true);
    }

    public static void SetIsFirstOpen(Boolean value) {
        appPreferences.put(APP_FIRST_OPEN, value);
    }

    public static void SetForbitDelCids(String cids) {
        appPreferences.put(FORBID_DEL_CIDS, cids);
    }

    public static String GetForbitDelCids() {
        return appPreferences.getString(FORBID_DEL_CIDS, "");
    }

    public static boolean GetIntroNewsFragment() {
        return appPreferences.getBoolean(INTRO_NEWS_FRAGMENT, true);
    }

    public static boolean GetIntroLiveFragment() {
        return appPreferences.getBoolean(INTRO_LIVE_FRAGMENT, true);
    }

    public static boolean GetIntroWemediaFragment() {
        return appPreferences.getBoolean(INTRO_WEMEDIA_FRAGMENT, true);
    }

    public static void setIntroNewsFragment(boolean isShow) {
        appPreferences.put(INTRO_NEWS_FRAGMENT, isShow);
    }

    public static void setIntroLiveFragment(boolean isShow) {
        appPreferences.put(INTRO_LIVE_FRAGMENT, isShow);
    }

    public static void setIntroWemediaFragment(boolean isShow) {
        appPreferences.put(INTRO_WEMEDIA_FRAGMENT, isShow);
    }

    public static void SetNewsType(int newsStyle) {
        appPreferences.put(NEWS_TYPE, newsStyle);
    }

    public static int GetNewsType() {
        return appPreferences.getInt(NEWS_TYPE, 1);
    }

    public static boolean HasModifyNewsChannel() {
        return appPreferences.getBoolean(HAS_MODIFY_NEWS_CHANNEL, false);
    }

    public static void SetModifyNewsChannel(boolean modify) {
        appPreferences.put(HAS_MODIFY_NEWS_CHANNEL, modify);
    }

    public static boolean GetIntroLievDiscoveryFragment() {
        return appPreferences.getBoolean(INTRO_Live_Discovery_FRAGMENT, true);
    }

    public static void setIntroLievDiscoveryFragment(boolean isShow) {
        appPreferences.put(INTRO_Live_Discovery_FRAGMENT, isShow);
    }
}
