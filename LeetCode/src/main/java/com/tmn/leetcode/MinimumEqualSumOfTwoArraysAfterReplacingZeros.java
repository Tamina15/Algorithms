package com.tmn.leetcode;

/**
 * You are given two arrays consisting of positive integers.
 * <br>
 * <br>
 * You have to replace <b>all</b> the 0's in both arrays
 * with <b>strictly</b> positive integers
 * such that the sum of elements of both arrays becomes <b>equal</b>.
 * <br>
 * <br>
 * Return the minimum equal sum you can obtain, or -1 if it is impossible.
 *
 * @author NhatTranMinh
 */
public class MinimumEqualSumOfTwoArraysAfterReplacingZeros {

    public static long minSum(int[] nums1, int[] nums2) {
        long minSum1 = 0, minSum2 = 0;
        int countZero1 = 0, countZero2 = 0;
        for (int i = 0; i < nums1.length; i++) {
            if (nums1[i] == 0) {
                minSum1 += 1;
                countZero1++;
            } else {
                minSum1 += nums1[i];
            }
        }
        for (int i = 0; i < nums2.length; i++) {
            if (nums2[i] == 0) {
                minSum2 += 1;
                countZero2++;
            } else {
                minSum2 += nums2[i];
            }
        }
        if (countZero2 == 0) {
            if (minSum1 > minSum2) {
                return -1;
            }
        }
        if (countZero1 == 0) {
            if (minSum2 > minSum1) {
                return -1;
            }
        }
        return Math.max(minSum1, minSum2);
    }

    public static void main(String[] args) {
        System.out.println(minSum(new int[]{3, 2, 0, 1, 0}, new int[]{6, 5, 0}));
    }
}
