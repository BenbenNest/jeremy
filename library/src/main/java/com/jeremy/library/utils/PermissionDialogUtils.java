package com.jeremy.library.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.jeremy.library.R;

/**
 * Created by changqing.zhao on 2017/2/18.
 */

public class PermissionDialogUtils {

    //引导用户去设置存储权限的时候弹出的Dialog
    public static void showSDPermissionDialog(final Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(R.string.permission_reminder)
                .setMessage(R.string.permissions_SD)
                .setCancelable(true)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SystemUtils.launchAppDetailSettingIntent(activity.getApplicationContext(), activity.getPackageName());
                    }
                })
                .show();

    }

    //引导用户去设置相机权限的时候弹出的Dialog
    public static void showCameraPermissionDialog(final Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(R.string.permission_reminder)
                .setMessage(R.string.permissions_CAMERA)
                .setCancelable(true)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SystemUtils.launchAppDetailSettingIntent(activity.getApplicationContext(), activity.getPackageName());
                    }
                })
                .show();
    }

    //引导用户去设置文件读取权限和相机权限的时候弹出的Dialog
    public static void showSDAndCameraPermissionDialog(final Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(R.string.permission_reminder)
                .setMessage(R.string.permissions_SD_CAMERA)
                .setCancelable(true)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SystemUtils.launchAppDetailSettingIntent(activity.getApplicationContext(), activity.getPackageName());
                    }
                })
                .show();
    }


}
