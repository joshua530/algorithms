package com.josh.graph;

public class DirectedGraph {
    final int MAXV = 1000;
    int degree[];
    int numVertices;
    int numEdges;
    boolean directed;
    private EdgeNode edges[];

    protected class EdgeNode {
        int adjacent;
        int weight;
        EdgeNode next;
    }

    public void prim(int start) {
        EdgeNode e; // temporary pointer
        boolean inTree[] = new boolean[MAXV]; // indicates whether node belongs to minimum
                                              // spanning tree
        int distance[] = new int[MAXV]; // cost of adding a non tree edge to the tree
        int parent[] = new int[MAXV]; // parents of nodes
        int currentVertex;
        int nextVertex;
        int weight;
        int bestDistance;

        for (int i = 0; i < numVertices; ++i) {
            inTree[i] = false;
            distance[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        distance[start] = 0;
        currentVertex = start;

        while (inTree[currentVertex] == false) {
            inTree[currentVertex] = true;
            e = edges[currentVertex];

            while (e != null) {
                //TODO adjacent, next in edgenode
                nextVertex = e.adjacent;
                weight = e.weight;
                if (distance[nextVertex] > weight && inTree[nextVertex] == false) {
                    distance[nextVertex] = weight;
                    parent[nextVertex] = currentVertex;
                }
                e = e.next;
            }
            currentVertex = 0;
            bestDistance = Integer.MAX_VALUE;
            for (int i = 0; i < numVertices; ++i)
                if (inTree[i] == false && bestDistance > distance[i]) {
                    bestDistance = distance[i];
                    currentVertex = i;
                }
        }
    }
}
