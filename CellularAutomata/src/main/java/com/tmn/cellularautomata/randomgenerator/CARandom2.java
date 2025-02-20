package com.tmn.cellularautomata.randomgenerator;

import java.util.Random;

/**
 * The CARandom2 class implements a random number generator
 * based on the principles of elemental cellular automata.
 * <p>
 * This generator uses a one-dimensional array of cells, where each cell
 * can be in one of two states (0 or 1), represented by 3 {@code long} values.
 * The state of each cell in the next generation is determined by a fixed rule
 * that considers the current state of the cell and its two immediate neighbors.
 * <p>
 * Example usage:
 * <pre>
 * CARandom2 rng = new CARandom2();
 * int randomNumber = rng.nextInt();
 * </pre>
 * <p>
 * This class is not thread-safe.
 *
 * @implNote
 * This generator use rule 150, 3-cell neighborhood
 * with bounded edge (cells outside of the array is considered to be 0).
 * The next state of each cell can be calculated as <i> r XOR q XOR p </i>.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Elementary_cellular_automaton">Elementary Cellular Automaton</a>
 * @see <a href="https://mathworld.wolfram.com/Rule150.html">Rule 150</a>
 * @see java.util.Random
 * @see CARandom
 *
 * @author nhat.tranminh
 */
public class CARandom2 extends Random {

    /**
     * Number of bits in 3 {@code long} values.
     */
    private final int length = 192;

    /**
     * Padding to increase randomness in case {@link #seed} has low entropy
     * (too few or too many 1 bits, or continuous 1s or 0s, ...).
     * <p/>
     * E.g: {@link Long#MAX_VALUE}, 0, 1, 2<sup>{@code n}</sup>, 0xFF00000, 0xFFFF00000000L, ...
     * <p/>
     * This number is chosen arbitrarily.
     */
    private static final int[] randomPadding = toBinaryArray(1515151515151515151l);

    /**
     * The initial state of the automaton
     */
    private long seed;

    /**
     * Value represent the state of the automaton
     */
    private long left, mid, right;

    /**
     * Creates a new random number generator with a random seed.
     */
    public CARandom2() {
        this(System.nanoTime());
    }

    /**
     * Creates a new random number generator using a single {@code long} seed.
     * The seed is the initial state of the automaton.
     *
     * @param seed the initial seed
     * @see #setSeed(long)
     */
    public CARandom2(long seed) {
        this.seed = seed;
        init();
    }

    /**
     * Initializes the internal state of the cellular automaton random number generator.
     * The method performs the following steps:
     * <ul>
     * <li>Converts the seed to a binary array.</li>
     * <li>Reverses the binary array.</li>
     * <li>Interleaves the binary array, {@link randomPadding}
     * and the reversed seed binary array to create the initial state array.</li>
     * <li>Split the initial state array into {@link left}, {@link mid}, and {@link right}.</li>
     * </ul>
     *
     * @see #interleave(int length, int[]... arrays)
     * @see #newReversedArray(int[] array)
     * @see #randomPadding
     */
    private void init() {
        int[] seeds = toBinaryArray(seed);
        int[] reversedSeeds = newReversedArray(seeds);
        int[] cells = interleave(length, seeds, randomPadding, reversedSeeds);
        for (int i = 0; i < 64; i++) {
            left = (left << 1) + cells[i];
            mid = (mid << 1) + cells[i + 64];
            right = (right << 1) + cells[i + 128];
        }
    }

    /**
     * Decide which part to return
     */
    private int switcher;

    /**
     * {@inheritDoc }
     */
    @Override
    protected int next(int bits) {
        next();
        switcher = (switcher + 1) % 3;
        long result = switch (switcher) {
            case 1 ->
                left;
            case 2 ->
                right;
            default ->
                mid;
        };
        return (int) (result >>> (64 - bits));
    }

    /**
     * Advance the current state to the next state.
     * <p/>
     * This RNG use Rule 150, which can be expressed as <i> r XOR q XOR p </i>.
     *
     * @implNote
     * {@code left} is calculated as the XOR result between {@code left},
     * {@code left} unsigned right-shifted once and
     * {@code left} left-shifted once with the RMB is the LMB of {@code mid}.
     * <p/>
     * {@code mid} is calculated as the XOR result between {@code mid},
     * {@code mid} unsigned right-shifted once with LMB is the RMB of {@code left} and
     * {@code mid} left-shifted once with the RMB is the LMB of {@code right}.
     * <p/>
     * {@code right} is calculated as the XOR result between {@code right},
     * {@code right} unsigned right-shifted once with RMB is the LMB of {@code mid} and
     * {@code right} left-shifted once.
     */
    private void next() {
        long nextA = ((left << 1) | (mid >>> 63)) ^ left ^ (left >>> 1);
        long nextB = ((mid << 1) | (right >>> 63)) ^ mid ^ ((mid >>> 1) | ((left & 1) << 63));
        long nextC = (right << 1) ^ right ^ ((right >>> 1) | ((mid & 1) << 63));
        left = nextA;
        mid = nextB;
        right = nextC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSeed(long seed) {
        this.seed = seed;
        init();
    }

    /**
     * Interleaving the supply arrays together.
     *
     * @param length the length of the output array
     * @param arrays the input arrays
     * @return the interleaved array
     */
    private int[] interleave(int length, int[]... arrays) {
        int[] output = new int[length];
        for (int i = 0; i < length; i++) {
            output[i] = arrays[(i) % arrays.length][(i) / arrays.length];
        }
        return output;
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
     * Return a new array in which the elements are in reversed order.
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

    private static String print(int[] array) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                b.append(".");
            } else {
                b.append(array[i]);
            }
        }
        return b.toString();
    }
}
