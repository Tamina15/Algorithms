package com.tmn.leetcode;

public class MaximumScoreAfterSplittingAString {

    public static int maxScore(String s) {
        int max = 0;
        char[] c = s.toCharArray();
        int left = (c[0] == '0') ? 1 : 0;
        int right = 0;
        for (int i = 1; i < c.length; i++) {
            right += c[i] == '1' ? 1 : 0;
        }
        for (int i = 1; i < c.length; i++) {
            int t = left + right;
            if (t > max) {
                max = t;
            }
            left += (c[i] == '0') ? 1 : 0;
            right -= c[i] == '1' ? 1 : 0;
        }
        return max;
    }

    public static void main(String[] args) {
        String s = "011101";
        System.out.println(maxScore(s));
    }
}
