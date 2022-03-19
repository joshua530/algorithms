package com.josh;

/**
 * dynamic array implementation
 */
@SuppressWarnings("unchecked")
public class Array<T> implements Iterable<T> {
    private T[] arr;

    /** the number of items in the array */
    private int len = 0;

    /** how many items the array is capable of storing */
    private int capacity = 0;

    public Array() {
        this(16);
    }

    public Array(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Illegal capacity: " + capacity);

        this.capacity = capacity;
        arr = (T[]) new Object[capacity];
    }

    public int size() {
        return len;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public T get(int index) {
        return arr[index];
    }

    public void set(int index, T element) {
        arr[index] = element;
    }

    public void clear() {
        for (int i = 0; i < arr.length; ++i)
            arr[i] = null;
        len = 0;
    }

    public void add(T item) {
        // check whether array is full and adjust appropriately
        if (len == capacity) {
            if (capacity == 0)
                capacity = 1;
            else
                capacity *= 2;

            T[] tmp = (T[]) new Object[capacity];

            for (int i = 0; i < len; ++i)
                tmp[i] = arr[i];
            arr = tmp;
        }

        arr[len++] = item;
    }

    /**
     * delete object at given index and return it
     * 
     * @param indexToRemove
     * @throws IndexOutOfBoundsException
     */
    public T removeAt(int indexToRemove) {
        if (indexToRemove > capacity - 1 || indexToRemove < 0)
            throw new IndexOutOfBoundsException();

        capacity -= 1;
        T[] newArray = (T[]) new Object[capacity];
        T toRemove = arr[indexToRemove];

        for (int i = 0, j = 0; i < len; ++i, ++j) {
            if (i == indexToRemove)
                --j;
            else
                newArray[j] = arr[i];
        }
        len -= 1;
        arr = newArray;
        return toRemove;
    }

    public boolean remove(T object) {
        int index = indexOf(object);
        if (index != -1) {
            removeAt(index);
            return true;
        }
        return false;
    }

    public int indexOf(T object) {
        for (int i = 0; i < len; ++i) {
            if (arr[i].equals(object)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(T object) {
        return indexOf(object) != -1;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            int index = 0;

            public boolean hasNext() {
                return index < capacity;
            }

            public T next() {
                return arr[index++];
            }
        };
    }

    @Override
    public String toString() {
        if (len == 0)
            return "[]";

        StringBuilder sb = new StringBuilder(len);
        sb.append("[");

        for (int i = 0; i < len - 1; ++i)
            sb.append(arr[i] + ", ");

        sb.append(arr[len - 1] + "]");
        return sb.toString();
    }
}
