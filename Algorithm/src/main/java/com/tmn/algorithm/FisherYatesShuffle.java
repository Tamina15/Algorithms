package com.tmn.algorithm;

import java.util.Arrays;
import java.util.Random;

public class FisherYatesShuffle {

    private static void shuffle(Object[] array) {
        int n = array.length;
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int j = r.nextInt(i + 1);
            Object temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    // ??????????
    private static void SattoloCycle(Object[] array) {
        int n = array.length;
        Random r = new Random();
        for (int i = n - 1; i > 0; i--) {
            int j = r.nextInt(i);
            Object swap = array[j];
            array[j] = array[i];
            array[i] = swap;
        }
    }

    private static void set(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (char) (i + 'A');
        }
    }

    public static void main(String[] args) {
        int n = 4;
        Character[] array = new Character[n];

        set(array);
        System.out.println(Arrays.toString(array));

        for (int i = 0; i < 100; i++) {
            shuffle(array);
            System.out.println(Arrays.toString(array));
        }

        set(array);
        System.out.println("");

        for (int i = 0; i < 100; i++) {
            SattoloCycle(array);
            System.out.println(Arrays.toString(array));
        }
    }
}
