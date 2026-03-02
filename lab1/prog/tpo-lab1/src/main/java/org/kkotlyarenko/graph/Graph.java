package org.kkotlyarenko.graph;

import java.util.*;
public class Graph {

    private final int vertexCount;
    private final List<List<Integer>> adjacencyList;

    public Graph(int vertexCount) {
        if (vertexCount <= 0) {
            throw new IllegalArgumentException("Vertex count must be positive");
        }
        this.vertexCount = vertexCount;
        this.adjacencyList = new ArrayList<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v) {
        validateVertex(u);
        validateVertex(v);
        adjacencyList.get(u).add(v);
        adjacencyList.get(v).add(u);
    }

    public List<Integer> getNeighbors(int v) {
        validateVertex(v);
        return Collections.unmodifiableList(adjacencyList.get(v));
    }

    public int getVertexCount() {
        return vertexCount;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= vertexCount) {
            throw new IllegalArgumentException("Vertex " + v + " is out of range [0, " + (vertexCount - 1) + "]");
        }
    }
}
