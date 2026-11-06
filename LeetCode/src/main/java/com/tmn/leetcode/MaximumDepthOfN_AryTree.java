package com.tmn.leetcode;

public class MaximumDepthOfN_AryTree {

    public int maxDepth(Node root) {
        if (root == null) {
            return 0;
        }
        if (root.children == null || root.children.isEmpty()) {
            return 1;
        }
        int max = 0;
        for (Node n : root.children) {
            int m = maxDepth(root);
            if (m > max) {
                max = m;
            }
        }
        return max + 1;
    }

}
