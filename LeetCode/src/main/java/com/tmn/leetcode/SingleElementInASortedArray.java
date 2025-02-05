package com.tmn.leetcode;

import java.util.Arrays;
import java.util.Random;

public class SingleElementInASortedArray {

    /**
     * O(n)
     */
    public static int singleNonDuplicate0(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        int result = -1;
        for (int i = 0; i < nums.length - 1; i = i + 2) {
            if (nums[i] != nums[i + 1]) {
                result = nums[i];
                break;
            }
        }
        if (nums[nums.length - 1] != nums[nums.length - 2]) {
            result = nums[nums.length - 1];
        }
        return result;
    }

    /**
     * Binary Search
     */
    public static int singleNonDuplicate(int[] nums) {
        int n = nums.length;
        return search(nums, 0, n - 1);
    }

    public static int search(int nums[], int start, int endInclusive) {
        // if has one element
        if (endInclusive - start <= 1) {
            return nums[start];
        }
        int mid = (endInclusive - start) / 2 + start;
        if (nums[mid] == nums[mid - 1]) {
            // if left part has odd number of elements, then the single value will be in that half
            // else it will be in the right part (minus the middle element)
            // (mid - start) == even means thst left part is odd
            if (((mid - start) % 2) == 0) {
                return search(nums, start, mid - 2);
            } else {
                return search(nums, mid + 1, endInclusive);
            }
        }
        if (nums[mid] == nums[mid + 1]) {
            if (((endInclusive - mid) % 2) == 0) {
                return search(nums, mid + 2, endInclusive);
            } else {
                return search(nums, start, mid - 1);
            }
        }
        return nums[mid];
    }

    private static int[] random(int length) {
        Random r = new Random();
        int[] nums = new int[length * 2 - 1];
        int a = r.nextInt(0, length);
        int count = 0;
        for (int i = 0; i < length; i = i + 2) {
            nums[i] = count;
            nums[i + 1] = count;
            if (i == a) {
                i--;
            }
            count++;
        }
        return nums;
    }

    public static void main(String[] args) {
//        int[] nums = {1, 1, 2, 2, 3, 3, 4};
        int[] nums = random(1000);
        System.out.println(Arrays.toString(nums));
        System.out.println(singleNonDuplicate(nums));
    }
}
