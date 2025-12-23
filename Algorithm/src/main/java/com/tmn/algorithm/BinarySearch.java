/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tmn.algorithm;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author HP
 */
public class BinarySearch {

    static int binarySearch(int[] nums, int x) {

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

    static int linearSearch(int[] nums, int x) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == x) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int n = 1000000;
        int[] a = new int[n];
        Arrays.setAll(a, (operand) -> operand);
        long start, end;
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            int b = r.nextInt(n);

            start = System.nanoTime();
            binarySearch(a, b);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);

            start = System.nanoTime();
            linearSearch(a, b);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);

            start = System.nanoTime();
            Arrays.binarySearch(a, b);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);
            
            System.out.println("");
        }
    }
}
