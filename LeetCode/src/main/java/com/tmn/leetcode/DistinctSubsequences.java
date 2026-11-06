package com.tmn.leetcode;

import java.util.Arrays;

public class DistinctSubsequences {

    public static int numDistinct(String s, String t) {
        return distinct(s.toCharArray(), t.toCharArray(), 0, 0);
    }

    public static int distinct(char[] s, char[] t, int si, int ti) {
        if (ti >= t.length) {
            return 1;
        }
        if (si >= s.length) {
            return 0;
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

    public static int numDistinct1(String s, String t) {
        return distinct1(s.toCharArray(), t.toCharArray(), 0, 0);
    }

    public static int distinct1(char[] s, char[] t, int si, int ti) {
        if (ti >= t.length) {
            return 1;
        }
        while (si < s.length && s[si] != t[ti]) {
            si++;
        }
        if (si >= s.length) {
            return 0;
        }
        int to = 0;
        to += distinct1(s, t, si + 1, ti + 1);
        to += distinct1(s, t, si + 1, ti);
        return to;
    }

    public static int numDistinct2(String s, String t) {
        int sl = s.length();
        int tl = t.length();
        int m = Math.max(sl, tl);
        int[][] cache = new int[m][m];
        for (int[] c : cache) {
            Arrays.fill(c, -1);
        }
        return distinct2(s.toCharArray(), t.toCharArray(), sl - 1, tl - 1, cache);
    }

    public static int distinct2(char[] s, char[] t, int si, int ti, int[][] cache) {
        if (ti < 0) {
            return 1;
        }
        while (si >= 0 && s[si] != t[ti]) {
            si--;
        }
        if (si < 0) {
            return 0;
        }
        int to = 0;
        // s[si] == t[ti]
        to += distinct2(s, t, si - 1, ti - 1, cache);
        to += distinct2(s, t, si - 1, ti, cache);
        return to;
    }

    public static int numDistinct3(String s, String t) {
        int m = s.length();
        int n = t.length();

        int[] dp = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            int last = 1;

            for (int j = 1; j <= n; j++) {
                int cur = dp[j];

                int take = 0, notTake;
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    take = last;
                }
                notTake = cur;

                dp[j] = (take + notTake);
                last = cur;
            }
        }

        return dp[n];
    }

    public static void main(String[] args) {
        String s = "aaddbacbbcabdbceaeeaacbabbbaccacaacbabeddbedcdceceeabccabdadccceebcbcbecdbacedcecdeadbaebceaedaaecbbebdecabbddccebaccabaaabbabceddceecadcccdceabbcdadbbadebdadeacbaddccdeebcddaebbcbedbd";
        String t = "ccdeddeabb";
        System.out.println(s);
        System.out.println(t);

        long start, end;
        System.out.println("527764581");

//        start = System.nanoTime();
//        System.out.println(numDistinct(s, t));
//        end = System.nanoTime();
//        System.out.println((end - start) * 1.0 / 1000000);
//
//        start = System.nanoTime();
//        System.out.println(numDistinct1(s, t));
//        end = System.nanoTime();
//        System.out.println((end - start) * 1.0 / 1000000);
        start = System.nanoTime();
        System.out.println(numDistinct2(s, t));
        end = System.nanoTime();
        System.out.println((end - start) * 1.0 / 1000000);

        start = System.nanoTime();
        System.out.println(numDistinct3(s, t));
        end = System.nanoTime();
        System.out.println((end - start) * 1.0 / 1000000);

    }
}
