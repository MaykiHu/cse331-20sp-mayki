package graph;

/**
 * This class represents the concept of an edge in a graph, which may be labelled.
 */

public class Edge<NodeType, EdgeType> {
    private final Node<NodeType> startNode;
    private final Node<NodeType> endNode;
    private final EdgeType label;

    // Abstraction Function:
    //   An Edge e is immutable and contains two non-null nodes, a startNode and endNode,
    //   which indicates the edges direction, and may have a label associating these two nodes.
    //   This is a visual representation: startNode --(label)--> endNode
    //   Note that the label is optional, indicated by ( ).
    //   If no label, label is null.  Otherwise, label contains a value.

    // Representation invariant for every Edge e:
    // (e.startNode != null && e.endNode != null) &&
    // In other words,
    //   * e.startNode and e.endNode are always non-null
    //   * e.label is non-null

    /**
     * Constructs a new Edge.
     * @param startNode the node at the beginning of the edge
     * @param endNode the node at the end of the edge
     * @param label the value associating these two nodes on this edge
     * @spec.requires startNode != null and endNode != null
     * @spec.effects Constructs a new Edge = (startNode, endNode, label).
     */
    public Edge(Node<NodeType> startNode, Node<NodeType> endNode, EdgeType label) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.label = label;
        checkRep();
    }

    /**
     * Returns the starting node of this edge.
     * @return a Node, that is the starting node of this edge
     */
    public Node<NodeType> getStart() {
        return startNode; // Since Node is immutable, returning the node is fine
    }

    /**
     * Returns the ending node of this edge.
     * @return a Node, that is the ending node of the edge
     */
    public Node<NodeType> getEnd() {
        return endNode; // Since Node is immutable, returning the node is fine
    }

    /**
     * Returns the type representation of this edge's label.
     * @return the label associated with this edge
     */
    public EdgeType getLabel() {
        return label;
    }

    /**
     * Returns a String representation of this edge.  The returned string will be in
     * the form: e'label' from 'startNode' to 'endNode'     ex: e8 from n9 to n10
     * @return a String representation of this edge.
     */
    @Override
    public String toString() {
        return "e" + label.toString() + " from " + startNode.toString() + " to " + endNode.toString();
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    public int hashCode() {
        return startNode.hashCode() + endNode.hashCode() + label.hashCode();
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
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Edge<?, ?> e = (Edge<?, ?>) obj;
        return startNode.equals(e.startNode) && endNode.equals(e.endNode) && label.equals(e.label);
    }

    /*
        Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert (startNode != null) : "startNode cannot be null";
        assert (endNode != null) : "endNode cannot be null";
        assert (label != null) : "label cannot be null";
    }
}
