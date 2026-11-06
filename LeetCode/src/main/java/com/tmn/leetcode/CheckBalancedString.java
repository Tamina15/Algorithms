package com.tmn.leetcode;

public class CheckBalancedString {

    // A string of digits is called balanced if the 
    // sum of the digits at even indices is equal 
    // to the sum of digits at odd indices.
    public static boolean isBalanced(String num) {
        int a = 0;
        int b = 0;
        for (int i = 0; i < num.length(); i += 2) {
            a += (int) (num.charAt(i) - '0');
        }
        for (int i = 1; i < num.length(); i += 2) {
            b += (int) (num.charAt(i) - '0');
        }
        return a == b;
    }

    public static void main(String[] args) {
        isBalanced("24123");
    }
}
