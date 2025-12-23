package com.tmn.leetcode;

import java.util.Arrays;

public class RotateArray {

    /**
     * Given an integer array {@code nums}, rotate the array to the right by k steps, where k is non-negative.
     * <br>
     * e.g:
     * <br>
     * input: nums = [1, 2, 3, 4, 5, 6, 7], k=3
     * <br>
     * output: [5, 6, 7, 1, 2, 3, 4]
     *
     * @param nums
     * @param k
     */
    public static void rotate(int[] nums, int k) {
        int l = nums.length;
        k = k % l;
        int a = l - k;
        int[] result = new int[l];
        for (int i = 0; i < k; i++) {
            result[i] = nums[a + i];
        }
        System.arraycopy(nums, 0, result, k, a);
        System.arraycopy(result, 0, nums, 0, l);
    }

    public static void main(String[] args) {
        int n = 30000;
        int k = 1001;
        int[] nums = new int[n];
        Arrays.setAll(nums, (value) -> value);
        for (int i = 0; i < 10; i++) {
            rotate(nums, k);
        }
        long start = System.nanoTime();
        rotate(nums, k);
        long end = System.nanoTime();
        System.out.println((end - start) * 1.0 / 1000000);
        System.out.println(Arrays.toString(nums));
    }
}
