package com.tmn.cellularautomata.RNG;

public final class LongHashSet {

    private static final int EMPTY = Integer.MIN_VALUE;

    private long[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public LongHashSet() {
        this(8);
    }

    public LongHashSet(int expectedSize) {
        this(expectedSize, 0.75f);
    }

    public LongHashSet(int expectedSize, float loadFactor) {
        this.loadFactor = loadFactor;

        int capacity = 1;
        while (capacity < expectedSize / loadFactor) {
            capacity <<= 1;
        }

        table = new long[capacity];
        java.util.Arrays.fill(table, EMPTY);

        threshold = (int) (capacity * loadFactor);
    }

    public boolean add(long value) {
        if (value == EMPTY) {
            return false;
//            throw new IllegalArgumentException("Integer.MIN_VALUE reserved as EMPTY marker");
        }

        if (size >= threshold) {
            resize();
        }

        int idx = findSlot(value);

        if (table[idx] == value) {
            return false;
        }

        table[idx] = value;
        size++;

        return true;
    }

    public boolean contains(int value) {
        int idx = probe(value);
        return idx >= 0;
    }

    public int size() {
        return size;
    }

    private int probe(int value) {
        int mask = table.length - 1;
        int idx = (int) mix(value) & mask;

        while (true) {
            long current = table[idx];

            if (current == EMPTY) {
                return -1;
            }

            if (current == value) {
                return idx;
            }

            idx = (idx + 1) & mask;
        }
    }

    private int findSlot(long value) {
        int mask = table.length - 1;
        int idx = (int) mix(value) & mask;

        while (true) {
            long current = table[idx];

            if (current == EMPTY || current == value) {
                return idx;
            }

            idx = (idx + 1) & mask;
        }
    }

    private void resize() {
        var old = table;

        table = new long[old.length << 1];
        java.util.Arrays.fill(table, EMPTY);

        threshold = (int) (table.length * loadFactor);

        for (var value : old) {
            if (value != EMPTY) {
                int idx = findSlot(value);
                table[idx] = value;
            }
        }
    }

    private static long mix(long z) {
        z ^= (z >>> 33);
        z *= 0xff51afd7ed558ccdL;
        z ^= (z >>> 33);
        z *= 0xc4ceb9fe1a85ec53L;
        z ^= (z >>> 33);
        return z;
    }
}
