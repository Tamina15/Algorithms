package com.tmn.algorithm;

public class TowerOfHanoi {

    public static void towerOfHanoi(int n, int start, int end) {
        if (n <= 1) {
            print(start, end);
        } else {
            int other = 6 - (start + end);
            towerOfHanoi(n - 1, start, other);
            print(start, end);
            towerOfHanoi(n - 1, other, end);
        }
    }

    public static void print(int start, int end) {
        System.out.println("Move from rod " + start + " to rod " + end);
    }

    public static void main(String[] args) {
        towerOfHanoi(5, 1, 3);
    }
}
