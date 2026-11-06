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

    public static char findTheDifference2(String s, String t) {
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            sum = sum - s.charAt(i) + t.charAt(i);
        }
        sum += t.charAt(t.length() - 1);
        return (char) sum;
    }

    public static char findTheDifference3(String s, String t) {
        int x = 0;
        for (int i = 0; i < s.length(); i++) {
            x = x ^ s.charAt(i) ^ t.charAt(i);
        }
        x ^= t.charAt(t.length() - 1);
        return (char) x;
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
        long start, end;
//        String s = randomString(1000);
//        String t = shuffle(s + randomString(1));
        for (int i = 0; i < 100; i++) {
            String s = randomString(10000);
            String t = shuffle(s + randomString(1));

            start = System.nanoTime();
            findTheDifference(s, t);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);

            start = System.nanoTime();
            findTheDifference2(s, t);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);

            start = System.nanoTime();
            findTheDifference3(s, t);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);

            System.out.println("");
        }
    }
}
