package com.jeremy.algorithm;

import org.junit.Test;

public class BinaryTreeTest {

    @Test
    public void initData(){
        BinaryTree.BTNode root = new BinaryTree.BTNode<Integer>(1);
        root.left= new BinaryTree.BTNode(2);
        root.right=new BinaryTree.BTNode(3);
        root.left.left=new BinaryTree.BTNode(4);
        root.left.right=new BinaryTree.BTNode(5);
        root.right.left=new BinaryTree.BTNode(6);

        int[] nums= new int[]{1,2,3,4,5,6};
        root = BinaryTree.buildTree(nums,0);

        BinaryTree.levelTraverse(root);


    }

//    class Node {
//        int data;
//        Node left;
//        Node right;
//    }
//
//    /**
//     * 给定一个二叉树，判断它是不是二叉搜索树。
//     * 思路：对于一棵二叉树，最简单的方法就是中序遍历，看是不是一个递增数列，
//     * 如果是，则是一棵二叉搜索树，如果不是，则不是二叉搜索树。
//     * 在这里用一个lastVisit去记录上一次搜索的节点。
//     * 这个过程就是先找到最左下角的节点，更新lastVisit为这个节点的值，然后按照中序遍历依次更新即可。
//     */
//    private static int lastVisit = Integer.MIN_VALUE;
//
//    public static boolean isBST(Node root) {
//        if (root == null) return true;
//        boolean judgeLeft = isBST(root.left); // 先判断左子树
//        if (root.data >= lastVisit && judgeLeft) { // 当前节点比上次访问的数值要大
//            lastVisit = root.data;
//        } else {
//            return false;
//        }
//        boolean judgeRight = isBST(root.right); // 后判断右子树
//        return judgeRight;
//    }
//
//
//    /**
//     * 给定一个二叉树，判断它是不是二叉搜索树。
//     * 思路：对于一棵完全二叉树采用广度优先遍历，从根节点开始，入队列，如果队列不为空，循环。
//     * 遇到第一个没有左儿子或者右儿子的节点，设置标志位，如果之后再遇到有左儿子或者右儿子的节点，那么这不是一棵完全二叉树。
//     * 这个方法需要遍历整棵树，复杂度为O(N)，N为节点的总数。
//     */
//
//    //实现广度遍历需要的队列
//    private LinkedList<Node> queue = new LinkedList<Node>();
//    //第n层最右节点的标志
//    private boolean leftMost = false;
//
//    public boolean isCompleteTree(Node root) {
//        //空树也是完全二叉树
//        if (root == null) return true;
//        //首先根节点入队列
//        queue.addLast(root);
//        while (!queue.isEmpty()) {
//            Node node = queue.removeFirst();
//            //处理左子结点
//            if (!processChild(node.left))
//                return false;
//            //处理右子结点
//            if (!processChild(node.right))
//                return false;
//        }
//        //广度优先遍历完毕，此树是完全二叉树
//        return true;
//    }
//
//    public boolean processChild(Node child) {
//        if (child != null) {
//            if (!leftMost) {
//                queue.addLast(child);
//            } else {
//                return false;
//            }
//        } else {
//            leftMost = true;
//        }
//        return true;
//    }

}
