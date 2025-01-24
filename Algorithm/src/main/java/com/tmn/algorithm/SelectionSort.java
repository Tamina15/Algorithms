/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author HP
 */
public class SelectionSort {

    int n = 20000;
    Random random = new Random();
    int values[] = new int[n];
    long timeStart;
    long timeEnd;
    long time;

    public SelectionSort() {
        for (int i = 0; i < n; i++) {
            values[i] = random.nextInt();
        }
        timeStart = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (values[i] > values[j]) {
                    swap(values, i, j);
                }
            }
        }
        timeEnd = System.currentTimeMillis();
//        for (int i = 0; i < n; i++) {
//            System.out.println(values[i]);
//        }
        time = timeEnd - timeStart;
        System.out.println(timeStart);
        System.out.println(timeEnd);
        System.out.println(time);
    }

    public SelectionSort(int[] value) {
        timeStart = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (value[i] > value[j]) {
                    swap(value, i, j);
                }
            }
        }
        timeEnd = System.currentTimeMillis();
        time = timeEnd - timeStart;
        System.out.println("Start time: " + timeStart + "; End time: " + timeEnd + "; Total: " + time);
    }

    public void swap(int values[], int a, int b) {
        int temp = values[a];
        values[a] = values[b];
        values[b] = temp;
    }

    public static void main(String[] agrs) {
        int num = 20000;
        int[] value1 = new int[num], value2 = new int[num];
        Random r = new Random();
        for (int t = 0; t < 10; t++) {
            System.out.println(t);
            for (int i = 0; i < num; i++) {
                int j = r.nextInt();
                value1[i] = j;
                value2[i] = j;
            }
            new SelectionSort(value1);
            new QuickSort(value2);
            System.out.println("");
        }
    }
}
