package com.tmn.leetcode;

import java.text.DecimalFormat;
import java.util.Arrays;

public class DiagonalTraverse {

    public static int[] findDiagonalOrder(int[][] mat) {
        int height = mat.length;
        int width = mat[0].length;
        int[] result = new int[height * width];
        int dirX = 1, dirY = -1;
        int x = 0, y = 0, i = 0;
// dvd bounce
//        while (i<result.length) {
//            if (y + dirY < 0 || y + dirY >= height) {
//                dirY *= -1;
//            }
//            if (x + dirX < 0 || x + dirX >= width) {
//                dirX *= -1;
//            }
//            result[i] = mat[y][x];
//            i++;
//            x += dirX;
//            y += dirY;
//        }
        while (i < result.length) {
            result[i] = mat[y][x];
            i++;
            // if cannot go up, then go right and left
            if (y == 0 && dirY == -1) {
                // if cannot go right, then go down
                if (x == width - 1) {
                    y++;
                } else {
                    x++;
                }
                dirX = -1;
                dirY = 1;
                continue;
            }
            // if cannot go left, then go down and right
            if (x == 0 && dirX == -1) {
                // if cannot go down, then go right
                if (y != height - 1) {
                    y++;
                } else {
                    x++;
                }
                dirX = 1;
                dirY = -1;
                continue;
            }
            // if cannot go down, then go right and up
            if (y == height - 1 && dirY == 1) {
                x++;
                dirX = 1;
                dirY = -1;
                continue;
            }
            // if cannot go right, then go down and left
            if (x == width - 1 && dirX == 1) {
                y++;
                dirX = -1;
                dirY = 1;
                continue;
            }
            x += dirX;
            y += dirY;
        }
        return result;
    }

    public static void main(String[] args) {
        int height, width;
        int[][] a;
        height = 3;
        width = 3;
        a = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                a[i][j] = i * width + j + 1;
                System.out.print(a[i][j]);
            }
            System.out.println("");
        }
        System.out.println(Arrays.toString(findDiagonalOrder(a)));
    }
}
