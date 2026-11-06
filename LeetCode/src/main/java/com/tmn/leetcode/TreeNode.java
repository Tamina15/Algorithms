package com.tmn.leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class TreeNode implements Iterable<TreeNode> {

    int val;
    TreeNode left;
    TreeNode right;

    public TreeNode() {
        this(0);
    }

    TreeNode(int val) {
        this(val, null, null);
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public static TreeNode fromRange(int start, int end) {
        int n = Math.abs(end - start);
        Integer[] array = new Integer[n];
        if (end > start) {
            for (int i = 0; i < n; i++) {
                array[i] = i + start;
            }
        } else {
            for (int i = 0; i < n; i++) {
                array[i] = start - i;
            }
        }
        return createTree(array);
    }

    public static TreeNode createTree(Integer... values) {
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

    public static enum TraversalOrder {
        IN_ORDER_TRAVERSAL,
        PRE_ORDER_TRAVERSAL,
        POST_ORDER_TRAVERSAL,
        LEVEL_ORDER_TRAVERSAL,
        ZIGZAG_ORDER_TRAVERSAL;
    }

    public Iterator<TreeNode> iterator(TraversalOrder order) {
        ArrayList<TreeNode> list = new ArrayList<>();
        switch (order) {
            case IN_ORDER_TRAVERSAL ->
                inOrderIter(this, list);
            case PRE_ORDER_TRAVERSAL ->
                preOrderIter(this, list);
            case POST_ORDER_TRAVERSAL ->
                postOrderIter(this, list);
            case LEVEL_ORDER_TRAVERSAL ->
                levelOrderIter(this, list);
            case ZIGZAG_ORDER_TRAVERSAL ->
                zigzagOrderIter(this, list);
        }
        return list.iterator();
    }

    @Override
    public Iterator<TreeNode> iterator() {
        return iterator(TreeNode.TraversalOrder.IN_ORDER_TRAVERSAL);
    }

    private void inOrderIter(TreeNode root, List<TreeNode> list) {
        if (root == null) {
            return;
        }
        inOrderIter(root.left, list);
        list.add(root);
        inOrderIter(root.right, list);
    }

    private void preOrderIter(TreeNode root, List<TreeNode> list) {
        if (root == null) {
            return;
        }
        list.add(root);
        preOrderIter(root.left, list);
        preOrderIter(root.right, list);
    }

    private void postOrderIter(TreeNode root, List<TreeNode> list) {
        if (root == null) {
            return;
        }
        postOrderIter(root.left, list);
        postOrderIter(root.right, list);
        list.add(root);
    }

    private void levelOrderIter(TreeNode root, List<TreeNode> list) {
        if (root == null) {
            return;
        }
        list.add(root);
        int i = 0;
        while (i < list.size()) {
            TreeNode node = list.get(i++);
            if (node.left != null) {
                list.add(node.left);
            }
            if (node.right != null) {
                list.add(node.right);
            }
        }
    }

    /**
     * Iterate tree in zigzag order.
     * 
     * <pre>
     *      1
     *    /   \
     *   2     3
     *  / \   / \
     * 4   5 6   7
     * -> 1, 3, 2, 4, 5, 6, 7, 15, 14, 13, 12, 11, 10, 9, 8, 16, 17, 18, 19, ...
     * </pre>
     *
     * @param root
     * @param list
     */
    private void zigzagOrderIter(TreeNode root, List<TreeNode> list) {
        if (root == null) {
            return;
        }
        list.add(root);
        int i = 0;
        boolean addLeftFirst = false;
        while (i < list.size()) {
            int a = i;
            int b = list.size() - 1;
            while (b >= a) {
                i++;
                TreeNode node = list.get(b--);
                if (addLeftFirst) {
                    if (node.left != null) {
                        list.add(node.left);
                    }
                }
                if (node.right != null) {
                    list.add(node.right);
                }
                if (!addLeftFirst) {
                    if (node.left != null) {
                        list.add(node.left);
                    }
                }
            }
            addLeftFirst = !addLeftFirst;
        }
    }
}
