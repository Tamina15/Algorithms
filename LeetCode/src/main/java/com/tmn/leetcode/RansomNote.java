package com.tmn.leetcode;

public class RansomNote {

    public static boolean canConstruct(String ransomNote, String magazine) {
        int[] r = new int[26];
        int[] m = new int[26];
        char[] rc = ransomNote.toCharArray();
        char[] rm = magazine.toCharArray();
        for (int i = 0; i < rc.length; i++) {
            r[(rc[i] - 'a')]++;
        }
        for (int i = 0; i < rm.length; i++) {
            m[(rm[i] - 'a')]++;
        }
        for (int i = 0; i < 26; i++) {
            if (m[i] < r[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(canConstruct("bg", "efjbdfbdgfjhhaiigfhbaejahgfbbgbjagbddfgdiaigdadhcfcj"));
    }
}
