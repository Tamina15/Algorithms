package com.tmn.cellularautomata.randomgenerator;

import java.util.HashMap;
import java.util.Random;

public class test {

    private static void R(Random r) {
        HashMap<Integer, Integer> map = new HashMap<>();
        long s = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            r.nextInt();
        }
        long e = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            int a = r.nextInt();
            if (map.containsKey(a)) {
                map.put(a, map.get(a) + 1);
            } else {
                map.put(a, 1);
            }
        }
        System.out.println(map.size() + " in: " + ((e - s) * 1.0 / 1000000));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            long s = System.nanoTime();
            R(new Random(s));
            R(new CARandom(s));
            System.out.println("------------");
        }
//            R(new CARandom(1152921504606846976l));
    }
}
