package com.tmn.leetcode;

/* You are given an m x n matrix board containing letters 'X' and 'O', capture regions that are surrounded:

Connect: A cell is connected to adjacent cells horizontally or vertically.
Region: To form a region connect every 'O' cell.
Surround: A region is surrounded if none of the 'O' cells in that region are on the edge of the board. Such regions are completely enclosed by 'X' cells.
To capture a surrounded region, replace all 'O's with 'X's in-place within the original board. You do not need to return anything.
 */
public class SurroundedRegions {

    public static void solve(char[][] board) {
        int m = board.length;
        int n = board[0].length;
        int[][] stack = new int[m * n][2];
        for (int i = 0; i < n; i++) {
            if (board[0][i] == 'O') {
                check(board, 0, i, stack);
            }
            if (board[m - 1][i] == 'O') {
                check(board, m - 1, i, stack);
            }
        }
        for (int i = 0; i < m; i++) {
            if (board[i][0] == 'O') {
                check(board, i, 0, stack);
            }
            if (board[i][n - 1] == 'O') {
                check(board, i, n - 1, stack);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 'Z') {
                    board[i][j] = 'X';
                } else {
                    board[i][j] = 'O';
                }
            }
        }
    }

    public static void check(char[][] board, int x, int y, int[][] stack) {
        int m = board.length, n = board[0].length;
        int a = 0;
        stack[a][0] = x;
        stack[a][1] = y;
        while (a >= 0) {
            int ax = stack[a][0];
            int ay = stack[a][1];
            a--;
            if (board[ax][ay] == 'X') {
                continue;
            }
            board[ax][ay] = 'Z';
            if (ax > 0 && board[ax - 1][ay] == 'O') {
                a++;
                stack[a][0] = ax - 1;
                stack[a][1] = ay;
            }
            if (ax < m - 1 && board[ax + 1][ay] == 'O') {
                a++;
                stack[a][0] = ax + 1;
                stack[a][1] = ay;
            }
            if (ay > 0 && board[ax][ay - 1] == 'O') {
                a++;
                stack[a][0] = ax;
                stack[a][1] = ay - 1;
            }
            if (ay < n - 1 && board[ax][ay + 1] == 'O') {
                a++;
                stack[a][0] = ax;
                stack[a][1] = ay + 1;
            }
        }
    }

    public static void main(String[] args) {
//        char[][] board = {{'X','X','X','X'},{'X','O','O','X'},{'X','X','O','X'},{'X','O','X','X'}};
        char[][] board = {{'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}};
        solve(board);
        for (char[] c : board) {
            System.out.println(c);
        }
    }
}
// 11:27:58 AM -> 11:38:45 AM
