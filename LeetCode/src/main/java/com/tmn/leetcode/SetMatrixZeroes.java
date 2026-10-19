package com.tmn.leetcode;

import java.util.Arrays;

/**
 * Given an m x n integer matrix, if an element is 0, set its entire row and column to 0's.
 *
 * @author NhatTranMinh
 */
public class SetMatrixZeroes {

    public static void setZeroes(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean[] rowZero = new boolean[n];
        boolean[] colZero = new boolean[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    colZero[i] = true;
                    rowZero[j] = true;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (colZero[i] || rowZero[j]) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][]{{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        print(matrix);
        setZeroes(matrix);
        print(matrix);
    }

    public static void print(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}
