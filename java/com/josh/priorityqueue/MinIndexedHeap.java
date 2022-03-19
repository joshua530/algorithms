package com.josh.priorityqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Key index -> the value a key is mapped to in the doubly linked hash map eg
 * 
 * Name Key Index<br>
 * ---- ---------<br>
 * John 0 <br>
 * Jane 1 <br>
 * Mary 2 <br>
 */
public class MinIndexedHeap<T extends Comparable<T>> {
    /** Total number of items in the heap */
    private int size;

    /** Maximum number of elements in the heap */
    private final int MAX_NUMBER;

    /** Maximum number of childern per node */
    private final int DEGREE;

    /**
     * Lookup arrays for the child and parent for each node. Saves us from dynamic
     * computation of parent and child nodes
     * 
     * The child array holds the expected index for the first child of a given node
     */
    private final int[] child, parent;

    /** Maps key indices to the position of the given keys in the heap */
    public final int[] positionMap;

    /** Maps indexed heap nodes their corresponding key indices in the hash table */
    public final int[] inverseMap;

    /**
     * Contains the values assigned to various keys in the heap. It is indexed by
     * key indices
     */
    public final Object[] values;

    public MinIndexedHeap(int degree, int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size <= 0");
        }

        DEGREE = max(2, degree);
        MAX_NUMBER = max(DEGREE + 1, maxSize);

        positionMap = new int[MAX_NUMBER];
        inverseMap = new int[MAX_NUMBER];
        child = new int[MAX_NUMBER];
        parent = new int[MAX_NUMBER];
        values = new Object[MAX_NUMBER];

        for (int i = 0; i < MAX_NUMBER; ++i) {
            parent[i] = (i - 1) / DEGREE;
            child[i] = i * DEGREE + 1;
            positionMap[i] = inverseMap[i] = -1;
        }
    }

    private int max(int a, int b) {
        return Math.max(a, b);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(int keyIndex) {
        keyInBoundsOrThrow(keyIndex);
        if (keyIndex >= size)
            throw new IndexOutOfBoundsException();

        return positionMap[keyIndex] != -1;
    }

    public int peekMinKeyIndex() {
        isNotEmptyOrThrow();
        if (positionMap[0] == -1)
            throw new NullPointerException("Item at index 0 does not exist");
        return inverseMap[0];
    }

    public int pollMinKeyIndex() {
        if (positionMap[0] == -1)
            throw new NullPointerException("Item at index 0 does not exist");
        int minKeyIndex = peekMinKeyIndex();
        delete(minKeyIndex);
        return minKeyIndex;
    }

    @SuppressWarnings("unchecked")
    public T peekMinValue() {
        isNotEmptyOrThrow();
        return (T) values[inverseMap[0]];
    }

    public T pollMinValue() {
        T minValue = peekMinValue();
        delete(peekMinKeyIndex());
        return minValue;
    }

    public void insert(int keyIndex, T value) {
        if (contains(keyIndex))
            throw new IllegalArgumentException("Index already exists; received:" + keyIndex);
        valueNotNullOrThrow(value);
        positionMap[keyIndex] = size;
        inverseMap[size] = keyIndex;
        values[keyIndex] = value;
        swim(size);
    }

    @SuppressWarnings("unchecked")
    public T valueOf(int keyIndex) {
        keyExistsOrThrow(keyIndex);
        return (T) values[keyIndex];
    }

    @SuppressWarnings("unchecked")
    public T delete(int keyIndex) {
        keyExistsOrThrow(keyIndex);
        final int i = positionMap[keyIndex];
        swap(i, --size);
        sink(i);
        swim(i);
        T value = (T) values[keyIndex];
        values[keyIndex] = null;
        positionMap[keyIndex] = -1;
        inverseMap[keyIndex] = -1;
        --size;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T update(int keyIndex, T value) {
        keyExistsOrThrow(keyIndex);
        valueNotNullOrThrow(value);
        final int i = positionMap[keyIndex];
        T oldValue = (T) values[keyIndex];
        values[keyIndex] = value;
        sink(i);
        swim(i);
        return oldValue;
    }

    /** Strictly make value at given key index smaller */
    public void decrease(int keyIndex, T value) {
        keyExistsOrThrow(keyIndex);
        valueNotNullOrThrow(value);
        if (less(value, values[keyIndex])) {
            values[keyIndex] = value;
            swim(positionMap[keyIndex]);
        }
    }

    /** Strictly make value at given key index larger */
    public void increase(int keyIndex, T value) {
        keyExistsOrThrow(keyIndex);
        valueNotNullOrThrow(value);
        if (less(values[keyIndex], value)) {
            values[keyIndex] = value;
            sink(positionMap[keyIndex]);
        }
    }

    private void sink(int i) {
        for (int j = minChild(i); j != -1;) {
            swap(i, j);
            i = j;
            j = minChild(i);
        }
    }

    private void swim(int i) {
        while (less(i, parent[i])) {
            swap(i, parent[i]);
            i = parent[i];
        }
    }

    private int minChild(int j) {
        int index = -1, from = child[j], to = Math.min(size, from + DEGREE);

        for (int i = from; i < to; ++i)
            if (less(i, j))
                index = j = i;

        return index;
    }

    private void swap(int i, int j) {
        int keyIndexI = inverseMap[i], keyIndexJ = inverseMap[j];
        positionMap[keyIndexI] = j;
        positionMap[keyIndexJ] = i;
        inverseMap[i] = keyIndexJ;
        inverseMap[j] = keyIndexI;
    }

    /** Test whether the value of node i < value of node j */
    @SuppressWarnings("unchecked")
    private boolean less(int i, int j) {
        Object valI = values[inverseMap[i]], valJ = values[inverseMap[j]];
        return ((Comparable<? super T>) valI).compareTo((T) valJ) < 0;
    }

    /** Check whether i < j */
    @SuppressWarnings("unchecked")
    private boolean less(Object i, Object j) {
        return ((Comparable<? super T>) i).compareTo((T) j) < 0;
    }

    @Override
    public String toString() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size(); ++i)
            list.add(inverseMap[i]);
        return list.toString();
    }

    /* helper methods */
    private void isNotEmptyOrThrow() {
        if (isEmpty())
            throw new NoSuchElementException("Priority queue underflow");
    }

    private void keyExistsOrThrow(int keyIndex) {
        if (!contains(keyIndex))
            throw new NoSuchElementException("Index does not exist; received:" + keyIndex);
    }

    private void valueNotNullOrThrow(Object value) {
        if (value == null)
            throw new IllegalArgumentException("Value cannot be null");

    }

    private void keyInBoundsOrThrow(int keyIndex) {
        if (keyIndex < 0 || keyIndex >= MAX_NUMBER)
            throw new IllegalArgumentException("Key index out of bounds; received: " + keyIndex);
    }

}
