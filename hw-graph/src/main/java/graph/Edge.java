package graph;

/**
 * This class represents the concept of an edge in a graph, which may be labelled.
 */

public class Edge {
    private final Node startNode;
    private final Node endNode;
    private final String label;

    /**
     * Constructs a new Edge.
     * @param startNode the node at the beginning of the edge
     * @param endNode the node at the end of the edge
     * @param label the value associating these two nodes on this edge
     * @spec.requires startNode != null and endNode != null
     * @spec.effects Constructs a new Edge = (startNode, endNode, label).
     */
    public Edge(Node startNode, Node endNode, String label) {
        throw new RuntimeException("Edge constructor is not yet implemented.");
    }

    /**
     * Returns the starting node of this edge.
     * @return a Node, that is the starting node of this edge
     */
    public Node getStart() {
        throw new RuntimeException("Edge.getStart() is not yet implemented.");
    }

    /**
     * Returns the ending node of this edge.
     * @return a Node, that is the ending node of the edge
     */
    public Node getEnd() {
        throw new RuntimeException("Edge.getEnd() is not yet implemented.");
    }

    /**
     * Returns a String representation of this edge's label.
     * @return a String of the label associated with this edge
     */
    public String getLabel() {
        throw new RuntimeException("Edge.getLabel() is not yet implemented.");
    }

    /**
     * Returns a String representation of this edge.  The returned string will be in
     * the form: e'label' from 'startNode' to 'endNode'     ex: e8 from n9 to n10
     * @return a String representation of this edge.
     */
    @Override
    public String toString() {
        throw new RuntimeException("Edge.toString() is not yet implemented.");
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    public int hashCode() {
        throw new RuntimeException("Edge.hashCode() is not yet implemented.");
    }

    /**
     * Standard equality operation.
     *
     * @param obj the object to be compared for equality
     * @return true if and only if 'obj' is an instance of Edge and 'this' and 'obj'
     * represent the same edge.
     */
    @Override
    public boolean equals(Object obj) {
        throw new RuntimeException("Edge.equals() is not yet implemented.");
    }

    private void checkRep() {
        throw new RuntimeException("checkRep() is not yet implemented.");
    }
}
