package com.tmn.leetcode;

public class InvertBinaryTree {

    public static TreeNode invertTree(TreeNode root) {
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

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTree(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        System.out.println(root);
        root = invertTree(root);
        System.out.println(root);
    }
}
