package graph.junitTests;

import graph.DirectedGraph;
import graph.Edge;
import graph.Graph;
import graph.Node;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;

public class DirectedGraphTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    // Some simple base Edges and Nodes
    private Node<String> negOne = new Node<String>("-1");
    private Node<String> zero = new Node<String>("0");
    private Node<String> one = new Node<String>("1");
    private Node<String> two = new Node<String>("2");
    private Edge<String> neg_one_1 = new Edge<String>(negOne, one, "1");
    private Edge<String> neg_one_0 = new Edge<String>(negOne, one, "0");
    private Edge<String> neg_zero_1 = new Edge<String>(negOne, zero, "1");
    private Edge<String> neg_zero_0 = new Edge<String>(negOne, zero, "0");
    private Edge<String> zero_neg_0 = new Edge<String>(zero, negOne, "0");
    private Edge<String> zero_neg_1 = new Edge<String>(zero, negOne, "1");

    // Varying nodes with data {}
    private  Node[] nodes = new Node[] {negOne, zero, one};

    // Graph that has nodes and edges listed above
    private DirectedGraph directedGraph = new DirectedGraph();

    /**
     * Tests and methods will use this set up graph that only has nodes 0, 1, -1
     */
    @Before
    public void populateGraph() {
        for (int i = 0; i < nodes.length; i++) {
            directedGraph.addNode(nodes[i]);
        }
    }

    /**
     * Tests adding a duplicate node; the graph should remain the same.
     */
    @Test
    public void testAddDupNode() {
        String beforeNodes = directedGraph.toString();
        directedGraph.addNode(negOne); // negOne is already in the graph
        assertEquals(beforeNodes, directedGraph.toString());
    }

    /**
     * Tests adding a duplicate edge; the graph should remain unchanged.
     */
    @Test
    public void testAddDupEdge() {
        directedGraph.addEdge(neg_one_0);
        String beforeGraph = directedGraph.toString(); // Has one edge in graph
        directedGraph.addEdge(neg_one_0); // Tries re-adding same edge
        assertEquals(beforeGraph, directedGraph.toString());
    }

    /**
     * Tests adding an edge that only has the start node in the graph.
     * Graph should not be changed.
     */
    @Test
    public void testAddEdgeOnlyStartNode() {
        Edge<String> hasStart = new Edge<String>(zero, two, "1"); // only has starting node
        String beforeGraph = directedGraph.toString();

        directedGraph.addEdge(hasStart);
        assertEquals(beforeGraph, directedGraph.toString());
    }

    /**
     * Tests adding an edge that only has the end node in the graph.
     * Graph should not be changed.
     */
    @Test
    public void testAddEdgeOnlyEndNode() {
        Edge<String> hasEnd = new Edge<String>(two, zero, "0"); // only has ending node
        String beforeGraph = directedGraph.toString();

        directedGraph.addEdge(hasEnd);
        assertEquals(beforeGraph, directedGraph.toString());
    }

    /**
     * Tests adding an edge that has neither start or end nodes on the graph.
     * Graph should remain unchanged.
     */
    @Test
    public void testAddNonExistentEdge() {
        Edge<String> noEdge = new Edge<String>(two, two, "0"); // only has ending node
        String beforeGraph = directedGraph.toString();

        directedGraph.addEdge(noEdge);
        assertEquals(beforeGraph, directedGraph.toString());
    }

    /**
     * Tests removing a node that is not in graph.  Graph is unchanged.
     */
    public void testRemoveNonExistentNode() {
        String beforeGraph = directedGraph.toString();
        directedGraph.removeNode(two); // Graph does not have a node equivalent to two
        assertEquals(beforeGraph, directedGraph.toString());
    }

    /**
     * Tests removing an edge that only has the start node.  Graph should be unchanged.
     */
    @Test
    public void testRemoveEdgeOnlyStartNode() {
        Edge<String> hasStart = new Edge<String>(zero, two, "1"); // only has starting node
        String beforeGraph = directedGraph.toString();

        directedGraph.removeEdge(hasStart);
        assertEquals(beforeGraph, directedGraph.toString());
    }

    /**
     * Tests removing an edge that only has the end node in the graph.
     * Graph should not be changed.
     */
    @Test
    public void testRemoveEdgeOnlyEndNode() {
        Edge<String> hasEnd = new Edge<String>(two, zero, "0"); // only has ending node
        String beforeGraph = directedGraph.toString();

        directedGraph.removeEdge(hasEnd);
        assertEquals(beforeGraph, directedGraph.toString());
    }

    /**
     * Tests removing an edge that has neither start or end nodes on the graph.
     * Graph should remain unchanged.
     */
    @Test
    public void testRemovingNonExistentEdge() {
        Edge<String> noEdge = new Edge<String>(two, two, "0"); // only has ending node
        String beforeGraph = directedGraph.toString();

        directedGraph.addEdge(noEdge);
        assertEquals(beforeGraph, directedGraph.toString());
    }
}
