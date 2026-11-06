package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.List;

public class FlattenBinaryTreeToLinkedList {

    public static void flatten(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        preOrderIter(root, list);
        for (int i = 0; i < list.size() - 1; i++) {
            var node = list.get(i);
            node.left = null;
            node.right = list.get(i + 1);
        }
        list.getLast().right = null;
    }

    private static void preOrderIter(TreeNode root, List<TreeNode> list) {
        if (root == null) {
            return;
        }
        list.add(root);
        preOrderIter(root.left, list);
        preOrderIter(root.right, list);
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTree(1, 2, 5, 3, 4, null, 6);
        flatten(root);
        System.out.println(root.left);
        System.out.println(root.right.val);
        System.out.println(root);
    }
}
