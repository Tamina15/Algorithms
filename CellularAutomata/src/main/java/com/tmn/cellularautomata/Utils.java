package com.tmn.cellularautomata;

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
    public static String toBinaryString(int number, int minLength) {
        if (minLength == 0) {
            return Integer.toBinaryString(number);
        }
        return String.format("%" + minLength + "s", Integer.toBinaryString(number)).replace(' ', '0');
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
    public static int[] toBinaryArray(int number, int minLength) {
        return toBinaryArray(toBinaryString(number, minLength));
    }

    /**
     * Convert an binary string to binary array representation.
     * It it the caller responsibility to ensure the {@code binaryString}
     * only contain the characters '0' and '1' for accurate representation.
     *
     * @param binaryString the binary string to be converted to a binary array
     * @return a binary array representation of the binary string.
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
     * @param array the integer array to be reversed
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
        int[] reverse = new int[array.length];
        newReversedArray(reverse, array);
        return reverse;
    }

    /**
     * Populate an array with data which is the reverse elements of supplied array
     *
     * @param array           the integer array to be written to.
     * @param supplementArray the elements from this array
     *                        will be written in reverse to the {@code array}
     */
    public static void newReversedArray(int[] array, int[] supplementArray) {
        int length = supplementArray.length;
        for (int i = 0; i < length; i++) {
            array[i] = supplementArray[length - i - 1];
        }
    }
    
    /**
     * Copy elements from one array to another
     * @param array the array to copy elements to
     * @param supplementArray the array to copy elements from 
     */
    public static void copyOf(int[] array, int[] supplementArray) {
        System.arraycopy(supplementArray, 0, array, 0, array.length);
    }
}
