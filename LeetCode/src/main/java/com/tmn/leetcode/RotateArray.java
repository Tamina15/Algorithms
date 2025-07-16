package com.tmn.leetcode;

import java.util.Arrays;

public class RotateArray {

    public void rotate(int[] nums, int k) {
        int l = nums.length;
        k = k % l;
        System.out.println(k);
        int[] result = new int[l];
        for (int i = 0; i < k; i++) {
            result[i] = nums[l - k + i];
        }
        System.arraycopy(nums, 0, result, k, l - k);
        System.arraycopy(result, 0, nums, 0, l);
        System.out.println(Arrays.toString(result));
    }

    public static void main(String[] args) {
        RotateArray r = new RotateArray();
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        r.rotate(nums, 3);
    }
}
