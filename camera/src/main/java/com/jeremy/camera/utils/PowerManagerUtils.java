package com.jeremy.camera.utils;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * Created by changqing on 2018/2/13.
 */

/**
 *
 PowerManager负责对Android设备电源相关进行管理，而系统通过各种锁对电源进行控制，WakeLock是一种锁机制，只要有人拿着这把所，系统就无法进入休眠阶段。
 既然要保持应用程序一直在后台运行，那自然要获得这把锁才可以保证程序始终在后台运行。
 之前我做过一个需求是要在后台跑一个Service执行轮询，但发现一段时间以后，轮询就中断了（我测试是二十分钟后请求停止），但重新解锁屏幕后，轮询请求又开始了，
 后来在Stackoverflow上找到的WakeLock的用法，试了一下，还挺管用。
 在使用这个方法之前，我把Service置成前台Service等方法都不奏效.
 *
 */


public class PowerManagerUtils {

    WakeLock wakeLock = null;

    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLock(Context context) {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    private void releaseWakeLock() {
        if (null != wakeLock) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    /**
     * 上面第一个方法是获取锁，第二个方法是释放锁，一旦获取锁后，及时屏幕在熄灭或锁屏长时间后，系统后台一直可以保持获取到锁的应用程序运行。
     获取到PowerManager的实例pm后，再通过newWakeLock方法获取wakelock的实例，其中第一个参数是指定要获取哪种类型的锁，不同的锁对系统CPU、屏幕和键盘有不同的影响，第二个参数是自定义名称。
     各种锁的类型对CPU 、屏幕、键盘的影响：

     PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。

     SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯

     SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯

     FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度

     ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.

     ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
     */

}
