package com.jeremy.library.LogSDK;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by changqing on 2017/6/30.
 */
public class MyThreadPool {

    private static int CORE_POOL_SIZE = 5;
    private static int MAX_POOL_SIZE = 10;
    private static int KEEP_ALIVE_TIME = 10000;

    private static ThreadPoolExecutor threadPool;

    private MyThreadPool(){

    }

    //阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);

    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "MyThreadPool thread:" + integer.getAndIncrement());
        }
    };

    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue,
                threadFactory);
    }

    /**
     * 从线程池中抽取线程，执行指定的Runnable对象
     */
    public static void execute(Runnable runnable){
        threadPool.execute(runnable);
    }

}
