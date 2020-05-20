package graph.junitTests;

import graph.Node;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public final class NodeTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    // Some simple base Nodes
    private Node<String> negTwo = new Node<>("-2");
    private Node<String> negOne = new Node<>("-1");
    private Node<String> zero = new Node<>("0");
    private Node<String> one = new Node<>("1");
    private Node<String> two = new Node<>("2");
    private Node<String> pointEight = new Node<>("0.8");

    // Varying nodes with data {"-2", "-1", "0", "1", "2", "0.8"}
    private Node[] nodes = new Node[]{negTwo, negOne, zero, one, two, pointEight};

    // We will test the equals method below, these are modeled off of past jUnit tests seen
    /**
     * This test check is equals is reflexive. In other words that x.equals(x) is always true.
     */
    @Test
    public void testEqualsReflexive() {
        for(int i = 0; i < nodes.length; i++) {
            assertEquals(nodes[i], nodes[i]);
        }
    }

    @Test
    public void testEquals() {
        // Simple cases
        assertEquals(negOne, negOne);
        assertEquals(zero, zero);
        assertEquals(one, one);

        // Check with new node creations of same data
        assertEquals(pointEight, new Node<String>("0.8"));
        assertEquals(new Node<String>("2"), new Node<String>("2"));

        // Simple cases for checking false positives
        assertNotEquals(negTwo, negOne);
        assertNotEquals(negOne, negTwo);
        assertNotEquals(negOne, zero);
        assertNotEquals(zero, negOne);

        // Check that equals does not neglect sign
        assertNotEquals(one, negOne);
        assertNotEquals(negOne, one);
    }

    /**
     * Tests that the hashCodes() for same objects are the same, and different if not
     */
    @Test
    public void testHashCode() {
        // Same objects
        assertEquals(negOne.hashCode(), negOne.hashCode());
        assertEquals(negOne.hashCode(), new Node<String>("-1").hashCode());

        // Different objects
        assertNotEquals(zero.hashCode(), two.hashCode());
        assertNotEquals(two.hashCode(), negTwo.hashCode());
        assertNotEquals(pointEight.hashCode(), new Node<String>("8").hashCode());
    }
}
