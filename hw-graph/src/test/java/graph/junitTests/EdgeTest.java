package graph.junitTests;

import graph.Edge;
import graph.Node;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EdgeTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    // Some simple base Edges and Nodes
    private Node negOne = new Node("-1");
    private Node zero = new Node("0");
    private Node one = new Node("1");
    private Edge neg_one_1 = new Edge(negOne, one, "1");
    private Edge neg_one_0 = new Edge(negOne, one, "0");
    private Edge neg_zero_1 = new Edge(negOne, zero, "1");
    private Edge neg_zero_0 = new Edge(negOne, zero, "0");
    private Edge zero_neg_0 = new Edge(zero, negOne, "0");
    private Edge zero_neg_1 = new Edge(zero, negOne, "1");

    // Varying edges with data {}
    private Edge[] edges = new Edge[]
            {neg_one_1, neg_one_0, neg_zero_1, neg_zero_0, zero_neg_0, zero_neg_1};

    // We will test the equals method below, these are modeled off of past jUnit tests seen
    /**
     * This test check is equals is reflexive. In other words that x.equals(x) is always true.
     */
    @Test
    public void testEqualsReflexive() {
        for(int i = 0; i < edges.length; i++) {
            assertEquals(edges[i], edges[i]);
        }
    }

    @Test
    public void testEquals() {
        // Simple cases
        assertEquals(neg_one_0, neg_one_0);
        assertEquals(neg_zero_0, neg_zero_0);
        assertEquals(zero_neg_0, zero_neg_0);

        // Check with new node creations of same data
        assertEquals(neg_zero_1, new Edge(negOne, zero, "1"));
        assertEquals(new Edge(zero, negOne, "1"), new Edge(zero, negOne, "1"));

        // Simple cases for checking false positives
        assertNotEquals(neg_zero_1, zero_neg_1);
        assertNotEquals(zero_neg_0, neg_zero_1);

        // Check that equals does not neglect label
        assertNotEquals(neg_zero_1, neg_zero_0);
        assertNotEquals(zero_neg_0, zero_neg_1);
    }

    /**
     * Tests that the startNode returned from getStart() is the same startNode of the edge.
     */
    @Test
    public void testGetStartNode() {
        // Tests same starting node for two edges branching from same start
        assertEquals(neg_zero_0.getStart(), negOne);
        assertEquals(neg_zero_0.getStart(), neg_zero_1.getStart());
        assertEquals(neg_zero_1.getStart(), new Node("-1"));

        // Tests different starting nodes
        assertNotEquals(zero_neg_1.getStart(), negOne);
        assertNotEquals(zero_neg_1, new Node("-1"));
        assertNotEquals(neg_zero_0.getStart(), zero_neg_0.getStart());
    }

    /**
     * Tests that the endNode returned from getEnd() is the same endNode of the edge.
     */
    public void testGetEndNode() {
        // Tests same end node for two edges branching from same end
        assertEquals(neg_zero_0.getEnd(), zero);
        assertEquals(neg_zero_1.getEnd(), new Node("0"));
        assertNotEquals(neg_zero_1.getEnd(), neg_zero_0.getEnd());

        // Tests different ending nodes
        assertNotEquals(zero_neg_1.getEnd(), one);
        assertNotEquals(neg_zero_0.getEnd(), new Node("1"));
        assertNotEquals(zero_neg_1.getEnd(), neg_zero_0.getEnd());
    }

    /**
     * Tests that the hashCodes() for same objects are the same, and different if not
     */
    @Test
    public void testHashCode() {
        // Same objects
        assertEquals(zero_neg_0.hashCode(), zero_neg_0.hashCode());
        assertEquals(neg_one_0.hashCode(), new Edge(negOne, one, "0").hashCode());

        // Different objects
        assertNotEquals(zero_neg_1.hashCode(), zero_neg_0.hashCode()); // different label
        assertNotEquals(neg_one_0.hashCode(), zero_neg_0.hashCode());
        assertNotEquals(neg_zero_1.hashCode(), new Edge(zero, negOne, "1").hashCode());
    }
}
