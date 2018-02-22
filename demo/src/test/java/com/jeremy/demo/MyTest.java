package com.jeremy.demo;

import com.jeremy.demo.study.DeadLock;
import com.jeremy.demo.study.MyJsonParser;

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

        //如果t2使用了task2，则不会产生死锁，因为没有使用相同的资源，不过如果obj1和obj2定义的时候如果没有使用new String 的话，还是会产生死锁
        //因为字符串因为字符串池的问题，产生的对象还是同一个
//        DeadLock.MyTask task2=new DeadLock.MyTask();
        task.setFlag(2);
        Thread t2 = new Thread(task);
        t2.start();

        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }

    }

    @Test
    public void testJsonParser() {
        MyJsonParser.parse();
    }

}
