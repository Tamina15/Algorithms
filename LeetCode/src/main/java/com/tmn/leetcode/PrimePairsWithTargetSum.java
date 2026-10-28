package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * You are given an integer n. We say that two integers x and y form a prime
 * number pair if:
 *
 * 1 <= x <= y <= n
 * x + y == n
 * x and y are prime numbers
 *
 * Return the 2D sorted list of prime number pairs [xi, yi].
 * The list should be sorted in increasing order of xi.
 * If there are no prime number pairs at all, return an empty array.
 *
 * @author NhatTranMinh
 */
public class PrimePairsWithTargetSum {

    public static List<List<Integer>> findPrimePairs(int n) {
        // Eratosthenes Seive
        int[] result = new int[n];
        int count = 0;
        boolean[] composite = new boolean[n + 1];
        composite[0] = composite[1] = true;
        for (int p = 2; p * p <= n; p++) {
            if (!composite[p]) {
                for (int multiple = p * p; multiple <= n; multiple += p) {
                    composite[multiple] = true;
                }
            }
        }
        for (int i = 0; i < composite.length; i++) {
            if (composite[i] == false) {
                result[count++] = i;
            }
        }
        var primes = Arrays.copyOf(result, count);

        List<List<Integer>> list = new ArrayList<>();
        int left = 0, right = primes.length - 1;
        // Two Sum
        while (left <= right) {
            int sum = primes[left] + primes[right];
            if (sum == n) {
                list.add(List.of(primes[left], primes[right]));
                left++;
                while (left < right && primes[left] == primes[left - 1]) {
                    left++;
                }
                continue;
            }
            if (sum < n) {
                left++;
            } else {
                right--;
            }
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(findPrimePairs(2));
    }
}
