package com.jeremy.demo.activity.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by jeremy on 2018/06/22.
 */

public class PluginManager {
    private PackageInfo packageInfo;
    private Resources resources;
    private Context context;
    private DexClassLoader dexClassLoader;
    private static final PluginManager ourInstance = new PluginManager();

    public static PluginManager getInstance() {
        return ourInstance;
    }

    private PluginManager() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void loadPath(Context context) {

        File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String name = "plugin.apk";
        String path = new File(filesDir, name).getAbsolutePath();

        PackageManager packageManager=context.getPackageManager();
        packageInfo=packageManager.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);


//        activity 名字
        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        dexClassLoader = new DexClassLoader(path, dexOutFile.getAbsolutePath()
                , null, context.getClassLoader());
//        resource

        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath=AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch ( Exception e) {
            e.printStackTrace();
        }
        parseReceivers(context, path);
     }

    private void parseReceivers(Context context, String path) {

//        Package对象
//        PackageParser pp = new PackageParser();

//        PackageParser.Package  pkg = pp.parsePackage(scanFile, parseFlags);


        try {

            Class   packageParserClass = Class.forName("android.content.pm.PackageParser");
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
            Object packageParser = packageParserClass.newInstance();
           Object packageObj=  parsePackageMethod.invoke(packageParser, new File(path), PackageManager.GET_ACTIVITIES);

            Field receiverField=packageObj.getClass().getDeclaredField("receivers");
//拿到receivers  广播集合    app存在多个广播   集合  List<Activity>  name  ————》 ActivityInfo   className
            List receivers = (List) receiverField.get(packageObj);

            Class<?> componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentsField = componentClass.getDeclaredField("intents");


            // 调用generateActivityInfo 方法, 把PackageParser.Activity 转换成
            Class<?> packageParser$ActivityClass = Class.forName("android.content.pm.PackageParser$Activity");
//            generateActivityInfo方法
            Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
           Object defaltUserState= packageUserStateClass.newInstance();
            Method generateReceiverInfo = packageParserClass.getDeclaredMethod("generateActivityInfo",
                    packageParser$ActivityClass, int.class, packageUserStateClass, int.class);
            Class<?> userHandler = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandler.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);

            for (Object activity : receivers) {
               ActivityInfo info= (ActivityInfo) generateReceiverInfo.invoke(packageParser,  activity,0, defaltUserState, userId);
               BroadcastReceiver broadcastReceiver= (BroadcastReceiver) dexClassLoader.loadClass(info.name).newInstance();
                List<? extends IntentFilter> intents= (List<? extends IntentFilter>) intentsField.get(activity);
                for (IntentFilter intentFilter : intents) {
                    context.registerReceiver(broadcastReceiver, intentFilter);
                }
            }
            //generateActivityInfo
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Resources getResources() {
        return resources;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }
}
