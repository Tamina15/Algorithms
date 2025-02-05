package com.tmn.leetcode;

public class DistinctSubsequences {

    public static int numDistinct(String s, String t) {
        return distinct(s.toCharArray(), t.toCharArray(), 0, 0);
    }

    public static int distinct(char[] s, char[] t, int si, int ti) {
        System.out.println(si + " " + ti);
        if (si >= s.length) {
            System.out.println("return 0");
            return 0;
        }
        if (ti >= t.length) {
            System.out.println("return 1");
            return 1;
        }
        int to = 0;
        if (s[si] != t[ti]) {
            to += distinct(s, t, si + 1, ti);
        } else {
            to += distinct(s, t, si + 1, ti + 1);
            to += distinct(s, t, si + 1, ti);
        }
        return to;
    }

    // Not done
    public static void main(String[] args) {
        String s = "rabbbit";
        String t = "rabbit";
        System.out.println(s);
        System.out.println(t);
        System.out.println(numDistinct(s, t));
    }
}
