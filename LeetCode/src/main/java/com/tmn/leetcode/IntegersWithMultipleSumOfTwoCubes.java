package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * You are given an integer n.
 *
 * An integer x is considered good if there exist at least two distinct pairs (a, b) such that:
 *
 * a and b are positive integers.
 * a <= b
 * x = a^3 + b^3
 * Return an array containing all good integers less than or equal to n, sorted in ascending order.
 *
 * @author NhatTranMinh
 */
public class IntegersWithMultipleSumOfTwoCubes {

    static final Map<Integer, Integer> map = new HashMap<>();
    static final List<Integer> list = new ArrayList<>();

    static {
        int n = 1_000_000_000;
        for (int i = 1; i < n; i++) {
            int iCubed = i * i * i;
            if (iCubed > n) {
                break;
            }
            for (int j = i; j < n; j++) {
                int jCubed = j * j * j;
                int nCubed = iCubed + jCubed;
                if (jCubed > n || nCubed > n) {
                    break;
                }
                map.merge(nCubed, 1, Integer::sum);
            }
        }
        for (int v : map.keySet()) {
            if (map.get(v) > 1) {
                list.add(v);
            }
        }
        list.sort((o1, o2) -> o1 - o2);
    }

    public static List<Integer> findGoodIntegers(int n) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 1; i < n; i++) {
            int iCubed = i * i * i;
            if (iCubed > n) {
                break;
            }
            for (int j = i; j < n; j++) {
                int jCubed = j * j * j;
                int nCubed = iCubed + jCubed;
                if (jCubed > n || nCubed > n) {
                    break;
                }
                map.merge(nCubed, 1, Integer::sum);
            }
        }
        List<Integer> list = new ArrayList<>();
        for (int v : map.keySet()) {
            if (map.get(v) > 1) {
                list.add(v);
            }
        }
        list.sort((o1, o2) -> o1 - o2);
        return list;
    }

    public static List<Integer> findGoodIntegers2(int n) {
        List<Integer> ans = new ArrayList<>();
        for (int no : list) {
            if (no > n) {
                break;
            }
            ans.add(no);
        }

        return ans;
    }
    static Random r = new Random();

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            int a = r.nextInt(10000000, 100000000);
            Long start = System.nanoTime();
            findGoodIntegers(a);
            Long end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000 + "ms");
            start = System.nanoTime();
            findGoodIntegers2(a);
            end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000 + "ms");
            System.out.println("");
        }
    }
}
