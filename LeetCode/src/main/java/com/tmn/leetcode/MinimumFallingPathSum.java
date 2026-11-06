package com.tmn.leetcode;

import java.util.Arrays;

/*
Given an n x n array of integers matrix, 
return the minimum sum of any falling path through matrix.

A falling path starts at any element in the first row 
and chooses the element in the next row that is either 
directly below or diagonally left/right. 

Specifically, the next element from position (row, col) 
will be (row + 1, col - 1), (row + 1, col), or (row + 1, col + 1).
 */
public class MinimumFallingPathSum {

    public static int minFallingPathSum(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        for (int[] i : matrix) {
            System.out.println(Arrays.toString(i));
        }
        System.out.println("");
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int min = Integer.MAX_VALUE;
                for (int k = j - 1; k <= j + 1; k++) {
                    if (k >= 0 && k < n) {
                        if (matrix[i - 1][k] < min) {
                            min = matrix[i - 1][k];
                        }
                    }
                }
                matrix[i][j] = min + matrix[i][j];
            }
        }
        for (int[] i : matrix) {
            System.out.println(Arrays.toString(i));
        }
        int last = m - 1;
        int min = matrix[last][0];
        for (int i = 1; i < n; i++) {
            if (matrix[last][i] < min) {
                min = matrix[last][i];
            }
        }
        return min;
    }

    public static void main(String[] args) {
//        int[][] matrix = {{2, 1, 3}, {6, 5, 4}, {7, 8, 9}};
//        int[][] matrix = {{2, 1, 3}};
//        int[][] matrix = {{2, 1, 3}, {2, 1, 3}};
//        int[][] matrix = {{2}};
//        int[][] matrix = {{-2, 1}};
        int[][] matrix = {{100, -42, -46, -41}, {31, 97, 10, -10}, {-58, -51, 82, 89}, {51, 81, 69, -51}};
        System.out.println(minFallingPathSum(matrix));
    }
}
