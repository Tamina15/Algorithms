package com.tmn.leetcode;

import java.util.Arrays;

public class CountServersThatCommunicate {

    /**
     * Return the number of servers that communicate with any other server.
     * Two servers are said to communicate if they are on the same row or on the same column.
     * <p/>
     * Count all servers in each rows and each columns.
     * If a server is the only one in a row and a column,
     * then that server can't communicate with any other servers
     *
     * @param grid
     * @return the number of servers that can communicate with any other server.
     */
    public static int countServers(int[][] grid) {
        int result = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    rows[i]++;
                    cols[j]++;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1 && (rows[i] > 1 || cols[j] > 1)) {
                    result++;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[][] grid = {{1, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
        for (int[] i : grid) {
            System.out.println(Arrays.toString(i));
        }
        System.out.println(countServers(grid));
    }
}
