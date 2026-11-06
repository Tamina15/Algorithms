package com.tmn.leetcode;

public class ShiftingLettersII {

    public static String shiftingLetters(String s, int[][] shifts) {
        char[] c = s.toCharArray();
        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int dir = shift[2] == 1 ? 1 : -1;
            for (int j = start; j <= end; j++) {
                c[j] = (char) (((c[j] - 71 + dir) % 26) + 97);
            }
        }
        return new String(c);
    }

    public static String shiftingLetters2(String s, int[][] shifts) {
        int[] v = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            v[i] = s.charAt(i) - 'a';
        }
        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int dir = shift[2] == 1 ? 1 : -1;
            for (int j = start; j <= end; j++) {
                v[j] += dir;
            }
        }
        StringBuilder sb = new StringBuilder(s.length());
        int ai = (int) 'a';
        for (int i = 0; i < v.length; i++) {
            int a = v[i] % 26;
            if (a < 0) {
                sb.append((char) (26 + a + ai));
            } else {
                sb.append((char) (a + ai));
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        String s = "sadadw";
//        int[][] shifts = {{0, 4, 0}, {0, 2, 1}, {1, 3, 1}, {0, 4, 1}, {4, 4, 1}, {2, 3, 0}, {5, 5, 0}, {3, 3, 0}, {2, 3, 0}, {5, 5, 1}, {5, 5, 1}, {5, 5, 0}};
        int[][] shifts = new int[50000][];
        for (int i = 0, len = shifts.length; i < len; i++) {
            int[] t = new int[]{0, 49999, 0};
            shifts[i] = t;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50000; i++) {
            sb.append("a");
        }
        s = sb.toString();
        long start, end;
        for (int i = 0; i < 10; i++) {
            start = System.nanoTime();
            String result = shiftingLetters(s, shifts);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);
            start = System.nanoTime();
            String result2 = shiftingLetters2(s, shifts);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);
            System.out.println(result.equals(result2));
        }
    }
}
