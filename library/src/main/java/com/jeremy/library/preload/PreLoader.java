package com.jeremy.library.preload;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.Nullable;

import java.lang.reflect.Method;

public class PreLoader<T> {

    private int token;
    private Handler mainThreadHandler;//用于将结果处理的task放在主线程中执行
    private HandlerThread handlerThread;//提供一个异步线程来运行预加载任务
    private Handler handler;//预加载使用的handler
    private Method methodPostSyncBarrier;
    private T data;
    private boolean destroyed;

    private PreLoader(final Loader<T> loader, final Listener<T> listener) {
        final Runnable loaderWrapper = new Runnable() {
            @Override
            public void run() {
                data = loader.load();
            }
        };
        final Runnable listenerWrapper = new Runnable() {
            @Override
            public void run() {
                if (destroyed) {
                    return;
                }
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    listener.onDataArrived(data);
                    //数据被listener处理后，PreLoader自行销毁
                    destroy();
                } else {
                    mainThreadHandler.post(this);
                }
            }
        };

        methodPostSyncBarrier = getHideMethod("postSyncBarrier");

        mainThreadHandler = new Handler(Looper.getMainLooper());
        handlerThread = new HandlerThread("pre-loader") {
            @Override
            protected void onLooperPrepared() {
                super.onLooperPrepared();
                handler = new Handler(handlerThread.getLooper());
                handler.post(loaderWrapper);
                //设置同步分割,后面post进来的sync为true的message将暂停执行
                Looper looper = handlerThread.getLooper();
                if (methodInQueue()) {
                    postSyncBarrier(looper.getQueue());
                } else {
                    postSyncBarrier(looper);
                }
                handler.post(listenerWrapper);
            }
        };
        handlerThread.start();
    }

    /**
     * 开启预加载
     * 比如:开始网络请求(在HandlerThread中执行)
     * @param loader 预加载任务
     * @param listener 加载完成后执行的任务
     */
    public static <T> PreLoader<T> preLoad(final Loader<T> loader, final Listener<T> listener) {
        return new PreLoader<>(loader, listener);
    }

    /**
     * 可以开始执行预加载结果处理
     */
    public void readyToGetData() {
        if (destroyed || handlerThread == null) {
            return;
        }
        Looper looper = handlerThread.getLooper();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            removeSyncBarrier(looper.getQueue());
        } else {
            removeSyncBarrier(looper);
        }
    }

    /**
     * 数据加载
     */
    public interface Loader<DATA> {
        DATA load();
    }

    /**
     * 数据监听
     */
    public interface Listener<DATA> {
        void onDataArrived(DATA data);
    }

    @Nullable
    private Method getHideMethod(String name) {
        Method method = null;
        try{
            if (methodInQueue()) {
                method = MessageQueue.class.getMethod(name);
            } else {
                method = Looper.class.getMethod(name);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return method;
    }

    //postSyncBarrier 和 removeSyncBarrier 方法是否在MessageQueue类中（api23及以上）
    private static boolean methodInQueue() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M;
    }

    private void postSyncBarrier(Object obj) {
        try{
            methodPostSyncBarrier = obj.getClass().getMethod("postSyncBarrier");
            token = (int) methodPostSyncBarrier.invoke(obj);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSyncBarrier(Object obj) {
        try{
            Method method = MessageQueue.class.getMethod("removeSyncBarrier", int.class);
            method.invoke(obj, token);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        handlerThread.quit();
        handlerThread = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        mainThreadHandler.removeCallbacksAndMessages(null);
        mainThreadHandler = null;
    }

}
