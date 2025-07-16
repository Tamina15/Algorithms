package com.tmn.leetcode;

public class FindElementsInAContaminatedBinaryTree {

    static class FindElements {

        TreeNode root;

        public FindElements(TreeNode root) {
            this.root = root;
            this.root.val = 0;
            traverse(root);
        }

        private void traverse(TreeNode node) {
            if (node.left != null) {
                node.left.val = 2 * node.val + 1;
                traverse(node.left);
            }
            if (node.right != null) {
                node.right.val = 2 * node.val + 2;
                traverse(node.right);
            }
        }

        public boolean find(int target) {
            if (target == root.val) {
                return true;
            }
            int layer = log2(target + 2);
            boolean f = find(root, target, layer);
            boolean r = findRight(root, target, layer);
            return f | r;
        }

        private boolean findRight(TreeNode node, int target, int layer) {
            if (node == null || layer < 0) {
                return false;
            }
            if (node.val == target) {
                return true;
            }
            boolean r = findRight(node.right, target, layer - 1);
            return r;
        }

        private boolean find(TreeNode node, int target, int layer) {
            if (node == null) {
                return false;
            }
            if (layer == 0) {
                if (node.val == target) {
                    return true;
                }
            }
            boolean l = find(node.left, target, layer - 1);
            boolean r = find(node.right, target, layer - 1);
            return l | r;
        }

        public static final double LOG_2 = Math.log(2);

        public static int log2(int N) {
            return (int) (Math.log(N) / LOG_2);
        }
    }

    public static void main(String[] args) {
        Integer[] values = new Integer[]{-1, null, -1, -1, null, -1};
        TreeNode root = TreeNode.createTree(values);
        FindElements fe = new FindElements(root);
        System.out.println(root);
        for (int i = 0; i < 70; i++) {
//            System.out.println(i+ " " + FindElements.log2(i));
            System.out.println(i + " " + fe.find(i));
        }
    }
}
