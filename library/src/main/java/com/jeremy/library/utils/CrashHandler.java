package com.jeremy.library.utils;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by changqing.zhao on 2017/2/11.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 我们在单例中引用了Context对象的时候，最简单的，我们可以使用ApplicationContext来解决Activity-Context的内存泄露问题
     */
    private Context mContext;
    private static CrashHandler instance = null;
    //定义文件后缀
    private static final String FILE_NAME_SUFFIX = ".txt";
    //系统默认的异常处理器
    private Thread.UncaughtExceptionHandler defaultCrashHandler;
    private static final String TAG = "CrashHandler";

    //饿汉式单例构造
//    private static CrashHandler crashHandler = new CrashHandler();

    //私有化构造函数
    private CrashHandler(Context context) {
        this.mContext = context;
    }

    //懒汉式实现单例
    public static CrashHandler getInstance(Context context) {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler(context);
                }
            }
        }
        return instance;
    }

    public void init() {
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置系统的默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        //记录异常信息到本地文本中
        dumpExceptionToSDCard(throwable);
        if (defaultCrashHandler != null) {
            //如果在自定义异常处理器之前，系统有自己的默认异常处理器的话，调用它来处理异常信息
            defaultCrashHandler.uncaughtException(thread, throwable);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    //记录异常信息到本地文本中
    private void dumpExceptionToSDCard(Throwable throwable) {
        File dir = StorageUtils.getCacheDirectoryCrash(mContext);
        if (dir == null) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime));
        //建立记录Crash信息的文本
        File file = new File(dir, time + FILE_NAME_SUFFIX);
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            printWriter.println(time);
            dumpPhoneInfo(printWriter);
            printWriter.println();
            throwable.printStackTrace(printWriter);
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "记录Crash信息失败");
        }
    }

    //记录手机信息
    private void dumpPhoneInfo(PrintWriter printWriter) {
        //系统版本号
        printWriter.print("OS Version:");
        printWriter.print(Build.VERSION.RELEASE);
        printWriter.print("_");
        printWriter.println(Build.VERSION.SDK_INT);
        //硬件制造商
        printWriter.print("Vendor:");
        printWriter.println(Build.MANUFACTURER);
        //系统定制商
        printWriter.print("Brand:");
        printWriter.println(Build.BRAND);
    }

}
