package com.tmn.leetcode;

import java.util.Arrays;

public class SpiralMatrix4 {

    public static int[][] spiralMatrix(int m, int n, ListNode head) {
        int height = m;
        int width = n;
        int[][] matrix = new int[height][width];
        int length = height * width;
        ListNode current = head;
        int t = 0, b = height - 1, l = 0, r = width - 1; // bounds
        int x = 0, y = 0, dirX = 1, dirY = 0;
        for (int i = 0; i < length; i++) {
            if (current != null) {
                matrix[y][x] = current.val;
                current = current.next;
            } else {
                matrix[y][x] = -1;
            }
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
        int height, width;
        height = 3;
        width = 5;
        ListNode list = ListNode.randomize(13, 1, 20);
        System.out.println(list.toString());
        int[][] a = spiralMatrix(height, width, list);
        for (int[] b : a) {
            System.out.println(Arrays.toString(b));
        }
    }
}
