package com.jeremy.library.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;

import com.jeremy.library.R;
import com.jeremy.library.common.Constants;
import com.jeremy.library.permission.IPermissionSuccess;

import java.io.File;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.jeremy.library.permission.Permissions.REQUEST_CODE_CAMERA;

/**
 * Created by changqing.zhao on 2017/2/13.
 */

public class CameraUtils {
    /**
     * 开启相机，需要处理activity onRequestPermissionsResult
     *
     * @param activity
     * @return 相机源文件
     */
    public static File openCamera(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                return null;
            } else {
                return launchCamera(activity);
            }
        } else {
            return launchCamera(activity);
        }
    }

    /**
     * 开启相机 （已经授权过的情况调用）
     *
     * @param activity
     */
    public static File launchCamera(Activity activity) {
//        File file = PhotoUtils.getHeadImagePhotoPath(activity);
        File file = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        Uri uri;
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(activity, Constants.FILE_PROVIDER, file);
            } else {
                uri = Uri.fromFile(file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 处理回调结果 写在onRequestPermissionsResult里面
     *
     * @param requestCode
     * @param permission
     */
    public static void onRequestPermissionsResult(final Activity activity, IPermissionSuccess permissionSuccess, int requestCode, int permission[]) {
        if (REQUEST_CODE_CAMERA == requestCode && permission.length == 3) {
            if (PackageManager.PERMISSION_GRANTED == permission[0]
                    && PackageManager.PERMISSION_GRANTED == permission[1]
                    && PackageManager.PERMISSION_GRANTED == permission[2]) {
                permissionSuccess.work();
            } else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                dialogBuilder.setTitle(R.string.permission_reminder)
                        .setMessage(R.string.camera_permission_need)
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
    }
}
