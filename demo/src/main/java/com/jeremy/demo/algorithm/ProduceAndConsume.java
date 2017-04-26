package com.jeremy.demo.algorithm;

import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by changqing.zhao on 2017/4/25.
 * <p>
 * 生产者消费者模型主要由3种方法实现
 * 1.利用wait notify
 * 2.利用condition
 * 3.利用BlockingQueue阻塞队列
 */

public class ProduceAndConsume {
//    LinkedBlockingDeque
//    PriorityQueue<>

    //方法1
    private int queueSize = 10;
    private PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);

    public void test() {
        Producer1 producer1 = new Producer1();
        Consumer1 consumer1 = new Consumer1();
        producer1.start();
        consumer1.start();
    }

    class Consumer1 extends Thread {
        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空，等待生产。。。");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.poll();
                    queue.notify();
                    System.out.println("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                }
            }
        }
    }

    class Producer1 extends Thread {
        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                while (queue.size() == queueSize) {
                    try {
                        System.out.println("队列满，等待空余空间");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        notify();
                    }
                }
                queue.offer(1);
                queue.notify();
                System.out.println("向队列中插入一个元素，队列剩余" + queue.size() + "个元素");
            }
        }
    }

    //方法1
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public void test2() {
        Producer2 producer = new Producer2();
        Consumer2 consumer = new Consumer2();
        producer.start();
        consumer.start();
    }

    class Consumer2 extends Thread {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                lock.lock();
                try {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空，等待数据");
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.poll();                //每次移走队首元素
                    notFull.signal();
                    System.out.println("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Producer2 extends Thread {

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                lock.lock();
                try {
                    while (queue.size() == queueSize) {
                        try {
                            System.out.println("队列满，等待有空余空间");
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(1);        //每次插入一个元素
                    notEmpty.signal();
                    System.out.println("向队列取中插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private PriorityBlockingQueue<Integer> blockingQueue = new PriorityBlockingQueue<>(queueSize);

    class Consumer extends Thread {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                try {
                    blockingQueue.take();//blockingQueue.poll();
                    System.out.println("从队列取走一个元素，队列剩余" + blockingQueue.size() + "个元素");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Producer extends Thread {
        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                blockingQueue.put(1);
                System.out.println("向队列取中插入一个元素，队列剩余空间：" + (queueSize - blockingQueue.size()));
            }
        }
    }


}
