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
    private static volatile CrashHandler instance2 = null;//双重锁的时候必须使用volatile关键字，这样才能实现线程安全
    //定义文件后缀
    private static final String FILE_NAME_SUFFIX = ".txt";
    //系统默认的异常处理器
    private Thread.UncaughtExceptionHandler defaultCrashHandler;
    private static final String TAG = "CrashHandler";

    //饿汉式单例构造
//    private static CrashHandler crashHandlerHungry = new CrashHandler();

    //私有化构造函数
    private CrashHandler(Context context) {
        this.mContext = context;
    }

    //懒汉式实现单例
    public synchronized static CrashHandler getInstance(Context context) {
        if (instance == null) {
            instance = new CrashHandler(context);
        }
        return instance;
    }

    public static CrashHandler getInstance2(Context context) {
        if (instance2 == null) {
            synchronized (CrashHandler.class) {
                if (instance2 == null) {
                    instance2 = new CrashHandler(context);
                }
            }
        }
        return instance2;
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
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
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

/**
 * Double-checked locking should not be used
 * <p>
 * Bug
 * <p>
 * Blocker
 * squid:S2168
 * <p>
 * Double-checked locking is the practice of checking a lazy-initialized object's state both before and after a synchronized block is entered to determine whether or not to initialize the object.
 * It does not work reliably in a platform-independent manner without additional synchronization for mutable instances of anything other than float or int. Using double-checked locking for the lazy initialization of any other type of primitive or mutable object risks a second thread using an uninitialized or partially initialized member while the first thread is still creating it, and crashing the program.
 * There are multiple ways to fix this. The simplest one is to simply not use double checked locking at all, and synchronize the whole method instead. With early versions of the JVM, synchronizing the whole method was generally advised against for performance reasons. But synchronized performance has improved a lot in newer JVMs, so this is now a preferred solution. If you prefer to avoid using synchronized altogether, you can use an inner static class to hold the reference instead. Inner static classes are guaranteed to load lazily.
 * Noncompliant Code Example
 *
 * @NotThreadSafe public class DoubleCheckedLocking {
 * private static Resource resource;
 * <p>
 * public static Resource getInstance() {
 * if (resource == null) {
 * synchronized (DoubleCheckedLocking.class) {
 * if (resource == null)
 * resource = new Resource();
 * }
 * }
 * return resource;
 * }
 * <p>
 * static class Resource {
 * <p>
 * }
 * }
 * Compliant Solution
 * @ThreadSafe public class SafeLazyInitialization {
 * private static Resource resource;
 * <p>
 * public synchronized static Resource getInstance() {
 * if (resource == null)
 * resource = new Resource();
 * return resource;
 * }
 * <p>
 * static class Resource {
 * }
 * }
 * With inner static holder:
 * @ThreadSafe public class ResourceFactory {
 * private static class ResourceHolder {
 * public static Resource resource = new Resource(); // This will be lazily initialised
 * }
 * <p>
 * public static Resource getResource() {
 * return ResourceFactory.ResourceHolder.resource;
 * }
 * <p>
 * static class Resource {
 * }
 * }
 * See
 * The "Double-Checked Locking is Broken" Declaration
 * CERT, LCK10-J. - Use a correct form of the double-checked locking idiom
 * MITRE, CWE-609 - Double-checked locking
 * JLS 12.4 - Initialization of Classes and Interfaces
 */



