package com.tmn.leetcode;

/**
 * You are given an integer array nums of length n,
 * where nums is a permutation of the numbers in the range [0..n - 1].
 *
 * You may swap elements at indices i and j only if nums[i] AND nums[j] == k,
 * where AND denotes the bitwise AND operation and k is a non-negative integer.
 *
 * Return the maximum value of k such that the array can be sorted
 * in non-decreasing order using any number of such swaps.
 * If nums is already sorted, return 0.
 *
 * @author NhatTranMinh
 */
public class MaximumKToSortAPermutation {

    /**
     * Hint 1: Take the bitwise AND of all elements
     * that are not in their correct position.
     *
     * Reason:
     * a & b & c = (a & b) & c
     *
     * As all elements that is not in their correct position
     * will be in the AND calculation at least one,
     * we can accumulate all AND operations into one.
     *
     * @param nums
     * @return k
     */
    public static int sortPermutation(int[] nums) {
        int k = -1;
        for (int i = 0; i < nums.length; i++) {
            if (k == 0) {
                break;
            }
            if (i != nums[i]) {
                k = k & nums[i];
            }
        }
        return k == -1 ? 0 : k;
    }

    public static void main(String[] args) {
        sortPermutation(new int[]{});
    }
}
