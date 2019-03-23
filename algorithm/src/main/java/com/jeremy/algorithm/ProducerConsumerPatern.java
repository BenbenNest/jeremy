package com.jeremy.algorithm;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerPatern {

    private static Integer count = 0;
    private static final Integer FULL = 100;


    //使用wait notify实现生产中消费者模式
    private static String LOCK = "lock";
    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < FULL; i++) {
                try {
                    Thread.sleep(100);//加这个的原因是让线程切换更加明显随机
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (LOCK) {
                    while (count == FULL) {
                        try {
                            LOCK.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + "生产者生产，目前总共有" + count);
                    LOCK.notifyAll();
                }
            }
        }
    }
    class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < FULL; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (LOCK) {
                    while (count == 0) {
                        try {
                            LOCK.wait();
                        } catch (Exception e) {
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName() + "消费者消费，目前总共有" + count);
                    LOCK.notifyAll();
                }
            }
        }
    }


    //使用ReentrantLock实现
    //创建一个锁对象
    private Lock lock = new ReentrantLock();
    //创建两个条件变量，一个为缓冲区非满，一个为缓冲区非空
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    class ProducerByLock implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //获取锁
                lock.lock();
                try {
                    while (count == FULL) {
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName()
                            + "生产者生产，目前总共有" + count);
                    //唤醒消费者
                    notEmpty.signal();
                } finally {
                    //释放锁
                    lock.unlock();
                }
            }
        }
    }
    class ConsumerByLock implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                lock.lock();
                try {
                    while (count == 0) {
                        try {
                            notEmpty.await();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName()
                            + "消费者消费，目前总共有" + count);
                    notFull.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }


    //使用阻塞队列实现
    //创建一个阻塞队列
    final BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
    class ProducerByBQ implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    blockingQueue.put(1);
                    count++;
                    System.out.println(Thread.currentThread().getName()
                            + "生产者生产，目前总共有" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class ConsumerByBQ implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    blockingQueue.take();
                    count--;
                    System.out.println(Thread.currentThread().getName()
                            + "消费者消费，目前总共有" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
