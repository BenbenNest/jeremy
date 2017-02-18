package com.jeremy.library.permission;


import android.Manifest;

public class Permissions {
    public final static int REQUEST_CODE_SD_CAMERA = 2000;
    public final static int REQUEST_CODE_SD = 2001;
    public final static int REQUEST_CODE_CAMERA = 2002;


    //即没有文件读写权限也没有相册权限
    public static final String[] PERMISSIONS_STORAGE_CAMERA = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    //有文件读写权限没有相册权限
    public static final String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };
    //没有文件读写权限有相册权限
    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


}
