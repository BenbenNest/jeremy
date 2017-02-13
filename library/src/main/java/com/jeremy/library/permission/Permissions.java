package com.jeremy.library.permission;


import android.Manifest;

public class Permissions {
    public final static int REQUEST_CODE_CAMERA = 0x01;
    public final static int REQUEST_CODE_EXTERNAL_STORAGE = 0x02;


    //Android 6.0以上动态获取SD卡权限
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
}
