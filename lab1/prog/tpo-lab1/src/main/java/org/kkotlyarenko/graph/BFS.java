package org.kkotlyarenko.graph;

import java.util.*;
public class BFS {
    public static class BFSResult {
        private final List<Integer> visitOrder;
        private final Map<Integer, Integer> distances;
        private final Map<Integer, Integer> parents;

        public BFSResult(List<Integer> visitOrder, Map<Integer, Integer> distances, Map<Integer, Integer> parents) {
            this.visitOrder = Collections.unmodifiableList(visitOrder);
            this.distances = Collections.unmodifiableMap(distances);
            this.parents = Collections.unmodifiableMap(parents);
        }

        public List<Integer> getVisitOrder() {
            return visitOrder;
        }

        public Map<Integer, Integer> getDistances() {
            return distances;
        }

        public Map<Integer, Integer> getParents() {
            return parents;
        }
    }

    public static BFSResult bfs(Graph graph, int startVertex) {
        int n = graph.getVertexCount();
        if (startVertex < 0 || startVertex >= n) {
            throw new IllegalArgumentException("Start vertex is out of range");
        }

        boolean[] visited = new boolean[n];
        List<Integer> visitOrder = new ArrayList<>();
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> parents = new HashMap<>();

        Queue<Integer> queue = new LinkedList<>();

        visited[startVertex] = true;
        queue.add(startVertex);
        distances.put(startVertex, 0);
        parents.put(startVertex, -1);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            visitOrder.add(current);

            for (int neighbor : graph.getNeighbors(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                    distances.put(neighbor, distances.get(current) + 1);
                    parents.put(neighbor, current);
                }
            }
        }

        return new BFSResult(visitOrder, distances, parents);
    }
}
