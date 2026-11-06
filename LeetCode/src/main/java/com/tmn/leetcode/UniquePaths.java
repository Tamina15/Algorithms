package com.tmn.leetcode;

import java.util.Arrays;

/*
There is a robot on an m x n grid. 
The robot is initially located at the top-left corner (i.e., grid[0][0]). 
The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). 
The robot can only move either down or right at any point in time.

Given the two integers m and n, return the number of possible 
unique paths that the robot can take to reach the bottom-right corner.

 */
public class UniquePaths {

    public static int uniquePaths(int m, int n) {
        int[][] paths = new int[m][n];
        for (int i = 0; i < m; i++) {
            paths[i][0] = 1;
        }
        for (int i = 0; i < n; i++) {
            paths[0][i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                paths[i][j] = paths[i - 1][j] + paths[i][j - 1];
            }
        }
        return paths[m - 1][n - 1];
    }

    // Use 2 rows to keep track of current and previous row only
    // And switch between current and previous.
    public static int uniquePaths2(int m, int n) {
        int[][] paths = new int[2][n];
        int previous = 0;
        int now = 1;
        for (int i = 0; i < n; i++) {
            paths[0][i] = 1;
        }
        paths[1][0] = 1;
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                paths[now][j] = paths[previous][j] + paths[now][j - 1];
            }
            if (previous == 0) {
                previous = 1;
                now = 0;
            } else {
                previous = 0;
                now = 1;
            }
        }
        return (int) paths[previous][n - 1];
    }

    // Use one row
    public static int uniquePaths3(int m, int n) {
        int[] paths = new int[n];
        for (int i = 0; i < n; i++) {
            paths[i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                paths[j] = paths[j] + paths[j - 1];
            }
        }
        return paths[n - 1];
    }

    static void print(int[][] m) {
        for (int[] i : m) {
            System.out.println(Arrays.toString(i));
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        for (int i = 1; i < 50; i++) {
            for (int j = 1; j < 50; j++) {
                System.out.print(uniquePaths3(i, j) + " ");
            }
            System.out.println("");
        }
    }
}
// 10:19:58 AM -> 10:41:47 AM
