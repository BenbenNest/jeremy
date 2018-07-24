package com.jeremy.library.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by changqing on 2018/7/23.
 */

public class ApkUtils {

    public static void openApp(Context context, String packageName) {
        // TODO 把应用杀掉然后再启动，保证进入的是第一个页面
        PackageInfo pi = null;
        try {
            pi = context.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);
        PackageManager pManager = context.getApplicationContext().getPackageManager();
        List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent,
                0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String startappName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            System.out.println("启动的activity是: " + startappName + ":" + className);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName(startappName, className);

            intent.setComponent(cn);
            context.getApplicationContext().startActivity(intent);
        }
    }
}
