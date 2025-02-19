package com.tmn.cellularautomata;

import java.util.Arrays;

public class Utils {

    /**
     * Convert an integer to its binary string representation,
     * padded with zero to ensure the result has at least a specified minimum length.
     *
     * @param number    the integer to be converted to a binary string
     * @param minLength the minimum length of the resulting binary string;
     *                  If the binary representation of the number is shorter than this length,
     *                  it will be padded with leading zeros if {@code minLength} &lt; 0,
     *                  else with trailing zeros if {@code minLength} &gt; 0
     * @return a binary string representation of the specified integer,
     *         padded with zeros if necessary to meet the minimum length.
     */
    public static String toBinaryString(long number, int minLength) {
        if (minLength == 0) {
            return Long.toBinaryString(number);
        }
        return String.format("%" + minLength + "s", Long.toBinaryString(number)).replace(' ', '0');
    }

    /**
     * Convert an integer to its binary array representation.
     *
     * @param number the integer to be converted to a binary array
     * @param length the length of the result array
     * @param onLeft should zero padded or truncate the left of the result array.
     *               {@code false} to change to right
     *
     * @return a binary array representation of the input integer
     */
    public static int[] toExactBinaryArray(int number, int length, boolean onLeft) {
        if (length <= 0) {
            return new int[]{};
        }
        int[] result = toBinaryArray(toBinaryString(number, length));
        if (result.length > length) {
            if (onLeft) {
                return Arrays.copyOfRange(result, result.length - length, result.length);
            } else {
                return Arrays.copyOfRange(result, 0, length);
            }
        }
        return result;
    }

    /**
     * Convert an integer to its binary array representation,
     * padded with zero to ensure the result has at least a specified minimum length.
     *
     * @param number    the integer to be converted to a binary array
     * @param minLength the minimum length of the resulting binary array;
     *                  If the array length is shorter than this length,
     *                  it will be padded with leading zeros if {@code minLength} &lt; 0,
     *                  else with trailing zeros if {@code minLength} &gt; 0
     * @return a binary array representation of the specified integer,
     *         padded with zeros if necessary to meet the minimum length.
     */
    public static int[] toBinaryArray(long number, int minLength) {
        return toBinaryArray(toBinaryString(number, minLength));
    }

    /**
     * Convert an binary string to binary array representation.
     * It it the caller responsibility to ensure the {@code binaryString}
     * only contain the characters '0' and '1' for accurate representation.
     *
     * @param binaryString the binary string to be converted to a binary array
     * @return a binary array representation of the input binary string.
     */
    public static int[] toBinaryArray(String binaryString) {
        int[] array = binaryString.chars().map((operand) -> operand - 48 /* '0' = 48 */).toArray();
        return array;
    }

    /**
     * Reverse the array elements in-place.
     *
     * @apiNote
     * This method change the input array element positions.
     *
     * @param array the array to be reversed
     *
     */
    public static void reverseArray(int[] array) {
        int length = array.length;
        int half = length / 2;
        for (int i = 0; i < half; i++) {
            int temp = array[i];
            array[i] = array[length - i - 1];
            array[length - i - 1] = temp;
        }
    }

    /**
     * Return a new array that is the reverse of the input array
     *
     * @param array
     * @return a new array
     */
    public static int[] newReversedArray(int[] array) {
        int length = array.length;
        int[] reverse = new int[length];
        for (int i = 0; i < length; i++) {
            reverse[i] = array[length - i - 1];
        }
        return reverse;
    }

    /**
     * Copy elements from one array to another
     *
     * @param dest the array to copy elements to
     * @param src  the array to copy elements from
     */
    public static void copyOf(int[] dest, int[] src) {
        System.arraycopy(src, 0, dest, 0, dest.length);
    }

}
