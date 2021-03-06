package com.jeremy.library.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.jeremy.library.common.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Storage工具，主要提供文件访问
 *
 * @author changqing.zhao
 */
public class StorageUtils {

    private static final String TAG = "StorageUtils";
    public static final String FILE_PROVIDER = "com.jeremy.demo.fileprovider";
    public static final String FILE_SEPERATOR = File.separator;
    public static final String ROOT_SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();

    //Android使用了权限组，所以申请了READ_EXTERNAL_STORAGE，也自动会申请WRITE_EXTERNAL_STORAGE
    private static boolean hasExternalStoragePermission(@Nullable Context context) {
        int permission = context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isSDCardAvailable(@Nullable Context context) {
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            return true;
        }
        return false;
    }

    /**
     * Jacoco代码覆盖文件存储地址
     *
     * @param context
     * @return
     */
    public static File getCoverageFile(Context context) {
        File dir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            dir = Environment.getExternalStorageDirectory();
        } else {
            dir = context.getFilesDir();
        }
        dir = new File(dir, "coverage");
        if (dir == null) {
            Log.w(TAG, "Can't define system cache directory! The app should be re-installed.");
            return null;
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, "coverage.ec");
    }

    public static void deleteFile(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Files.delete(file.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            file.delete();
        }
    }


    public static File getFilesDir(@Nullable Context context) {
        //得到/data/user/0/com.jeremy.demo.pro/files  映射为 /data/data/com.jeremy.demo.pro/files，即 user/0就是第二个data
        return context.getFilesDir();
    }


    /**
     * 图片存储目录
     *
     * @param context
     * @return
     */
    public static File getPhotoDir(Context context) {
        File dir = null;
        if (isSDCardAvailable(context)) {
            dir = Environment.getExternalStorageDirectory();
            dir = new File(dir, "Android");
            if (Build.VERSION.SDK_INT < 24) {
                //在华为P9升级到7.1.1之后(升级之前并没有测试，未知)，如果添加data这个文件夹，就无法映射到相册，所以针对所有的7.0以上机型都不再添加data这个文件夹，以免其他手机也有类似情况
                dir = new File(dir, "data");
            }
            dir = new File(dir, context.getPackageName());
            dir = new File(dir, "Image");
        }
        if (dir == null) {
            dir = context.getCacheDir();
        }
        if (dir == null) {
            Log.w(TAG, "Can't define system cache directory! The app should be re-installed.");
        } else {
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return null;
                }
            }
        }
        return dir;
    }

    public static String getPhotoFileName(int photo_type) {
        switch (photo_type) {
            case Constants.PHOTO_TYPE_HEAD:
                return Constants.PHOTO_HEAD_FILE_NAME;
            default:
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("\'IMG\'_yyyyMMdd_HHmmss", Locale.getDefault());
                return dateFormat.format(date) + ".jpg";
        }
    }

    public static File getCacheDirectoryCrash(Context context) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = Environment.getExternalStorageDirectory();
            appCacheDir = new File(appCacheDir, "Android");
            if (Build.VERSION.SDK_INT < 24) {
                //在华为P9升级到7.1.1之后，如果添加data这个文件夹，就无法映射到相册，所以针对所有的7.0以上机型都不再添加data这个文件夹，以免其他手机也有类似情况
                appCacheDir = new File(appCacheDir, "data");
            }
            appCacheDir = new File(appCacheDir, context.getPackageName());
            appCacheDir = new File(appCacheDir, "CrashInfo");
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            Log.w(TAG, "Can't define system cache directory! The app should be re-installed.");
        } else {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
        }
        return appCacheDir;
    }


    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @return Cache {@link File directory}
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            Log.w(TAG, "Can't define system cache directory! The app should be re-installed.");
        }
        return appCacheDir;
    }

    public static File getCacheDirectoryNougat(Context context) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = Environment.getExternalStorageDirectory();
            appCacheDir = new File(appCacheDir, "Android");
            appCacheDir = new File(appCacheDir, context.getPackageName());
            appCacheDir = new File(appCacheDir, "cache/download");
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            Log.w(TAG, "Can't define system cache directory! The app should be re-installed.");
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
//            if (!appCacheDir.mkdirs()) {
//                Log.w(TAG, "Unable to create external cache directory");
//                return null;
//            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                Log.i(TAG, "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    /**
     * @param ctx
     * @param name
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @see #getCacheDir(Context, String, boolean)
     */
    public static File getCacheDir(Context ctx, String name)
            throws IOException, IllegalAccessException {
        return getCacheDir(ctx, name, true);
    }

    /**
     * 获取缓存目录。<br/>
     * <ol>
     * <li>{@code preferExternal}为true.优先获取设备的外部存储，若获取失败，则获取设备的内部存储
     * <li>{@code preferExternal}为false，直接获取设备的内部存储
     * </ol>
     *
     * @param ctx
     * @param name
     * @param preferExternal
     * @return
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static File getCacheDir(Context ctx, String name, boolean preferExternal)
            throws IllegalAccessException, IOException {
        File appCacheDir = null;
        if (preferExternal) {
            appCacheDir = getExternalCacheDir(ctx, name);
        }

        if (appCacheDir == null) {
            appCacheDir = getInternalCacheDir(ctx, name);
        }

        return appCacheDir;
    }

    /**
     * @param dirPath
     * @return
     * @see #getDir(File, String)
     */
    public static File getDir(String dirPath) {
        return getDir(dirPath, "");
    }

    /**
     * @param dirPath
     * @param name
     * @return
     * @see #getDir(File, String)
     */
    public static File getDir(String dirPath, String name) {
        return getDir(new File(dirPath), name);
    }

    /**
     * 获取目录
     *
     * @param dir
     * @param name
     * @return
     */
    @Nullable
    public static File getDir(File dir, String name) {
        if (!TextUtils.isEmpty(name)) {
            dir = new File(dir, name);
        }

        if (dir != null && !dir.exists() && !dir.mkdirs()) {
            return null;
        }

        return dir;
    }

    /**
     * 获取设备的外部存储目录，存在于sd卡上，是公有可访问
     *
     * @param ctx
     * @param name
     * @return
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Nullable
    public static File getExternalCacheDir(Context ctx, String name)
            throws IllegalAccessException, IOException {
        checkExternalStorageState();

        StringBuilder cacheDirPath = new StringBuilder(ROOT_SD_CARD);
        cacheDirPath.append(FILE_SEPERATOR).append("Android").append(FILE_SEPERATOR).append("data").append(FILE_SEPERATOR)
                .append(ctx.getApplicationContext().getPackageName()).append(FILE_SEPERATOR).append("cache");
        File cacheDir = getDir(cacheDirPath.toString(), name);
        if (cacheDir == null) {
            throw new IOException("External directory not created->" + cacheDirPath.toString() + FILE_SEPERATOR + name);
        }
        return cacheDir;
    }

    /**
     * 获取设备的内部缓存目录
     * <ul>
     * <li>app私有
     * <li>注意设备的内部缓存大小随版本不同而不同，<b>小心使用</b>。设备内部缓存总目录为/data
     * </ul>
     *
     * @param ctx
     * @param name
     * @return
     * @throws IOException
     */
    @Nullable
    public static File getInternalCacheDir(Context ctx, String name) throws IOException {
        File cacheDir = getDir(ctx.getCacheDir(), name);
        if (cacheDir != null) {
            return cacheDir;
        }

        StringBuilder cacheDirPath = new StringBuilder();
        cacheDirPath.append(FILE_SEPERATOR).append("data").append(FILE_SEPERATOR).append("data").append(FILE_SEPERATOR)
                .append(ctx.getPackageName()).append(FILE_SEPERATOR).append("cache");
        cacheDir = getDir(cacheDirPath.toString(), name);
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new IOException("Internal directory not created->" + cacheDirPath.toString() + FILE_SEPERATOR + name);
        }
        return cacheDir;
    }

    /**
     * 检查SD卡状态
     *
     * @return
     * @throws IllegalAccessException
     */
    public static String checkExternalStorageState() throws IllegalAccessException {
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens (Issue
            externalStorageState = "";
        }

        switch (externalStorageState) {
            case Environment.MEDIA_MOUNTED:
                return externalStorageState;
            default:
                throw new IllegalAccessException("SD卡未正确安装");

        }
    }
}
