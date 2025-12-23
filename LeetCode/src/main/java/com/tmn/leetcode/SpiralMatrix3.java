package com.tmn.leetcode;

import java.util.Arrays;

public class SpiralMatrix3 {

    /**
     * Do Ulam spiral from [{@code rStart}, {@code cStart}],
     * add to the output array the coordination of current position
     * if it's inbound of {@code rows} and {@code col},
     * until pass through all coordination.
     * <br>
     * The spiral direction is right -> down -> left -> up
     *
     * @param rows height of the array
     * @param cols width of the array
     * @param rStart start position of row
     * @param cStart start position of col
     * @return the path array
     */
    public static int[][] spiralMatrixIII(int rows, int cols, int rStart, int cStart) {
        int height = rows;
        int width = cols;
        int length = height * width;
        int[][] matrix = new int[length][2];

        // turn bounds
        int t = rStart - 1, b = rStart + 1, l = cStart - 1, r = cStart + 1;
        int x = cStart, y = rStart, dirX = 1, dirY = 0;

        for (int i = 0; i < length;) {
            // check bounds
            if (x >= 0 && x < width && y >= 0 && y < height) {
                matrix[i][0] = y;
                matrix[i][1] = x;
                i++;
            }
            // if cannot goes right, then goes down, and extends right bound
            if (dirX == 1 && x == r) {
                dirY = 1;
                dirX = 0;
                r++;
            }

            // if cannot goes down, then goes left, and extends bottom bound
            if (dirY == 1 && y == b) {
                dirY = 0;
                dirX = -1;
                b++;
            }

            // if cannot goes left, then goes up, and extends left bound
            if (dirX == -1 && x == l) {
                dirY = -1;
                dirX = 0;
                l--;
            }

            // if cannot goes up, then goes right, and extends top bound
            if (dirY == -1 && y == t) {
                dirY = 0;
                dirX = 1;
                t--;
            }

            x += dirX;
            y += dirY;
        }
        return matrix;
    }

    public static void main(String[] args) {
        int[][] a = spiralMatrixIII(5, 6, 1, 4);
        for (int[] b : a) {
            System.out.println(Arrays.toString(b));
        }
    }
}
