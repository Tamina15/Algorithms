package com.tmn.leetcode;

import java.util.Arrays;

public class SpiralMatrix2 {

    public static int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        int length = n * n;
        int t = 0, b = n - 1, l = 0, r = n - 1; // bounds
        int x = 0, y = 0, dirX = 1, dirY = 0;
        for (int i = 1; i <= length; i++) {
            matrix[y][x] = i;
            // if go right, then go down
            if (dirX == 1 && x == r) {
                dirY = 1;
                dirX = 0;
                t++;
            }

            // if go down, then go left
            if (dirY == 1 && y == b) {
                dirY = 0;
                dirX = -1;
                r--;
            }

            // if go left, then go up
            if (dirX == -1 && x == l) {
                dirY = -1;
                dirX = 0;
                b--;
            }

            // if go up, then go right
            if (dirY == -1 && y == t) {
                dirY = 0;
                dirX = 1;
                l++;
            }
            x += dirX;
            y += dirY;
        }
        return matrix;
    }

    public static void main(String[] args) {
        int[][] a = generateMatrix(3);
        for (int[] b : a) {
            System.out.println(Arrays.toString(b));
        }
    }
}
