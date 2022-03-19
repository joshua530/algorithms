package com.josh;

public class DoublyLinkedList<T> implements Iterable<T> {
    private int size = 0;
    private Node<T> head = null;
    private Node<T> tail = null;

    /**
     * internal node class that represents data
     */
    private class Node<B> {
        private B data;
        private Node<B> previous;
        private Node<B> next;

        public Node(B data, Node<B> next, Node<B> previous) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    /**
     * deletes all the nodes and their references
     */
    public void clear() {
        Node<T> current = head;

        while (current != null) {
            Node<T> tmp = current;
            current = current.next;
            tmp.next = null;
            tmp.previous = null;
            tmp.data = null;
        }
        head = tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * alias of addLast
     * 
     * @param data
     */
    public void add(T data) {
        addLast(data);
    }

    public void addFirst(T data) {
        if (isEmpty())
            head = tail = new Node<T>(data, null, null);
        else {
            Node<T> newNode = new Node<T>(data, head, null);
            head.previous = newNode;
            head = newNode;
        }
        ++size;
    }

    /**
     * append data to the end of the linked list
     * 
     * @param data
     */
    public void addLast(T data) {
        if (isEmpty())
            head = tail = new Node<T>(data, null, null);
        else {
            Node<T> newNode = new Node<T>(data, null, tail);
            tail.next = newNode;
            tail = newNode;
        }
        ++size;
    }

    /**
     * runtime O(1)
     */
    public T peekFirst() {
        ensureContainsItems();
        return head.data;
    }

    public T peekLast() {
        ensureContainsItems();
        return tail.data;
    }

    public T removeFirst() {
        ensureContainsItems();

        T data = head.data;
        head = head.next;

        if (isEmpty())
            tail = null;
        else
            head.previous = null;

        --size;

        return data;
    }

    public T removeLast() {
        ensureContainsItems();

        T data = tail.data;
        tail = tail.previous;
        --size;

        if (isEmpty())
            head = null;
        else
            tail.next = null;

        return data;
    }

    private void ensureContainsItems() {
        if (isEmpty())
            throw new RuntimeException("Empty list");
    }

    private T remove(Node<T> node) {
        if (node.previous == null)
            return removeFirst();
        if (node.next == null)
            return removeLast();

        node.previous.next = node.next;
        node.next.previous = node.previous;

        T data = node.data;
        node.next = null;
        node.previous = null;
        node.data = null;
        --size;

        return data;
    }

    public T removeAt(int index) {
        if (index > size - 1 || index < 0)
            throw new IllegalArgumentException();

        int i;
        Node<T> trav;

        if (index < size / 2) {
            for (i = 0, trav = head; i != index; ++i)
                trav = trav.next;
        } else {
            for (i = size - 1, trav = tail; i != index; --i)
                trav = trav.previous;
        }

        return remove(trav);
    }

    /**
     * removes nodes containing data that matches the passed data
     * 
     * @param data
     * @return
     */
    public boolean remove(Object data) {
        Node<T> trav;

        for (trav = head; trav != null; trav = trav.next) {
            if (data == null && trav.data == null) {
                remove(trav);
                return true;
            } else if (trav.data.equals(data)) {
                remove(trav);
                return true;
            }
        }
        return false;
    }

    public int indexOf(Object obj) {
        Node<T> trav;
        int index;

        for (trav = head, index = 0; trav != null; trav = trav.next, ++index) {
            if (obj == null && trav.data == null)
                return index;
            else if (trav.data.equals(obj))
                return index;
        }

        return -1;
    }

    public boolean contains(Object data) {
        return indexOf(data) != -1;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            private Node<T> trav = head;

            @Override
            public boolean hasNext() {
                return trav.next != null;
            }

            @Override
            public T next() {
                T data = trav.data;
                trav = trav.next;
                return data;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(size);
        sb.append("[");

        for (Node<T> iter = head; !iter.equals(tail); iter = iter.next)
            sb.append(iter.data.toString() + ", ");

        sb.append(tail.data + "]");
        return sb.toString();
    }
}
