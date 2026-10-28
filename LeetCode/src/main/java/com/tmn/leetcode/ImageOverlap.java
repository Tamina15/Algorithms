package com.tmn.leetcode;

/**
 * You are given two images, img1 and img2, represented as binary, square
 * matrices of size n x n. A binary matrix has only 0s and 1s as values.
 *
 * We translate one image however we choose by sliding all the 1 bits left,
 * right, up, and/or down any number of units. We then place it on top of the
 * other image. We can then calculate the overlap by counting the number of
 * positions that have a 1 in both images.
 *
 * Note also that a translation does not include any kind of rotation. Any 1
 * bits that are translated outside of the matrix borders are erased.
 *
 * Return the largest possible overlap.
 *
 * @author NhatTranMinh
 */
public class ImageOverlap {

    public int largestOverlap(int[][] img1, int[][] img2) {
        int n = img1.length;
        if (n == 1) {
            if (img1[0][0] == 1 && img2[0][0] == 1) {
                return 1;
            }
            return 0;
        }
        int max = 0;
        for (int offsetX = -n; offsetX < n; offsetX++) {
            for (int offsetY = -n; offsetY < n; offsetY++) {
                int count = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        int ax = i + offsetX;
                        int ay = j + offsetY;
                        if (ax >= 0 && ax < n && ay >= 0 && ay < n) {
                            if (img1[i][j] == 1 && img2[ax][ay] == 1) {
                                count++;
                            }
                        }
                    }
                }
                if (count >= max) {
                    max = count;
                }
            }
        }
        return max;
    }
}
