package com.tmn.leetcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * You are given the root of a binary tree.
 *
 * Return all root-to-leaf paths in any order.
 *
 * A leaf is a node with no children.
 *
 * @author NhatTranMinh
 */
public class BinaryTreePaths {

    public static List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        String prefix = Integer.toString(root.val);
        if (root.left == null && root.right == null) {
            result.add(prefix);
        } else {
            binaryTreePath(root.left, result, prefix);
            binaryTreePath(root.right, result, prefix);
        }
        return result;
    }

    public static void binaryTreePath(TreeNode node, List<String> result, String prefix) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            result.add(prefix + "->" + node.val);
            return;
        }
        String prefix1 = prefix + "->" + node.val;
        binaryTreePath(node.left, result, prefix1);
        binaryTreePath(node.right, result, prefix1);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TreeNode root = TreeNode.createTree(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        var result = binaryTreePaths(root);
        System.out.println(root);
        System.out.println(result);
    }
}
