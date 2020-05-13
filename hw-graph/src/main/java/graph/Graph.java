package graph;

import java.util.Set;

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
     * @return the node that was removed
     */
    Node removeNode(Node node);

    /**
     * Removes the edge from this graph.
     * @param edge the edge to be removed from the graph
     * @spec.effects removes the edge from the graph
     * @return the edge that was removed
     */
    Edge removeEdge(Edge edge);

    /**
     * Returns the set of all nodes in this graph.
     * @return a set; the set of all nodes in this graph
     */
    Set<Node> listNodes();

    /**
     * Returns the set of all edges to children of this parent node
     * @param parentNode the parent node of the children in graph
     * @param includeSelf boolean to include parent as a child, true if considering reflexive edges
     * @return a set; the set of edges associated with this parent node
     */
    Set<Edge> listChildren(Node parentNode, boolean includeSelf);

    /**
     * Returns the size of this graph.
     * @return an int, the size of this graph.
     */
    int size();

    /**
     * Returns the number of edges in this graph.
     * @return an int; the number of edges in this graph
     */
    int getEdgeCount();

    /**
     * Returns if graph is empty.
     * @return a boolean, true if graph is empty.  False otherwise.
     */
    boolean isEmpty();

    /**
     * Returns if graph contains node.
     * @param node the node in question of being in graph
     * @return a boolean, true if graph contains node.  False otherwise.
     */
    boolean containsNode(Node node);
}
