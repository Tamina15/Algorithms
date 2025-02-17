package com.tmn.cellularautomata.randomgenerator;

import java.util.Arrays;
import java.util.Random;

public class CARandom extends Random {

    /**
     * The number of neighbor in the neighborhood, including itself
     */
    private final byte neightbors = 3;

    /**
     * The rule. Value less than 0 can cause unexpected behavior.
     */
    private final int rule = 150;

    /**
     * The numbers of possible next state. i.e 2<sup>{@code neighbors}</sup>.
     */
    private final int numberOfStates = 1 << neightbors;

    /**
     * The binary representation of the rule
     */
    private final int[] ruleArray = Utils.toExactBinaryArray(rule, numberOfStates, true);

    private final int length = 66 + 2;
    private int[] cells = new int[length];
    private int[] nexts = new int[length];
    private int cI = 1;

    private final long seed;
    private final long mask = 5351862787966346137l;

    public CARandom() {
        this(System.nanoTime());
    }

    public CARandom(long seed) {
        this.seed = seed;
        init();
    }

    private void init() {
        int[] bits = Utils.toBinaryArray(seed, 64);
        int[] reversedBits = Utils.newReversedArray(bits);
        int[] iBits = Utils.toBinaryArray(~seed, 64);
//        insert(insert(insert(1, reversedBits), iBits), bits);
//        insert(insert(1, reversedBits), bits);
        insert(1, bits);

        Utils.reverseArray(ruleArray);
    }

    private int insert(int i, int[] seedArray) {
        for (int s : seedArray) {
            cells[i] = s;
            i = i + 1;
        }
        return i;
    }

    @Override
    protected int next(int bits) {
        int result = 0;
        for (int i = 0; i < bits; i++) {
            result = result * 10 + update();
        }
        return result;
    }

    private int update() {
        if (cI >= length - 1) {
            cI = 1;
            int[] temp = cells;
            cells = nexts;
            nexts = temp;
        }
        int value = cells[cI - 1] ^ cells[cI] ^ cells[cI + 1];
        nexts[cI] = ruleArray[value];
        cI++;
        return value;
    }

    class Utils {

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
         * @return a binary array representation of the specified integer
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
         * @param array           the array to be written to.
         * @param supplementArray the elements from this array
         *                        will be written in reverse to the {@code array}
         * @deprecated
         */
        public static void newReversedArray(int[] array, int[] supplementArray) {
            int length = supplementArray.length;
            for (int i = 0; i < length; i++) {
                array[i] = supplementArray[length - i - 1];
            }
        }

        /**
         * Copy elements from one array to another
         *
         * @param array           the array to copy elements to
         * @param supplementArray the array to copy elements from
         */
        public static void copyOf(int[] array, int[] supplementArray) {
            System.arraycopy(supplementArray, 0, array, 0, array.length);
        }

    }

}
