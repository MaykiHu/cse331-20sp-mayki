package graph;

/**
 * An interface of the concept of a graph, which all types of graphs must follow.
 * Graphs have the ability to have Nodes and Edges which can be modified, can be empty
 * and have a certain size.
 */

public interface Graph {
    /**
     * Adds a node to the graph.
     * @param node the node to be added to the graph.
     * @spec.effects adds the node to the graph
     */
    void addNode(Node node);

    /**
     * Adds the edge to the graph.
     * @param edge the edge to be added to the graph.
     * @spec.effects adds the edge to the graph.
     */
    void addEdge(Edge edge);

    /**
     * Removes the node from the graph.
     * @param node the node to be removed from the graph.
     * @spec.effects removes the node from the graph.
     */
    void removeNode(Node node);

    /**
     * Removes the edge from this graph.
     * @param edge the edge to be removed from the graph
     * @spec.effects removes the edge from the graph
     */
    void removeEdge(Edge edge);

    /**
     * Returns the string representation of the nodes in this graph.
     * @return the String representation of the Nodes in this graph.
     */
    String getNodes();

    /**
     * Returns the size of this graph.
     * @return an int, the size of this graph.
     */
    int size();

    /**
     * Returns if graph is empty.
     * @return a boolean, true if graph is empty.  False otherwise.
     */
    boolean isEmpty();
}
