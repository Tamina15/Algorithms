package com.tmn.leetcode;

import java.util.Arrays;
import java.util.Random;
import java.util.function.IntUnaryOperator;

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
        ListNode l = new ListNode(args[0]);
        ListNode current = l;
        for (int i = 1; i < args.length; i++) {
            ListNode c = new ListNode(args[i]);
            current.next = c;
            current = c;
        }
        return l;
    }

    public static ListNode randomize(int length) {
        return from(length, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static ListNode randomize(int length, int lowerBound, int upperBound) {
        if (lowerBound >= upperBound) {
            return null;
        }
        Random r = new Random();
        return from(length, (operand) -> r.nextInt(lowerBound, upperBound));
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
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = i + startRange;
            array[i] = temp;
        }
        return from(array);
    }

    private static ListNode from(int length, IntUnaryOperator generator) {
        if (length <= 0) {
            return null;
        }
        int[] args = new int[length];
        Arrays.setAll(args, generator);
        return from(args);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int a) {
        if (a == 0) {
            return "[" + val + next.toString(1);
        }
        String v = ", " + val;
        if (next == null) {
            return v + "]";
        }
        return v + next.toString(1);
    }
}
