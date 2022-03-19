package com.josh.hashtable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class HashTableQuadraticProbing<K, V> implements Iterable<K> {
    /** items in table / maximum size of table */
    private double loadFactor;
    private int capacity, modificationCount = 0;
    /** loadFactor * capacity */
    private int threshold;
    /**
     * Total number of used buckets in hash table including cells marked as deleted
     */
    private int usedBuckets = 0;
    /** Number of unique keys inside hash table */
    private int keyCount = 0;

    // These two arrays store key-value pairs
    private K[] keyTable;
    private V[] valueTable;

    /** Indicates whether an item was found in the hash table */
    private boolean containsFlag = false;

    /** Used to replace an item immediately after deletion */
    private final K TOMBSTONE = (K) (new Object());

    private final static int DEFAULT_CAPACITY = 8;
    private final static double DEFAULT_LOAD_FACTOR = 0.45;

    public HashTableQuadraticProbing() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableQuadraticProbing(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableQuadraticProbing(int capacity, double loadFactor) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Invalid capacity: " + capacity);
        if (loadFactor <= 0 || Double.isNaN(loadFactor) || Double.isInfinite(loadFactor))
            throw new IllegalArgumentException("Invalid load factor: " + loadFactor);

        this.capacity = Math.max(DEFAULT_CAPACITY, next2Power(capacity));
        this.loadFactor = loadFactor;
        threshold = (int) loadFactor * capacity;

        keyTable = (K[]) new Object[capacity];
        valueTable = (V[]) new Object[capacity];
    }

    /** Find the closest power of 2 above n */
    public int next2Power(int n) {
        return Integer.highestOneBit(n) << 1;
    }

    /** Quadratic probing function P(x)=(x^2+x)/2 */
    private static int P(int n) {
        // get the floor of n/2
        return (n * n + n) >> 1;
    }

    /** Strip index negative sign and add it to the domain [0,capacity] */
    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7fffffff) % capacity;
    }

    /** Empties the hash table */
    public void clear() {
        for (int i = 0; i < capacity; ++i) {
            valueTable[i] = null;
            keyTable[i] = null;
        }
        keyCount = usedBuckets = 0;
        ++modificationCount;
    }

    public int size() {
        return keyCount;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    // insert, put and add pretty much do the same thing
    public V put(K key, V value) {
        return insert(key, value);
    }

    public V add(K key, V value) {
        return insert(key, value);
    }

    /**
     * Inserts a key, value pair into the hash table or updates value if key already
     * exists
     */
    public V insert(K key, V value) {
        if (key == null)
            throw new IllegalArgumentException("Null key");
        if (usedBuckets >= threshold)
            resizeTable();

        final int hash = normalizeIndex(key.hashCode());
        int probeIndex = 0, currentIndex = hash, tombstoneIndex = -1;
        boolean probeCompleted = false;

        while (!probeCompleted) {
            if (tombstoneIndex == -1 && keyTable[currentIndex].equals(TOMBSTONE))
                tombstoneIndex = currentIndex;

            if (keyTable[currentIndex].equals(key)) {
                V oldValue = valueTable[currentIndex];
                if (tombstoneIndex != -1) {
                    keyTable[tombstoneIndex] = key;
                    valueTable[tombstoneIndex] = value;
                    keyTable[currentIndex] = null;
                    valueTable[currentIndex] = null;
                } else
                    valueTable[currentIndex] = value;
                ++modificationCount;
                return oldValue;
            }
            if (keyTable[currentIndex] == null)
                probeCompleted = true;
            else {
                ++probeIndex;
                currentIndex = normalizeIndex(P(probeIndex) + hash);
            }
        }

        if (tombstoneIndex != -1) {
            keyTable[tombstoneIndex] = key;
            valueTable[tombstoneIndex] = value;
        } else {
            keyTable[currentIndex] = key;
            valueTable[currentIndex] = value;
            ++usedBuckets;
        }

        ++keyCount;
        ++modificationCount;
        return null;
    }

    public boolean containsKey(K key) {
        return hasKey(key);
    }

    public boolean hasKey(K key) {
        get(key); // sets the 'contains flag'
        return containsFlag;
    }

    public V get(K key) {
        if (key == null)
            throw new IllegalArgumentException("Null key");

        final int hash = normalizeIndex(key.hashCode());
        int probeIndex = 0, currentIndex = hash, tombstoneIndex = -1;
        boolean foundKey = false;

        K currentKey = keyTable[currentIndex];
        while (!foundKey && currentKey != null) {
            if (tombstoneIndex == -1 && currentKey.equals(TOMBSTONE))
                tombstoneIndex = currentIndex;
            if (currentKey.equals(key))
                foundKey = true;

            ++probeIndex;
            currentIndex = normalizeIndex(hash + P(probeIndex));
            currentKey = keyTable[currentIndex];
        }

        if (!foundKey)
            return null;

        containsFlag = true;
        V foundValue = valueTable[currentIndex];
        if (tombstoneIndex != -1) {
            keyTable[tombstoneIndex] = keyTable[currentIndex];
            valueTable[tombstoneIndex] = foundValue;
            keyTable[currentIndex] = null;
            valueTable[currentIndex] = null;
            ++modificationCount;
        }
        return foundValue;
    }

    public V remove(K key) {
        int hash = normalizeIndex(key.hashCode());
        boolean keyFound = false;
        int probeIndex = 0;
        int index = hash;

        while (!keyFound && keyTable[index] != null) {
            if (keyTable[index].equals(key))
                keyFound = true;
            ++probeIndex;
            index = normalizeIndex(P(probeIndex) + hash);
        }

        if (!keyFound)
            return null;

        V oldValue = valueTable[index];
        keyTable[index] = TOMBSTONE;
        valueTable[index] = null;
        ++modificationCount;
        --keyCount;

        return oldValue;
    }

    public List<K> keys() {
        List<K> theKeys = new ArrayList<>(capacity);
        for (int i = 0; keyTable[i] != null || i < capacity; ++i) {
            if (!keyTable[i].equals(TOMBSTONE))
                theKeys.add(keyTable[i]);
        }
        return theKeys;
    }

    public List<V> values() {
        List<V> theValues = new ArrayList<>(capacity);
        for (int i = 0; keyTable[i] != null || i < capacity; ++i) {
            if (!keyTable[i].equals(TOMBSTONE))
                theValues.add(valueTable[i]);
        }
        return theValues;
    }

    public void resizeTable() {
        capacity *= 2;
        threshold = (int) (loadFactor * capacity);

        K[] tmpKeys = (K[]) new Object[capacity];
        V[] tmpVals = (V[]) new Object[capacity];
        K[] oldKeys = keyTable;
        V[] oldValues = valueTable;
        keyTable = tmpKeys;
        valueTable = tmpVals;

        usedBuckets = 0;

        for (int i = 0; i < oldKeys.length; ++i) {
            if (!oldKeys[i].equals(TOMBSTONE) || oldKeys[i] != null)
                insert(oldKeys[i], oldValues[i]);
            oldKeys[i] = null;
            oldValues[i] = null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        // for ensuring that the hash table is not modified while we are
        // iterating it
        final int MODIFICATION_COUNT = modificationCount;

        return new Iterator<K>() {
            int keysLeft = keyCount, index = 0;

            @Override
            public boolean hasNext() {
                if (MODIFICATION_COUNT != modificationCount)
                    throw new ConcurrentModificationException();
                return keysLeft != 0;
            }

            @Override
            public K next() {
                while (keyTable[index] == null || keyTable[index].equals(TOMBSTONE))
                    ++index;
                --keysLeft;
                return keyTable[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int i = 0; i < keyTable.length; ++i) {
            if (keyTable[i] != null && keyTable[i] != TOMBSTONE)
                sb.append(keyTable[i] + " => " + valueTable[i] + ",");
        }
        sb.append("}");
        return sb.toString();
    }
}
