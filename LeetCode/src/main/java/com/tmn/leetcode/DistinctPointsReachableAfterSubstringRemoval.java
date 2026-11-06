package com.tmn.leetcode;

import java.util.HashSet;

/*
You are given a string s consisting of characters 'U', 'D', 'L', and 'R', 
representing moves on an infinite 2D Cartesian grid.

'U': Move from (x, y) to (x, y + 1).
'D': Move from (x, y) to (x, y - 1).
'L': Move from (x, y) to (x - 1, y).
'R': Move from (x, y) to (x + 1, y).
You are also given a positive integer k.

You must choose and remove exactly one contiguous substring of length k from s. 
Then, start from coordinate (0, 0) and perform the remaining moves in order.

Return an integer denoting the number of distinct final coordinates reachable.
 */
public class DistinctPointsReachableAfterSubstringRemoval {

    /*
    4
    UDLRUDLRUDLRUDLRUDLR
    UDLRUDLRUDLRUDLR
    UDLRUDLRUDLRUDL    R
    
     */
    public static int distinctPoints(String s, int k) {
        int n = s.length();
        if (n == k) {
            return 1;
        }
        int x = 0, y = 0;
        HashSet<Long> set = new HashSet<>();
        for (int i = 0; i < n - k; i++) {
            switch (s.charAt(i)) {
                case 'U' ->
                    y += 1;
                case 'D' ->
                    y -= 1;
                case 'L' ->
                    x -= 1;
                case 'R' ->
                    x += 1;
            }
        }
        set.add(((long) x << 32) | (y & 0xFFFFFFFFL));
        int l = n - k, r = n;
        for (int i = 0; i < n - k; i++) {
            l--;
            r--;
            if (l < 0) {
                break;
            }
            char cl = s.charAt(l);
            char cr = s.charAt(r);
            // Undo
            switch (cl) {
                case 'U' ->
                    y -= 1;
                case 'D' ->
                    y += 1;
                case 'L' ->
                    x += 1;
                case 'R' ->
                    x -= 1;
            }
            switch (cr) {
                case 'U' ->
                    y += 1;
                case 'D' ->
                    y -= 1;
                case 'L' ->
                    x -= 1;
                case 'R' ->
                    x += 1;
            }
            set.add(((long) x << 32) | (y & 0xFFFFFFFFL));
        }
        return set.size();
    }

    public static void main(String[] args) {
        int a = distinctPoints("LUL", 1);
        System.out.println(a);
    }
}
// Oct 28, 2026, 4:15:59 PM -> Oct 28, 2026, 4:41:23 PM
