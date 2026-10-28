package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * You are given a 0-indexed integer array nums and a target element target.
 *
 * A target index is an index i such that nums[i] == target.
 *
 * Return a list of the target indices of nums after sorting nums in non-decreasing order.
 * If there are no target indices, return an empty list.
 * The returned list must be sorted in increasing order.
 *
 * @author NhatTranMinh
 */
public class FindTargetIndicesAfterSortingArray {

    public static List<Integer> targetIndices(int[] nums, int target) {
        int countIndex = 0;
        int countValue = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < target) {
                countIndex++;
            }
            if (nums[i] == target) {
                countValue++;
            }
        }
        List<Integer> result = new ArrayList<>(countValue);
        while (countValue > 0) {
            countValue--;
            result.add(countIndex);
            countIndex++;
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 5, 2, 3};
        System.out.println(targetIndices(nums, 5));
    }
}
