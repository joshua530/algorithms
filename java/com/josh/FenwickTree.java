package com.josh;

/**
 * A data structure that supports sum range queries as well as setting values in
 * a static array and getting the value of the prefix sum up to some index
 * efficiently
 */
public class FenwickTree {
    /** Contains the range sums */
    private long[] tree;

    public FenwickTree(int size) {
        tree = new long[size + 1];
    }

    public FenwickTree(long[] values) {
        if (values == null)
            throw new IllegalArgumentException("Values array cannot be null");

        tree = values.clone();

        for (int i = 0; i < values.length; ++i) {
            int j = i + lsb(i);

            if (j < values.length)
                tree[j] += tree[i];
        }
    }

    /** Fetches value of the least significant bit */
    private int lsb(int i) {
        return i & -i;
        // or return Integer.lowestOneBit(i);
        // bit manipulation makes program run faster
    }

    public long prefixSum(int i) {
        long sum = 0;
        while (i > 0) {
            sum += tree[i];
            i &= ~lsb(i); // or i -= lsb(i)
        }
        return sum;
    }

    /** Find the sum of a range */
    public long sum(int i, int j) {
        if (i > j)
            throw new IllegalArgumentException("Ensure j >= i");

        return prefixSum(j) - prefixSum(i - 1);
    }

    /** Add a value to a certain index. (one based) */
    public void add(int index, long toAdd) {
        while (index < tree.length) {
            tree[index] += toAdd;
            index += lsb(index);
        }
    }

    /** Set the value at index to k */
    public void set(int index, int k) {
        long value = sum(index, index);
        add(index, k - value);
    }

    @Override
    public String toString() {
        return java.util.Arrays.toString(tree);
    }
}
