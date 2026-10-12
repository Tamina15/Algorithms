package com.tmn.leetcode;

import java.util.HashSet;
import java.util.Set;

public class LongestSubstringWithoutDuplicateCharacter {

    public static void main(String[] args) {
        String s = "a";
        Set<Character> set = new HashSet<>();
        int l = 0, r = 0;
        int max = 0, maxl = 0, maxr = 0;
        for (; r < s.length();) {
            char c = s.charAt(r);
            if (set.contains(c)) {
                set.remove(s.charAt(l));
                l++;
            } else {
                int t = r - l + 1;
                if (t >= max) {
                    max = t;
                    maxl = l;
                    maxr = r;
                }
                r++;
                set.add(c);
            }
        }
        System.out.println(max);
        System.out.println(maxl + " " + maxr);

    }

}
