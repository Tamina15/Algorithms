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
        for (int i = 0; i < 10; i++) {

            long start = System.nanoTime();
            String result = shiftingLetters(s, shifts);
            long end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);
        }
    }
}
