package com.tmn.leetcode;

public class GridGame {

    public static long gridGame(int[][] grid) {
        int n = grid[0].length;
        long topRowSum = 0;
        for (int i = 0; i < n; i++) {
            topRowSum += grid[0][i];
        }
        long maxValue = Long.MAX_VALUE;
        long bottomRowSum = 0;
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                bottomRowSum += grid[1][i - 1];
            }
            if (i < n) {
                topRowSum -= grid[0][i];
            }
            // max value second robot can get now
            long max = Math.max(topRowSum, bottomRowSum);
            // actual value second robot can get, because we (the first robot) will get that value
            maxValue = Math.min(maxValue, max);
        }
        return maxValue;
    }

    public static void main(String[] args) {
        int[][] grid = {
            {20, 3, 20, 17, 2, 12, 15, 17, 4, 15},
            {20, 10, 13, 14, 15, 5, 2, 3, 14, 3}};
//        int[][] grid = {{1, 3, 1, 15}, {1, 3, 3, 1}};
//        for (int[] row : grid) {
//            System.out.println(Arrays.toString(row));
//        }
        System.out.println(gridGame(grid));
    }
}
