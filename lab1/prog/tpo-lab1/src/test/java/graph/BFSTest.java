package graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kkotlyarenko.graph.BFS;
import org.kkotlyarenko.graph.BFS.BFSResult;
import org.kkotlyarenko.graph.Graph;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {
    @Test
    @DisplayName("Linear graph: 0-1-2-3, start from 0")
    void testLinearGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        BFSResult result = BFS.bfs(g, 0);

        assertEquals(List.of(0, 1, 2, 3), result.getVisitOrder());
        assertEquals(Map.of(0, 0, 1, 1, 2, 2, 3, 3), result.getDistances());
        assertEquals(Map.of(0, -1, 1, 0, 2, 1, 3, 2), result.getParents());
    }

    @Test
    @DisplayName("Diamond graph: 0-1, 0-2, 1-3, 2-3, start from 0")
    void testDiamondGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);

        BFSResult result = BFS.bfs(g, 0);

        assertEquals(List.of(0, 1, 2, 3), result.getVisitOrder());
        assertEquals(0, result.getDistances().get(0));
        assertEquals(1, result.getDistances().get(1));
        assertEquals(1, result.getDistances().get(2));
        assertEquals(2, result.getDistances().get(3));
    }

    @Test
    @DisplayName("Star graph with center 0 and leaves 1-4")
    void testStarGraph() {
        Graph g = new Graph(5);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 3);
        g.addEdge(0, 4);

        BFSResult result = BFS.bfs(g, 0);

        assertEquals(0, result.getVisitOrder().get(0));
        assertEquals(5, result.getVisitOrder().size());
        for (int i = 1; i <= 4; i++) {
            assertEquals(1, result.getDistances().get(i));
            assertEquals(0, result.getParents().get(i));
        }
    }

    @Test
    @DisplayName("Disconnected graph: single component traversal")
    void testDisconnectedGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(2, 3);

        BFSResult result = BFS.bfs(g, 0);

        assertEquals(List.of(0, 1), result.getVisitOrder());
        assertFalse(result.getDistances().containsKey(2));
        assertFalse(result.getDistances().containsKey(3));
    }

    @Test
    @DisplayName("Single vertex, no edges")
    void testSingleVertex() {
        Graph g = new Graph(1);

        BFSResult result = BFS.bfs(g, 0);

        assertEquals(List.of(0), result.getVisitOrder());
        assertEquals(0, result.getDistances().get(0));
        assertEquals(-1, result.getParents().get(0));
    }

    @Test
    @DisplayName("Complete graph K4, start from 0")
    void testCompleteGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 3);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);

        BFSResult result = BFS.bfs(g, 0);

        assertEquals(List.of(0, 1, 2, 3), result.getVisitOrder());
        for (int i = 1; i <= 3; i++) {
            assertEquals(1, result.getDistances().get(i));
        }
    }

    @Test
    @DisplayName("Linear graph, start from the middle")
    void testLinearGraphFromMiddle() {
        Graph g = new Graph(5);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 4);

        BFSResult result = BFS.bfs(g, 2);

        assertEquals(2, result.getVisitOrder().get(0));
        assertEquals(5, result.getVisitOrder().size());
        assertEquals(0, result.getDistances().get(2));
        assertEquals(1, result.getDistances().get(1));
        assertEquals(1, result.getDistances().get(3));
        assertEquals(2, result.getDistances().get(0));
        assertEquals(2, result.getDistances().get(4));
    }

    @Test
    @DisplayName("Binary tree, start from root")
    void testBinaryTree() {
        Graph g = new Graph(6);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 5);

        BFSResult result = BFS.bfs(g, 0);

        assertEquals(List.of(0, 1, 2, 3, 4, 5), result.getVisitOrder());
        assertEquals(0, result.getDistances().get(0));
        assertEquals(1, result.getDistances().get(1));
        assertEquals(1, result.getDistances().get(2));
        assertEquals(2, result.getDistances().get(3));
        assertEquals(2, result.getDistances().get(4));
        assertEquals(2, result.getDistances().get(5));
    }

    @Test
    @DisplayName("Cycle of 4 vertices, start from 0")
    void testCycleGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 0);

        BFSResult result = BFS.bfs(g, 0);

        assertEquals(0, result.getVisitOrder().get(0));
        assertEquals(4, result.getVisitOrder().size());
        assertEquals(0, result.getDistances().get(0));
        assertEquals(1, result.getDistances().get(1));
        assertEquals(1, result.getDistances().get(3));
        assertEquals(2, result.getDistances().get(2));
    }

    @Test
    @DisplayName("Invalid start vertex (negative)")
    void testInvalidStartNegative() {
        Graph g = new Graph(3);
        assertThrows(IllegalArgumentException.class, () -> BFS.bfs(g, -1));
    }

    @Test
    @DisplayName("Invalid start vertex (>= n)")
    void testInvalidStartTooLarge() {
        Graph g = new Graph(3);
        assertThrows(IllegalArgumentException.class, () -> BFS.bfs(g, 3));
    }

    @Test
    @DisplayName("Graph creation with invalid vertex count")
    void testInvalidGraphCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Graph(0));
        assertThrows(IllegalArgumentException.class, () -> new Graph(-5));
    }

    @Test
    @DisplayName("Edge creation with invalid vertex")
    void testInvalidEdge() {
        Graph g = new Graph(3);
        assertThrows(IllegalArgumentException.class, () -> g.addEdge(0, 5));
        assertThrows(IllegalArgumentException.class, () -> g.addEdge(-1, 0));
    }

    @Test
    @DisplayName("getNeighbors for invalid vertex")
    void testGetNeighborsInvalid() {
        Graph g = new Graph(3);
        assertThrows(IllegalArgumentException.class, () -> g.getNeighbors(-1));
        assertThrows(IllegalArgumentException.class, () -> g.getNeighbors(3));
    }

    @Test
    @DisplayName("getVertexCount returns expected value")
    void testGetVertexCount() {
        Graph g = new Graph(7);
        assertEquals(7, g.getVertexCount());
    }
}
