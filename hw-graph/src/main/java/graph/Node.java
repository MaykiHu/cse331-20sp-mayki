package graph;

/**
 * This class represents the concept of a node in a graph, capable of containing data.
 */

public class Node {
    private final String data;

    // Abstraction Function:
    //   A Node n is immutable and solely associated with the value of its data, which is non-null.

    // Representation invariant for every Node n:
    // n.data != null && n.length() > 0
    // In other words,
    //   * n.data is always non-null
    //   * n.data is not an empty string

    /**
     * Constructs a new Node.
     * @param data the value of the new Node
     * @spec.requires data != null
     * @spec.effects Constructs a new Node = data.
     */
    public Node(String data) {
        this.data = data;
        checkRep();
    }

    /**
     * Returns a string representation of this node.  The returned string will be in
     * the form: n'data'    ex: n8
     * @return a String representation of this node.
     */
    @Override
    public String toString() {
        return "n" + data;
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    public int hashCode() {
        return data.hashCode();
    }

    /**
     * Standard equality operation.
     *
     * @param obj the object to be compared for equality
     * @return true if and only if 'obj' is an instance of Node and 'this' and 'obj'
     * represent the same node.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false; // Not a Node
        } // Otherwise, check if nodes equal
        Node n = (Node) obj;
        return data.equals(n.data);
    }

    /*
        Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert (data != null) : "A node's data cannot be null";
        assert (data.length() > 0) : "A node's data cannot be an empty string";
    }
}
