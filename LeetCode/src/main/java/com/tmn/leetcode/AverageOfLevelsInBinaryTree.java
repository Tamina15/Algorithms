package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.List;

public class AverageOfLevelsInBinaryTree {

    public static List<Double> averageOfLevels(TreeNode root) {
        ArrayList<Double> out = new ArrayList<>();
        if (root == null) {
            return out;
        }
        ArrayList<TreeNode> list = new ArrayList<>();
        list.add(root);
        int i = 0, j = i;
        int n = list.size();
        double sum = 0;
        while (i < list.size()) {
            TreeNode node = list.get(i++);
            if (node.left != null) {
                list.add(node.left);
            }
            if (node.right != null) {
                list.add(node.right);
            }
            sum += node.val;
            if (i == n) {
                out.add(sum / (n - j));
                sum = 0;
                n = list.size();
                j = i;
            }
        }
        return out;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createTree(3, 9, 20, null, null, 15, 7);
        System.out.println(averageOfLevels(root));
    }
}
