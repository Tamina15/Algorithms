package com.tmn.leetcode;

/**
 * Given the root of a binary tree, check whether it is a mirror of itself
 * (i.e., symmetric around its center).
 *
 * @author NhatTranMinh
 */
public class SymmetricTree {

    public static boolean isSymmetric(TreeNode root) {
        return isSymmetric(root.left, root.right);
    }

    public static boolean isSymmetric(TreeNode left, TreeNode right) {
        if (left == null || right == null) {
            return left == null && right == null;
        }
        if (isSymmetric(left.right, right.left) && isSymmetric(left.left, right.right)) {
            return left.val == right.val;
        }
        return false;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTree(2, null, 3, null, 4, null, 5, null, 6);
        System.out.println(root);
        System.out.println(isSymmetric(root));
    }
}
