package com.jeremy.library.util;

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

}
