package com.josh.priorityqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * a max priority queue implementation
 * 
 * also called a Binary Heap
 * 
 * to reduce complexity from O(n) to O(log n), use a hashmap to look up the
 * items whenever you need to do so[only do so when doing additions and removals
 * frequently because hash maps add a lot of constant overhead->for
 * adding,removing and altering items in the hashmap]
 */
public class PriorityQueue<T extends Comparable<T>> {
    /** items are stored here */
    List<T> heap;

    public static void main(String[] args) {
        Integer[] items2 = { 20, 1, 3, 12, 33, 1, 16, 0, 4, 2, 13 };
        System.out.println(java.util.Arrays.toString(items2));
        PriorityQueue<Integer> heap = new PriorityQueue<>(items2);
        System.out.println(heap);
        heap.add(40);
        System.out.println(heap);
        heap.poll();
        System.out.println(heap);
        heap.remove(20);
        System.out.println(heap);
    }

    public PriorityQueue(int size) {
        heap = new ArrayList<T>(size);
    }

    public PriorityQueue() {
        heap = new ArrayList<>(1);
    }

    /** HEAPIFY */
    public PriorityQueue(T[] items) {
        int heapSize = items.length;
        heap = new ArrayList<>(heapSize);

        for (int i = 0; i < heapSize; ++i)
            heap.add(i, items[i]);

        sortHeap();
    }

    /** HEAPIFY */
    public PriorityQueue(Collection<T> items) {
        int heapSize = items.size();
        heap = new ArrayList<>(heapSize);
        heap.addAll(items);
        sortHeap();
    }

    private void sortHeap() {
        for (int i = 0; i < heap.size(); ++i)
            bubbleDown(i);
    }

    private void bubbleDown(int parent) {
        T parentNode = heap.get(parent);
        // Swap a parent with its child to satisfy the heap invariant
        if (hasChildren(parent)) {
            T leftChild = leftChild(parent);
            T rightChild = rightChild(parent);

            if (greater(leftChild, parentNode) || greater(rightChild, parentNode)) {
                T biggerNode = greater(leftChild, rightChild) ? leftChild : rightChild;
                int child = biggerNode.equals(leftChild) ? leftChildIndex(parent) : rightChildIndex(parent);
                swap(parent, child);
                // move the child node up the tree repeatedly till the heap invariant is met
                bubbleUp(parent);
                // move the initial parent node down the tree till the heap invariant
                // is met
                bubbleDown(child);
            }
        } else
            // there might still be a node above the current one with a lower value. if
            // there is, swap the current node with it
            bubbleUp(parent);
    }

    /** Move a node up the tree till the heap invariant is satisfied */
    private void bubbleUp(int i) {
        if (hasParent(i)) {
            int parentIndex = parentIndex(i);
            T parent = heap.get(parentIndex);
            T child = heap.get(i);

            if (greater(child, parent)) {
                swap(i, parentIndex);
                bubbleUp(parentIndex);
            }
        }
    }

    private boolean hasParent(int i) {
        int parent = parentIndex(i);
        return parent >= 0 && parent < heap.size();
    }

    /** Fetches the index of a node's parent */
    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    private void swap(int i, int swapWith) {
        T tmp = heap.get(i);
        heap.set(i, heap.get(swapWith));
        heap.set(swapWith, tmp);
    }

    private int leftChildIndex(int nodeIndex) {
        return nodeIndex * 2 + 1;
    }

    private int rightChildIndex(int nodeIndex) {
        return nodeIndex * 2 + 2;
    }

    /** Checks whether a is greater than b */
    private boolean greater(T a, T b) {
        if (a == null)
            return false;
        if (b == null)
            return true;
        return a.compareTo(b) > 0;
    }

    /** Checks whether a is lesser than b */
    private boolean lesser(T a, T b) {
        if (a == null)
            return true;
        if (b == null)
            return true;
        return a.compareTo(b) < 0;
    }

    private boolean hasChildren(int nodeIndex) {
        int left = leftChildIndex(nodeIndex);
        int right = rightChildIndex(nodeIndex);
        return left < heap.size() || right < heap.size();
    }

    private T leftChild(int nodeIndex) {
        int left = leftChildIndex(nodeIndex);
        if (left < heap.size())
            return heap.get(left);
        return null;
    }

    private T rightChild(int nodeIndex) {
        int right = rightChildIndex(nodeIndex);
        if (right < heap.size())
            return heap.get(right);
        return null;
    }

    /** Add an item to the queue. null cannot be added */
    public void add(T item) {
        if (item == null)
            throw new IllegalArgumentException();

        // add item to bottom left most node
        heap.add(item);
        int lastIndex = heap.size() - 1;

        bubbleUp(lastIndex);
    }

    public boolean isEmpty() {
        return heap.size() == 0;
    }

    public void clear() {
        heap.clear();
    }

    public int size() {
        return heap.size();
    }

    /** Fetch item at the top of the heap */
    public T peek() {
        if (isEmpty())
            return null;
        return heap.get(0);
    }

    /** Remove item at the top of the heap */
    public T poll() {
        return removeAt(0);
    }

    public boolean contains(T item) {
        if (isEmpty())
            return false;
        for (int i = 0; i < size(); ++i) {
            if (heap.get(i).equals(item))
                return true;
        }
        return false;
    }

    public boolean remove(T item) {
        if (item == null || isEmpty())
            return false;
        for (int i = 0; i < size(); ++i) {
            if (heap.get(i).equals(item)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    public T removeAt(int i) {
        if (isEmpty())
            return null;

        // store item in variable before deleting
        T toRemove = heap.get(i);
        int lastIndex = size() - 1;
        int parent = parentIndex(i);
        T swappedItem = heap.get(lastIndex);

        swap(i, lastIndex);
        heap.remove(lastIndex);

        if (greater(swappedItem, heap.get(parent)))
            bubbleUp(i);
        else
            bubbleDown(i);

        return toRemove;
    }

    /**
     * Utility method for ascertaining that the heap conforms to the max heap
     * invariant
     */
    public boolean isMaxHeap(int k) {
        int root = k;

        // if called from outside the bounds of the heap, return true
        if (k >= size())
            return true;

        // if there are no children, there is no need to recurse further stop here
        if (!hasChildren(root))
            return true;

        int leftChild = leftChildIndex(k);
        int rightChild = rightChildIndex(k);

        T rootNode = heap.get(root);

        // default values account for unavailable nodes
        boolean rightNodeConforms = true;
        boolean leftNodeConforms = true;

        // if children exist, ensure they are greater than their parents
        // also recursively check their children to ensure they conform
        if (leftChild < size()) {
            T rightNode = heap.get(rightChild);
            if (lesser(rootNode, rightNode))
                return false;
            rightNodeConforms = isMaxHeap(rightChild);
        }

        if (rightChild < size()) {
            T leftNode = heap.get(leftChild);
            if (lesser(rootNode, leftNode))
                return false;
            leftNodeConforms = isMaxHeap(leftChild);
        }

        return rightNodeConforms && leftNodeConforms;
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}
