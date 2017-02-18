package com.jeremy.library.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;

import com.jeremy.library.R;
import com.jeremy.library.common.Constants;
import com.jeremy.library.permission.IPermissionSuccess;
import com.jeremy.library.permission.PermissionUtils;

import java.io.File;

import static com.jeremy.library.permission.Permissions.REQUEST_CODE_CAMERA;

/**
 * Created by changqing.zhao on 2017/2/13.
 */

public class CameraUtils {

    /**
     * 开启相机，需要处理activity onRequestPermissionsResult
     * 使用相机的时候也需要把照片路径传进去，如果不传进去，部分手机如三星获取到的intent为null,不同的手机照片存储的路径也不一样，这时候取到照片就很困难了，
     * 把路径传进去之后，可以从路径中把照片取出来，所以这里把相机权限和存储权限也当作权限的一组进行处理
     *
     * @param activity
     * @return 相机源文件
     */

    public static void openCamera(Activity activity, int photo_type) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!PermissionUtils.checkCameraPermission(activity)) {
                PermissionUtils.requestCameraPermission(activity);
            } else {
                launchCamera(activity, photo_type);
            }
        } else {
            launchCamera(activity, photo_type);
        }
    }

    public static void openCameraWithSD(Activity activity, int photo_type) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!PermissionUtils.checkSDPermission(activity) || !PermissionUtils.checkCameraPermission(activity)) {
                PermissionUtils.requestSDAndCameraPermission(activity);
            } else {
                launchCamera(activity, photo_type);
            }
        } else {
            launchCamera(activity, photo_type);
        }
    }

    /**
     * 开启相机 （已经授权过的情况调用）
     *
     * @param activity
     */
    public static void launchCamera(Activity activity, int photo_type) {
        File dir = StorageUtils.getPhotoDir(activity);
        if (dir == null) {
            //判断文件权限，打开相册的时候如果同意了
            ToastUtils.showCenter(activity, activity.getResources().getString(R.string.errcode_take_photo_sdcard));
            return;
        }
        File file = new File(dir, StorageUtils.getPhotoFileName(photo_type));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        Uri uri;
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(activity, Constants.FILE_PROVIDER, file);
            } else {
                uri = Uri.fromFile(file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, photo_type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data, OnPickFinishedCallback callback) {
        if (callback == null) {
            return;
        }
        switch (requestCode) {
            case Constants.PHOTO_TYPE_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null || data.getData() == null) {
                        callback.onPickFailed();
                    } else {
                        callback.onPickSuccessed(data.getData());
                    }
                } else {
                    callback.onPickCancel();
                }
                break;
        }
    }

    public static void onRequestPermissionsResultWithSD(final Activity activity, int photo_type, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE_CAMERA == requestCode) {
            if (!PermissionUtils.checkSDPermission(activity) && !PermissionUtils.checkCameraPermission(activity)) {
                PermissionDialogUtils.showSDAndCameraPermissionDialog(activity);
            } else if (!PermissionUtils.checkSDPermission(activity)) {
                PermissionDialogUtils.showSDPermissionDialog(activity);
            } else if (!PermissionUtils.checkCameraPermission(activity)) {
                PermissionDialogUtils.showCameraPermissionDialog(activity);
            } else {
                launchCamera(activity, photo_type);
            }
        }
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


    public interface OnPickFinishedCallback {
        void onPickSuccessed(Uri uri);

        void onPickFailed();

        void onPickCancel();
    }

}
