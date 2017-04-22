package com.jeremy.demo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jeremy.demo.R;
import com.jeremy.library.permission.PermissionUtils;
import com.jeremy.library.utils.StorageUtils;
import com.jeremy.library.utils.SystemUtils;
import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.compat.PackageManagerCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DroidPluginActivity extends AppCompatActivity {
    private TextView tvTest;
    private File[] plugins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droid_plugin);
        init();
    }

    private void init() {
        tvTest = (TextView) findViewById(R.id.tv_test);
        Button btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage("com.jeremy.plugin");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if (PermissionUtils.checkSDPermission(this)) {
            copyApk();
        } else {
            PermissionUtils.requestSDPermission(this);
        }
    }

//    public void installApk(String apkPath) {
//        if (TextUtils.isEmpty(apkPath)) {
//            return;
//        }
//        File file = new File(apkPath);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //判读版本是否在7.0以上
//        if (Build.VERSION.SDK_INT >= 24) {
//            Uri apkUri = FileProvider.getUriForFile(this, Constants.FILE_PROVIDER, file);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            copyApk();
        } else {
            android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
            dialogBuilder.setTitle(R.string.permission_reminder)
                    .setMessage(R.string.camera_external_storage_permission_need)
                    .setCancelable(true)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SystemUtils.launchAppDetailSettingIntent(DroidPluginActivity.this, DroidPluginActivity.this.getPackageName());
                        }
                    })
                    .show();
        }
    }

    private void copyApk() {
        try {
            InputStream is = getAssets().open("plugin-debug.apk");
            File dir = StorageUtils.getCacheDirectory(this);
            dir = new File(dir, "plugin");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File apkFile = new File(dir, "plugin.apk");
            //文件存在并且进行MD5校验
            if (!apkFile.exists()) {
                FileOutputStream fos = new FileOutputStream(apkFile);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
            PluginManager.getInstance().installPackage(apkFile.getAbsolutePath(), PackageManagerCompat.INSTALL_REPLACE_EXISTING);
            tvTest.setText(apkFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        AssetUtils.copyFilesFromassets(this, "plugin-debug.apk", apkPath.getAbsolutePath());

//        //获取插件
//        File file = new File(Environment.getExternalStorageDirectory(), "/plugin");
//        plugins = file.listFiles();
//        //没有插件
//        if (plugins == null || plugins.length == 0) {
//            return;
//        }
//        //安装第一个插件
//        else {
//            try {
//                PluginManager.getInstance().installPackage(plugins[0].getAbsolutePath(), PackageManagerCompat.INSTALL_REPLACE_EXISTING);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            tvTest.setText(plugins[0].getAbsolutePath());
//        }

    }

}
