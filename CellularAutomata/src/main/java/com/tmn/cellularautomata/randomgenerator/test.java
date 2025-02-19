package com.tmn.cellularautomata.randomgenerator;

import java.util.HashSet;
import java.util.Random;

public class test {

    private static void R(Random r) {
        HashSet<Integer> intSet = new HashSet<>();
        HashSet<Long> longSet = new HashSet<>();
        HashSet<Double> doubleSet = new HashSet<>();
        HashSet<Float> floatSet = new HashSet<>();
        long s = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            r.nextGaussian(r.nextDouble(), r.nextDouble());
        }
        long e = System.nanoTime();
        for (int i = 0; i < 500_000; i++) {
            intSet.add(r.nextInt());
            longSet.add(r.nextLong());
            doubleSet.add(r.nextDouble());
            floatSet.add(r.nextFloat());
        }
        System.out.println("Int: " + intSet.size() + ", Long:" + longSet.size() + ", Double:" + doubleSet.size() + ", Float:" + floatSet.size() + " in: " + ((e - s) * 1.0 / 1000000));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            long s = System.nanoTime();
            Random r = new Random(s);
            Random ca = new CARandom2(s);
            R(r);
            R(ca);
            System.out.println("------------");
        }
    }
}
