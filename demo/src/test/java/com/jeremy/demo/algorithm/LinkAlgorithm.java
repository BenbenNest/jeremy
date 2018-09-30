package com.jeremy.demo.algorithm;

import org.junit.Test;

/**
 * Created by changqing on 2018/9/5.
 */

public class LinkAlgorithm {

    @Test
    public void testLink() {
        Node head = null;
        Node temp = null;
//        for (int i = 0; i < 5; i++) {
//            Node node = new Node(i + 1);
//            if (i == 0) {
//                head = node;
//                temp = node;
//            } else {
//                temp.next = node;
//                temp = temp.next;
//            }
//        }

        head = new Node(3);
        head.next = new Node(8);
        head.next.next = new Node(6);
        head.next.next.next = new Node(4);
        head.next.next.next.next = new Node(2);
        head.next.next.next.next.next = new Node(7);
        head.next.next.next.next.next.next = new Node(5);
        printNodeList(head);

//        head = reverseList1(head);
//        printNodeList(head);

//        reversePart(head,2,4);
//        head=rotateList(head,1);
//        head=rotateListRight(head,2);

        //输入一个链表，输出该链表中倒数第k个结点。
//        Node node = findKthToTail(head, 2);
//        node = node == null ? head : node;
//        printNodeList(node);

        //单链表快速排序

        quickSort(head, null);
        printNodeList(head);
    }


    public static Node reverseList1(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node reHead = reverseList(head.next);
        head.next.next = head;  //把Head接在最后
        head.next = null;  //防止循环链表
        return reHead;
    }

    public static Node reverseList(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node reHead = null;
        Node cur = head;
        while (cur != null) {
            Node temp = cur.next;
            cur.next = reHead;
            reHead = cur;
            cur = temp;
        }
        return reHead;
    }

    public void printNodeList(Node head) {
        if (head == null) return;
        while (head != null) {
            System.out.print(head.value + "_");
            head = head.next;
        }
        System.out.print("\n");
    }

    /**
     * 题目：给定一个单向链表的头结点head,以及两个整数from和to
     * ,在单项链表上把第from个节点和第to个节点这一部分进行反转
     * 列如：
     * 1->2->3->4->5->null,from=2,to=4
     * 结果：1->4->3->2->5->null
     * 列如：
     * 1->2->3->null from=1,to=3
     * 结果为3->2->1->null
     * <p>
     * 要求
     * 1、如果链表长度为N，时间复杂度要求为O（N),额外空间复杂度要求为O（1）
     * 2、如果不满足1<=from<=to<=N,则不调整
     * <p>
     * 思路：先判断是否满足1<=from<=to<=N
     * 先找到from-1个节点fPre,和to+1个节点tPos,fPre,tPos分别是反转的前一个节点和后 一个节点，反准部分反转后
     * 然后连接fPre和tPos,如果fPre为null,则说明反转部分包含头结点，则返回新的头结点，也就是没反转之前反转部分
     * 的最后一个节点，如果fPre不为null,子返回旧的头结点
     *
     * @author Think
     */
    public static class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    public Node reversePart(Node head, int from, int to) {
        int len = 0;
        Node start = head;
        Node pre = null;
        Node end = null;
        while (start != null) {
            len++;
            pre = len == from - 1 ? start : pre;
            end = len == to ? start : end;
            start = start.next;
        }
        if (from > to || from < 1 || to > len) {
            return head;
        }
        start = pre == null ? head : pre.next;
        Node left = end.next;
        end.next = null;
        pre.next = reverseList(start);
        start.next = left;
        return head;
    }


    /**
     * 旋转单链表(把右边的K个节点放到左边)
     * 题目描述：给定一个单链表，设计一个算法实现链表向右旋转 K 个位置。
     * 举例： 给定 1->2->3->4->5->6->NULL, K=3 则4->5->6->1->2->3->NULL
     * 方法一 双指针，快指针先走k步，然后两个指针一起走，当快指针走到末尾时，慢指针的下一个位置是新的顺序的头结点，这样就可以旋转链表了。
     * 方法二 先遍历整个链表获得链表长度n，然后此时把链表头和尾链接起来，在往后走n - k % n个节点就到达新链表的头结点前一个点，这时断开链表即可。
     *
     * @param head
     * @param k
     * @return
     */
    public Node rotateList(Node head, int k) {
        if (head == null || k < 1) return head;
        int n = 0;
        Node cur = head;
        Node quick = head, slow = head;
        while (n < k && quick.next != null) {
            ++n;
            quick = quick.next;
        }
        if (n < k) {
            return head;
        }
        while (quick.next != null) {
            quick = quick.next;
            slow = slow.next;
        }
        Node newHead = slow.next;
        slow.next = null;
        quick.next = head;
        return newHead;
    }

    public Node rotateListRight(Node head, int k) {
        if (head == null || k < 1) return null;
        int n = 1;
        Node cur = head;
        while (cur.next != null) {
            ++n;
            cur = cur.next;
        }
        cur.next = head;
        int m = n - k % n;
        for (int i = 0; i < m; ++i) {
            cur = cur.next;
        }
        Node newhead = cur.next;
        cur.next = null;
        return newhead;
    }

    /**
     * 题目描述
     * 输入一个链表，输出该链表中倒数第k个结点。
     * <p>
     * 解题思路
     * 经典的双指针法。定义两个指针，第一个指针从链表的头指针开始遍历向前走k-1步，第二个指针保持不动，从第k步开始，第二个指针也开始从链表的头指针开始遍历，由于两个指针的距离保持在k-1，当第一个指针到达链表的尾节点时，第二个指针刚好指向倒数第k个节点。
     * <p>
     * 关注要点
     * 1. 链表头指针是否为空，若为空则直接返回回null
     * 2. k是否为0，k为0也就是要查找倒数第0个节点，由于计数一般是从1开始的，所有输入0没有实际意义，返回null
     * 3. k是否超出链表的长度，如果链表的节点个数少于k，则在指针后移的过程中会出现next指向空指针的错误，所以程序中要加一个判断
     *
     * @param head
     * @param k
     * @return
     */
    public Node findKthToTail(Node head, int k) {
        if (head == null || k == 0)
            return null;
        Node quick = head;
        Node slow = head;
        //判断k是否超过链表节点的个数，注意是 i < k - 1
        for (int i = 0; i < k - 1; i++) {
            if (quick.next != null)
                quick = quick.next;
            else
                return null;
        }
        while (quick.next != null) {
            quick = quick.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * 求单链表的中间节点
     * 题目描述：求单链表的中间节点，如果链表的长度为偶数，返回中间两个节点的任意一个，若为奇数，则返回中间节点。
     * 解题思路：快慢指针，慢的走一步，快的走两步，当快指针到达尾节点时，慢指针移动到中间节点。
     */
    public Node findMiddleNode(Node head) {
        if (null == head) {
            return null;
        }
        Node slow = head;
        Node fast = head;

        while (null != fast && null != fast.next) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }


    /**
     * 链表划分
     * 题目描述： 给定一个单链表和数值x，划分链表使得所有小于x的节点排在大于等于x的节点之前。
     */
    public Node partition(Node head, int x) {
        if (head == null) return null;
        Node leftDummy = new Node(0);
        Node rightDummy = new Node(0);
        Node left = leftDummy, right = rightDummy;

        while (head != null) {
            if (head.value < x) {
                left.next = head;
                left = head;
            } else {
                right.next = head;
                right = head;
            }
            head = head.next;
        }

        right.next = null;
        left.next = rightDummy.next;
        return leftDummy.next;
    }

    /**
     * 链表求和
     * 题目描述：你有两个用链表代表的整数，其中每个节点包含一个数字。数字存储按照在原来整数中相反的顺序，使得第一个数字位于链表的开头。写出一个函数将两个整数相加，用链表形式返回和。
     * 解题思路：做个大循环，对每一位进行操作：当前位：(A[i]+B[i])%10 进位：（A[i]+B[i]）/10
     *
     * @param l1
     * @param l2
     * @return
     */
    public Node addLinkNumbers(Node l1, Node l2) {
        Node c1 = l1;
        Node c2 = l2;
        Node result = new Node(0);
        Node d = result;
        int sum = 0;
        while (c1 != null || c2 != null) {
            sum /= 10;
            if (c1 != null) {
                sum += c1.value;
                c1 = c1.next;
            }
            if (c2 != null) {
                sum += c2.value;
                c2 = c2.next;
            }
            d.next = new Node(sum % 10);
            d = d.next;
        }
        if (sum / 10 == 1)
            d.next = new Node(1);
        return result.next;
    }


    /**
     * 单链表排序(快速排序)
     * 题目描述：在O(nlogn)时间内对链表进行排序。
     *
     * @param head
     * @param end
     * @return
     */

    public static void quickSort(Node head, Node end) {
        if (head != end) {
            Node node = partion(head, end);
            quickSort(head, node);
            quickSort(node.next, end);
        }
    }

    public static Node partion(Node head, Node end) {
        Node p1 = head, p2 = head.next;
        while (p2 != end) {
            //大于key值时，p1向前走一步，交换p1与p2的值
            if (p2.value < head.value) {
                p1 = p1.next;
                int temp = p1.value;
                p1.value = p2.value;
                p2.value = temp;
            }
            p2 = p2.next;
        }

        //当有序时，不交换p1和key值
        if (p1 != head) {
            int temp = p1.value;
            p1.value = head.value;
            head.value = temp;
        }
        return p1;
    }


    /**
     * 合并两个排序的链表
     * 题目描述
     * 输入两个单调递增的链表，输出两个链表合成后的链表，当然我们需要合成后的链表满足单调不减规则。
     * 解题思路
     * 两种解法：递归和非递归
     */

    //递归解法
    public Node Merge(Node list1, Node list2) {
        if (list1 == null)
            return list2;
        else if (list2 == null)
            return list1;
        Node mergehead = null;
        if (list1.value <= list2.value) {
            mergehead = list1;
            mergehead.next = Merge(list1.next, list2);
        } else {
            mergehead = list2;
            mergehead.next = Merge(list1, list2.next);
        }
        return mergehead;
    }
    //非递归解法
    public Node Merge2(Node list1,Node list2) {
        if(list1 == null)
            return list2;
        else if(list2 == null)
            return list1;
        Node mergehead = null;
        if(list1.value <= list2.value){
            mergehead = list1;
            list1 = list1.next;
        }else{
            mergehead = list2;
            list2 = list2.next;
        }
        Node cur = mergehead;
        while(list1 != null && list2 != null){
            if(list1.value <= list2.value){
                cur.next = list1;
                list1 = list1.next;
            }else{
                cur.next = list2;
                list2 = list2.next;
            }
            cur = cur.next;
        }
        if(list1 == null)
            cur.next = list2;
        else if(list2 == null)
            cur.next = list1;
        return mergehead;
    }


    /**
     * 删除链表中重复的结点
     * 题目描述
     在一个排序的链表中，存在重复的结点，请删除该链表中重复的结点，重复的结点不保留，返回链表头指针。 例如，链表1->2->3->3->4->4->5 处理后为 1->2->5

     解题思路
     首先添加一个头节点，以方便碰到第一个，第二个节点就相同的情况
     设置 first ，second 指针， first 指针指向当前确定不重复的那个节点，而second指针相当于工作指针，一直往后面搜索。
     * @param pHead
     * @return
     */
    public Node deleteDuplication(Node pHead)
    {
        if(pHead == null || pHead.next == null)
            return pHead;
        Node head = new Node(-1);
        head.next = pHead;
        Node first = head;
        Node second = first.next;
        while(second != null){
            if(second.next != null && second.value == second.next.value){
                while(second.next != null && second.value == second.next.value){
                    second = second.next;
                }
                first.next = second.next;
            }else{
                first = first.next;
            }
            second = second.next;
        }
        return head.next;
    }

    /**
     * 判断单链表是否存在环
     题目描述：判断一个单链表是否有环
     分析：快慢指针，慢指针每次移动一步，快指针每次移动两步，如果存在环，那么两个指针一定会在环内相遇。
     参考：判断链表中是否有环 ----- 有关单链表中环的问题（https://www.cnblogs.com/dancingrain/p/3405197.html）
     */
    public static boolean isLinkLoop(Node head)
    {
        Node fast, slow ;
        slow = fast = head ;
        while (slow != null && fast.next != null)
        {
            slow = slow.next ;
            fast = fast.next.next ;
            if (slow == fast)
                return true ;
        }
        return false ;
    }


    public static Node findLoopStart(Node head)
    {
        Node fast, slow ;
        slow = fast = head ;

        while (slow != null && fast.next != null)
        {
            slow = slow.next ;
            fast = fast.next.next ;
            if (slow == fast) break ;
        }
        if (slow == null || fast.next == null) return null ; //没有环，返回NULL值

        Node ptr1 = head ; //链表开始点
        Node ptr2 = slow ; //相遇点
        while (ptr1 != ptr2)
        {
            ptr1 = ptr1.next ;
            ptr2 = ptr2.next ;
        }
        return ptr1 ; //找到入口点
    }



}
