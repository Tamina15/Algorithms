package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpiralMatrix {

    public static List<Integer> spiralOrder(int[][] matrix) {
        int height = matrix.length;
        int width = matrix[0].length;
        int n = height * width;
        List<Integer> result = new ArrayList<>(n);
        int t = 0, b = height - 1, l = 0, r = width - 1; // bounds
        int x = 0, y = 0, dirX = 1, dirY = 0;
        for (int i = 0; i < n; i++) {
            result.add(matrix[y][x]);
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
        return result;
    }

    public static void main(String[] args) {
        int height, width;
        int[][] a;
        height = 5;
        width = 6;
        a = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                a[i][j] = i * width + j + 1;
                System.out.print(String.format("%3s", a[i][j]));
            }
            System.out.println("");
        }
        System.out.println(Arrays.toString(spiralOrder(a).toArray()));
    }
}
