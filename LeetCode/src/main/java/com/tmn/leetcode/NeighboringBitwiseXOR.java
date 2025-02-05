package com.tmn.leetcode;

public class NeighboringBitwiseXOR {

    public static boolean doesValidArrayExist(int[] derived) {
        if (derived.length == 1) {
            return derived[0] == 0;
        }
        int n = derived.length;
        int[] original = new int[n];
        original[0] = derived[0];
        original[1] = 0;
        for (int i = 1; i < n - 1; i++) {
            if (derived[i] == 1) {
                original[i + 1] = original[i] == 1 ? 0 : 1;
            }
            if (derived[i] == 0) {
                original[i + 1] = original[i] == 1 ? 1 : 0;
            }
        }
        if (derived[n - 1] == 1) {
            return original[0] != original[n - 1];
        } else {
            return original[0] == original[n - 1];
        }
    }

    public static void main(String[] args) {
        int[] derived = {1, 1, 0};
        System.out.println(doesValidArrayExist(derived));
    }
}
