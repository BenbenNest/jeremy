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
        if (null == head || null == head.next) return head;
        Node cur = head, pre = head, reHead = null;
        while (null != cur) {
            pre = cur;
            pre.next = reHead;
            reHead = pre;
            cur = cur.next;
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


    /**
     * 2个链表表示的整数进行相加求和
     * LinkedList属于双向链表
     */

    public Node<Integer> addList(Node<Integer> n1, Node<Integer> n2) {
        if (null == n1) return n2;
        if (null == n2) return n1;
        int temp, key = 0;
        Node<Integer> result = null;
        while (null != n1 && null != n2) {
            temp = n1.val + n2.val;
            if (temp > 10) {
                key = 1;
                temp -= 10;
            } else {
                key = 0;
            }
            if (key > 0) {
                temp += key;
            }
            Node<Integer> node = new Node<>(temp);
            if (null == result) {
                result = node;
            } else {
                result.next = node;
                result = node;
            }
        }
        if (null != n1) {
            while (null != n1) {
                temp = n1.val;
                if (key > 0) {
                    temp += key;
                    key = 0;
                }
                Node<Integer> node = new Node<>(temp);
                result.next = node;
                result = node;
            }
        }
        if (null != n2) {
            while (null != n2) {
                temp = n2.val;
                if (key > 0) {
                    temp += key;
                    key = 0;
                }
                Node<Integer> node = new Node<>(temp);
                result.next = node;
                result = node;
            }
        }
        return result;
    }

    public Node<Integer> add(Node<Integer> n1, Node<Integer> n2) {
        if (null == n1) return n2;
        if (null == n2) return n1;
        int length = dealListLen(n1, n2);
        int temp, key = 0;
        Node<Integer> result = new Node<>(0);
        while (null != n1.next) {
            temp = n1.val + n2.val;
        }
        return result;
    }

    public int dealListLen(Node<Integer> n1, Node<Integer> n2) {
        int len1 = 0, len2 = 0;
        Node<Integer> list1 = n1;
        Node<Integer> list2 = n2;
        Node<Integer> cur = n1;
        while (null != cur) {
            len1 += 1;
            cur = cur.next;
        }
        cur = n2;
        while (null != cur) {
            len2 += 1;
            cur = cur.next;
        }

        if (len1 > len2) {
            int count = 0;
            cur = n2;
            while (count < len1) {
                count += 1;
                if (null == cur.next) {
                    Node<Integer> node = new Node<>(0);
                    cur.next = node;
                    cur = node;
                } else {
                    cur = cur.next;
                }
            }
            return len1;
        }

        if (len1 < len2) {
            int count = 0;
            cur = n1;
            while (count < len2) {
                count += 1;
                if (null == cur.next) {
                    Node<Integer> node = new Node<>(0);
                    cur.next = node;
                    cur = node;
                } else {
                    cur = cur.next;
                }
            }
            return len2;
        }
        return len1;
    }

}
