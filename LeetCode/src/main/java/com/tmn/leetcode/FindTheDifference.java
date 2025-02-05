package com.tmn.leetcode;

import java.util.Random;

public class FindTheDifference {

    public static char findTheDifference(String s, String t) {
        int[] a = new int[26];
        char[] ss = s.toCharArray();
        char[] tt = t.toCharArray();
        for (int i = 0; i < ss.length; i++) {
            a[(ss[i] - 'a')]--;
            a[(tt[i] - 'a')]++;
        }
        a[tt[tt.length - 1] - 'a']++;
        for (int i = 0; i < 26; i++) {
            if (a[i] != 0) {
                return (char) (i + 'a');
            }
        }
        return ' ';
    }

    private static String randomString(int length) {
        Random r = new Random();
        byte[] b = new byte[length];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (r.nextInt(0, 26) + 'a');
        }
        return new String(b);
    }

    static String shuffle(String string) {
        Random r = new Random();
        byte[] b = string.getBytes();
        for (int i = b.length - 1; i > 0; i--) {
            int index = r.nextInt(i + 1);
            // Simple swap
            byte a = b[index];
            b[index] = b[i];
            b[i] = a;
        }
        return new String(b);
    }

    public static void main(String[] args) {
        String s = randomString(1000);
        String t = shuffle(s + randomString(1));
        System.out.println(s);
        System.out.println(t);
        System.out.println(findTheDifference(s, t));
    }
}
