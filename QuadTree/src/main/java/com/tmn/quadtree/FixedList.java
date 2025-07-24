package com.tmn.quadtree;

import java.util.Objects;

public class FixedList<E> {

    private final Object[] elements;
    private final int size;
    private int elementsIndex = 0;

    public FixedList(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Illegal Capacity: "
                    + length);
        }
        this.size = length;
        this.elements = new Object[length];
    }

    public int size() {
        return elementsIndex;
    }

    public boolean isEmpty() {
        return elementsIndex == 0;
    }

    E elementData(int index) {
        return (E) elements[index];
    }

    public E get(int index) {
        Objects.checkIndex(index, elementsIndex);
        return elementData(index);
    }

    public boolean add(E e) {
        Objects.checkIndex(elementsIndex, size);
        elements[elementsIndex++] = e;
        return true;
    }

    public void clear() {
        elementsIndex = 0; // soft clear
    }

    public void clearAll() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
    }

    public E getLast() {
        return get(elementsIndex - 1);
    }

}
