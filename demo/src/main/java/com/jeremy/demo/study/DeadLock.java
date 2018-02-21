package com.jeremy.demo.study;

/**
 * Created by changqing on 2018/2/21.
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
