package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.List;

/*
Implement the BSTIterator class that represents 
an iterator over the in-order traversal of a binary search tree (BST)
 */
public class BinarySearchTreeIterator {

    static class BSTIterator {

        List<TreeNode> list;
        int n;

        public BSTIterator(TreeNode root) {
            list = new ArrayList<>();
            iter(root, list);
            n = 0;
        }

        private void iter(TreeNode root, List<TreeNode> list) {
            if (root == null) {
                return;
            }
            iter(root.left, list);
            list.add(root);
            iter(root.right, list);
        }

        public int next() {
            return list.get(n++).val;
        }

        public boolean hasNext() {
            return n < list.size();
        }
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTree(7, 3, 15, null, null, 9, 20);
        BSTIterator iterator = new BSTIterator(root);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println(root);
    }
}
