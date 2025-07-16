package com.tmn.cellularautomata.randomgenerator;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;

public class test22 {

    public static void main(String[] args) throws FileNotFoundException {
        HashSet<Integer> set = new HashSet();
        CARandom caRandom = new CARandom();
        Random random = new Random();
        try {
            for (int i = 0; i < 10000000; i++) {
                set.add(caRandom.nextInt());
//                set.add(random.nextInt());
            }
        } catch (Exception e) {
        }
        System.out.println(set.size());
    }
}
