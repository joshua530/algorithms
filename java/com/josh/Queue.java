package com.josh;

import java.util.Iterator;

public class Queue<T> implements Iterable<T> {
    private java.util.LinkedList<T> list = new java.util.LinkedList<T>();

    public Queue() {
    }

    public Queue(T data) {
        list.addFirst(data);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private void ensureNotEmpty() {
        if (isEmpty())
            throw new RuntimeException("Queue empty");
    }

    /** get item at the front of the queue */
    public T peek() {
        ensureNotEmpty();
        return list.getFirst();
    }

    /** remove element from the front of the queue */
    public T poll() {
        ensureNotEmpty();
        return list.removeFirst();
    }

    /** add item to the back of the queue */
    public void offer(T data) {
        list.addLast(data);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
