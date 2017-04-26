package com.jeremy.demo.algorithm;

import java.util.Stack;

/**
 * Created by changqing.zhao on 2017/4/26.
 * 用栈来实现队列，大家都知道队列是先进先出  而栈是先进后出，这样我们可以借助两个栈。

 插入的时候进入第一个栈 当输出的时候用第二个栈。输出的时候先判断一下队列是否为空，也就是size是否为0 。

 如果为0说明还没有插入元素或者插入的元素已经都出去了。如果不为空 再判断一下第二个栈是否为空 ，如果为空且队列里有元素 ，则把第一个栈中的所有元素都放进第二个栈中 。
 */

//参考：http://blog.csdn.net/u013078669/article/details/51029553
public class QueueByStack {

    private Stack<Integer> stack1 = new Stack<Integer>();
    private Stack<Integer> stack2 = new Stack<Integer>();
    private int size;

    public void test() {
        QueueByStack s = new QueueByStack();
        s.enQueue(1);
        s.enQueue(2);
        s.enQueue(3);
        s.enQueue(4);
        s.enQueue(11);
        s.enQueue(12);
        while (!s.isEmpty()) {
            System.out.print(s.deQueue() + " ");
        }
    }

    public void enQueue(int e) {
        stack1.push(e);
        size++;
    }

    public int deQueue() {
        if (!isEmpty()) {
            if (stack2.isEmpty()) {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
                size--;
                return stack2.pop();
            } else {
                size--;
                return stack2.pop();
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return size == 0;
    }


}
