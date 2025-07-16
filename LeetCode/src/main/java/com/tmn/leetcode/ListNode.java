package com.tmn.leetcode;

import java.util.Arrays;
import java.util.Random;

public class ListNode {

    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public static ListNode from(int... args) {
        if (args.length == 0) {
            return null;
        }
        ListNode root = new ListNode(args[0]);
        ListNode current = root;
        for (int i = 1; i < args.length; i++) {
            ListNode c = new ListNode(args[i]);
            current.next = c;
            current = c;
        }
        return root;
    }

    public static ListNode randomize(int length) {
        return randomize(length, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static ListNode randomize(int length, int lowerBound, int upperBound) {
        if (lowerBound >= upperBound || length <= 0) {
            return null;
        }
        Random r = new Random();
        int[] args = new int[length];
        Arrays.setAll(args, (operand) -> r.nextInt(lowerBound, upperBound));
        return from(args);
    }

    public static ListNode shuffle(int length) {
        return shuffle(length, 0);
    }

    public static ListNode shuffle(int length, int startRange) {
        if (length <= 0) {
            return null;
        }
        int[] array = new int[length];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            int index = random.nextInt(i);
            int temp = array[index];
            array[index] = i + startRange;
            array[i] = temp;
        }
        return from(array);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        toString1(sb);
        return sb.toString();
//        return toString(0);
    }

    private String toString(int a) {
        return ((a == 0) ? "[" : ", ") + val + ((next == null) ? "]" : next.toString(1));
    }

    private void toString1(StringBuilder sb) {
        sb.append(val);
        if (next == null) {
            sb.append("]");
        } else {
            sb.append(", ");
            next.toString1(sb);
        }
    }
}
