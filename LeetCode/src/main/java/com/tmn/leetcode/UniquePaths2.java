package com.tmn.leetcode;

/*
You are given an m x n integer array grid. 
There is a robot initially located at the top-left corner (i.e., grid[0][0]). 
The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). 
The robot can only move either down or right at any point in time.

An obstacle and space are marked as 1 or 0 respectively in grid. 
A path that the robot takes cannot include any square that is an obstacle.

Return the number of possible unique paths that the robot can take to reach the bottom-right corner.
 */
public class UniquePaths2 {

    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m - 1][n - 1] == 1) {
            return 0;
        }
        int[] paths = new int[n];
        paths[0] = 1;
        for (int j = 1; j < n; j++) {
            if (obstacleGrid[0][j] == 1) {
                paths[j] = 0;
            } else {
                paths[j] = paths[j - 1];
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    paths[j] = 0;
                } else {
                    if (j != 0) {
                        paths[j] = paths[j] + paths[j - 1];
                    }
                }
            }
        }
        return paths[n - 1];
    }

    public static void main(String[] args) {
        // int[][] obstacleGrid = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        // int[][] obstacleGrid = {{0, 1}, {0, 0}};
        int[][] obstacleGrid = {{0, 1, 0, 0}};
        System.out.println(uniquePathsWithObstacles(obstacleGrid));
    }
}
// 10:46:25 AM -> 11:03:45 AM
