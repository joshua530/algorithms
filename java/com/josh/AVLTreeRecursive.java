package com.josh;

import java.util.ConcurrentModificationException;

import com.josh.TreePrinter.PrintableNode;

/** An AVL tree is an implementation of the balanced binary search tree */
public class AVLTreeRecursive<T extends Comparable<T>> implements Iterable<T> {
    class Node implements TreePrinter.PrintableNode {
        /** Legal balance factor values are -1,0 and 1 only */
        int balanceFactor = 0;
        T value;
        int height = 0;
        Node left = null, right = null;

        public Node(T value) {
            this.value = value;
        }

        @Override
        public PrintableNode getLeft() {
            return left;
        }

        @Override
        public PrintableNode getRight() {
            return right;
        }

        @Override
        public String getText() {
            return String.valueOf(value);
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private Node root;
    private int nodeCount = 0;

    public int height() {
        if (root == null)
            return 0;
        return root.height;
    }

    public int size() {
        return nodeCount;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void display() {
        System.out.println(TreePrinter.getTreeDisplay(root));
    }

    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(Node node, T value) {
        if (node == null)
            return false;

        if (lesser(value, node.value))
            return contains(node.left, value);
        else if (greater(value, node.value))
            return contains(node.right, value);
        return true;
    }

    public boolean insert(T value) {
        if (value == null)
            return false;
        if (!contains(root, value)) {

            // insert value and update nodes recursively
            root = insert(root, value);
            ++nodeCount;
            return true;
        }
        return false;
    }

    private Node insert(Node n, T value) {
        if (n == null)
            return new Node(value);

        if (greater(value, n.value))
            n.right = insert(n.right, value);
        else if (lesser(value, n.value))
            n.left = insert(n.left, value);

        n = update(n);
        n = balance(n);
        return n;
    }

    /** Updates the height and balance factor of the node */
    private Node update(Node n) {
        int leftHeight = -1;
        int rightHeight = -1;

        if (n.left != null)
            leftHeight = n.left.height;
        if (n.right != null)
            rightHeight = n.right.height;

        n.balanceFactor = rightHeight - leftHeight;
        n.height = 1 + Math.max(leftHeight, rightHeight);
        return n;
    }

    /** Provides self balancing functionality */
    private Node balance(Node n) {
        if (n.balanceFactor > 1) { // right heavy
            if (n.right.balanceFactor < 0)
                n = rightLeftCase(n);
            else
                n = rightRightCase(n);
        } else if (n.balanceFactor < -1) { // left heavy
            if (n.left.balanceFactor > 0)
                n = leftRightCase(n);
            else
                n = leftLeftCase(n);
        }
        // balance factor bf: -1 <= bf <= 1, no need to handle since tree is balanced
        return n;
    }

    private Node rightRightCase(Node n) {
        return leftRotate(n);
    }

    private Node rightLeftCase(Node n) {
        n.right = rightRotate(n.right);
        return rightRightCase(n);
    }

    private Node leftLeftCase(Node n) {
        return rightRotate(n);
    }

    private Node leftRightCase(Node n) {
        n.left = leftRotate(n.left);
        return leftLeftCase(n);
    }

    private Node leftRotate(Node node) {
        Node newParent = node.right;
        node.right = newParent.left;
        newParent.left = node;
        node = update(node);
        newParent = update(newParent);
        return newParent;
    }

    private Node rightRotate(Node node) {
        Node newParent = node.left;
        node.left = newParent.right;
        newParent.right = node;
        node = update(node);
        newParent = update(newParent);
        return newParent;
    }

    /** Check if a is greater than b. Returns true if a > b, false otherwise */
    private boolean greater(T a, T b) {
        return a.compareTo(b) > 0;
    }

    /**
     * Check if a is lesser than or equal to b. Returns true if a < b, false
     * otherwise
     */
    private boolean lesser(T a, T b) {
        return a.compareTo(b) < 0;
    }

    public boolean remove(T node) {
        if (node == null)
            return false;
        if (contains(root, node)) {
            root = remove(root, node);
            --nodeCount;
            return true;
        }
        return false;
    }

    private Node remove(Node node, T data) {
        if (lesser(data, node.value)) {
            node.left = remove(node.left, data);
        } else if (greater(data, node.value)) {
            node.right = remove(node.right, data);
        } else {
            // node has only one child or no children at all
            if (node.right == null)
                return node.left;
            if (node.left == null)
                return node.right;

            // node has two children
            Node replacement;
            // delete element from subtree which has a higher number of elements
            if (node.left.height > node.right.height) {
                replacement = maxNode(node.left);
                node.left = remove(node.left, replacement.value);
            } else {
                replacement = minNode(node.right);
                node.right = remove(node.right, replacement.value);
            }

            node.value = replacement.value;

            replacement = null; // get rid of temporary object
        }

        update(node);
        balance(node);
        return node;
    }

    /** Fetches maximum node using n as a starting point */
    private Node maxNode(Node n) {
        if (n.right == null)
            return n;
        return maxNode(n.right);
    }

    /** Fetches minimum node using n as a starting point */
    private Node minNode(Node n) {
        if (n.left == null)
            return n;
        return minNode(n.left);
    }

    /** In order traversal of the BBST */
    public java.util.Iterator<T> iterator() {
        final int expectedNodeCount = nodeCount;
        Stack<Node> stack = new Stack<>();
        stack.push(root);

        return new java.util.Iterator<T>() {
            Node current = root;

            @Override
            public T next() {
                while (current != null && current.left != null) {
                    stack.push(current.left);
                    current = current.left;
                }
                Node tmp = stack.pop();
                if (tmp.right != null) {
                    stack.push(tmp.right);
                    current = tmp.right;
                }
                return tmp.value;
            }

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != nodeCount)
                    throw new ConcurrentModificationException();
                return !stack.isEmpty() && root != null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public boolean validateBSTInvariant(Node node) {
        if (node == null)
            return true;

        boolean invariantIsSatisfied = true;

        if (node.left != null)
            if (greater(node.left.value, node.value))
                invariantIsSatisfied = false;

        if (node.right != null)
            if (lesser(node.right.value, node.value))
                invariantIsSatisfied = false;

        return invariantIsSatisfied && validateBSTInvariant(node.left) && validateBSTInvariant(node.right);
    }

    public static void main(String[] args) {
        AVLTreeRecursive<Integer> tree = new AVLTreeRecursive<>();
        for (int i = 0; i < 22; ++i)
            tree.insert((int) (Math.random() * 100));

        tree.display();
    }
}
