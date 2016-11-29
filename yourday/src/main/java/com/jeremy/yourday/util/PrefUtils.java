package com.jeremy.yourday.util;

/**
 * Created by benbennest on 16/8/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jeremy.yourday.app.YourDayApplication;

public class PrefUtils {
    // Preference 文件名
    public static final String DEFAULT = "DEFAULT_PREF";
    public static final String APPS = "APPS";

    /**
     * 获取boolean类型数据
     */
    public static boolean getBoolData(String key, boolean def) {
        return getBoolData(key, def, DEFAULT);
    }

    /**
     * 获取boolean类型数据
     *
     * @param key
     * @param def
     * @param spName
     */
    public static boolean getBoolData(String key, boolean def, String spName) {
        Context context = YourDayApplication.getInstance().getBaseContext();
        if (context == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(spName)) {
            return def;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, def);
    }

    /**
     * 保存boolean类型数据
     *
     * @param key
     * @param data
     */
    public static void saveBoolData(String key, boolean data) {
        saveBoolData(key, data, DEFAULT);
    }

    /**
     * 保存boolean类型数据
     *
     * @param key
     * @param data
     * @param spName
     */
    public static void saveBoolData(String key, boolean data, String spName) {
        Context context = YourDayApplication.getInstance();
        if (context == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(spName)) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    /**
     * 获取long类型数据
     *
     * @param key
     * @return
     */
    public static long getLongData(String key, long def) {
        return getLongData(key, def, DEFAULT);
    }

    /**
     * 获取long类型数据
     *
     * @param key
     * @param def
     * @param spName
     */
    public static long getLongData(String key, long def, String spName) {
        Context context = YourDayApplication.getInstance();
        if (context == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(spName)) {
            return def;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getLong(key, def);
    }

    /**
     * 保存Long类型数据
     *
     * @param key
     * @param data
     */
    public static void saveLongData(String key, long data) {
        saveLongData(key, data, DEFAULT);
    }

    /**
     * 保存long类型数据
     *
     * @param key
     * @param data
     * @param spName
     */
    public static void saveLongData(String key, long data, String spName) {
        Context context = YourDayApplication.getInstance();
        if (context == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(spName)) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, data);
        editor.commit();
    }

    /**
     * 获取String类型数据
     *
     * @param key
     * @param def
     * @return
     */
    public static String getStringData(String key, String def) {
        return getStringData(key, def, DEFAULT);
    }

    /**
     * 获取String类型数据
     *
     * @param key
     * @param def
     * @param spName
     * @return
     */
    public static String getStringData(String key, String def, String spName) {
        Context context = YourDayApplication.getInstance();
        if (context == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(spName)) {
            return def;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getString(key, def);
    }

    /**
     * 保存String类型数据
     *
     * @param key
     * @param data
     */
    public static void saveStringData(String key, String data) {
        saveStringData(key, data, DEFAULT);
    }

    /**
     * 保存String类型数据
     *
     * @param key
     * @param data
     * @param spName
     */
    public static void saveStringData(String key, String data, String spName) {
        Context context = YourDayApplication.getInstance();
        if (TextUtils.isEmpty(spName)) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, data);
        editor.commit();
    }

    /**
     * 获取Int类型数据
     *
     * @param key
     * @param def
     * @return
     */
    public static int getIntData(String key, int def) {
        return getIntData(key, def, DEFAULT);
    }

    /**
     * 获取int类型数据
     *
     * @param key
     * @param def
     * @param spName
     */
    public static int getIntData(String key, int def, String spName) {
        Context context = YourDayApplication.getInstance();
        if (context == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(spName)) {
            return def;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getInt(key, def);
    }

    /**
     * 保存Int类型数据
     *
     * @param key
     * @param data
     */
    public static void saveIntData(String key, int data) {
        saveIntData(key, data, DEFAULT);
    }

    /**
     * 保存int类型数据
     *
     * @param key
     * @param data
     * @param spName
     */
    public static void saveIntData(String key, int data, String spName) {
        Context context = YourDayApplication.getInstance();
        if (context == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(spName)) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, data);
        editor.commit();
    }

    /**
     * 删除key-value配置数据
     *
     * @param keys
     */
    public static void clearValuesByKey(String[] keys) {
        Context context = YourDayApplication.getInstance();
        if (context == null || keys == null || keys.length <= 0) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }
}

