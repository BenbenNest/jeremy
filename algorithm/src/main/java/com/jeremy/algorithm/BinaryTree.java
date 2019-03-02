package com.jeremy.algorithm;

import java.util.ArrayDeque;
import java.util.LinkedList;

/**
 * Created by changqing.zhao on 2017/4/24.
 */

public class BinaryTree {

    public static class BTNode<T> {
        public T value;
        public BTNode left;
        public BTNode right;

        public BTNode(T val) {
            value = val;
        }
    }

    /**
     * 递归
     * 堆的话一般都是用完全二叉树来实现的，比如大堆和小堆。依据二叉树的性质，完全二叉树和满二叉树采用顺序存储比较合适
     * 数组构造完全二叉树
     * 若设二叉树的深度为h，除第 h 层外，其它各层 (1～h-1) 的结点数都达到最大个数，第 h 层所有的结点都连续集中在最左边，这就是完全二叉树。
     * @param nums
     * @param index
     * @return
     */
    public static BTNode<Integer> buildTree(int[] nums, int index) {
        if (index >= nums.length) {
            return null;
        }
        BTNode<Integer> root = new BTNode(nums[index]);
        root.left = buildTree(nums, 2 * index + 1);
        root.right = buildTree( nums, 2 * index + 2);
        return root;
    }

    /**
     * 分层遍历二叉树（按层次从上往下，从左往右）
     * 相当于广度优先搜索，使用队列实现。队列初始化，将根节点压入队列。当队列不为空，进行如下操作：
     * 弹出一个节点，访问，若左子节点或右子节点不为空，将其压入队列。
     * Push() and pop() are by convention operations related to Stacks (Deque, more specifically in this context) and that's why you should expect your LinkedList to work that way when you use those method. If you want your LinkedList to work as a Queue instead (it implements the Queue interface) the methods you want to use (as stated in the Documentation) are add() and remove().
     * Push() and pop() 是使用栈的模式实现
     * offer() and poll 是使用队列的模式实现的，
     * 这里的LinkedList可以替换为ArrayDeque，考虑到性能问题，换成了ArrayDeque
     * @param root
     */
    public static void levelTraverse(BTNode root) {
        if (null == root)
            return;
        ArrayDeque<BTNode> list = new ArrayDeque<>();
        list.offer(root);
        while (!list.isEmpty()) {
            BTNode node = list.poll();
            visit(node);
                if (null != node.left)
                    list.offer(node.left);
                if (null != node.right)
                    list.offer(node.right);
        }
        return;
    }

    /**
     * 求二叉树第K层的节点个数
     * 6. 求二叉树第K层的节点个数
     * 递归解法：
     * （1）如果二叉树为空或者k<1返回0
     * （2）如果二叉树不为空并且k==1，返回1
     * （3）如果二叉树不为空且k>1，返回左子树中k-1层的节点个数与右子树k-1层节点个数之和
     *
     * @param k
     * @return
     */
    int GetNodeNumKthLevel(BTNode root, int k) {
        if (null == root || k < 1)
            return 0;
        if (k == 1)
            return 1;
        int numLeft = GetNodeNumKthLevel(root.left, k - 1); // 左子树中k-1层的节点个数
        int numRight = GetNodeNumKthLevel(root.right, k - 1); // 右子树中k-1层的节点个数
        return (numLeft + numRight);
    }

    //    1. 求二叉树中的节点个数
    //    递归解法：
    //   （1）如果二叉树为空，节点个数为0
    //   （2）如果二叉树不为空，二叉树节点个数 = 左子树节点个数 + 右子树节点个数 + 1
    public static int getTreeNodeCount(BTNode root) {
        if (null == root)
            return 0;
        return getTreeNodeCount(root.left) + getTreeNodeCount(root.right) + 1;
    }

    //2. 求二叉树的深度
//    递归解法：
//            （1）如果二叉树为空，二叉树的深度为0
//（2）如果二叉树不为空，二叉树的深度 = max(左子树深度， 右子树深度) + 1
    public static int GetDepth(BTNode root) {
        if (null != root)
            return 0;
        int depthLeft = GetDepth(root.left);
        int depthRight = GetDepth(root.right);
        return depthLeft > depthRight ? (depthLeft + 1) : (depthRight + 1);
    }

    public static void visit(BTNode node) {
        System.out.println(node.value + "\n");
    }

    void PreOrderTraverse(BTNode root) {
        if (null == root)
            return;
        visit(root); // 访问根节点
        PreOrderTraverse(root.left); // 前序遍历左子树
        PreOrderTraverse(root.right); // 前序遍历右子树
    }

    void InOrderTraverse(BTNode root) {
        if (null == root)
            return;
        InOrderTraverse(root.left); // 中序遍历左子树
        visit(root); // 访问根节点
        InOrderTraverse(root.right); // 中序遍历右子树
    }

    void PostOrderTraverse(BTNode root) {
        if (null == root)
            return;
        PostOrderTraverse(root.left); // 后序遍历左子树
        PostOrderTraverse(root.right); // 后序遍历右子树
        visit(root); // 访问根节点
    }


}
