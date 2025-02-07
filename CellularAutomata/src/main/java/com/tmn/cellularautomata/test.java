package com.tmn.cellularautomata;

import java.util.Arrays;

public class test {

    public static String toBinary(int number, byte exponent) {
        if (exponent < 0 || exponent > 31) {
            return "0";
        }
        return toBinary(number, 1 << exponent);
    }

    public static String toBinary(int number, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(number)).replace(' ', '0');
    }

    public static int[] toIntArray(String binary) {
        return binary.chars().map((operand) -> operand == 48 /*'0' = 48*/ ? 0 : 1).toArray();
    }

    public static void main(String[] args) {
        byte neightbors = 3;
        int pattern = 15, states = 1 << neightbors;
        int[] patternArray = toIntArray(toBinary(pattern, states));
        System.out.println(states);
        System.out.println(patternArray.length);
        System.out.println(Arrays.toString(patternArray));
    }
}
