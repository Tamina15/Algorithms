package com.tmn.leetcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SingleNumberIII {

    public static int[] singleNumber(int[] nums) {
        if (nums.length <= 3) {
            return nums;
        }
        int[] result = new int[2];
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) {
                set.remove(nums[i]);
            } else {
                set.add(nums[i]);
            }
        }
        int index = 0;
        for (Integer value : set) {
            result[index++] = value;
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 1, 7, 6, 2};
        System.out.println(Arrays.toString(singleNumber(nums)));
    }
}
