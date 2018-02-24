package com.jeremy.demo;

import com.jeremy.demo.study.thread.ReentrancLockTest;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by changqing on 2018/2/21.
 */

public class MyTest {

    @Test
    public void testDeadLock() {
//        DeadLock.MyTask task = new DeadLock.MyTask();
//        task.setFlag(1);
//        Thread t1 = new Thread(task);
//        t1.start();
//
//        //如果没有sleep，t1和t2的执行顺序是不确定的，而且task.setFlag(1)可能会被编译器优化掉，这样的话只有task.setFlag(2)
//        try {
//            Thread.sleep(1000);
//        } catch (Exception e) {
//
//        }
//
//        //如果t2使用了task2，则不会产生死锁，因为没有使用相同的资源，不过如果obj1和obj2定义的时候如果没有使用new String 的话，还是会产生死锁
//        //因为字符串因为字符串池的问题，产生的对象还是同一个
////        DeadLock.MyTask task2=new DeadLock.MyTask();
//        task.setFlag(2);
//        Thread t2 = new Thread(task);
//        t2.start();
//
//        try {
//            Thread.sleep(10000);
//        } catch (Exception e) {
//
//        }

    }

    @Test
    public void testJsonParser() {
//        MyJsonParser.parse();
    }

    @Test
    public void testReentrantLock() throws InterruptedException {
        final ReentrancLockTest.AttemptLocking al = new ReentrancLockTest.AttemptLocking();
        al.untimed(); // True -- 可以成功获得锁
        al.timed(); // True --可以成功获得锁
        //新创建一个线程获得锁并且不释放
        new Thread() {
            {
                setDaemon(true);
            }

            public void run() {
                al.lock.lock();
                System.out.println("acquired");
            }
        }.start();
        Thread.sleep(100);// 保证新线程能够先执行
        al.untimed(); // False -- 马上中断放弃
        al.timed(); // False -- 等两秒超时后中断放弃
    }

    /**

     Lock接口的 线程请求锁的 几个方法：
     lock(), 拿不到lock就不罢休，不然线程就一直block。 比较无赖的做法。
     tryLock()，马上返回，拿到lock就返回true，不然返回false。 比较潇洒的做法。
     带时间限制的tryLock()，拿不到lock，就等一段时间，超时返回false。比较聪明的做法。

     下面的lockInterruptibly()就稍微难理解一些。
     先说说线程的打扰机制，每个线程都有一个 打扰 标志。这里分两种情况，
     1. 线程在sleep或wait,join， 此时如果别的进程调用此进程的 interrupt（）方法，此线程会被唤醒并被要求处理InterruptedException；(thread在做IO操作时也可能有类似行为，见java thread api)
     2. 此线程在运行中， 则不会收到提醒。但是 此线程的 “打扰标志”会被设置， 可以通过isInterrupted()查看并 作出处理。

     lockInterruptibly()和上面的第一种情况是一样的， 线程在请求lock并被阻塞时，如果被interrupt，则“此线程会被唤醒并被要求处理InterruptedException”。

     */

    //lock()忽视interrupt(), 拿不到锁就 一直阻塞：
    @Test
    public void testLock() throws Exception{
        final Lock lock=new ReentrantLock();
        lock.lock();
        Thread.sleep(1000);
        Thread t1=new Thread(new Runnable(){
            @Override
            public void run() {
                lock.lock();
                System.out.println(Thread.currentThread().getName()+" interrupted.");
            }
        });
        t1.start();
        Thread.sleep(1000);
        t1.interrupt();
        Thread.sleep(1000000);
    }

    //lockInterruptibly()会响应打扰 并catch到InterruptedException
    @Test
    public void testLockInterruptibly() throws Exception{
        final Lock lock=new ReentrantLock();
        lock.lock();
        Thread.sleep(1000);
        Thread t1=new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+" interrupted.");
                }
            }
        });
        t1.start();
        Thread.sleep(1000);
        t1.interrupt();
        Thread.sleep(1000000);
    }



}
