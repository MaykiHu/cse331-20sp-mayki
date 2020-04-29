package graph;

/**
 * This class represents the concept of a node in a graph, capable of containing data.
 */

public class Node {
    private final String data;

    /**
     * Constructs a new Node.
     * @param data the value of the new Node
     * @spec.requires data != null
     * @spec.effects Constructs a new Node = data.
     */
    public Node(String data) {
        throw new RuntimeException("Node constructor is not yet implemented.");
    }

    /**
     * Returns a string representation of this node.  The returned string will be in
     * the form: n'data'    ex: n8
     * @return a String representation of this node.
     */
    @Override
    public String toString() {
        throw new RuntimeException("Node.toString() is not yet implemented.");
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    public int hashCode() {
        throw new RuntimeException("Node.hashCode() is not yet implemented.");
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
        throw new RuntimeException("Node.equals() is not yet implemented.");
    }

    private void checkRep() {
        throw new RuntimeException("checkRep() not yet implemented.");
    }
}
