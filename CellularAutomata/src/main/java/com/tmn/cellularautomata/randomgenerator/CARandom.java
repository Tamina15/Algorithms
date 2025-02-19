package com.tmn.cellularautomata.randomgenerator;

import java.util.Arrays;
import java.util.Random;

public class CARandom extends Random {

    /**
     * Length of the machine.
     *
     * @apiNote
     * The machine true length is 3 bits short of 192 (64 * 3).
     * <p/>
     * The first and last bits is always zero to remove the need for bound check.
     * <p/>
     * 1 bit is remove to make {@code length} odd.
     */
    private final int length = 194;

    /**
     * The states.
     *
     */
    private int[] cells;

    /**
     * Current index on the {@code cells}.
     */
    private int cI = 1;

    /**
     * Initial state of the machine.
     */
    private long CAseed;

    public CARandom() {
        this(System.nanoTime());
    }

    public CARandom(long CAseed) {
        this.CAseed = CAseed;
        init();
    }
    /**
     * Random value array to ensure the initial states has sufficient randomness.
     */
    private static final int[] randomPadding = toBinaryArray(1515151515151515151l);

    /**
     * Initialize the state for the generator
     *
     * @implNote
     * For an particular CAseed [s1, s2, s3, s4, ..., s64]
     * and random padding [p1, p2, p3, p4, ..., p64],
     * this method create the following arrays:
     * <p/>
     * [0, s1, p1, s64, s2, p2, s63, s3, p3, s62, ..., s63, p63, s2, s64, p64, s1, 0]
     */
    private void init() {
        cI = 1;
        left = 0;
        cells = new int[length];
        int[] CAseeds = toBinaryArray(CAseed);
        int[] reversedSeeds = newReversedArray(CAseeds);
        interleave(cells, CAseeds, randomPadding, reversedSeeds);
        cells[length - 1] = 0;
        System.out.println(Arrays.toString(cells));
    }

    /**
     * Interleaving the supplies array together.
     *
     * @param output the result array from the interleave process
     * @param arrays the input arrays
     * @return the {@code output} array
     */
    private int[] interleave(int[] output, int[]... arrays) {
        for (int i = 1; i < length - 1; i++) {
            try {
                output[i] = arrays[(i - 1) % arrays.length][(i - 1) / arrays.length];
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
        for (int i = 0; i < 32; i++) {
            if (cI >= length - 1) {
                cI = 1;
                left = 0;
            }
            // Rule 150: r XOR q XOR p
            int value = left ^ cells[cI] ^ cells[cI + 1];
            left = cells[cI];
            cells[cI] = value;
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
    public static int[] toBinaryArray(long number) {
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
    public  static int[] newReversedArray(int[] array) {
        int[] reverse = new int[array.length];
        for (int i = 0; i < reverse.length; i++) {
            reverse[i] = array[array.length - 1 - i];
        }
        return reverse;
    }

}
