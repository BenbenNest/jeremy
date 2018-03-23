package com.jeremy.demo.study.base;

/**
 * Created by changqing on 2018/3/23.
 */


import android.support.v4.util.Pools;

/**
 * 前言
 * 在阅读Glide源码时,发现Glide大量使用对象池Pools来对频繁需要创建和销毁的代码进行优化。
 * <p>
 * 比如Glide中,每个图片请求任务,都需要用到 EngineJob 、DecodeJob类。
 * 若每次都需要重新new这些类,并不是很合适。而且在大量图片请求时,频繁创建和销毁这些类,可能会导致内存抖动,影响性能。
 * <p>
 * Glide使用对象池的机制,对这种频繁需要创建和销毁的对象保存在一个对象池中。
 * 每次用到该对象时,就取对象池空闲的对象,并对它进行初始化操作,从而提高框架的性能。
 * <p>
 * Android官方对象池的简单实现:SimplePool,也是用得最多的实现
 * 原理:使用mPool数组来维护对象池。使用mPoolSize来表示当前对象池剩余的空间。
 * <p>
 * 由于当请求SimplePool对象池时,若对象池由于空间满了,会返回为null。
 * 在实战起来,会很不方便,所以要对SimplePool进行优化,使得当对象池的容量满了,请求对象池能重新生成一个新的对象。
 * <p>
 * 改造原理:
 * 参考了Glide源码,在基于SimplePool的基础上,定义接口类Factory,里面的成员函数create()能用来生成新的对象。
 * 请求对象池时,先请求SimplePool能否返回对象,若返回则返回给调用者。若返回为null,则调用Factory的create(),生成新对象来返回给调用者。
 * <p>
 * 注意:每次获取到空闲的对象时,需要对该对象进行一次初始化操作。
 */


public class CustomPool {

    private final String TAG = "CustomPool";

    private static class LogMessage {
        private static final Pools.SimplePool<LogMessage> sPools = new Pools.SimplePool<>(10);

        public static LogMessage obtain() {
            LogMessage instance = sPools.acquire();
            if (instance == null) {
                instance = new LogMessage();
            } else {

            }
            return instance;
        }

        public void recycle() {
            init();
            sPools.release(this);
        }

        private volatile String msg;
        private volatile String threadName;
        private volatile byte[] bytes;
        private volatile boolean isBinary = false;
        private volatile long timeStamp;

        private void init() {
            msg = null;
            threadName = null;
            bytes = null;
            isBinary = false;
            timeStamp = -1;
        }
    }

}
