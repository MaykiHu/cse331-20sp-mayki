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
    private Node negOne = new Node("-1");
    private Node zero = new Node("0");
    private Node one = new Node("1");
    private Node two = new Node("2");
    private Edge neg_one_1 = new Edge(negOne, one, "1");
    private Edge neg_one_0 = new Edge(negOne, one, "0");
    private Edge neg_zero_1 = new Edge(negOne, zero, "1");
    private Edge neg_zero_0 = new Edge(negOne, zero, "0");
    private Edge zero_neg_0 = new Edge(zero, negOne, "0");
    private Edge zero_neg_1 = new Edge(zero, negOne, "1");

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
        String beforeNodes = directedGraph.listNodes();
        directedGraph.addNode(negOne); // negOne is already in the graph
        assertEquals(beforeNodes, directedGraph.listNodes());
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
        Edge hasStart = new Edge(zero, two, "1"); // only has starting node
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
        Edge hasEnd = new Edge(two, zero, "0"); // only has ending node
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
        Edge noEdge = new Edge(two, two, "0"); // only has ending node
        String beforeGraph = directedGraph.toString();

        directedGraph.addEdge(noEdge);
        assertEquals(beforeGraph, directedGraph.toString());
    }

    /**
     * Tests removing a node that is not in graph.  Graph is unchanged.
     */
    public void testRemoveNonExistentNode() {
        String beforeGraph = directedGraph.listNodes();
        directedGraph.removeNode(two); // Graph does not have a node equivalent to two
        assertEquals(beforeGraph, directedGraph.listNodes());
    }

    /**
     * Tests removing an edge that only has the start node.  Graph should be unchanged.
     */
    @Test
    public void testRemoveEdgeOnlyStartNode() {
        Edge hasStart = new Edge(zero, two, "1"); // only has starting node
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
        Edge hasEnd = new Edge(two, zero, "0"); // only has ending node
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
        Edge noEdge = new Edge(two, two, "0"); // only has ending node
        String beforeGraph = directedGraph.toString();

        directedGraph.addEdge(noEdge);
        assertEquals(beforeGraph, directedGraph.toString());
    }
}
