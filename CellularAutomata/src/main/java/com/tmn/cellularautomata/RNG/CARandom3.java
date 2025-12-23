package com.tmn.cellularautomata.RNG;

import java.util.Random;

public class CARandom3 extends Random {

    /**
     * The initial state of the automaton
     */
    private long seed;

    /**
     * Creates a new random number generator with a random seed.
     */
    public CARandom3() {
        this(System.nanoTime());
    }

    /**
     * Creates a new random number generator using a single {@code long} seed.
     * The seed is the initial state of the automaton.
     *
     * @param seed the initial seed
     * @see #setSeed(long)
     */
    public CARandom3(long seed) {
        if (seed == 0) {
            seed = seed ^ Integer.MIN_VALUE;
        }
        this.seed = seed;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected int next(int bits) {
        seed = ((seed >>> 1) | ((seed & 1) << 63)) ^ (seed | ((seed << 1) | (seed >>> 63)));
        return (int) (seed >>> (64 - bits));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }

    private static void print(long number) {
        String s = String.format("%" + 64 + "s", Long.toBinaryString(number)).replace(' ', '0').replace('0', '.');
        System.out.println(s);
    }
}
