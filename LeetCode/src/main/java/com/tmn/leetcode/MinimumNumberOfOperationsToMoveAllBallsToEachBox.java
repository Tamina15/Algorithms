package com.tmn.leetcode;

public class MinimumNumberOfOperationsToMoveAllBallsToEachBox {

    public static int[] minOperations(String boxes) {
        int n = boxes.length();
        char[] c = boxes.toCharArray();
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (c[j] == '1') {
                    result[i] += Math.abs(j - i);
                }
            }
        }
        return result;
    }

    public static int[] minOperations_v2(String boxes) {
        int n = boxes.length();
        char[] c = boxes.toCharArray();
        int[] result = new int[n];
        int left = 0, right = 0, totalLeft = 0, totalRight = 0;
        for (int i = 0; i < n; i++) {
            if (c[i] == '1') {
                right++;
                totalRight += i;
            }
        }
        for (int i = 0; i < n; i++) {
            result[i] = totalRight + totalLeft;
            if (c[i] == '1') {
                right--;
                left++;
            }
            totalLeft += left;
            totalRight -= right;
        }
        return result;
    }

    private static String random(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(Math.random() > 0.5 ? '1' : '0');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
//            String boxes = "10101010101010011001010101101010101010100110010101011010101010101001100101010110101010101010011001010101";
            String boxes = random(2000);
            long s = System.nanoTime();
            minOperations(boxes);
            long e = System.nanoTime();
            System.out.println((e - s) * 1.0 / 1000000);

            long s2 = System.nanoTime();
            minOperations_v2(boxes);
            long e2 = System.nanoTime();
            System.out.println((e2 - s2) * 1.0 / 1000000);
            System.out.println("---------------");
        }
    }
}
