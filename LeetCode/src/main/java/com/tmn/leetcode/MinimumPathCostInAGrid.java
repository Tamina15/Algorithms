package com.tmn.leetcode;

/*
You are given a 0-indexed m x n integer matrix grid 
consisting of distinct integers from 0 to m * n - 1. 

You can move in this matrix from a cell to any other cell in the next row. 
That is, if you are in cell (x, y) such that x < m - 1, 
you can move to any of the cells (x + 1, 0), (x + 1, 1), ..., (x + 1, n - 1). 

Note that it is not possible to move from cells in the last row.

Each possible move has a cost given by a 0-indexed 2D array moveCost of size (m * n) x n, 
where moveCost[i][j] is the cost of moving from a cell with value i to a cell in column j of the next row. 
The cost of moving from cells in the last row of grid can be ignored.

The cost of a path in grid is the sum of all values of cells visited 
plus the sum of costs of all the moves made. 

Return the minimum cost of a path that starts from any cell 
in the first row and ends at any cell in the last row.
 */
public class MinimumPathCostInAGrid {

    public static int minPathCost(int[][] grid, int[][] moveCost) {
        int m = grid.length;
        int n = grid[0].length;
        int[] mins = new int[n];
        int[] temp = new int[n];
        System.arraycopy(grid[0], 0, mins, 0, n);
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int min = Integer.MAX_VALUE;
                int value = grid[i][j];
                for (int k = 0; k < n; k++) {
                    int moveIndex = grid[i - 1][k];
                    int cost = moveCost[moveIndex][j];
                    int v = mins[k] + cost + value;
                    if (v < min) {
                        min = v;
                    }
                }
                temp[j] = min;
            }
            int[] t = temp;
            temp = mins;
            mins = t;
        }
        int min = mins[0];
        for (int i = 0; i < mins.length; i++) {
            if (mins[i] < min) {
                min = mins[i];
            }
        }
        return min;
    }

    public static void main(String[] args) {
        // int[][] grid = {{5, 3}, {4, 0}, {2, 1}};
        // int[][] moveCost = {{9, 8}, {1, 5}, {10, 12}, {18, 6}, {2, 4}, {14, 3}};
        int[][] grid = {{5, 1, 2}, {4, 0, 3}};
        int[][] moveCost = {{12, 10, 15}, {20, 23, 8}, {21, 7, 1}, {8, 1, 13}, {9, 10, 25}, {5, 3, 2}};
        System.out.println(minPathCost(grid, moveCost));
    }
}
