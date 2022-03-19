package com.josh.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class Dijkstra {
    /**
     * Returns empty array if path is not found, else an array with the found is
     * returned
     */
    public static String[] shortestPath(Graph g, String start, String end) {
        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Integer> distances = new HashMap<>();
        for (String node : g.edges.keySet()) {
            visited.put(node, false);
            distances.put(node, Integer.MAX_VALUE);
        }

        ArrayList<ArrayList<String>> paths = new ArrayList<>(); // paths already found
        ArrayList<String> tmp = new ArrayList<>(); // add initial node to paths list
        tmp.add(start);
        paths.add(tmp);
        distances.put(start, 0);

        int dist; // least cumulative distance so far
        ArrayList<String> shortest = null;
        String shortestPathNode = null;
        while (!paths.isEmpty()) {
            // System.out.println(paths.toString());
            dist = Integer.MAX_VALUE;
            for (ArrayList<String> path : paths) { // get shortest path covered so far
                String lastPathNode = path.get(path.size() - 1); // node at the end of shortest path
                if (distances.get(lastPathNode) < dist) {
                    shortest = path;
                    shortestPathNode = lastPathNode;
                    dist = distances.get(lastPathNode);
                }
                // System.out.println("Path=" + path.toString() + ";Distance=" + distances.get(lastPathNode));
            }
            // System.out.println("Pick: " + shortest.toString());
            // System.out.println("End node: " + shortestPathNode);
            // System.out.println();

            for (String neighbour : g.getNeighbours(shortestPathNode)) {
                if (!visited.get(neighbour)) {
                    int d = g.edgeWeights.get(shortestPathNode + neighbour); // edge weight between current node and
                                                                             // neighbour
                    if (d < distances.get(neighbour)) {
                        distances.put(neighbour, dist + d);
                        ArrayList<String> tmpPath = (ArrayList<String>) shortest.clone(); // create new path with
                                                                                        // neighbour's updated distance
                        tmpPath.add(neighbour);
                        paths.add(tmpPath);

                        if (neighbour.equals(end)) {
                            String[] n = new String[tmpPath.size()];
                            for (int i = 0; i < tmpPath.size(); ++i)
                                n[i] = tmpPath.get(i);
                            // System.out.println("Distance="+(dist+d));
                            return n;
                        }
                    }
                }
            }
            // we're done with node, delete this path since we've overwritten it, there are
            // no neighbouring nodes
            // or the nodes neighbouring it have already been visited
            paths.remove(shortest);
            visited.put(shortestPathNode, true);
        }

        return new String[0];
    }
}

class Graph {
    // [node => [n,n2,n3], node2 => [n,n2],...]
    HashMap<String, ArrayList<String>> edges = new HashMap<>();
    // [node_n => weight, node_n2 => weight,...]
    HashMap<String, Integer> edgeWeights = new HashMap<>();

    public void addEdge(String start, String end, int weight) {
        ArrayList<String> startNeighbours = new ArrayList<>();
        ArrayList<String> endNeighbours = new ArrayList<>();
        if (edges.containsKey(start))
            startNeighbours = edges.get(start);
        if (edges.containsKey(end))
            endNeighbours = edges.get(end);

        startNeighbours.add(end);
        endNeighbours.add(start);
        edges.put(start, startNeighbours);
        edges.put(end, endNeighbours);

        edgeWeights.put(start + end, weight);
        edgeWeights.put(end + start, weight);
    }

    public String[] getNeighbours(String node) {
        if (!edges.containsKey(node) || node == null) {
            return new String[0];
        }

        ArrayList<String> neighbours = edges.get(node);
        String[] n = new String[neighbours.size()];
        for (int i = 0; i < neighbours.size(); ++i)
            n[i] = neighbours.get(i);
        return n;
    }

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addEdge("a", "b", 14);
        g.addEdge("a", "c", 9);
        g.addEdge("a", "d", 7);
        g.addEdge("b", "e", 9);
        g.addEdge("b", "c", 2);
        g.addEdge("c", "d", 10);
        g.addEdge("c", "f", 11);
        g.addEdge("d", "f", 15);
        g.addEdge("e", "f", 6);

        System.out.println(Arrays.toString(Dijkstra.shortestPath(g, "a", "e")));

        // System.out.println(Arrays.toString(g.getNeighbours("a")));
        // System.out.println(Arrays.toString(g.getNeighbours("b")));
        // System.out.println(Arrays.toString(g.getNeighbours("c")));
        // System.out.println(Arrays.toString(g.getNeighbours("d")));
        // System.out.println(Arrays.toString(g.getNeighbours("e")));
        // System.out.println(Arrays.toString(g.getNeighbours("f")));
        // System.out.println(Arrays.toString(g.getNeighbours("g")));
        // System.out.println(Arrays.toString(g.getNeighbours("h")));
        // System.out.println(Arrays.toString(g.getNeighbours("i")));
    }
}
