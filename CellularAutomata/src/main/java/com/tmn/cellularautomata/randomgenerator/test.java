package com.tmn.cellularautomata.randomgenerator;

import java.util.HashSet;
import java.util.Random;

public class test {

    private static void R(Random r) {
        HashSet<Integer> intSet = new HashSet<>();
        HashSet<Long> longSet = new HashSet<>();
        HashSet<Double> doubleSet = new HashSet<>();
        HashSet<Float> floatSet = new HashSet<>();
        int booleanCount = 0;

        long time = 0, s, e;
        int i;
        long l;
        double d;
        float f;
        boolean b;

        for (int n = 0; n < 1_000_000; n++) {
            s = System.nanoTime();
            i = r.nextInt();
            l = r.nextLong();
            d = r.nextDouble();
            f = r.nextFloat();
            b = r.nextBoolean();
            e = System.nanoTime();
            time += (e - s);
            if (n < 100_000) {
                intSet.add(i);
                longSet.add(l);
                doubleSet.add(d);
                floatSet.add(f);
                booleanCount = booleanCount + (b ? 1 : -1);
            }
        }
        System.out.print("Int: " + intSet.size());
        System.out.print(", Long: " + longSet.size());
        System.out.print(", Double: " + doubleSet.size());
        System.out.print(", Float: " + floatSet.size());
        System.out.print(", Boolean: " + booleanCount);
        System.out.println(" in: " + (time * 1.0 / 1000000));
    }

    public static void main(String[] args) {
        long s = System.nanoTime();
        Random r = new Random(s);
        Random ca = new CARandom2(s);
        for (int i = 0; i < 100; i++) {
//            R(r);
//            R(ca);
            System.out.println(r.nextInt());
            System.out.println(ca.nextInt());
            System.out.println("------------");
        }
    }
}
