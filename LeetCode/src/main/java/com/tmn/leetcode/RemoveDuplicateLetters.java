package com.tmn.leetcode;

public class RemoveDuplicateLetters {

    public String removeDuplicateLetters(String s) {
        StringBuilder sb = new StringBuilder();
        char[] chars = s.toCharArray();
        boolean[] unique = new boolean[26];
        int minChar = chars[0] - 'a';
        int min = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] - 'a' < minChar) {
                minChar = chars[i];
                min = i;
            }
        }
        for (int i = min; i < chars.length; i++) {
            if (!unique[chars[i] - 'a']) {
                sb.append(chars[i]);
                unique[chars[i] - 'a'] = true;
            }
        }
        return sb.toString();
    }
    // not done
}
