package com.tmn.leetcode;

public class MinimumLengthOfStringAfterOperations {

    public static int minimumLength(String s) {
        int result = 0;
        char[] c = s.toCharArray();
        int[] alphabet = new int[26];
        for (int i = 0; i < c.length; i++) {
            alphabet[c[i] - 'a']++;
        }
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] < 2) {
                result += alphabet[i];
                continue;
            }
            if (alphabet[i] % 2 == 0) {
                result += 2;
            }
            if (alphabet[i] % 2 == 1) {
                result += 1;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(minimumLength("abcaabbcbb"));
    }
}
