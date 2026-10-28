package com.tmn.leetcode;

public class ClearDigits {

    public static String clearDigits(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c < 'a') {
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(clearDigits("qm93xjkpaaovhqckjhg5j1o4rndtg3bobgeke"));
    }
}
