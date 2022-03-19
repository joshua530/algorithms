package com.josh.graph.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Node states
 * 
 * <ul>
 * <li>Undiscovered - not yet visited</li>
 * <li>Discovered - visited but not all neighbours have been visited</li>
 * <li>Processed - vertex as well as all its neighbours have been visited</li>
 * </li>
 */
public class Graph {
    int NUM_VERTICES;
    ArrayList<ArrayList<Integer>> graph;

    /** Indicates whether graph edges are directed or undirected */
    boolean directed = true;

    public Graph(int size) {
        NUM_VERTICES = size;
        graph = new ArrayList<>(size);

        for (int i = 0; i < NUM_VERTICES; ++i) {
            graph.add(new ArrayList<Integer>());
        }
    }

    public void addEdge(int source, int destination) {
        // add two edges since it is an undirected graph
        graph.get(source).add(destination);
        graph.get(destination).add(source);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NUM_VERTICES; ++i) {
            sb.append(String.format("Node %s:", i));

            for (int j : graph.get(i)) {
                sb.append(String.format(" %s ", j));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Breadth first search implementation
     * 
     * There are three ways of processing vertices:
     * 
     * <ul>
     * <li>early - before traversing node's neighbours</li>
     * <li>normally - during the processing of a node's neighbour</li>
     * <li>late - after processing a node's neighbours</li>
     * </ul>
     */
    public void bfs(int start) {
        graph.get(start); // throw IndexOutOfBounds exception if invalid node is given

        boolean[] discovered = new boolean[NUM_VERTICES];
        boolean[] processed = new boolean[NUM_VERTICES];
        Queue<Integer> q = new LinkedList<>();

        q.offer(start);

        int numEdges = 0;
        int current;
        while (!q.isEmpty()) {
            current = q.poll();
            processed[current] = true;
            discovered[current] = true;
            processVertexEarly(current);

            for (int i : graph.get(current)) {
                if (!processed[i] || !directed) {
                    processEdge(current, i);
                    ++numEdges;
                }
                if (discovered[i] == false) {
                    q.offer(i);
                    discovered[i] = true;
                }
            }

            processVertexLate(current);
            System.out.println();
        }

        System.out.printf("The number of edges is %d\n", numEdges);
    }

    private void processVertexLate(int current) {
        System.out.printf("Processed %d late\n", current);
    }

    private void processEdge(int edge, int successor) {
        System.out.printf("Processed edge (%d,%d)\n", edge, successor);
    }

    private void processVertexEarly(int current) {
        System.out.printf("Processed vertex %d early\n", current);
    }

    /**
     * Find whether there is a path between any two nodes
     * 
     * This algorithm uses bfs
     * 
     * Note that BFS gives the shortest possible path only when the graph is
     * unweighted or the vertices are of equal weight
     * 
     * # sloppy algorithm though # :)
     */
    public void findPath(int source, int destination) {
        int[] parents = new int[NUM_VERTICES];
        Queue<Integer> q = new LinkedList<>();
        q.offer(source);

        boolean[] visited = new boolean[NUM_VERTICES];

        int current;
        parents[source] = source;
        boolean pathFound = false;

        while (!q.isEmpty()) {
            current = q.poll();

            if (current == destination) {
                pathFound = true;
                break;
            }

            visited[current] = true;
            // traverse node's neighbours
            for (int i : graph.get(current)) {
                // System.out.printf("Current=%d, neighbour=%d\n", current, i);
                // System.out.println(Arrays.toString(parents));
                if (!visited[i]) {
                    q.offer(i);
                    visited[i] = true;
                    parents[i] = current;
                }
            }
        }

        if (pathFound) {
            // traverse parents in reverse order to get the path we're looking for
            StringBuilder sb = new StringBuilder();
            int tmp = destination;

            ArrayList<Integer> path = new ArrayList<>();
            while (tmp != source) {
                path.add(tmp);
                tmp = parents[tmp];
            }
            path.add(source);

            String prefix = "";
            for (int i = path.size() - 1; i >= 0; --i) {
                sb.append(prefix + path.get(i));
                prefix = "->";
            }

            System.out.printf("Path between %d and %d is: ", source, destination);
            System.out.println(sb.toString());
        } else
            System.out.printf("Path from %d to %d does not exist\n", source, destination);
    }

    /**
     * Depth first search implementation
     */
    public int dfs(int start) {
        ensureNodeExists(start);
        boolean visited[] = new boolean[NUM_VERTICES];
        int time = dfs(start, visited);
        return time;
    }

    /** @throws IndexOutOfBoundsException */
    private void ensureNodeExists(int start) {
        graph.get(start);
    }

    private int dfs(int start, boolean[] visited) {
        int time = 1;

        visited[start] = true;

        processVertexEarly(start);
        for (int i : graph.get(start)) {
            if (visited[i] != true) {
                visited[i] = true;
                processEdge(start, i);
                time += dfs(i, visited);
            }
        }
        processVertexLate(start);
        time += 1;
        return time;
    }

    public static void main(String[] args) {
        Graph g = new Graph(7);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 4);
        g.addEdge(1, 3);
        g.addEdge(2, 4);
        g.addEdge(3, 5);
        g.addEdge(4, 5);
        g.addEdge(5, 6);

        // System.out.println(g);
        // g.bfs(-1);
        // g.findPath(4, 0);
        g.dfs(0);
    }
}
