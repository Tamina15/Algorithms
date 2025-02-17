package com.tmn.cellularautomata.randomgenerator;

import java.util.Random;

public class CARandom extends Random {

    /**
     * rule 150 in binary, reversed.
     */
    private final int[] ruleArray = {0, 1, 1, 0, 1, 0, 0, 1};

    /**
     * Length of the machine.
     *
     * @apiNote
     * The machine true length is 3 bits short of 192 (64 * 3).
     * <p/>
     * The first and last bits is always zero to remove bound check.
     * <p/>
     * 1 bit is remove to make {@code length} odd.
     */
    private final int length = 191;

    /**
     * The states.
     *
     */
    private final int[] cells = new int[length];

    /**
     * Current index on the {@code cells}.
     */
    private int cI = 1;

    /**
     * Initial state of the machine.
     */
    private final long seed;

    public CARandom() {
        this(System.nanoTime());
    }

    public CARandom(long seed) {
        this.seed = seed;
        init();
    }

    /**
     * Random value array to ensure the initial state has sufficient randomness.
     */
    final int[] randomPadding = toBinaryArray(1515151515151515151l);

    /**
     * Interleaving the seed arrays to create the initial state.
     */
    private void init() {
        int[] bits = toBinaryArray(seed);
        int[] reversedBits = newReversedArray(bits);
        interleave(cells, bits, randomPadding, reversedBits);
    }

    private int[] interleave(int[] output, int[]... arrays) {
        for (int i = 0; i < length; i++) {
            try {
                output[i] = arrays[i % arrays.length][i / arrays.length];
            } catch (Exception e) {
//                break;
            }
        }
        return output;
    }

    private int left = 0;

    @Override
    protected int next(int bits) {
        int result = 0;
        int times = bits > 32 ? 32 : bits;
        for (int i = 0; i < times; i++) {
            if (cI >= length - 1) {
                cI = 1;
                left = 0;
            }
            int value = left ^ cells[cI] ^ cells[cI + 1];
            left = cells[cI];
            cells[cI] = ruleArray[value];
            cI++;
            result = (result << 1) + value;
        }
        return result;
    }

    /**
     * Convert an long value to its binary array representation,
     * padded with zero to ensure the result array's length is 64.
     *
     * @param number the {@code long} to be converted to a binary array
     * @return a binary array representation of the specified long,
     *         padded with zeros if necessary to meet the length of 64.
     */
    private static int[] toBinaryArray(long number) {
        String string = String.format("%" + 64 + "s", Long.toBinaryString(number)).replace(' ', '0');
        int[] array = string.chars().map((operand) -> operand - 48 /* '0' = 48 */).toArray();
        return array;
    }

    /**
     * Return a new array that is the reverse of the input array
     *
     * @param array the input array
     * @return a new array
     */
    private static int[] newReversedArray(int[] array) {
        int[] reverse = new int[array.length];
        for (int i = 0; i < reverse.length; i++) {
            reverse[i] = array[array.length - 1 - i];
        }
        return reverse;
    }

}
