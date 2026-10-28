package com.tmn.leetcode;

/**
 * You are given an m x n binary matrix grid.
 * An island is a group of 1's (representing land)
 * connected 4-directionally (horizontal or vertical.)
 * You may assume all four edges of the grid are surrounded by water.
 *
 * The area of an island is the number of cells with a value 1 in the island.
 *
 * Return the maximum area of an island in grid.
 * If there is no island, return 0.
 *
 * @author NhatTranMinh
 */
public class MaxAreaOfIsland {

    public static int maxAreaOfIsland(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] stack = new int[m * n][2];
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    int count = check(grid, i, j, stack);
                    if (count > max) {
                        max = count;
                    }
                }
            }
        }
        return max;
    }

    public static int check(int[][] grid, int x, int y, int[][] stack) {
        int m = grid.length, n = grid[0].length;
        int a = 0;
        stack[a][0] = x;
        stack[a][1] = y;
        int count = 0;
        while (a >= 0) {
            int ax = stack[a][0];
            int ay = stack[a][1];
            a--;
            if (grid[ax][ay] == 0) {
                continue;
            }
            grid[ax][ay] = 0;
            count++;
            if (ax > 0 && grid[ax - 1][ay] == 1) {
                a++;
                stack[a][0] = ax - 1;
                stack[a][1] = ay;
            }
            if (ax < m - 1 && grid[ax + 1][ay] == 1) {
                a++;
                stack[a][0] = ax + 1;
                stack[a][1] = ay;
            }
            if (ay > 0 && grid[ax][ay - 1] == 1) {
                a++;
                stack[a][0] = ax;
                stack[a][1] = ay - 1;
            }
            if (ay < n - 1 && grid[ax][ay + 1] == 1) {
                a++;
                stack[a][0] = ax;
                stack[a][1] = ay + 1;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        // int[][] grid = {{1, 1, 1, 1, 1, 1, 1, 0}, {1, 0, 0, 0, 0, 1, 1, 0}, {1, 0, 1,
        // 0, 1, 1, 1, 0}, {1, 0, 0, 0, 0, 1, 0, 1}, {1, 1, 1, 1, 1, 1, 1, 0}};
        // int[][] grid = {{0, 0, 1, 0, 0}, {0, 1, 0, 1, 0}, {0, 1, 1, 1, 0}};
        // int[][] grid = {{1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 0, 0, 0, 1}, {1, 0, 1, 1, 1,
        // 0, 1}, {1, 0, 1, 0, 1, 0, 1}, {1, 0, 1, 1, 1, 0, 1}, {1, 0, 0, 0, 0, 0, 1},
        // {1, 1, 1, 1, 1, 1, 1}};
        int[][] grid = {{0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
        {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}};
        // int[][] grid = { { 0, 0, 0, 0, 0, 0, 0, 0 } };
        System.out.println(maxAreaOfIsland(grid));
    }
}
