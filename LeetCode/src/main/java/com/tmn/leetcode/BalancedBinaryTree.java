package com.tmn.leetcode;

public class BalancedBinaryTree {

    public static boolean isBalanced(TreeNode root) {
        int depth = depth(root);
        return depth != -1;
    }

    public static int depth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = depth(root.left);
        int right = depth(root.right);
        if (left == -1 || right == -1) {
            return -1;
        }
        int diff = Math.abs(left - right);
        if (diff != 1 && diff != 0) {
            return -1;
        }
        return Math.max(left, right) + 1;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTree(3, 9, 20, null, null, 15, 7, null, null, 8, 10);
        System.out.println(root);
        System.out.println(isBalanced(root));
    }
}
