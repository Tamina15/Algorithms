/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmn.algorithm;

import java.util.Random;

/**
 *
 * @author HP
 */
public class QuickSort {

    int width = 20000;
    Random random = new Random();
    int values[] = new int[width];
    long timeStart;
    long timeEnd;
    long time;

    public QuickSort() {
        for (int i = 0; i < width; i++) {
            values[i] = random.nextInt();
        }
        timeStart = System.currentTimeMillis();
        quickSort(values, 0, values.length - 1);
        timeEnd = System.currentTimeMillis();
        time = timeEnd - timeStart;
//        for (int i = 0; i < width; i++) {
//            System.out.println(values[i]);
//        }
        System.out.println(timeStart);
        System.out.println(timeEnd);
        System.out.println(time);
    }

    public QuickSort(int[] value) {
        timeStart = System.currentTimeMillis();
        quickSort(value, 0, value.length - 1);
        timeEnd = System.currentTimeMillis();
        time = timeEnd - timeStart;
        System.out.println("Start time: " + timeStart + "; End time: " + timeEnd + "; Total: " + time);
    }

    public void quickSort(int values[], int start, int end) {
        if (start < end) {
            int index = partition(values, start, end);
            quickSort(values, start, index - 1);
            quickSort(values, index + 1, end);
        }
    }

    private int partition(int[] values, int start, int end) {
        int pivotIndex = start;
        int pivotValue = values[end];
        for (int i = start; i < end; i++) {
            if (values[i] < pivotValue) {
                swap(values, i, pivotIndex);
                pivotIndex++;
            }
        }
        swap(values, pivotIndex, end);
        return pivotIndex;
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
            new QuickSort(value2);
//            new SelectionSort(value1);
            System.out.println("");
        }
    }
}
