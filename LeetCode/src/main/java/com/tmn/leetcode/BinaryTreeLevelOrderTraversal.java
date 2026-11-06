package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BinaryTreeLevelOrderTraversal {

    public static List<List<Integer>> levelOrder(TreeNode root) {
        Map<Integer, List<Integer>> map = new TreeMap<>();
        levelOrder(root, 1, map);
        return new ArrayList<>(map.values());
    }

    public static void levelOrder(TreeNode root, int level, Map<Integer, List<Integer>> map) {
        if (root == null) {
            return;
        }
        var list = map.computeIfAbsent(level, k -> new ArrayList<>());
        list.add(root.val);
        levelOrder(root.left, level + 1, map);
        levelOrder(root.right, level + 1, map);
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTree(3, 9, 20, null, null, 15, 7);
        for (var l : levelOrder(root)) {
            System.out.println(l);
        }
        for (Iterator<TreeNode> iterator = root.iterator(TreeNode.TraversalOrder.LEVEL_ORDER_TRAVERSAL); iterator.hasNext();) {
            TreeNode next = iterator.next();
            System.out.println(next.val);
        }
    }
}
