package graph;

import java.util.Set;

/**
 * An interface of the concept of a graph, which all types of graphs must follow.
 * Graphs have the ability to have Nodes and Edges which can be modified, can be empty
 * and have a certain size.
 */

public interface Graph<NodeType, EdgeType> {
    /**
     * Adds a node to the graph.
     * @param node the node to be added to the graph.
     * @spec.effects adds the node to the graph
     */
    void addNode(Node<NodeType> node);

    /**
     * Adds the edge to the graph.
     * @param edge the edge to be added to the graph.
     * @spec.effects adds the edge to the graph.
     */
    void addEdge(Edge<NodeType, EdgeType> edge);

    /**
     * Removes the node from the graph.
     * @param node the node to be removed from the graph.
     * @spec.effects removes the node from the graph.
     * @return the node that was removed
     */
    Node<NodeType> removeNode(Node<NodeType> node);

    /**
     * Removes the edge from this graph.
     * @param edge the edge to be removed from the graph
     * @spec.effects removes the edge from the graph
     * @return the edge that was removed
     */
    Edge<NodeType, EdgeType> removeEdge(Edge<NodeType, EdgeType> edge);

    /**
     * Returns the set of all nodes in this graph.
     * @return a set; the set of all nodes in this graph
     */
    Set<Node<NodeType>> listNodes();

    /**
     * Returns the set of all edges to children of this parent node
     * @param parentNode the parent node of the children in graph
     * @param includeSelf boolean to include parent as a child, true if considering reflexive edges
     * @return a set; the set of edges associated with this parent node
     */
    Set<Edge<NodeType, EdgeType>> listChildren(Node<NodeType> parentNode, boolean includeSelf);

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
    boolean containsNode(Node<NodeType> node);
}