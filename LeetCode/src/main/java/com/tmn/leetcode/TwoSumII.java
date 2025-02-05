package com.tmn.leetcode;

import java.util.Arrays;

public class TwoSumII {

    public static int[] twoSum(int[] numbers, int target) {
        int n = numbers.length;
        int left = 0, right = n - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                break;
            }
            if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{left + 1, right + 1};
    }

    public static void main(String[] args) {
        int[] numbers = {-1,0};
        System.out.println(Arrays.toString(twoSum(numbers, -1)));
    }
}
