package com.jeremy.demo.study;

/**
 * Created by changqing on 2018/2/21.
 */

/**
 * 死锁的四个必要条件
 * 1.互斥条件：即某个资源在一段时间内只能有一个线程占有，不能同时被2个或2个以上的线程占有
 * 2.不可抢占条件：线程所获得的资源在未使用完毕之前，资源申请者不能强行从资源占有者手中夺取资源，而只能有该资源的占有者线程自行释放
 * 3.占有且申请条件：线程至少已经占有一个资源，但又申请新的资源，由于该资源被另外线程占有，此时该线程阻塞，但是该线程在等待新资源时，仍继续占有已经占有的资源
 * 4.循环等待条件：存在一个线程等待序列{p1,p2...pn}，p1等待p2占有的资源,p2等待p3占有的资源...，pn等待p1占有的资源，形成线程等待循环
 * 解决办法：1）加锁顺序：不要嵌套加锁 2）死锁检测 ReentrantLock:ReentrantLock lock=new ReentrantLock();lock.tryLock
 */


public class DeadLock {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t1 = new Thread(task);
        t1.start();
    }

    public static class MyTask implements Runnable {
        int flag = 1;
        Object obj1 = new String("obj1");
        Object obj2 = new String("obj2");

        public void setFlag(int flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            if (flag == 1) {
                synchronized (obj1) {
                    System.out.println("locking:" + obj1);
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {

                    }
                    synchronized (obj2) {
                        System.out.println("使用顺序：obj1->obj2");
                    }
                }
            } else {
                synchronized (obj2) {
                    System.out.println("locking:" + obj2);
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {

                    }
                    synchronized (obj1) {
                        System.out.println("使用顺序：obj2->obj1");
                    }
                }
            }
        }
    }


}
