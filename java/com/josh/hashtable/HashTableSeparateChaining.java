package com.josh.hashtable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class Entry<K, V> {
    K key;
    V value;
    int hash;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
        this.hash = key.hashCode();
    }

    public boolean equals(Entry<K, V> other) {
        if (hash != other.hash)
            return false;
        return value == other.value;
    }

    @Override
    public String toString() {
        return key + "=>" + value;
    }
}

@SuppressWarnings("unchecked")
public class HashTableSeparateChaining<K, V> implements Iterable<K> {
    private static final int DEFAULT_CAPACITY = 3;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    /** Once maximum load factor exceeds this value, resize the table */
    private double maxLoadFactor;

    /** Maximum number of items that can be stored in hashtable */
    private int capacity;

    /**
     * [Threshold = capacity * maxLoadFactor] -> tells us when to resize the
     * hashtable
     */
    private int threshold;

    /** Number of items stored in the hashtable */
    private int size = 0;

    /** Fixed length array of linked lists to represent the hashtable */
    private LinkedList<Entry<K, V>>[] table;

    public HashTableSeparateChaining() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity, double maxLoadFactor) {
        if (capacity < 0)
            throw new IllegalArgumentException("Illegal capacity");
        if (maxLoadFactor <= 0 || Double.isInfinite(maxLoadFactor) || Double.isNaN(maxLoadFactor))
            throw new IllegalArgumentException("Illegal maxLoadFactor");
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = Math.max(capacity, DEFAULT_CAPACITY);
        threshold = (int) (this.capacity * this.maxLoadFactor);
        table = new LinkedList[this.capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * The hashing method
     * 
     * Converts hash value to an index
     * 
     * It strips the negative sign and places the hash value in the domain<br>
     * [0, capacity]
     */
    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7fffffff) % capacity;
    }

    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    public boolean containsKey(K key) {
        return hasKey(key);
    }

    public boolean hasKey(K key) {
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketSeekEntry(bucketIndex, key) != null;
    }

    public V put(K key, V value) {
        return insert(key, value);
    }

    public V add(K key, V value) {
        return insert(key, value);
    }

    public V insert(K key, V value) {
        if (key == null)
            throw new IllegalArgumentException("Null key not allowed");
        Entry<K, V> newEntry = new Entry<>(key, value);
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketInsertEntry(bucketIndex, newEntry);
    }

    public V get(K key) {
        if (key == null)
            return null;

        int bucketIndex = normalizeIndex(key.hashCode());
        Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);
        if (entry != null)
            return entry.value;
        return null;
    }

    public V remove(K key) {
        if (key == null)
            return null;
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketRemoveEntry(bucketIndex, key);
    }

    public V bucketRemoveEntry(int bucketIndex, K key) {
        Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);

        if (entry != null) {
            LinkedList<Entry<K, V>> list = table[bucketIndex];
            list.remove(entry);
            --size;
            return entry.value;
        } else
            return null;
    }

    /**
     * Attempt to insert a new entry into the hashtable.
     * 
     * If the entry exists, the value of the existing entry is replaced with that of
     * the new entry and then returned. If the entry does not exist, it is inserted
     * into the hash table and nothing is returned
     */
    private V bucketInsertEntry(int bucketIndex, Entry<K, V> entry) {
        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        if (bucket == null)
            table[bucketIndex] = bucket = new LinkedList<>();

        Entry<K, V> existent = bucketSeekEntry(bucketIndex, entry.key);
        if (existent == null) {
            bucket.add(entry);
            if (++size > threshold)
                resize();
            return null;
        } else {
            V oldValue = existent.value;
            existent.value = entry.value;
            return oldValue;
        }
    }

    private Entry<K, V> bucketSeekEntry(int bucketIndex, K key) {
        if (key == null)
            return null;

        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        if (bucket == null)
            return null;

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key))
                return entry;
        }
        return null;
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * maxLoadFactor);
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[capacity];

        // copy all items in old hashtable to new hashtable
        for (int i = 0; i < table.length; ++i) {
            if (table[i] != null) {
                for (Entry<K, V> entry : table[i]) {
                    int bucketIndex = normalizeIndex(entry.hash);
                    LinkedList<Entry<K, V>> newBucket = newTable[bucketIndex];
                    if (newBucket == null)
                        newTable[i] = newBucket = new LinkedList<>();
                    newBucket.add(entry);
                }
                // remove old data
                table[i].clear();
                table[i] = null;
            }
        }
        table = newTable;
    }

    public List<K> keys() {
        List<K> keys = new ArrayList<>(size());
        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket)
                    keys.add(entry.key);
            }
        }
        return keys;
    }

    public List<V> values() {
        List<V> values = new ArrayList<>(size());
        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket)
                    values.add(entry.value);
            }
        }
        return values;
    }

    @Override
    public java.util.Iterator<K> iterator() {
        final int expectedElements = size();

        return new java.util.Iterator<K>() {
            int currentIndex = 0;
            java.util.Iterator<Entry<K, V>> bucketIterator = (table[0] == null) ? null : table[0].iterator();

            @Override
            public boolean hasNext() {
                if (expectedElements != size())
                    throw new ConcurrentModificationException();

                // we've reached end of iterator or iterator is null
                if (bucketIterator == null || !bucketIterator.hasNext()) {
                    while (++currentIndex < capacity) {
                        if (table[currentIndex] != null) {
                            Iterator<Entry<K, V>> tmp = table[currentIndex].iterator();
                            if (tmp.hasNext()) {
                                bucketIterator = tmp;
                                break;
                            }
                        }
                    }
                }
                return currentIndex < capacity;
            }

            @Override
            public K next() {
                return bucketIterator.next().key;
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

        for (int i = 0; i < table.length; ++i) {
            if (table[i] != null) {
                for (Entry<K, V> entry : table[i])
                    sb.append(entry + ",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
