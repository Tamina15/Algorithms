package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {

    public static List<List<Integer>> threeSum(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < n;) {
            int left = i + 1, right = n - 1, target = nums[i];
            // Two Sum
            while (left < right) {
                int sum = nums[left] + nums[right] + target;
                if (sum == 0) {
                    list.add(List.of(nums[i], nums[left], nums[right]));
                    left++;
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    continue;
                }
                if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
            i++;
            while ((i < n) && nums[i] == nums[i - 1]) {
                i++;
            }
        }
        return list;
    }

    public static void main(String[] args) {
//        int[] nums = {-1, 0, 1, 2, -1, -4};
        int[] nums = {0, 0, 0, 0, 0, 0, 0, 0};
        System.out.println(threeSum(nums));
    }
}
