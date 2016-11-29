package com.jeremy.demo.algorithm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jeremy.demo.R;

import java.util.Stack;

public class BinaryTreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary_tree);

    }


    /**
     * 满二叉树：高度为h，并且由2的h次方－1个结点的二叉树，被称为满二叉树
     * 完全二叉树：一棵二叉树中，只有最下面两层结点的度可以小于2，并且最下一层的结点集中在靠左的若干位置上
     * （叶子结点只能出现在最下层和次下层，且最下层的叶子结点集中在树的左部，一棵满二叉树必定是一棵完全二叉树，而完全二叉树未必是一棵满二叉树）
     * 二叉查找树：树中每个结点的左子树（如果有）的值小于等于根结点，右子树（如果有）的值大于等于根结点的值
     */
    public static void BinaryTreeSearch() {

    }

    public class BSTree<T extends Comparable<T>> {
        private BSNode<T> root;

        public class BSNode<T extends Comparable<T>> {
            T key;
            BSNode<T> left, right, parent;

            public BSNode(T key, BSNode<T> parent, BSNode<T> left, BSNode<T> right) {
                this.key = key;
                this.parent = parent;
                this.left = left;
                this.right = right;
            }
        }

        private void preOrderRecurse(BSNode<T> tree) {
            if (tree != null) {
                System.out.print((tree.key + ""));
                preOrderRecurse(tree.left);
                preOrderRecurse(tree.right);
            }
        }

        private void inOrderRecurse(BSNode<T> tree) {
            if (tree != null) {
                inOrderRecurse(tree.left);
                System.out.print((tree.key + ""));
                inOrderRecurse(tree.right);
            }
        }

        private void postOrderRecurse(BSNode<T> tree) {
            if (tree != null) {
                postOrderRecurse(tree.left);
                postOrderRecurse(tree.right);
                System.out.print((tree.key + ""));
            }
        }

        private void preOrder(BSNode<T> tree) {
            Stack s = new Stack();
            s.push(tree);
            while (!s.isEmpty()) {
                BSNode<T> node = (BSNode<T>) s.pop();
                System.out.print(node.key + "\n");
                if (node.left != null) s.push(node.left);
                if (node.right != null) s.push(node.right);
            }
        }

        private void inOrder(BSNode<T> tree) {
            Stack s = new Stack();
            BSNode<T> node = tree;
            while (node != null || !s.isEmpty()) {
                if (tree != null) {
                    s.push(tree);
                    node = node.left;
                } else {
                    node = (BSNode<T>) s.pop();
                    System.out.print(node.key + "\n");
                    node = node.right;
                }
            }
        }

        private void inOrder1(BSNode<T> tree) {
            Stack s = new Stack();
            BSNode<T> node = tree;
            while (node != null || !s.isEmpty()) {
                if (tree != null) {
                    s.push(tree);
                    node = node.left;
                } else {
                    node = (BSNode<T>) s.pop();
                    System.out.print((node.key) + "\n");
                    node = node.right;
                }
            }
        }

        private void postOrder(BSNode<T> tree) {
            Stack s = new Stack();
            BSNode<T> node = tree;
            BSNode<T> pre = tree;//pre标记最近出栈的节点，用于判断是否是p节点的右孩子，如果是的话，就可以访问p节点
            boolean flag = true;//flag标记是出栈还是继续将左孩子进栈
            while ((node != null || !s.isEmpty())) {
                if (node != null && flag) {
                    s.push(node);
                    node = node.left;
                } else {
                    if (s.isEmpty()) return;
                    node = (BSNode<T>) s.peek();
                    if (node.right != null && node.right != pre) {
                        node = node.right;
                        flag = true;
                    } else {
                        node = (BSNode<T>) s.pop();
                        System.out.print(node.key + "\n");
                        flag = false;
                        pre = node;
                    }
                }
            }
        }

    }

}
