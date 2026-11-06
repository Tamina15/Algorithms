package com.tmn.leetcode;

/*
Given the root of a binary tree, return the sum of every tree node's tilt.

The tilt of a tree node is the absolute difference between the sum
of all left subtree node values and all right subtree node values. 
If a node does not have a left child, 
then the sum of the left subtree node values is treated as 0. 
The rule is similar if the node does not have a right child.
 */
public class BinaryTreeTilt {

    public static int findTilt(TreeNode root) {
        int[] cache = new int[1];
        findTilt(root, cache);
        return cache[0];
    }

    public static int findTilt(TreeNode root, int[] tilts) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return root.val;
        }
        int sumLeft = findTilt(root.left, tilts);
        int sumRight = findTilt(root.right, tilts);
        tilts[0] += Math.abs(sumLeft - sumRight);
        return root.val + sumLeft + sumRight;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTree(4, 2, 9, 3, 5, null, 7);
        System.out.println(findTilt(root));
    }
}
