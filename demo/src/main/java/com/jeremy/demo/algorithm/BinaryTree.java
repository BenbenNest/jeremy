package com.jeremy.demo.algorithm;

import java.util.LinkedList;

/**
 * Created by changqing.zhao on 2017/4/24.
 */

public class BinaryTree {

    class BinaryTreeNode<T> {
        public T value;
        public BinaryTreeNode left;
        public BinaryTreeNode right;

        public BinaryTreeNode(T val) {
            value = val;
        }
    }

    /**
     * 数组构造完全二叉树
     *
     * @param root
     * @param nums
     * @param index
     * @return
     */
    public BinaryTreeNode<Integer> buildTree(BinaryTreeNode<Integer> root, int[] nums, int index) {
        if (index >= nums.length) {
            return null;
        }
        root = new BinaryTreeNode(nums[index]);
        root.left = buildTree(root.left, nums, 2 * index + 1);
        root.right = buildTree(root.right, nums, 2 * index + 2);
        return root;
    }

    /**
     * 分层遍历二叉树（按层次从上往下，从左往右）
     * 相当于广度优先搜索，使用队列实现。队列初始化，将根节点压入队列。当队列不为空，进行如下操作：
     * 弹出一个节点，访问，若左子节点或右子节点不为空，将其压入队列。
     *
     * @param root
     */
    public static void LevelTraverse(BinaryTreeNode root) {
        if (null == root)
            return;
        LinkedList<BinaryTreeNode> list = new LinkedList<>();
        list.push(root);
        while (!list.isEmpty()) {
            BinaryTreeNode node = list.poll();
            visit(node);
            if (null != node.left)
                list.push(node);
            if (null != node.right)
                list.push(node.right);
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
    int GetNodeNumKthLevel(BinaryTreeNode root, int k) {
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
    public static int getTreeNodeCount(BinaryTreeNode root) {
        if (null == root)
            return 0;
        return getTreeNodeCount(root.left) + getTreeNodeCount(root.right) + 1;
    }

    //2. 求二叉树的深度
//    递归解法：
//            （1）如果二叉树为空，二叉树的深度为0
//（2）如果二叉树不为空，二叉树的深度 = max(左子树深度， 右子树深度) + 1
    public static int GetDepth(BinaryTreeNode root) {
        if (null != root)
            return 0;
        int depthLeft = GetDepth(root.left);
        int depthRight = GetDepth(root.right);
        return depthLeft > depthRight ? (depthLeft + 1) : (depthRight + 1);
    }

    public static void visit(BinaryTreeNode node) {
        System.out.println(node.value + "/n");
    }

    void PreOrderTraverse(BinaryTreeNode root) {
        if (null == root)
            return;
        visit(root); // 访问根节点
        PreOrderTraverse(root.left); // 前序遍历左子树
        PreOrderTraverse(root.right); // 前序遍历右子树
    }

    void InOrderTraverse(BinaryTreeNode root) {
        if (null == root)
            return;
        InOrderTraverse(root.left); // 中序遍历左子树
        visit(root); // 访问根节点
        InOrderTraverse(root.right); // 中序遍历右子树
    }

    void PostOrderTraverse(BinaryTreeNode root) {
        if (null == root)
            return;
        PostOrderTraverse(root.left); // 后序遍历左子树
        PostOrderTraverse(root.right); // 后序遍历右子树
        visit(root); // 访问根节点
    }


}
