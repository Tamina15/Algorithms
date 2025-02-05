package com.tmn.leetcode;

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

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 1};
        System.out.println(findDuplicate(nums));
    }
}
