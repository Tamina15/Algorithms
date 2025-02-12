package com.tmn.leetcode;

import java.util.Arrays;

public class ClearDigits {

    public static String clearDigits(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if(c < 'a'){
                sb.deleteCharAt(sb.length() -1);
            } else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(clearDigits("abc"));
        String[] message = {"hello","programming","fun", "funny", "fanny"};
        Arrays.sort(message);
        System.out.println(Arrays.toString(message));
    }
}
