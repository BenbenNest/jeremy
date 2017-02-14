package com.jeremy.library.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.jeremy.library.permission.Permissions;

import static com.jeremy.library.permission.Permissions.REQUEST_CODE_EXTERNAL_STORAGE;

/**
 * Created by changqing.zhao on 2017/1/12.
 */
public class PermissionUtils {

    public static boolean checkSDPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionRead != PackageManager.PERMISSION_GRANTED || permissionWrite != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestSDPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, Permissions.PERMISSIONS_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE);
    }


//    public static void checkSDPermission(Activity activity, UpdateInfo updateInfo) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
//            int permissionWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (permissionRead != PackageManager.PERMISSION_GRANTED || permissionWrite != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, Constants.PERMISSIONS_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE);
//            } else {
//                DownloadService downloadService = new DownloadService();
//                downloadService.startDownloading(activity, updateInfo);
//            }
//        } else {
//            DownloadService downloadService = new DownloadService();
//            downloadService.startDownloading(activity, updateInfo);
//        }
//    }
}
