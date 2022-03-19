package com.josh;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Formatter;

public class BinarySearchTree2<T extends Comparable<T>> {

    private Node root;
    private int nodeCount = 0;

    private class Node {
        private T data;
        private Node left, right;

        public Node(Node left, Node right, T data) {
            this.left = left;
            this.right = right;
            this.data = data;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private enum TreeTraversalOrder {
        PRE_ORDER, IN_ORDER, POST_ORDER, LEVEL_ORDER
    }

    public static void main(String[] args) {
        BinarySearchTree2<Integer> tree = new BinarySearchTree2<>();
        Integer[] items = { 64, 116, 150, 8, 56, 57, 128, 2, 112, 152 };
        for (int i : items)
            tree.add(i);
        tree.add(59);
        tree.add(57);
        tree.add(60);

        int[] items2 = { 160, 180, 165, 170, 166, 162, 172 };
        for (int i : items2)
            tree.add(i);

        // tree.print(TreeTraversalOrder.LEVEL_ORDER);
        // System.out.println(tree.getTreeString());
        // System.out.println();

        BinarySearchTree2<Integer> tree2 = new BinarySearchTree2<>();
        int[] items3 = { 64, 10, 20, 9, 7, 22, 25, 24, 6, 5, 65, 19 };
        for (int i : items3)
            tree2.add(i);

        // System.out.println(tree.getTreeString());
        // System.out.println(tree2.getTreeString());
        System.out.println(tree.getNodeString(112));
    }

    /** Check whether given data exists in the tree */
    public boolean exists(T data) {
        return exists(root, data);
    }

    /** Helper method for checking for existance of data against a node */
    private boolean exists(Node current, T data) {
        if (current == null) // we've reached the bottom of the tree. node doesn't exist
            return false;
        else if (lesser(data, current.data)) // move down left sub tree
            return exists(current.left, data);
        else if (greater(data, current.data)) // move down right sub tree
            return exists(current.right, data);
        else // current node contains data we are looking for
            return true;
    }

    /** Check whether a > b */
    private boolean greater(T a, T b) {
        return a.compareTo(b) > 0;
    }

    /** Check whether a < b */
    private boolean lesser(T a, T b) {
        return a.compareTo(b) < 0;
    }

    public int size() {
        return nodeCount;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean add(T data) {
        if (data == null)
            throw new IllegalArgumentException("Null elements not allowed");

        if (exists(data))
            return false;

        root = add(root, data);
        ++nodeCount;
        return true;
    }

    /** Helper method for adding node to tree */
    private Node add(Node current, T data) {
        if (current == null) // node doesn't exist, create a leaf node
            return new Node(null, null, data);
        else if (greater(data, current.data))
            current.right = add(current.right, data);
        else if (lesser(data, current.data))
            current.left = add(current.left, data);
        return current;
    }

    public boolean remove(T element) {
        if (element == null)
            throw new IllegalArgumentException("Null elements not allowed");

        if (!exists(element))
            return false;

        root = remove(root, element);
        --nodeCount;
        return true;
    }

    /**
     * Helper remove method
     * 
     * It recursively searches the current node and its children for the given data.
     * If the data is found, the node in which it is found is replaced with:
     * <ul>
     * <li>Nothing if it has no children</li>
     * <li>Its child if it has only one child</li>
     * <li>The least element in its right subtree if it has two children</li>
     * </ul>
     */
    private Node remove(Node current, T data) {
        if (greater(data, current.data)) // node is in right subtree
            current.right = remove(current.right, data);
        else if (lesser(data, current.data)) // node is in left subtree
            current.left = remove(current.left, data);
        else { // we've found the node
            if (hasNoChild(current))
                current = null;
            else if (hasOnlyOneChild(current)) {
                if (current.right == null)
                    current = current.left;
                else
                    current = current.right;
            } else {
                Node tmp = maxNode(current); // fetch node we'll use to replace the found node
                current.data = tmp.data;
                // delete node used for replacement
                current.right = remove(current.right, tmp.data);
            }
        }
        return current;
    }

    private boolean hasOnlyOneChild(Node n) {
        return n.left == null && n.right != null || n.right == null && n.left != null;
    }

    private boolean hasTwoChildren(Node n) {
        return n.left != null && n.right != null;
    }

    private boolean hasNoChild(Node n) {
        return n == null || n.left == null && n.right == null;
    }

    /** Fetches biggest node using node as the parent */
    private Node maxNode(Node node) {
        Node least = node.right;
        // check child's left subtree recursively
        while (least.left != null)
            least = least.left;
        return least;
    }

    /** Fetches least node using node as the parent */
    // private Node minNode(Node node) {
    // Node current = node;
    // while (current.right != null)
    // current = current.right;
    // return current;
    // }

    /** Calculate total height of the BST [O(n)] */
    public int height() {
        return height(root);
    }

    /** Calculate tree height relative to a given node [O(n)] */
    public int height(Node n) {
        if (hasNoChild(n))
            return 0;

        int leftHeight = height(n.left);
        int rightHeight = height(n.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

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

    // private void print(TreeTraversalOrder t) {
    // Iterator<T> iter = traverse(t);

    // while (iter.hasNext())
    // System.out.print(iter.next() + " ");
    // System.out.println();
    // }

    private void ensureNoConcurrentTreeModification(int expectedNodeCount) {
        if (expectedNodeCount != nodeCount)
            throw new ConcurrentModificationException("Tree was modified before traversal completed");
    }

    public Iterator<T> preOrderTraversal() {
        int expectedNodeCount = nodeCount;
        Node current = root;
        Stack<Node> stack = new Stack<>();
        stack.push(current);

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                ensureNoConcurrentTreeModification(expectedNodeCount);
                return root != null && !stack.isEmpty();
            }

            public T next() {
                ensureNoConcurrentTreeModification(expectedNodeCount);
                Node current = stack.pop();
                if (current.right != null)
                    stack.push(current.right);
                if (current.left != null) {
                    stack.push(current.left);
                    return current.data;
                }
                return current.data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public Iterator<T> inOrderTraversal() {
        Stack<Node> stack = new Stack<>();
        stack.push(root);

        int expectedNodeCount = nodeCount;

        return new Iterator<T>() {
            Node lastTraversed = root;

            @Override
            public boolean hasNext() {
                ensureNoConcurrentTreeModification(expectedNodeCount);
                return root != null && !stack.isEmpty();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public T next() {
                ensureNoConcurrentTreeModification(expectedNodeCount);
                // add left child nodes recursively
                while (lastTraversed != null) {
                    if (lastTraversed.left != null)
                        stack.push(lastTraversed.left);
                    lastTraversed = lastTraversed.left;
                }

                Node current = stack.pop();
                if (current.right != null)
                    stack.push(current.right);

                return current.data;
            }
        };
    }

    public Iterator<T> postOrderTraversal() {
        Stack<Node> stack = new Stack<>();
        Stack<Node> forTraversal = new Stack<>();
        int expectedNodeCount = nodeCount;
        stack.add(root);

        // loop over all nodes recursively while adding the node's children
        // to a stack that will be looped over
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            forTraversal.push(node);

            if (node.left != null)
                stack.push(node.left);
            if (node.right != null)
                stack.push(node.right);
        }

        return new Iterator<T>() {
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasNext() {
                ensureNoConcurrentTreeModification(expectedNodeCount);
                return root != null && !forTraversal.isEmpty();
            }

            @Override
            public T next() {
                ensureNoConcurrentTreeModification(expectedNodeCount);
                return forTraversal.pop().data;
            }
        };
    }

    public Iterator<T> levelOrderTraversal() {
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        int expectedNodeCount = nodeCount;

        return new Iterator<T>() {
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean hasNext() {
                ensureNoConcurrentTreeModification(expectedNodeCount);
                return root != null && !queue.isEmpty();
            }

            @Override
            public T next() {
                ensureNoConcurrentTreeModification(expectedNodeCount);
                Node current = queue.poll();
                if (current.left != null)
                    queue.offer(current.left);
                if (current.right != null)
                    queue.offer(current.right);
                return current.data;
            }
        };
    }

    /** Constructs the full tree representation */
    public String getTreeString() {
        if (root == null)
            return "";

        return getNodeString(root);
    }

    /**
     * Construct tree from a given node downward. If node does not exist, an empty
     * string is returned
     */
    public String getNodeString(T node) {
        Node theNode = find(node);

        if (theNode == null)
            return "";

        return getNodeString(theNode);
    }

    private Node find(T node) {
        return find(root, node);
    }

    private Node find(Node current, T toFind) {
        if (current == null)
            return null;
        else if (lesser(toFind, current.data))
            return find(current.left, toFind);
        else if (greater(toFind, current.data))
            return find(current.right, toFind);
        else
            return current;
    }

    /** Constructs a tree from a given node downwards */
    private String getNodeString(Node node) {
        if (node == null)
            return "";

        String tree = node.toString();

        boolean isFirstChild = false;
        String rightChildPointer = "└───";
        String leftChildPointer = "└───";

        if (hasTwoChildren(node)) {
            isFirstChild = true;
            leftChildPointer = "├───";
        }

        tree += getNodeString(node.left, "", leftChildPointer, isFirstChild);
        tree += getNodeString(node.right, "", rightChildPointer, false);
        return tree;
    }

    /**
     * Fetches the string representation for a node and its children recursively
     * 
     * Uses the pre order looping method for binary search trees
     * 
     * @param isFirstChild This parameter will be true if the current node's parent
     *                     has two children with the current node being the first
     *                     child to be looped through.
     */
    private String getNodeString(Node node, String parentPadding, String pointer, boolean isFirstChild) {
        Formatter formatter = new Formatter();
        formatter.format("%2s", node);
        String nodeValue = formatter.toString();
        formatter.close();

        if (node == null)
            return "";

        if (hasNoChild(node))
            return "\n" + parentPadding + pointer + nodeValue;

        String nodeString = "";
        String padding;

        nodeString += "\n";

        // prevent pipe character from appearing before left child in tree in case the
        // current node has two children
        // Also ensures that all the left child's children will have the pipe character
        // before them but all the right child's children will not
        if (isFirstChild) {
            nodeString += parentPadding + pointer + nodeValue;
            padding = parentPadding + "|" + "    "; // this padding will be used by the left child's children

        } else {
            nodeString += parentPadding + pointer + nodeValue;
            padding = parentPadding + "     "; // this padding will be used by the right child's children
        }

        if (hasTwoChildren(node)) {
            // recursively ensure the condition in the above if statement is
            // met
            nodeString += getNodeString(node.left, padding, "├───", true);
            nodeString += getNodeString(node.right, padding, "└───", false);
        } else {
            nodeString += getNodeString(node.left, padding, "└───", false);
            nodeString += getNodeString(node.right, padding, "└───", false);
        }

        return nodeString;
    }
}
