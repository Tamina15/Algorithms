package com.tmn.leetcode;

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

    public static void main(String[] args) {
        String s = "aaddbacbbcabdbceaeeaacbabbbaccacaacbabeddbedcdceceeabccabdadccceebcbcbecdbacedcecdeadbaebceaedaaecbbebdecabbddccebaccabaaabbabceddceecadcccdceabbcdadbbadebdadeacbaddccdeebcddaebbcbedbd";
        String t = "ccdeddeabb";
        System.out.println(s);
        System.out.println(t);
        System.out.println(numDistinct(s, t));
    }
}
