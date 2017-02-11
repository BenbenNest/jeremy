package com.jeremy.lychee.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;

import com.jeremy.lychee.base.ApplicationStatus;
import com.jeremy.lychee.base.Constants;
import com.jeremy.lychee.manager.Session;
import com.jeremy.lychee.preference.UserPreference;
import com.jeremy.lychee.utils.dialog.DialogUtil;
import com.jeremy.lychee.widget.TransformDrawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import rx.functions.Action;
import rx.functions.Action0;

public final class AppUtil {
    public static String getDeviceSerial() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }

    //点赞sign
    public static String zanSignString(String url) {
        TreeMap treeMap = new TreeMap();
        treeMap.put("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        treeMap.put("channel", "cid");
        treeMap.put("os", Build.DISPLAY);
        treeMap.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        treeMap.put("os_type", "Android");
        treeMap.put("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
        treeMap.put("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        treeMap.put("sid", Session.getSid());
        treeMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
        treeMap.put("key", url);
        return GetSign(treeMap);

    }

    //打点日子sign
    public static String hitLogSignString(String s) {
        TreeMap treeMap = new TreeMap();
        treeMap.put("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        treeMap.put("channel", "cid");
        treeMap.put("os", Build.DISPLAY);
        treeMap.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        treeMap.put("os_type", "Android");
        treeMap.put("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
        treeMap.put("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        treeMap.put("sid", Session.getSid());
        treeMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
        treeMap.put("s", s);
        return GetSign(treeMap);
    }


    //转推sign
    public static String signString(String nid, String subid, String url, String content, int mType, String title) {
        TreeMap treeMap = new TreeMap();
        treeMap.put("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        treeMap.put("channel", "cid");
        treeMap.put("os", Build.DISPLAY);
        treeMap.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        treeMap.put("os_type", "Android");
        treeMap.put("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
        treeMap.put("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        treeMap.put("sid", Session.getSid());
        treeMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));

        treeMap.put("nid", nid);
        treeMap.put("url", url);
        treeMap.put("sub_id", subid);
        treeMap.put("content", content);
        treeMap.put("type", mType);
        treeMap.put("title", title);
        Set<String> keySet = treeMap.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            stringBuilder.append(key);
            stringBuilder.append("=");
            if (treeMap.get(key) != null) {
                stringBuilder.append(treeMap.get(key));
            } else {
                stringBuilder.append("");
            }
            if (key != treeMap.lastKey()) {
                stringBuilder.append("&");
            }
        }
        stringBuilder.append("shi!@#$%^&*[xian!@#]*");
        String md5Str = AppUtil.getMD5code(stringBuilder.toString());
        return md5Str.substring(3, 10);

    }

    public static String getMD5code(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte b[] = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //sdk是否安装
    public static boolean isMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡根路径
     */
    public static String getAppSdRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.APP_FOLDER + File.separator;
    }

    public static String getTokenID(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String imei = tm.getDeviceId();

        String AndroidID = android.provider.Settings.System.getString(
                context.getContentResolver(), "android_id");

        String serialNo = getDeviceSerial();

        return getMD5code(imei + AndroidID + serialNo);
    }

    /*
     * 获取应用VersionName
     */
    public static String getVersionName(Context ctx) {
        String res = "";
        if (ctx == null) {
            return res;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            res = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    /*
     * 获取应用VersionCode
     */
    public static int getVersionCode(Context ctx) {
        if (ctx == null)
            return -1;
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取安装APP列表
     *
     * @return
     */
    public static List<String> getAppList(Context context) {
        List<String> list = new ArrayList<String>();
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo info = packageInfos.get(i);
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                list.add(info.packageName);
            }
        }
        return list;
    }

    public static float getDensity(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        return dm.density;
    }

    public static float getDisplayWidth(Activity context) {
        Point size = new Point();
        Display display = context.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        return size.x;
    }

    public static float getDisplayHeight(Activity context) {
        Point size = new Point();
        Display display = context.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        return size.y;
    }

    public static void hideSoft(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /*
     * 判断网络是否可用
     */
    public static boolean isNetAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager manager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }

    /**
     * 判断时否杂在移动网络下
     */
    public static boolean isNetTypeMobile(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager manager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        int netType = info.getType();
        if (ConnectivityManager.TYPE_MOBILE == netType) {
            return true;
        }
        return false;
    }

    /**
     * 判断时否杂在Wifi网络下
     */
    public static boolean isNetTypeWifi(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager manager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        int netType = info.getType();
        if (ConnectivityManager.TYPE_WIFI == netType) {
            return true;
        }
        return false;
    }

    /**
     * 实现文本复制功能
     *
     * @param content 需要赋值的文本内容
     */
    public static void copy(Context context, String content) {
        android.content.ClipboardManager cmb = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        String label = "copy text";
        ClipData clip = ClipData.newPlainText(label, content);
        cmb.setPrimaryClip(clip);
    }

    /**
     * 转化时间 "yyyy-MM-dd HH:mm:ss" 为时间戳
     *
     * @param timeString "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long parseTime(String timeString, SimpleDateFormat dateFormat) {
        long timestamp = 0;
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

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * 转化时间
     *
     * @param timestamp 时间字符串"yyyy-MM-dd HH:mm:ss"
     * @return 刚刚|分钟前|小时前|天前|MM月dd日
     */
    public static String formatTime(long timestamp) {
        if (timestamp == 0) {
            return null;
        }
        long nowTimestamp = System.currentTimeMillis();
        // 如果时间差 <= 0秒，则修正为1秒钟前
        int deltaSeconds = Math.max(1, (int) ((nowTimestamp - timestamp) / 1000));

        if (deltaSeconds <= 60) {
            return "刚刚";
        }
        // 规则2: 小于1小时的，显示xx分钟前
        if (deltaSeconds <= 3600 && deltaSeconds > 60) {
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

    /**
     * 转化时间
     *
     * @param time_str 时间字符串"yyyy-MM-dd HH:mm:ss"
     * @return 刚刚|分钟前|小时前|天前|MM月dd日
     */
    public static String formatTime(String time_str) {
        long timestamp = AppUtil.parseTime(time_str, SIMPLE_DATE_FORMAT);
        return formatTime(timestamp);


    }

    /**
     * 获取验证码 给参数加盐 参数顺序要保证跟算法一致
     * get comment verigy param from salt
     *
     * @param args
     * @return
     */
    public static String getVerify(String... args) {
        if (args == null || args.length == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            if (TextUtils.isEmpty(arg)) {
                arg = "0";
            }
            builder.append(arg);
        }
        String salt = getSalt();
        builder.append(salt);

        String s_code = StringUtil.MD5Encode(builder.toString()) + salt;
        return s_code;
    }

    /**
     * @return salt
     */
    public static String getSalt() {
        double r = Math.random();
        String md5String = StringUtil.MD5Encode(String.valueOf(r));
        String salt = md5String;
        try {
            if (!TextUtils.isEmpty(salt) && salt.length() > 8) {
                salt = md5String.substring(0, 8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salt;
    }

    public static float dpToPx(Context context, float dpValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, metrics);
    }

    public static <T> T decodeObj(T obj) {
        if (obj == null) {
            return null;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object fieldObj = field.get(obj);
                    if (fieldObj != null) {
                        if (Integer.class.isInstance(fieldObj)) {
                            continue;
                        }
                        if (Boolean.class.isInstance(fieldObj)) {
                            continue;
                        }
                        if (Double.class.isInstance(fieldObj)) {
                            continue;
                        }
                        if (Long.class.isInstance(fieldObj)) {
                            continue;
                        }
                        if (Byte.class.isInstance(fieldObj)) {
                            continue;
                        }
                        if (Short.class.isInstance(fieldObj)) {
                            continue;
                        }
                        if (Float.class.isInstance(fieldObj)) {
                            continue;
                        }
                        if (Character.class.isInstance(fieldObj)) {
                            continue;
                        }

                        if (fieldObj instanceof String) {
                            String s = (String) fieldObj;
                            s = URLDecoder.decode(s, "utf-8");
                            field.set(obj, s);
                        } else if (fieldObj instanceof List) {
                            List list = (List) fieldObj;
                            if (list.size() == 0) {
                                continue;
                            }
                            for (Object listObj : list) {
                                decodeObj(listObj);
                            }
                        } else {
                            decodeObj(fieldObj);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return obj;
    }


    public static Drawable getDefaultAppIcon(Context context, int width, int height) {
        WeakReference<Bitmap> m =
                new WeakReference<>(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
        Canvas c = new Canvas();
        c.setBitmap(m.get());
        c.drawColor(context.getResources().getColor(com.jeremy.lychee.R.color.news_image_default_color));
        Drawable drawable = context.getResources().getDrawable(com.jeremy.lychee.R.drawable.article_icon_logo);
        drawable.setBounds(
                (int) (width * 0.1),
                (int) (height / 2 - width * 0.8 * 0.12),
                (int) (width * 0.9),
                (int) (height / 2 + width * 0.8 * 0.12));
        drawable.draw(c);
        return new BitmapDrawable(m.get());
    }

//
//    public static Drawable getDefaultRectIcon_4_3(Context context, int width, int height) {
//        WeakReference<Bitmap> m =
//                new WeakReference<>(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
//        Canvas c = new Canvas();
//        c.setBitmap(m.get());
//        c.drawColor(context.getResources().getColor(R.color.news_image_default_color));
//        Drawable drawable = context.getResources().getDrawable(R.drawable.placeholder_4_3);
//        drawable.setBounds(
//                (int) (width * 0.3),
//                (int) (height / 2 - width * 0.4 * 0.13),
//                (int) (width * 0.7),
//                (int) (height / 2 + width * 0.4 * 0.13));
//        drawable.draw(c);
//        return new BitmapDrawable(m.get());
//    }

//    public static Drawable getDefaultRectIcon(Context context , int width, int height) {
//        WeakReference<Bitmap> m =
//                new WeakReference<>(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
//        Canvas c = new Canvas();
//        c.setBitmap(m.get());
//        c.drawColor(context.getResources().getColor(R.color.image_default_color));
//        Drawable drawable = context.getResources().getDrawable(R.drawable.article_icon_logo);
////        drawable.setBounds(25, 80, 175, 120);
//        drawable.setBounds(
//                (int) (width * 0.15),
//                (int) (height / 2 - width * 0.85 * 0.1),
//                (int) (width * 0.85),
//                (int) (height / 2 + width * 0.7 * 0.1));
//        drawable.draw(c);
//        return new BitmapDrawable(m.get());
//    }

    public static Drawable getDefaultSquareIcon(Context context) {
        WeakReference<Bitmap> m =
                new WeakReference<Bitmap>(Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565));
        Canvas c = new Canvas();
        c.setBitmap(m.get());
        c.drawColor(context.getResources().getColor(com.jeremy.lychee.R.color.news_image_default_color));

        return new TransformDrawable(
                new BitmapDrawable(m.get()),
                DensityUtils.dip2px(context, 4),
                TransformDrawable.TRANSFORM_ROUNDRECT);
    }

    public static Drawable getDefaultCircleIcon(Context context) {
        WeakReference<Bitmap> m =
                new WeakReference<Bitmap>(Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565));
        Canvas c = new Canvas();
        c.setBitmap(m.get());
        c.drawColor(context.getResources().getColor(com.jeremy.lychee.R.color.news_image_default_color));

        return new TransformDrawable(
                new BitmapDrawable(m.get()),
                DensityUtils.dip2px(context, 4),
                TransformDrawable.TRANSFORM_CIRCLE);
    }

    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(bytes, Base64.DEFAULT);

    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        java.lang.reflect.Field field;
        int x;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }


    //删除sign
    public static String transmitDeleteSignString(String feed_id) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        treeMap.put("channel", "cid");
        treeMap.put("os", Build.DISPLAY);
        treeMap.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        treeMap.put("os_type", "Android");
        treeMap.put("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
        treeMap.put("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        treeMap.put("sid", Session.getSid());
        treeMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
        treeMap.put("feed_id", feed_id);
        return GetSign(treeMap);

    }

    @NonNull
    private static String GetSign(TreeMap<String, String> treeMap) {
        if (treeMap == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("&");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("shi!@#$%^&*[xian!@#]*");
        String md5Str = AppUtil.getMD5code(stringBuilder.toString());
        return md5Str != null ? md5Str.substring(3, 10) : "";
    }

    public static boolean checkPackageName(Context context, String apkPath) {
        if (context == null) {
            return true;
        }

        String thisPackageName = context.getPackageName();
//    	String apkPackageName = getApkFilePkg(context, apkPath);
        String apkPackageName = getApkFilePkgByManager(context, apkPath);
        if (thisPackageName != null) {
            return thisPackageName.equals(apkPackageName);
        }
        return true;
    }

    private static String getApkFilePkgByManager(Context ctx, String apkPath) {
        try {
            PackageManager packageManager = ctx.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
            return packageInfo.packageName;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getNewsFeedbackSign(String ac, String url, String nid, String newType) {
        TreeMap<String, String> map = new TreeMap<>();
        map.putAll(getCommonParasMap());
        map.put("ac", ac);
        map.put("url", url);
        map.put("nid", nid);
        map.put("news_type", newType);
        return GetSign(map);
    }

    public static String getSyChannelSign(String cids) {
        TreeMap<String, String> map = new TreeMap<>();
        map.putAll(getCommonParasMap());
        map.put("sub", cids);
        return GetSign(map);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private static Map<String, String> getCommonParasMap() {
        Map<String, String> map = new HashMap<>();
        map.put("ver", String.valueOf(AppUtil.getVersionCode(ApplicationStatus.getApplicationContext())));
        map.put("channel", "cid");
        map.put("os", Build.DISPLAY);
        map.put("os_ver", String.valueOf(Build.VERSION.SDK_INT));
        map.put("os_type", "Android");
        map.put("carrier", ((TelephonyManager) ApplicationStatus.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
        map.put("token", AppUtil.getTokenID(ApplicationStatus.getApplicationContext()));
        map.put("sid", Session.getSid());
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
        return map;
    }

    public static void show3GTrafficAlarm(Context context, String text, Action0 cb) {
        if (AppUtil.isNetTypeMobile(context) &&
                !UserPreference.getInstance().getUseMobileNetEnabled()) {
            DialogUtil.showConfirmDialog(context, text,
                    context.getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                        cb.call();
                        UserPreference.getInstance().setUseMobileNetEnabled(true);
                        dialog.dismiss();
                    }, context.getString(com.jeremy.lychee.R.string.dialog_button_cancel), DialogInterface::dismiss
            );
            return;
        }
        cb.call();
    }


    public static void show3GTrafficAlarm(Context context, String text, Action0 actionConfirm, Action0 actoinDismiss) {
        if (AppUtil.isNetTypeMobile(context) &&
                !UserPreference.getInstance().getUseMobileNetEnabled()) {
            DialogUtil.showConfirmDialog(context, text,
                    context.getString(com.jeremy.lychee.R.string.dialog_button_confirm), (DialogInterface dialog) -> {
                        actionConfirm.call();
                        UserPreference.getInstance().setUseMobileNetEnabled(true);
                        dialog.dismiss();
                    }, context.getString(com.jeremy.lychee.R.string.dialog_button_cancel), (DialogInterface dialog) -> {
                        actoinDismiss.call();
                        dialog.dismiss();
                    }
            );
            return;
        }
        actionConfirm.call();
    }

    public static int transferAlpha(int color, float offset) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb((int) (alpha * offset), red, green, blue);
    }

    public static boolean actionEquals(Action action1, Action action2) {
        try {
            Field action1Fields[] = action1.getClass().getDeclaredFields();
            Field action2Fields[] = action2.getClass().getDeclaredFields();
            if (action1Fields.length != 1 || action1Fields.length != action2Fields.length) {
                return false;
            }

            action1Fields[0].setAccessible(true);
            action2Fields[0].setAccessible(true);
            Object action1Obj = action1Fields[0].get(action1);
            Object action2Obj = action2Fields[0].get(action2);

            return action1Obj == action2Obj;
        } catch (Throwable e) {
            return false;
        }
    }
}
