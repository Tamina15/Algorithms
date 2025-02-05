package com.tmn.leetcode;

public class ValidAnagram {
    static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] ss = new int[26];
        for (int i = 0; i < s.length(); i++) {
            int a = s.charAt(i) - 'a';
            int b = t.charAt(i) - 'a';
            ss[a]++;
            ss[b]--;
        }
        for (int i = 0; i < ss.length; i++) {
            if (ss[i] != 0) {
                return false;
            }
        }
        return true;
    }
}
