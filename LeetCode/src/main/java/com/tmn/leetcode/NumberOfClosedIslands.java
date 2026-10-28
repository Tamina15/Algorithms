package com.tmn.leetcode;

/**
 * Given a 2D grid consists of 0s (land) and 1s (water).
 * An island is a maximal 4-directionally connected group of 0s
 * and a closed island is an island totally (all left, top, right, bottom)
 * surrounded by 1s.
 *
 * Return the number of closed islands.
 *
 * @author NhatTranMinh
 */
public class NumberOfClosedIslands {

    public static int closedIsland(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] stack = new int[m * n][2];
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    if (check(grid, i, j, stack)) {
                        count++;
                    }
                }
            }

        }
        return count;
    }

    public static boolean check(int[][] grid, int x, int y, int[][] stack) {
        int m = grid.length, n = grid[0].length;
        int a = 0;
        stack[a][0] = x;
        stack[a][1] = y;
        boolean closed = true;
        while (a >= 0) {
            int ax = stack[a][0];
            int ay = stack[a][1];
            a--;
            if (grid[ax][ay] == 1) {
                continue;
            }
            grid[ax][ay] = 1;
            if (ax == 0 || ax == m - 1 || ay == 0 || ay == n - 1) {
                closed = false;
            }
            if (ax > 0 && grid[ax - 1][ay] == 0) {
                a++;
                stack[a][0] = ax - 1;
                stack[a][1] = ay;
            }
            if (ax < m - 1 && grid[ax + 1][ay] == 0) {
                a++;
                stack[a][0] = ax + 1;
                stack[a][1] = ay;
            }
            if (ay > 0 && grid[ax][ay - 1] == 0) {
                a++;
                stack[a][0] = ax;
                stack[a][1] = ay - 1;
            }
            if (ay < n - 1 && grid[ax][ay + 1] == 0) {
                a++;
                stack[a][0] = ax;
                stack[a][1] = ay + 1;
            }
        }
        return closed;
    }

    public static void main(String[] args) {
        // int[][] grid = {{1, 1, 1, 1, 1, 1, 1, 0}, {1, 0, 0, 0, 0, 1, 1, 0}, {1, 0, 1,
        // 0, 1, 1, 1, 0}, {1, 0, 0, 0, 0, 1, 0, 1}, {1, 1, 1, 1, 1, 1, 1, 0}};
        // int[][] grid = {{0, 0, 1, 0, 0}, {0, 1, 0, 1, 0}, {0, 1, 1, 1, 0}};
        int[][] grid = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1}};
        System.out.println(closedIsland(grid));
    }
}
