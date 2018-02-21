package com.jeremy.demo;

import com.jeremy.demo.study.DeadLock;

import org.junit.Test;

/**
 * Created by changqing on 2018/2/21.
 */

public class MyTest {

    @Test
    public void testDeadLock() {
        DeadLock.MyTask task = new DeadLock.MyTask();
        task.setFlag(1);
        Thread t1 = new Thread(task);
        t1.start();

        //如果没有sleep，t1和t2的执行顺序是不确定的，而且task.setFlag(1)可能会被编译器优化掉，这样的话只有task.setFlag(2)
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        task.setFlag(2);
        Thread t2 = new Thread(task);
        t2.start();

        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }

    }

}
