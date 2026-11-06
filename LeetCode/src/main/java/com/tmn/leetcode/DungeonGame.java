package com.tmn.leetcode;

public class DungeonGame {

    public static int calculateMinimumHP(int[][] dungeon) {
        int m = dungeon.length;
        int n = dungeon[0].length;

        boolean[][] written = new boolean[m][n];
        int[][] values = new int[m][n];
        values[m - 1][n - 1] = dungeon[m - 1][n - 1] > 0 ? 0 : dungeon[m - 1][n - 1];

        for (int sum = m + n - 2; sum >= 0; sum--) {
            int rStart = Math.min(m - 1, sum);
            int rEnd = Math.max(0, sum - (n - 1));
            for (int row = rStart; row >= rEnd; row--) {
                int col = sum - row;
                int value = values[row][col];
                // up will be new, left will be update
                if (row > 0) {
                    int up = row - 1;
                    written[up][col] = true;
                    int v = value + dungeon[up][col];
                    values[up][col] = v > 0 ? 0 : v;
                }
                if (col > 0) {
                    int left = col - 1;
                    // for last row
                    if (!written[row][left]) {
                        written[row][left] = true;
                        int v = value + dungeon[row][left];
                        values[row][left] = v > 0 ? 0 : v;
                    } else {
                        int v = value + dungeon[row][left];
                        if (v > values[row][left]) {
                            values[row][left] = v > 0 ? 0 : v;
                        }
                    }
                }
            }
        }
        return Math.abs(values[0][0]) + 1;
    }

    public static void main(String[] args) {
        int[][] dungeon = {
            {-2, -3, 3},
            {-5, -10, 1},
            {10, 30, -5}};
//        int[][] dungeon = {
//            {1, -3, 3},
//            {0, -2, 0},
//            {-3, -3, -3}};
//        int[][] dungeon = {{0}};
//        int[][] dungeon = {{1, -2, 1}};
        System.out.println(calculateMinimumHP(dungeon));
    }
}
// 2:38:40 PM -> 10:03:00 AM
