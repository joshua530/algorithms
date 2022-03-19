package com.josh;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * This BST does not support duplicate nodes
 */
public class BinarySearchTree<T extends Comparable<T>> {
    private Node root = null;
    private int nodeCount = 0;

    private class Node {
        private T data;
        private Node left, right;

        public Node(Node left, Node right, T data) {
            this.left = left;
            this.right = right;
            this.data = data;
        }
    }

    private enum TreeTraversalOrder {
        PRE_ORDER, IN_ORDER, POST_ORDER, LEVEL_ORDER
    }

    public static void main(String[] args) {
        int[] nums = { 64, 164, 116, 150, 8, 56, 128, 2 };
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        for (int num : nums)
            tree.add(num);

        Iterator<Integer> x = tree.traverse(TreeTraversalOrder.LEVEL_ORDER);

        while (x.hasNext()) {
            System.out.println(x.next());
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return nodeCount;
    }

    /**
     * Add item to tree. Returns true if item was successfully added and false if
     * addition was not successful
     */
    public boolean add(T data) {
        // item will not be added since it already exists
        if (contains(data))
            return false;
        else {
            root = add(root, data);
            ++nodeCount;
            return true;
        }
    }

    /**
     * Attempts to add a child node to a parent node
     */
    private Node add(Node parent, T data) {
        // null node found
        if (parent == null)
            parent = new Node(null, null, data);
        else {
            if (data.compareTo(parent.data) < 0)
                parent.left = add(parent.left, data);
            else
                parent.right = add(parent.right, data);
        }
        return parent;
    }

    /** Remove data from BST if it exists */
    public boolean remove(T data) {
        // ensure item exists before attempting to remove
        if (contains(data)) {
            root = remove(root, data);
            --nodeCount;
            return true;
        }
        return false;
    }

    /** Attempt to remove child node from parent node */
    private Node remove(Node parent, T data) {
        // we've encountered a leaf node, nothing to remove
        if (parent == null)
            return null;

        int cmp = data.compareTo(parent.data);

        if (cmp < 0) // value is lesser than current node, dive into left subtree
            parent.left = remove(parent.left, data);
        else if (cmp > 0)// value greater than current node, dive into right subtree
            parent.right = remove(parent.right, data);
        else { // we've found the node we're looking for
            if (parent.left == null) { // node has only a right subtree or no subtree at all
                Node rightChild = parent.right;
                parent.data = null;
                parent = null;
                return rightChild;
            } else if (parent.right == null) { // node has only a left subtree of no subtree at all
                Node leftChild = parent.left;
                parent.data = null;
                parent = null;
                return leftChild;
            } else { // node has both children. replace it with smallest node in right subtree
                // left most node in right subtree
                Node tmp = digLeft(parent.right);

                parent.data = tmp.data;
                // remove swapped node
                parent.right = remove(parent.right, tmp.data);
            }
        }
        return parent;
    }

    /** Find left most child of node. If node has no left child, it is returned */
    private Node digLeft(Node node) {
        Node current = node;
        while (current.left != null)
            current = node.left;
        return current;
    }

    /** Find right most child of node. If node has no right child, return it */
    // private Node digRight(Node node) {
    // Node current = node;
    // while (current.right != null)
    // current = current.right;
    // return current;
    // }

    /** Check whether tree contains given data */
    public boolean contains(T data) {
        return contains(root, data);
    }

    /** Recursively check whether node or node's subtree contains data */
    private boolean contains(Node node, T data) {
        // bottom of tree reached
        if (node == null)
            return false;

        int cmp = data.compareTo(node.data);

        if (cmp > 0) // data is in right subtree
            return contains(node.right, data);
        else if (cmp < 0) // data is in left subtree
            return contains(node.left, data);
        else // we've found the node
            return true;
    }

    /** Computes the tree's height */
    public int height() {
        return height(root);
    }

    /** Helper method for tree height computation. It computes the height that is */
    private int height(Node node) {
        if (node == null)
            return 0;
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    /** Returns an iterator for each of the four ways of traversing the tree */
    public Iterator<T> traverse(TreeTraversalOrder order) {
        switch (order) {
            case PRE_ORDER:
                return preOrderTraversal();
            case IN_ORDER:
                return inOrderTraversal();
            case POST_ORDER:
                return postOrderTraversal();
            case LEVEL_ORDER:
                return levelOrderTraversal();
            default:
                return null;
        }
    }

    private void ensureNoConcurrentModification(int expectedNodeCount) {
        if (expectedNodeCount != nodeCount)
            throw new ConcurrentModificationException();
    }

    private Iterator<T> preOrderTraversal() {
        final int expectedNodeCount = nodeCount;
        final Stack<Node> stack = new Stack<>();
        stack.push(root);

        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                // tree was modified while it was being traversed
                ensureNoConcurrentModification(expectedNodeCount);
                // root node exists and we haven't finished looping over all the nodes
                return root != null && !stack.isEmpty();
            }

            @Override
            public T next() {
                ensureNoConcurrentModification(expectedNodeCount);
                Node node = stack.pop();
                if (node.right != null)
                    stack.push(node.right);
                if (node.left != null)
                    stack.push(node.left);
                return node.data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private Iterator<T> inOrderTraversal() {
        final int expectedNodeCount = nodeCount;
        final Stack<Node> stack = new Stack<>();
        stack.push(root);

        return new Iterator<T>() {
            Node trav = root;

            @Override
            public boolean hasNext() {
                ensureNoConcurrentModification(expectedNodeCount);
                return root != null && !stack.empty();
            }

            @Override
            public T next() {
                ensureNoConcurrentModification(expectedNodeCount);

                // dig left
                while (trav != null && trav.left != null) {
                    stack.push(trav.left);
                    trav = trav.left;
                }
                Node node = stack.pop();
                // one step to the right
                if (node.right != null) {
                    stack.push(node.right);
                }
                return node.data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private Iterator<T> postOrderTraversal() {
        final int expectedNodeCount = nodeCount;
        final Stack<Node> stack1 = new Stack<>();
        final Stack<Node> stack2 = new Stack<>();
        stack1.push(root);
        while (!stack1.isEmpty()) {
            Node node = stack1.pop();
            if (node != null) {
                stack2.push(node);
                if (node.left != null)
                    stack1.push(node.left);
                if (node.right != null)
                    stack1.push(node.right);
            }
        }

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                ensureNoConcurrentModification(expectedNodeCount);
                return root != null && !stack2.isEmpty();
            }

            @Override
            public T next() {
                ensureNoConcurrentModification(expectedNodeCount);
                return stack2.pop().data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private Iterator<T> levelOrderTraversal() {
        final int expectedNodeCount = nodeCount;
        final Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                ensureNoConcurrentModification(expectedNodeCount);
                return root != null && !queue.isEmpty();
            }

            @Override
            public T next() {
                ensureNoConcurrentModification(expectedNodeCount);
                Node node = queue.poll();
                if (node.left != null)
                    queue.offer(node.left);
                if (node.right != null)
                    queue.offer(node.right);
                return node.data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
