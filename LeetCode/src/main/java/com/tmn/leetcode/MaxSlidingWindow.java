package com.tmn.leetcode;

import java.util.Arrays;

public class MaxSlidingWindow {

    public static int[] maxSlidingWindow(int[] nums, int k) {
        int[] win = new int[nums.length - k + 1];
        int max = findMax(nums, 0, k - 1);
        int count = 0;
        win[count++] = nums[max];
        for (int i = k; i < nums.length; i++) {
            if (i - max >= k) {
                max = findMax(nums, max + 1, i);
            } else {
                if (nums[i] >= nums[max]) {
                    max = i;
                }
            }
            win[count++] = nums[max];
        }

        return win;
    }

    public static int findMax(int[] nums, int s, int e) {
        System.out.println("find");
        int max = s;
        for (int i = s; i <= e; i++) {
            if (nums[i] >= nums[max]) {
                max = i;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        System.out.println(Arrays.toString(maxSlidingWindow(nums, 3)));
    }
}
