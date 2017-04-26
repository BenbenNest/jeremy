package com.jeremy.demo.algorithm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by changqing.zhao on 2017/4/26.
 * 用队列来实现栈 ，借助两个队列 。刚开始插入的时候因为都是空的 ，所以按理说插入哪一个队列都行 ，后面我们会进行分析，
 * <p>
 * 栈是先进后出 队列是先进先出 入栈的时候把这个数插入一个队列  出栈的时候怎么办呢？因为栈顶元素此时在队列的尾部所以我们把这个队列的前n-1个元素都放在另一个队列
 * <p>
 * 那么剩下的这个元素就是栈顶元素 直接poll()出来return就可以了  因为每一次都会把非空队列里的元素移动到空队列里面  而且插入的时候不能随便选择一个队列进行插入  例如如果此时 q1 队列为空 q2 队列非空 若往q1里面插入的话  那么在pop的时候 就找不到空的队列了 所以就出现了问题 因此插入的时候需要插入到那个非空的队列  如果两个队列都为空那插入哪一个都没有问题,下面我们来看一下简单的实现:
 */

//参考：http://blog.csdn.net/u013078669/article/details/51029708
public class StackByQueue {

    private Queue<Integer> q1 = new LinkedList<Integer>();
    private Queue<Integer> q2 = new LinkedList<Integer>();
    private int size;

    public void test(String[] args) {
        StackByQueue m = new StackByQueue();
        m.push(1);
        m.push(2);
        m.push(3);
        m.pop();
        m.push(4);
        while (!m.isEmpty()) {
            System.out.print(m.pop() + " ");
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(int e) {
        if (!q1.isEmpty()) {
            q1.add(e);
        } else {
            q2.add(e);
        }
        size++;
    }

    public int pop() {
        if (!isEmpty()) {
            if (q2.isEmpty()) {
                while (q1.size() > 1) {
                    q2.offer(q1.poll());
                }
                size--;
                return q1.poll();
            } else {
                while (q2.size() > 1) {
                    q1.offer(q2.poll());
                }
                size--;
                return q2.poll();
            }
        }
        return -1;
    }


}
