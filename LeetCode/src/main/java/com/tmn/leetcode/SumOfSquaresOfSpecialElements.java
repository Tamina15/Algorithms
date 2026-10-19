package com.tmn.leetcode;

/**
 * You are given a 1-indexed integer array nums of length n.
 *
 * An element nums[i] of nums is called special if i divides n, i.e. n % i == 0.
 *
 * Return the sum of the squares of all special elements of nums.
 *
 * @author NhatTranMinh
 */
public class SumOfSquaresOfSpecialElements {

    public int sumOfSquares(int[] nums) {
        int sum = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (n % (i + 1) == 0) {
                sum += nums[i] * nums[i];
            }
        }
        return sum;
    }

    public static int mySqrt(int x) {
        if (x == 0 || x == 1) {
            return x;
        }
        int i = 1;
        while (i < x) {
            long value = (long) i * i;
            if (value == x) {
                return i;
            }
            if (value > x) {
                return i - 1;
            }
            i++;
        }
        return i - 1;
    }

    public static void main(String[] args) {
        System.out.println(mySqrt(2147483647));
    }
}
