package com.tmn.leetcode;

import java.util.Random;

public class NumberOfWaysToSplitArray {

    public static int waysToSplitArray(int[] nums) {
        int result = 0;
        long left = nums[0];
        long right = 0;
        for (int i = 1; i < nums.length; i++) {
            right += nums[i];
        }
        for (int i = 1; i < nums.length; i++) {
            if (left >= right) {
                result++;
            }
            left += nums[i];
            right -= nums[i];
        }

        return result;
    }

    private static int[] random(int length) {
        int[] nums = new int[length];
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            nums[i] = r.nextInt(0, 1500);
        }
        return nums;
    }

    public static void main(String[] args) {
//        int[] nums = {10, 4, -8, 7};
        int[] nums = random(1000);
        System.out.println(waysToSplitArray(nums));
    }
}
