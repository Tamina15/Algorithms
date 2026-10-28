package com.tmn.leetcode;

import java.util.Arrays;

/**
 * Given an array of positive integers and a positive integer target,
 * return the minimal length of a subarray
 * whose sum is greater than or equal to target.
 * If there is no such subarray, return 0 instead.
 *
 * @author NhatTranMinh
 */
public class MinimumSizeSubarraySum {

    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0, right = 1;
        int minLeft = 0, minRight = nums.length + 1;
        int sum = nums[0];
        while (right < nums.length) {
            if (sum >= target) {
                if (right - left < minRight - minLeft) {
                    minRight = right;
                    minLeft = left;
                }
                sum -= nums[left++];
            } else {
                sum += nums[right++];
            }
        }
        while (left < right) {
            if (sum >= target) {
                if (right - left < minRight - minLeft) {
                    minRight = right;
                    minLeft = left;
                }
            }
            sum -= nums[left++];
        }
        if (minRight == nums.length + 1) {
            return 0;
        }
        return minRight - minLeft;
    }

    public static void main(String[] args) {
        int[] array = {1, 1, 1, 1, 1, 1, 1, 1};
        int target = 11;
        System.out.println(Arrays.toString(array));
        System.out.println(minSubArrayLen(target, array));
    }
}
