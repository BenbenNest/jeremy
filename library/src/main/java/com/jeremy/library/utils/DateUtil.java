package com.jeremy.library.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by benbennest on 16/8/25.
 */
public class DateUtil {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat SIMPLE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * @param time
     * @return yyyy-MM-dd
     */
    public static String getSimpleDate(long time) {
        return SIMPLE_DATE_FORMAT.format(time);
    }

    /**
     * @param time
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getSimpleTime(long time) {
        return SIMPLE_TIME_FORMAT.format(time);
    }

    public static String date2String(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
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

    public static int getYear(Date date) {
        if (date != null) {
            return date.getYear() + 1900;
        }
        return 1900;
    }

    public static String getYearStr(Date date) {
        if (date != null) {
            return (date.getYear() + 1900) + "";
        }
        return "";
    }

    public static int getMonth(Date date) {
        if (date != null) {
            return date.getMonth() + 1;
        }
        return 0;
    }

    public static String getMonthStr(Date date) {
        if (date != null) {
            return (date.getMonth() + 1) + "";
        }
        return "";
    }

}
