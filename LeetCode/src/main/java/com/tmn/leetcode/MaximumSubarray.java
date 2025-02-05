package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MaximumSubarray {

    /**
     * Kadane's algorithm
     * See https://en.wikipedia.org/wiki/Maximum_subarray_problem#Kadane%27s_algorithm"
     *
     * @param nums
     * @return
     */
    public static int maxSubArraySum(int[] nums) {
        // best result from the sum of the sub-array, 
        // which is the first element
        int best_sum = nums[0];

        // sum of the sub-array, which contains only the first element
        int current_sum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            current_sum = Math.max(nums[i], current_sum + nums[i]);
            best_sum = Math.max(current_sum, best_sum);
        }
        return best_sum;
    }

    /**
     *
     * @param nums
     * @return
     * @deprecated Replaced by maxSubArray2
     */
    @Deprecated
    public static int[] maxSubArray(int[] nums) {
        int bestSum = nums[0];
        int currentSum = nums[0];
        int end = 0;
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            if (currentSum > bestSum) {
                bestSum = currentSum;
                end = i;
            }
        }
        int start = end;
        currentSum = 0;
        for (int i = end; i >= 0; i--) {
            currentSum += nums[i];
            if (currentSum == bestSum) {
                start = i;
                break;
            }
        }
        int[] result = Arrays.copyOfRange(nums, start, end + 1);
        return result;
    }

    /**
     * allMaxSubArrays only return shortest max sub-arrays.
     * eg: [-1, 1, -1, 1] will return [[1],[1]] and not [[1],[1],[1,-1,1]]
     *
     * @param nums
     * @return all shortest-length maximum sub-array
     * @deprecated Replaced by allMaxSubArrays2
     */
    @Deprecated
    public static int[][] allMaxSubArrays(int[] nums) {
        int maxSum = nums[0];
        int bestSum = nums[0];
        int currentSum = nums[0];
        int[] end = new int[nums.length];
        int p = 0;
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            if (currentSum >= bestSum) {
                bestSum = currentSum;
                if (maxSum < bestSum) {
                    maxSum = bestSum;
                    p = 0;
                }
                end[p++] = i;
            }
        }
        int[][] result = new int[p][];
        for (int i = 0; i < p; i++) {
            int e = end[i];
            int start = e;
            currentSum = 0;
            for (int j = e; j >= 0; j--) {
                currentSum += nums[j];
                if (currentSum == maxSum) {
                    start = j;
                    break;
                }
            }
            result[i] = Arrays.copyOfRange(nums, start, e + 1);
        }
        return result;
    }

    /**
     * From: https://www.geeksforgeeks.org/print-the-maximum-subarray-sum/
     * Changed to return maximum sub-array
     *
     * @param arr
     * @return the maximum sub-array
     */
    public static int[] maxSubArray2(int[] arr) {
        // final start and end position
        int resStart = 0, resEnd = 0;
        // cache the start posistion;
        int currStart = 0;
        int maxSum = arr[0];
        int maxEnding = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (maxEnding + arr[i] < arr[i]) {
                maxEnding = arr[i];
                currStart = i;
            } else {
                maxEnding += arr[i];
            }
            if (maxEnding > maxSum) {
                maxSum = maxEnding;
                // this will not change resStart if currStart does not change;
                resStart = currStart;
                resEnd = i;
            }
        }
        return Arrays.copyOfRange(arr, resStart, resEnd + 1);
    }

    /**
     * Adaptation from maxSubArray2 to include all value-distinct maximum sub-arrays.
     * Note: [-1, 1, -1, 1] will return [[1],[1,-1,1]] and not [[1],[1],[1,-1,1]]
     *
     * @param nums
     * @return all distinct equally maximum sub-array
     */
    public static int[][] allMaxSubArrays2(int[] nums) {
        int maxSum = nums[0];
        int bestSum = nums[0];
        int currentSum = nums[0];
        ArrayList<Integer[]> pairs = new ArrayList<>();
        int start, end, currentStart = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > currentSum + nums[i]) {
                currentSum = nums[i];
                currentStart = i;
            } else {
                currentSum += nums[i];
            }
            if (currentSum >= bestSum) {
                bestSum = currentSum;
                if (maxSum < bestSum) {
                    maxSum = bestSum;
                    pairs.clear();
                }
                start = currentStart;
                end = i;
                pairs.add(new Integer[]{start, end});
            }
        }
        int[][] result = new int[pairs.size()][];
        for (int i = 0; i < pairs.size(); i++) {
            Integer[] pair = pairs.get(i);
            result[i] = Arrays.copyOfRange(nums, pair[0], pair[1] + 1);
        }
        return result;
    }

    private static int[] newArray() {
        Random r = new Random();
        int[] nums = new int[r.nextInt(1000) + 1];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = r.nextInt(-10000, 10000 + 1);
        }
        return nums;
    }

    public static void main(String[] args) {
        //        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -10, 3, -1, 3, 1};
        //        int[] nums = {8, -19, 5, -4, 20};
//        int[] nums = {-5, -4, -1, -7, -8, -1};
        int[] nums = newArray();

//        System.out.println(maxSubArraySum(nums));
//        System.out.println(Arrays.toString(maxSubArray(nums)));
//        System.out.println(Arrays.toString(maxSubArray2(nums)));
        System.out.println(Arrays.deepToString(allMaxSubArrays(nums)));
        System.out.println(Arrays.deepToString(allMaxSubArrays2(nums)));
    }
}
