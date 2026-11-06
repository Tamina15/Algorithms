package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BinaryTreeZigzagLevelOrderTraversal {

    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        TreeMap<Integer, List<Integer>> map = new TreeMap<>();
        zigzagLevelOrder(root, 1, map);
        List<List<Integer>> list = new ArrayList<>(map.size());
        for (var entry : map.entrySet()) {
            Integer key = entry.getKey();
            List<Integer> value = entry.getValue();
            if (key % 2 == 0) {
                Collections.reverse(value);
            }
            list.add(value);
        }
        return list;
    }

    public static void zigzagLevelOrder(TreeNode root, int level, Map<Integer, List<Integer>> map) {
        if (root == null) {
            return;
        }
        var list = map.computeIfAbsent(level, k -> new ArrayList<>());
        list.add(root.val);
        zigzagLevelOrder(root.left, level + 1, map);
        zigzagLevelOrder(root.right, level + 1, map);
    }

    public static void main(String[] args) {
//        TreeNode root = TreeNode.createTree(3, 9, 20, null, null, 15, 7);
        TreeNode root = TreeNode.fromRange(1, 20);
        System.out.println(zigzagLevelOrder(root));
        var iter = root.iterator(TreeNode.TraversalOrder.ZIGZAG_ORDER_TRAVERSAL);
        while (iter.hasNext()) {
            TreeNode next = iter.next();
            System.out.print(next.val + " ");
        }
    }
}
