package com.tmn.leetcode;

/**
 * You are given two positive integers num and sum.
 * A positive integer n is good if it satisfies both of the following:
 *
 * The number of digits in n is exactly num.
 * The sum of digits in n is exactly sum.
 *
 * The score of a good integer n is the sum of the squares of digits in n.
 *
 * Return a string denoting the good integer n that achieves the maximum score.
 * If there are multiple possible integers, return the maximum ​​​​​​​one.
 * If no such integer exists, return an empty string.
 *
 * @author NhatTranMinh
 */
public class MaximizeSumOfSquaresOfDigits {

    public static String maxSumOfSquares(int num, int sum) {
        StringBuilder sb = new StringBuilder(num);
        sb.append(sum < 10 ? sum : 9);
        long s = sum < 10 ? sum : 9;
        for (int i = 1; i < num; i++) {
            long diff = sum - s;
            if (diff > 9) {
                s += 9;
                sb.append('9');
            } else {
                s += diff;
                sb.append(diff);
            }
        }
        if (s != sum) {
            return "";
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(maxSumOfSquares(10, 18));
    }
}
