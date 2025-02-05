package com.tmn.leetcode;

public class InvertBinaryTree {

    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode l = root.left;
        root.left = root.right;
        root.right = l;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }
}
