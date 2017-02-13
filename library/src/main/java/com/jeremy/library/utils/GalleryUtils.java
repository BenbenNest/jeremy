package com.jeremy.library.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.jeremy.library.R;
import com.jeremy.library.common.Constants;

import java.io.File;

/**
 * Created by changqing.zhao on 2017/2/13.
 */
public class GalleryUtils {

    private void exportToGallery(final Context context, File file) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            } else {
                ContentValues values = new ContentValues(2);
                values.put(MediaStore.Video.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Video.Media.DATA, file.getAbsolutePath());
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Uri uri;
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(context, Constants.FILE_PROVIDER, file);
                } else {
                    uri = Uri.parse("file://" + file.getAbsolutePath());

                    //下面这段代码主要针对类似 oppo r9 5.1系统出现的问题，不能映射到相册
                    //但是context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    //这段代码还是不能够少的，如果少了，发现小米note2 6.0系统不能映射到相册，所以MediaScannerConnection.scanFile这个方法只是为了适配个别机型所添加的，并不是通用代码
                    //真正的通用代码还是context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    //补充，后来经过验证，下面这段代码是不用添加的，是测试人员在oppo手机里面没有打开更多相册，没有看到下载的图片导致，不过下面的代码先保留吧
                    MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            context.sendBroadcast(new Intent(android.hardware.Camera.ACTION_NEW_PICTURE, uri));
                            context.sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
                        }
                    });
                }
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
            ToastUtils.showCenter(context, context.getString(R.string.image_down_success));
        } catch (Exception e) {

        }
    }


//    Uri uri;
//    if (Build.VERSION.SDK_INT >= 24) {
//        uri = FileProvider.getUriForFile(context, Constants.FILE_PROVIDER, file);
//    } else {
//        uri = Uri.parse("file://" + file.getAbsolutePath());
//        MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
//            @Override
//            public void onScanCompleted(String path, Uri uri) {
//                context.sendBroadcast(new Intent(android.hardware.Camera.ACTION_NEW_PICTURE, uri));
//                context.sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
//            }
//        });
//    }
//    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

}
