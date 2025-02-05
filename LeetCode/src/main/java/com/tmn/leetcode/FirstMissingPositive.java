package com.tmn.leetcode;

import java.util.Arrays;

public class FirstMissingPositive {

    /**
     * Find the smaller non-zero positive number in an array.
     * <p>
     * Idea: The smallest positive number must be in range 1 -> (array's length + 1). Ex:
     * <p>
     * - For any array not containing 1, the result will be 1
     * <p>
     * - For any array that is full ([1, 2, 3, 4, ..., n]), the result will be n + 1
     * <p>
     * - For any array that is ([a, a, a, a, ..., n, n, n, ..., k, k, k]),
     * where n &lt;= 0, k > (n + 1) and 0 &lt; a &lt; (n + 1),
     * the result will be in range 1 -> array's length
     * <p>
     * Operations: Move each value to the corresponded (index - 1). Eg: [1, 2, 3, 4, 5, ..., n].
     * Any value that is missing will be represented as -1.
     * Any value that is &lt;=0 or > array's length will be excluded.
     * Iterate through the array and return first value that is &lt;= 0,
     * or array's length + 1 if not found any.
     * <p>
     * Conditions:
     * <p>
     * 
     * <p>
     *
     *
     * @param nums
     * @return
     */
    public static int firstMissingPositive(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int value = nums[i];
            if (value > nums.length || value <= 0) {
                nums[i] = -1;
                continue;
            }
            if (value == i + 1) {
                continue;
            }
            if (value == nums[value - 1]) {
                nums[i] = -1;
            } else {
                nums[i] = nums[value - 1];
                nums[value - 1] = value;
                i--;
            }
        }
        System.out.println(Arrays.toString(nums));
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= 0) {
                return i + 1;
            }
        }
        return nums.length + 1;
    }

    public static void main(String[] args) {
        int[] nums = {-1, -2, 3, 4, 1, 5, 6, 7, 8, 999, 99, 98, -1};
        System.out.println(firstMissingPositive(nums));
    }
}
