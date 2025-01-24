/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tmn.algorithm;

import java.util.Arrays;

/**
 *
 * @author HP
 */
public class BinarySearch {

    static int binarySearch(int[] nums, int left, int right, int x) {
        
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            // Check if x is present at mid
            if (nums[m] == x) {
                return m;
            }

            // If x greater, ignore left half
            if (nums[m] < x) {
                l = m + 1;
            } // If x is smaller, ignore right half
            else {
                r = m - 1;
            }
        }

        // If we reach here, then element was
        // not present
        return -1;
    }

    public static void main(String[] args) {

    }
}
