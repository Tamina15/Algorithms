package com.tmn.cellularautomata.RNG;

import java.util.Arrays;

public final class FloatHashSet {

    private static final int EMPTY = 0x80000000;

    private int[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public FloatHashSet(int expectedSize) {
        this(expectedSize, 0.75f);
    }

    public FloatHashSet(int expectedSize, float loadFactor) {
        this.loadFactor = loadFactor;

        int capacity = 1;
        
        float maxLoadCapacity = expectedSize / loadFactor;
        
        while (capacity < maxLoadCapacity) {
            capacity <<= 1;
        }

        table = new int[capacity];
        Arrays.fill(table, EMPTY);

        threshold = (int) (capacity * loadFactor);
    }

    public boolean add(float value) {
        int bits = Float.floatToIntBits(value);

        if (bits == EMPTY) {
            throw new IllegalArgumentException("Reserved sentinel value");
        }

        if (size >= threshold) {
            resize();
        }

        int slot = findSlot(bits);

        if (table[slot] == bits) {
            return false;
        }

        table[slot] = bits;
        size++;

        return true;
    }

    public boolean contains(float value) {
        int bits = Float.floatToIntBits(value);

        int mask = table.length - 1;
        int idx = mix(bits) & mask;

        while (true) {
            int current = table[idx];

            if (current == EMPTY) {
                return false;
            }

            if (current == bits) {
                return true;
            }

            idx = (idx + 1) & mask;
        }
    }

    public int size() {
        return size;
    }

    private int findSlot(int bits) {
        int mask = table.length - 1;
        int idx = mix(bits) & mask;

        while (true) {
            int current = table[idx];

            if (current == EMPTY || current == bits) {
                return idx;
            }

            idx = (idx + 1) & mask;
        }
    }

    private void resize() {
        int[] old = table;

        table = new int[old.length << 1];
        Arrays.fill(table, EMPTY);

        threshold = (int) (table.length * loadFactor);

        for (int bits : old) {
            if (bits != EMPTY) {
                int slot = findSlot(bits);
                table[slot] = bits;
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
