package com.josh.priorityqueue;

public class MinIndexedBinaryHeap<T extends Comparable<T>> extends MinIndexedHeap<T> {
    public MinIndexedBinaryHeap(int maxSize) {
        super(2, maxSize);
    }
}
