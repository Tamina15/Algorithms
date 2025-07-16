package com.tmn.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class TreeNode {

    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public static TreeNode createTree(Integer[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Tree length must not be null or 0");
        }
        int length = values.length;
        Deque<TreeNode> queue = new ArrayDeque<>(length);
        TreeNode root = new TreeNode(values[0]);
        queue.offer(root);
        for (int i = 1; !queue.isEmpty();) {
            TreeNode node = queue.poll();
            if (i < length && values[i] != null) {
                TreeNode left = new TreeNode(values[i]);
                node.left = left;
                queue.offer(left);
            }
            i++;
            if (i < length && values[i] != null) {
                TreeNode right = new TreeNode(values[i]);
                node.right = right;
                queue.offer(right);
            }
            i++;
        }
        return root;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(val);

        toString1(sb, new StringBuilder(""), left, right != null);
        toString1(sb, new StringBuilder(""), right, false);

        return sb.toString();
    }

    private void toString1(StringBuilder sb, StringBuilder padding, TreeNode node, boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n").append(padding.toString()).append("|__").append(node.val);
            padding.append(hasRightSibling ? "|  " : "   ");
            if (node.left != null) {
                toString1(sb, new StringBuilder(padding), node.left, node.right != null);
            } else {
                if (node.right != null) {
                    sb.append("\n").append(padding.toString()).append("|__").append("null");
                }
            }
            if (node.right != null) {
                toString1(sb, padding, node.right, false);
            }
        }
    }

}
