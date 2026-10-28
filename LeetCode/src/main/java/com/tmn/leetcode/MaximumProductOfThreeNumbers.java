package com.tmn.leetcode;

/**
 *
 * You are given an integer array.
 *
 * Find three numbers whose product is maximum and return the maximum product.
 *
 * @author NhatTranMinh
 */
public class MaximumProductOfThreeNumbers {

    public static int maximumProduct(int[] nums) {
        int n = nums.length;
        if (n == 3) {
            return nums[0] * nums[1] * nums[2];
        }
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > nums[max]) {
                max = i;
            }
        }
        int max2 = max == 0 ? 1 : 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > nums[max2] && i != max) {
                max2 = i;
            }
        }
        int max3 = 0;
        if (max == max3 || max2 == max3) {
            max3++;
        }
        if (max == max3 || max2 == max3) {
            max3++;
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > nums[max3] && i != max && i != max2) {
                max3 = i;
            }
        }
        int min1 = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < nums[min1]) {
                min1 = i;
            }
        }
        int min2 = min1 == 0 ? 1 : 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < nums[min2] && i != min1) {
                min2 = i;
            }
        }
        return Math.max(nums[max] * nums[max2] * nums[max3], nums[max] * nums[min1] * nums[min2]);
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-1, -2, -3, -4};
        System.out.println(maximumProduct(nums));
    }
}
