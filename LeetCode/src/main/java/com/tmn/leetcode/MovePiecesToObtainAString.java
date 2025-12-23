package com.tmn.leetcode;

public class MovePiecesToObtainAString {

    public static boolean canChange(String start, String target) {
        char[] s = start.toCharArray();
        char[] t = target.toCharArray();
        int n = t.length;
        int i = 0, j = 0;
        for (; i < n; i++) {
            // if target is L
            if (t[i] == 'L') {
                boolean good = false;
                // check left portion for leftover letter, as none should have exsist
                for (; j < i; j++) {
                    if (s[j] == 'L' || s[j] == 'R') {
                        return false;
                    }
                }

                // check right portion from i, as L can move left
                for (; j < n || j < i; j++) {
                    if (s[j] == 'L') {
                        good = true;
                        j++;
                        break;
                    }
                    // we should not have found R
                    if (s[j] == 'R') {
                        return false;
                    }
                }
                if (!good) {
                    return false;
                }
            }
            // if target is R
            if (t[i] == 'R') {
                boolean good = false;
                // check the left portion only, as R cannot move left
                for (; j <= i; j++) {
                    // have one R to move
                    if (s[j] == 'R') {
                        good = true;
                        j++;
                        break;
                    }
                    // L should not be here
                    if (s[j] == 'L') {
                        good = false;
                        break;
                    }
                }
                if (!good) {
                    return false;
                }
            }
        }
        // leftover L, R
        for (; j < n; j++) {
            if (s[j] != '_') {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        String s = "LLRRLRRRRLLLLRLRRLLRRRRRLLRLLRRRLRRLRLRLRRLLRRLRLLLLLLLRRLRRLRLLLRLLRRRRRLRLRRRLRRLLLLLLRLLRRLRLLRLRRLRLLLLLLRRLLRLLLLLLLRLRLRRLLLLLLLLRRRLLRLRLRLLRRRLRLRRLLLRLRLRRLRRLRRRRRLLLLRLLRLLRLRRRRLRRLRLRLLLLLRLRRRLLLRLRRLRRRRLLLLLRLLRRRLRRRRRRLLLRRRLRLRLLRLLLRRRRLRLLRLRRRLRRLLLLRRLLLRLRRLLRRLLRRLLLRLLRRRLLRLLRRRLRLRRLLRRLLLRRRLLRRLRRRLLLRRRRRLLLLRLLLRLLRRRLLRRLRRLRRRRLLRLLLRLLLLLLRRLRLRRRRRRRLLLRLRRRRLRLLRLLRLRRLRLRRRLLRLRRRLRLLLRLRLRLRLRRRRLLLLRLLRRLRLRLLRLLLLRRRRRLLLRLLRLLLLRRLRLRLRLLRRLLLLLLRRLLLLRRRLR";
        String t = "LLRRLRRRRLLLLRLRRLLRRRRRLLRLLRRRLRRLRLRLRRLLRRLRLLLLLLLRRLRRLRLLLRLLRRRRRLRLRRRLRRLLLLLLRLLRRLRLLRLRRLRLLLLLLRRLLRLLLLLLLRLRLRRLLLLLLLLRRRLLRLRLRLLRRRLRLRRLLLRLRLRRLRRLRRRRRLLLLRLLRLLRLRRRRLRRLRLRLLLLLRLRRRLLLRLRRLRRRRLLLLLRLLRRRLRRRRRRLLLRRRLRLRLLRLLLRRRRLRLLRLRRRLRRLLLLRRLLLRLRRLLRRLLRRLLLRLLRRRLLRLLRRRLRLRRLLRRLLLRRRLLRRLRRRLLLRRRRRLLLLRLLLRLLRRRLLRRLRRLRRRRLLRLLLRLLLLLLRRLRLRRRRRRRLLLRLRRRRLRLLRLLRLRRLRLRRRLLRLRRRLRLLLRLRLRLRLRRRRLLLLRLLRRLRLRLLRLLLLRRRRRLLLRLLRLLLLRRLRLRLRLLRRLLLLLLRRLLLLRRRLR";
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            canChange(s, t);
            long end = System.nanoTime();
            System.out.println((end - start) * 1.0 / 1000000);
        }
        System.out.println(canChange(s, t));
    }
}
