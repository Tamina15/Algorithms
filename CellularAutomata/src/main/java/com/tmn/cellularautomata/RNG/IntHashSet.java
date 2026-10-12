package com.tmn.cellularautomata.RNG;

public final class IntHashSet {

    private static final int EMPTY = Integer.MIN_VALUE;

    private int[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public IntHashSet() {
        this(8);
    }

    public IntHashSet(int expectedSize) {
        this(expectedSize, 0.75f);
    }

    public IntHashSet(int expectedSize, float loadFactor) {
        this.loadFactor = loadFactor;

        int capacity = 1;
        while (capacity < expectedSize / loadFactor) {
            capacity <<= 1;
        }

        table = new int[capacity];
        java.util.Arrays.fill(table, EMPTY);

        threshold = (int) (capacity * loadFactor);
    }

    public boolean add(int value) {
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
        int idx = mix(value) & mask;

        while (true) {
            int current = table[idx];

            if (current == EMPTY) {
                return -1;
            }

            if (current == value) {
                return idx;
            }

            idx = (idx + 1) & mask;
        }
    }

    private int findSlot(int value) {
        int mask = table.length - 1;
        int idx = mix(value) & mask;

        while (true) {
            int current = table[idx];

            if (current == EMPTY || current == value) {
                return idx;
            }

            idx = (idx + 1) & mask;
        }
    }

    private void resize() {
        int[] old = table;

        table = new int[old.length << 1];
        java.util.Arrays.fill(table, EMPTY);

        threshold = (int) (table.length * loadFactor);

        for (int value : old) {
            if (value != EMPTY) {
                int idx = findSlot(value);
                table[idx] = value;
            }
        }
    }

    private static int mix(int x) {
        x ^= (x >>> 16);
        x *= 0x7feb352d;
        x ^= (x >>> 15);
        x *= 0x846ca68b;
        x ^= (x >>> 16);
        return x;
    }
}
