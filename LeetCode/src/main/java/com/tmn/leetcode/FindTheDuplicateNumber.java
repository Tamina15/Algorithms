package com.tmn.leetcode;

import java.util.Arrays;

public class FindTheDuplicateNumber {

    public static int findDuplicate(int[] nums) {
        // cycle sort and pigeonhole principle
        for (int i = 0; i <= nums.length; i++) {
            int v = nums[0];
            nums[0] = nums[v];
            nums[v] = v;
        }
        return nums[0];
    }

    public static int[] findErrorNums(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int a = nums[i];
            if (a != i + 1 && a != nums[a - 1]) {
                nums[i] = nums[a - 1];
                nums[a - 1] = a;
                i--;
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                return new int[]{nums[i], i + 1};
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int[] nums = {5, 4, 2, 6, 6, 1, 8, 7};
        System.out.println(Arrays.toString(findErrorNums(nums)));
    }
}
