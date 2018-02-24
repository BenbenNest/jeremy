package com.jeremy.demo.study.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by changqing on 2018/2/24.
 */

public class ReentrancLockTest {

    //把解锁操作括在finally字句之内是至关重要的，如果受保护的代码抛出异常，锁可以得到释放，这样可以避免死锁的发生
    public void lockMethod() {
        ReentrantLock myLock = new ReentrantLock();
        myLock.lock();
        try{
            // 受保护的代码段
            //critical section
        } finally {
            // 可以保证发生异常 锁可以得到释放 避免死锁的发生
            myLock.unlock();
        }
    }

    // 以下是ReentrantLock中断机制的一个代码实现、如果换成synchronized就会出现死锁
    public static class AttemptLocking {
        public ReentrantLock lock = new ReentrantLock();

        public void untimed() {
            boolean captured = lock.tryLock();
            try {
                System.out.println("tryLock(): " + captured);
            } finally {
                if (captured)
                    lock.unlock();
            }
        }

        public void timed() {
            boolean captured = false;
            try {
                captured = lock.tryLock(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println("tryLock(2, TimeUnit.SECONDS): " + captured);
            } finally {
                if (captured)
                    lock.unlock();
            }
        }
    }




}
