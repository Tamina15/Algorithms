package com.tmn.leetcode;

import java.util.Arrays;

public class MaximumNumberOfPairsInArray {

    public static int[] numberOfPairs(int[] nums) {
        int total = 0, leftover = 0;
        if (nums.length == 1) {
            return new int[]{total, leftover + 1};
        }
        Arrays.sort(nums);
        int i = 0;
        for (; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                total++;
                i++;
            } else {
                leftover++;
            }
        }
        if (i < nums.length) {
            leftover++;
        }
        return new int[]{total, leftover};
    }

    public static void main(String[] args) {
        int[] nums = new int[]{0, 1, 2, 3, 4, 5, 5};
        System.out.println(Arrays.toString(numberOfPairs(nums)));
    }
}
