package com.tmn.leetcode;

import java.util.Random;

/**
 * Given a string <b>s</b>, return the longest palindromic substring in <b>s</b>.
 *
 *
 * @author NhatTranMinh
 */
public class LongestPalindromicSubstring {

    // Brute-Force
    public static String longestPalindrome0(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;
        int max = 1, maxl = 0, maxr = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int count = 1;
                boolean isPalindrome = true;
                for (int k = 0; k <= j - i; k++) {
                    if (chars[i + k] == chars[j - k]) {
                        count++;
                    } else {
                        isPalindrome = false;
                        break;
                    }
                }
                if (count > max && isPalindrome) {
                    max = count;
                    maxl = i;
                    maxr = j;
                }
            }
        }
        return s.substring(maxl, maxr + 1);
    }

    /**
     * For every character and gap between 2 characters, called center,
     * expand two pointers to the left and right of the center
     * while {@code s.charAt(left)} equals {@code s.charAt(right)}.
     *
     * <br><br>
     * The left and right pointers is the palindrome's start and end indexes of each center.
     *
     * <br><br>
     * Returns the longest palindrome of all centers.
     *
     * @param s input string
     * @return longest palindrome sub-string of {@code s}
     */
    public static String longestPalindrome(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;
        int max = 1, maxl = 0, maxr = 0, l, r;
        // increment by 0.5 to accommodate for gap center
        for (double i = 0; i < n; i += 0.5) {
            l = (int) i;
            r = (int) i;
            // 1 if center is a character, 0 for gap;
            int count = (l == i) ? 1 : 0;
            for (double j = 0.5; j < n; j += 0.5) {
                double m = (i - j);
                double p = (i + j);
                int mi = (int) m;
                int pi = (int) p;
                // bound check
                if (mi < 0 || pi >= n) {
                    break;
                }
                // we only need integer left and right bounds
                if (mi != m && pi != p) {
                    continue;
                }
                if (chars[mi] == chars[pi]) {
                    l = mi;
                    r = pi;
                    // add both left and right characters
                    count += 2;
                } else {
                    break;
                }
            }
            if (count > max) {
                max = count;
                maxl = l;
                maxr = r;
            }
        }
        return s.substring(maxl, maxr + 1);
    }

    static Random random = new Random();

    public static String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = random.nextInt(1, 10000);
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(longestPalindrome("a"));
//        for (int i = 0; i < 1000; i++) {
//            if (i % 10 == 0) {
//                System.out.println(i);
//            }
//            String s = randomString();
//            if (!longestPalindrome(s).equals(longestPalindrome0(s))) {
//                System.out.println("Wrong");
//            }
//        }
    }
}
