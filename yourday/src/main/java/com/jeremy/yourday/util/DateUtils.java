package com.jeremy.yourday.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by benbennest on 16/8/17.
 */
public class DateUtils {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String getDate(long time) {
        return SIMPLE_DATE_FORMAT.format(time);
    }

    /**
     * Date（long） 转换 String
     *
     * @param time time
     * @return String
     */
    public static String date2String(long time) {
        return SIMPLE_DATE_TIME_FORMAT.format(time);
    }

    /**
     * Date（long） 转换 String
     *
     * @param time   time
     * @param format format
     * @return String
     */
    public static String date2String(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }


    /**
     * 转化时间
     *
     * @param timeString "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String formatTime(String timeString) {
        return formatTime(TextUtils.isEmpty(timeString) ? System.currentTimeMillis() : Long.valueOf(timeString));
    }

    /**
     * 转化时间
     *
     * @param timestamp 时间戳
     * @return 刚刚|分钟前|小时前|天前|MM月dd日
     */
    public static String formatTime(long timestamp/*, boolean includeHour*/) {
        if (timestamp == 0) {
            return null;
        }
        long nowTimestamp = System.currentTimeMillis();
        // 如果时间差 <= 0秒，则修正为1秒钟前
        int deltaSeconds = Math.max(1, (int) ((nowTimestamp - timestamp) / 1000));

        if (deltaSeconds <= 5 * 60) {
            return "刚刚更新";
        }
        // 规则2: 小于1小时的，显示xx分钟前
        if (deltaSeconds <= 3600 && deltaSeconds > 5 * 60) {
            return (int) Math.max(1, Math.floor(deltaSeconds / 60)) + "分钟前";
        }
        if (deltaSeconds < 86400) {
            return (int) Math.floor(deltaSeconds / 3600) + "小时前";
        }
        if (deltaSeconds > 86400 && deltaSeconds < 3 * 86400) {
            return (int) Math.floor(deltaSeconds / 86400) + "天前";
        }
        String template = "MM月dd日";
      /*  if (includeHour) {
            template = "MM月dd日 HH:mm";
        } else {
            template = "MM月dd日";
        }*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(template, Locale.getDefault());
        return simpleDateFormat.format(timestamp);
    }

    public static long parseTime(String timeString) {
        return parseTime(timeString, SIMPLE_DATE_TIME_FORMAT);
    }

    /**
     * 服务器时间戳 * 1000 转换为timestamp
     *
     * @param time
     * @return
     */
    public static String formatLinuxTime(long time) {
        return formatTime(time * 1000);
    }

    /**
     * 转化时间 "yyyy-MM-dd HH:mm:ss" 为时间戳
     *
     * @param timeString "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long parseTime(String timeString, SimpleDateFormat dateFormat) {
        long timestamp = 0;
        timeString = dateFormat.format(new Date(Long.valueOf(timeString) * 1000));
        if (TextUtils.isEmpty(timeString))
            return timestamp;
        try {
            Date date = dateFormat.parse(timeString);
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }


    public static long parseFormatTime(String timeString) {
        long timestamp = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if (TextUtils.isEmpty(timeString))
            return timestamp;
        try {
            Date date = dateFormat.parse(timeString);
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}

