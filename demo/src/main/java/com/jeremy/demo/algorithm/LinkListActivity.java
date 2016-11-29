package com.jeremy.demo.algorithm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jeremy.demo.R;

public class LinkListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_list);
    }

    public static void test() {
        Node node1 = new Node<Integer>(5);
        Node node2 = new Node<Integer>(8);
        Node node3 = new Node<Integer>(9);
        Node node4 = new Node<Integer>(3);
        Node node5 = new Node<Integer>(7);
        Node node6 = new Node<Integer>(6);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        String before = printList(node1);
//        Node reNode = revertList(node1);
        Node reNode = revertListRecurse(node1);
        String after = printList(reNode);
    }

    public static class Node<T> {
        T val;
        Node<T> next;

        public Node(T val) {
            this.val = val;
        }
    }

    public static String printList(Node head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            if (head.next == null) {
                sb.append(head.val);
            } else {
                sb.append(head.val + "-");
            }
            head = head.next;
        }
        return sb.toString();
    }

    /**
     * 反转链表：非递归
     *
     * @param head
     * @return
     */
    public static Node revertList(Node head) {
        if (head == null || head.next == null) return head;
        Node reHead = null, pre = null;
        Node cur = head;
        while (cur != null) {
            pre = cur;
            cur = cur.next;
            pre.next = reHead;
            reHead = pre;
        }
        return reHead;
    }

    public static Node revertListRecurse(Node head) {
        if (head == null || head.next == null) return head;
        Node reHead = revertListRecurse(head.next);
        head.next.next = head;
        head.next = null;
        return reHead;
    }


}
