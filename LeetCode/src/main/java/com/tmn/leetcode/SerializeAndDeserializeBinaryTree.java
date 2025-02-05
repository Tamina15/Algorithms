package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class SerializeAndDeserializeBinaryTree {

    static public class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if(root == null){
            return "";
        }
        List<TreeNode> list = new ArrayList();
        list.add(root);
        list.add(root.left);
        list.add(root.right);
        return toString(serial(root, list));
    }

    public List<TreeNode> serial(TreeNode node, List<TreeNode> list) {
        if (node != null) {
            list.add(node.left);
            list.add(node.right);
        }
        return list;
    }

    public String toString(List<TreeNode> list) {
        StringBuilder sb = new StringBuilder();
        Object[] values;
        values = list.stream().map((t) -> t == null ? "null" : t.val).collect(Collectors.toList()).toArray();
        return Arrays.toString(values);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        StringTokenizer st = new StringTokenizer(data);
        return null;
    }
// not done
    public static void main(String[] args) {

        String root = "[1,2,3,null,null,4,5]";
        SerializeAndDeserializeBinaryTree sadbt = new SerializeAndDeserializeBinaryTree();
        sadbt.deserialize(sadbt.serialize(new TreeNode(0)));
    }
}
