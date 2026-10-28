package com.tmn.leetcode;

import java.util.Random;

/**
 * You are given a very large integer n, represented as a string,​​​​​​ and an
 * integer digit x.
 * The digits in n and the digit x are in the inclusive range [1, 9],
 * and n may represent a negative number, begin with '-'.
 *
 * You want to maximize n's numerical value by inserting x anywhere
 * in the decimal representation of n​​​​​​. You cannot insert x to the left of
 * the negative sign.
 *
 * Return a string representing the maximum value of n​​​​​​ after the
 * insertion.
 *
 * @author NhatTranMinh
 */
public class MaximumValueAfterInsertion {

    /**
     * Base on examples:
     *
     * For example, if n = 73 and x = 6, it would be best to insert it between 7 and
     * 3, making n = 763.
     * If n = -55 and x = 2, it would be best to insert it before the first 5,
     * making n = -255.
     *
     * Example 1:
     * Input: n = "99", x = 9
     * Output: "999"
     * Explanation: The result is the same regardless of where you insert 9.
     *
     * Example 2:
     * Input: n = "-13", x = 2
     * Output: "-123"
     * Explanation: You can make n one of {-213, -123, -132}, and the largest of
     * those three is -123.
     *
     * Then we should:
     * If negative, insert x before the first larger value.
     * If positive, insert x before the first smaller value.
     * Else, insert x at the last position.
     *
     * @param n
     * @param x
     * @return
     */
    public static String maxValue(String n, int x) {
        boolean negative = n.charAt(0) == '-';
        int i = 0;
        if (negative) {
            for (i = 1; i < n.length(); i++) {
                int c = n.charAt(i) - '0';
                if (c > x) {
                    break;
                }
            }
        } else {
            for (; i < n.length(); i++) {
                int c = n.charAt(i) - '0';
                if (c < x) {
                    break;
                }
            }
        }
        return n.substring(0, i) + x + n.substring(i);
    }

    static Random r = new Random();

    static String random() {
        int n = r.nextInt(99999);
        StringBuilder sb = new StringBuilder("");
        if (r.nextBoolean()) {
            sb.append('-');
        }
        for (int i = 0; i < n; i++) {
            sb.append(r.nextInt(1, 10));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String n = random();
        int x = r.nextInt(1, 10);
        System.out.println(maxValue(n, x));
    }

}
// 2:44:44 PM
// 2:53:34 PM
