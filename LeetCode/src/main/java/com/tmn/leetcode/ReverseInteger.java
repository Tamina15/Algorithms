package com.tmn.leetcode;

/**
 * Given a signed 32-bit integer x, return x with its digits reversed.
 * If reversing x causes the value to go outside
 * the signed 32-bit integer range [-231, 231 - 1], then return 0.
 * <br>
 * <br>
 * Assume the environment does not allow you to store 64-bit integers (signed or
 * unsigned).
 *
 * @author NhatTranMinh
 */
public class ReverseInteger {

    public static int reverse(int x) {
        boolean negative = x < 0;
        int a = negative ? -x : x;
        int z = 0;
        while (a != 0) {
            int b = z * 10;
            // Overflow check
            if (b / 10 != z) {
                return 0;
            }
            z = b + (a % 10);
            a /= 10;
        }
        return negative ? -z : z;
    }

    public static void main(String[] args) {
        System.out.println(reverse(0));
        System.out.println(reverse(1));
        System.out.println(reverse(-1));
        System.out.println(reverse(-1000000000));
        System.out.println(reverse(1000000000));
        System.out.println(reverse(1000010030));
        System.out.println(reverse(-1000010030));
        System.out.println(reverse(Integer.MAX_VALUE));
        System.out.println(reverse(Integer.MIN_VALUE));
        System.out.println(reverse(999999999));
        System.out.println(reverse(1999999999));
        System.out.println(reverse(-999999999));
        System.out.println(reverse(-1999999999));
    }
}
