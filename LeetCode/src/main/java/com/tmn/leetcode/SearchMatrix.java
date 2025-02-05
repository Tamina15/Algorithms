package com.tmn.leetcode;

import java.util.Arrays;

public class SearchMatrix {

    public static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int i = 0, j = n - 1;
        while (i < m && j >= 0) {
            if (target == matrix[i][j]) {
                return true;
            }
            if (target < matrix[i][j]) {
                j--;
                continue;
            }
            if (target > matrix[i][j]) {
                i++;
            }
        }
        return false;
    }

    public static void main(String[] args) {
//        int[][] matrix = {{1, 4, 7, 11, 15}, {2, 5, 8, 12, 19}, {3, 6, 9, 16, 22}, {10, 13, 14, 17, 24}, {18, 21, 23, 26, 30}};
        int[][] matrix = {{1, 3, 5, 7, 9}, {2, 4, 6, 8, 10}, {11, 13, 15, 17, 19}, {12, 14, 16, 18, 20}, {21, 22, 23, 24, 25}};
//        int[][] matrix = {{1, 1}};
        for (int[] m : matrix) {
            System.out.println(Arrays.toString(m));
        }
        System.out.println(searchMatrix(matrix, 13));
    }
}
