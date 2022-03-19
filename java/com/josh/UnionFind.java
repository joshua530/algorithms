package com.josh;

import java.util.Arrays;

/**
 * Union find - a data structure that keeps track of elements split into one or
 * more disjointed lists.
 * 
 * Has 2 primary operations:
 * <ul>
 * <li>Find -> gets the group that a node belongs to</li>
 * <li>Union -> unifies two nodes. ie adds them to the same group</li>
 * </ul>
 */
public class UnionFind {
    /**
     * Contains parents of nodes. If parents[i]=i, then i is a root node
     */
    private int[] parents;

    /** Contains sizes of the group/component/set that each component belongs to */
    private int[] groupSizes;

    /** Total number of nodes in Union find/Disjoint set */
    private int size;

    /** Total number of components in union set */
    private int numGroups;

    public static void main(String[] args) {
        UnionFind u = new UnionFind(7);
        System.out.println(u);
        u.unify(0, 2);
        u.unify(3, 1);
        u.unify(4, 5);
        System.out.println(u);
        u.unify(5, 6);
        System.out.println(u);
        u.unify(1, 4);
        System.out.println(u);
        u.unify(2, 4);
        System.out.println(u);
    }

    public UnionFind(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("The size of the union should be at least 1");

        this.size = numGroups = size;
        groupSizes = new int[size];
        parents = new int[size];

        // fill the nodes and parents
        for (int i = 0; i < size; ++i) {
            parents[i] = i; // each item is its own root node at first
            groupSizes[i] = 1; // no other nodes are attached to a node at first
        }
    }

    /** Find which component/group/set 'node' belongs to */
    public int find(int node) {
        int root = parents[node];

        // recursively look for the root node for the node
        while (parents[root] != root)
            root = parents[root];

        // compress the path leading to the root
        // move up the tree and repeatedly set the root node for each of the
        // nodes encountered till the final root node is reached
        int previousChild = node;
        while (previousChild != root) {
            int tmp = previousChild;
            previousChild = parents[previousChild];
            parents[tmp] = root;
        }
        return root;
    }

    /** Check whether node and node2 belong to the same set/group/component */
    public boolean connected(int node, int node2) {
        return find(node) == find(node2);
    }

    /** Fetch the size of the component that node belongs to */
    public int componentSize(int node) {
        return groupSizes[find(node)];
    }

    public int size() {
        return size;
    }

    /** Fetch total number of components in the union set */
    public int componentNum() {
        return numGroups;
    }

    /** Make node and node2 belong to the same component */
    public void unify(int node, int node2) {
        int nodeComponent = find(node);
        int node2Component = find(node2);

        if (nodeComponent == node2Component)
            return;

        // find smaller component and add it to bigger component
        // to save on linear time
        int smallerComponent = componentSize(nodeComponent) < componentSize(node2Component) ? nodeComponent
                : node2Component;
        int largerComponent = componentSize(nodeComponent) >= componentSize(node2Component) ? nodeComponent
                : node2Component;

        // move all nodes in smaller component to larger component
        for (int i = 0; i < parents.length; ++i) {
            if (parents[i] == smallerComponent) {
                int nodeInComponent = i; // the node whose component is being changed
                parents[nodeInComponent] = largerComponent;
                // increment number of nodes in larger component since one extra item has
                // been
                // added to it and decrement smaller component because one of its nodes has
                // been
                // added to the larger component
                ++groupSizes[largerComponent];
                --groupSizes[smallerComponent];
            }
        }
        --numGroups;
    }

    // public void unify(int p, int q) {
    // int root1 = find(p);
    // int root2 = find(q);

    // if (root1 == root2)
    // return;

    // if (componentSize(root1) < componentSize(root2)) {
    // parents[root1] = root2;
    // groupSizes[root2] += groupSizes[root1];
    // } else {
    // parents[root2] = root1;
    // groupSizes[root1] += groupSizes[root2];
    // }

    // --numGroups;
    // }

    @Override
    public String toString() {
        String parentsString = Arrays.toString(parents);
        String groupsString = Arrays.toString(groupSizes);
        String groupSizesString = Arrays.toString(groupSizes);
        String groupNumberString = Integer.toString(numGroups);
        String finalString = String.format("parents=%s\ngroups=%s\ngroup sizes=%s\ngroup number=%s\n", parentsString,
                groupsString, groupSizesString, groupNumberString);
        return finalString;
    }
}
