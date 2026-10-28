package com.tmn.leetcode;

import java.util.Arrays;
import java.util.List;

/**
 * Given a triangle array, return the minimum path sum from top to bottom.
 *
 * For each step, you may move to an adjacent number of the row below.
 * More formally, if you are on index i on the current row,
 * you may move to either index i or index i + 1 on the next row.
 *
 * @author NhatTranMinh
 */
public class MinimumPathSumTriangle {

    public static int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        for (int i = 1; i < n; i++) {
            var lastRow = triangle.get(i - 1);
            var row = triangle.get(i);
            int rowLength = row.size();
            for (int j = 0; j < rowLength; j++) {
                int leftSum = j == 0 ? Integer.MAX_VALUE : lastRow.get(j - 1);
                int rightSum = j == rowLength - 1 ? Integer.MAX_VALUE : lastRow.get(j);
                row.set(j, Math.min(leftSum, rightSum) + row.get(j));
            }
        }
        var lastSum = triangle.get(n - 1);
        int min = lastSum.getFirst();
        for (int i = 1; i < lastSum.size(); i++) {
            if (lastSum.get(i) < min) {
                min = lastSum.get(i);
            }
        }
        return min;
    }

    public static void main(String[] args) {
        List<List<Integer>> triangle = Arrays.asList(
                Arrays.asList(2),
                Arrays.asList(3, 4),
                Arrays.asList(6, 5, 7),
                Arrays.asList(4, 1, 8, 3));
        System.out.println(minimumTotal(triangle));
        System.out.println(triangle);
    }

}
