package graph;

import java.util.*;

/**
 * This class represents the concept of a directed graph, which have may have Nodes and Edges.
 * In this directed graph, no same startNode to endNode edge will have the same label.
 * A DirectedGraph has type parameters, NodeType the type of data stored in the node (ex: String)
 * and EdgeType the type of data stored in the edges (ex: Point).
 */

public class DirectedGraph<NodeType, EdgeType> implements Graph<NodeType, EdgeType> {

    private final Map<Node<NodeType>, Set<Edge<NodeType, EdgeType>>> graph;

    // Abstraction Function:
    //   A DirectedGraph g represents a directed graph which have nodes and each node
    //   has a set of outgoing edges associated with that node.  This means,
    //   by definition, that a node with no outgoing edges is either an
    //   end node or an island node.
    //   There are no duplicate nodes or edges and the graph is mutable as nodes and edges
    //   can be added/removed from a graph directly.
    //   If a graph has no nodes (thus, no edges), the graph represents an empty non-null graph.

    // Representation invariant for every DirectedGraph g:
    // g.graph != null &&
    // forall i such that (0 <= i < g.graph.size()), g.graph.get(i) != null &&
    // forall i such that (0 <= i < g.graph.size() - 1), g.graph.get(i) != g.graph.get(i+1) &&
    // forall j such that (0 <= j < g.graph.get(i).size()), g.graph.get(i).get(j) != null &&
    // forall j such that (0 <= j < g.graph.get(i).size() - 1),
    //      g.graph.get(i).get(j) != g.graph.get(i).get(i+1) &&
    // In other words,
    //   * the graph field always points to some usable object
    //   * no node in the graph is null
    //   * there are no duplicate nodes in the graph (by definition of a map)
    //   * no outgoing edge in the graph for any node is null
    //   * there are no duplicate outgoing edges in the graph for any node (by definition of a set)

    // Change this to run expensive methods in checkRep() if set to true, otherwise does not run.
    private final boolean needsCheckRep = false;

    private int edgeSize;

    /**
     * Constructs a DirectedGraph.
     * @spec.effects Constructs a new DirectedGraph.
     */
    public DirectedGraph() {
        graph = new HashMap<>();
        edgeSize = 0;
    }

    /**
     * Adds a node to the graph.
     * @param node that is added to the graph
     * @spec.requires node != null
     * @spec.effects Adds a node to the graph, if not in the graph.  Otherwise, does not add.
     */
    @Override
    public void addNode(Node<NodeType> node) {
        if (!graph.containsKey(node)) { // If node not in graph
            graph.put(node, new HashSet<>());
        }
        checkRep();
    }

    /**
     * Adds a directed edge to the graph.
     * @param edge that is to be added to the graph
     * @spec.requires edge != null, both nodes in this edge are on the graph
     * @spec.effects Adds the directed edge to the graph, if not in the graph.
     * Otherwise, does not.  Note that, since it's a directed edge, it is added as
     * startNode to endNode on the graph.
     */
    @Override
    public void addEdge(Edge<NodeType, EdgeType> edge) {
        Node<NodeType> startNode = edge.getStart();
        Node<NodeType> endNode = edge.getEnd();
        // If edge has valid arguments to be added
        if (graph.containsKey(startNode) && graph.containsKey(endNode)) {
            if (!graph.get(startNode).contains(edge)) { // And edge not in graph already
                graph.get(startNode).add(edge);
                if (!startNode.equals(endNode)) { // Ignores reflexive
                    edgeSize++;
                }
            }
        }
        checkRep();
    }

    /**
     * Removes a node from the graph.
     * @param node that is removed from the graph
     * @spec.requires node != null
     * @spec.effects Removes a node from the graph, if in the graph.
     * Otherwise, does not remove.  Note that removing this node removes
     * any nodes/edges in contact with this node.
     * @return the node that was removed
     */
    @Override
    public Node<NodeType> removeNode(Node<NodeType> node) {
        if (node != null && graph.containsKey(node)) { // graph has this node
            edgeSize -= graph.get(node).size();
            graph.remove(node); // Removes this node and all its outgoing edges
            Iterator<Map.Entry<Node<NodeType>, Set<Edge<NodeType, EdgeType>>>> itr = graph.entrySet().iterator();
            while (itr.hasNext()) { // Traverses all remaining Node<NodeType>s
                Map.Entry<Node<NodeType>, Set<Edge<NodeType, EdgeType>>> entry = itr.next();
                Iterator<Edge<NodeType, EdgeType>> eItr = entry.getValue().iterator();
                while (eItr.hasNext()) { // Traverses outgoing edges of remaining node
                    Edge<NodeType, EdgeType> edge = eItr.next();
                    if (edge.getEnd().equals(node)) { // If outgoing edge is to this removed node
                        removeEdge(edge); // Removes this outgoing edge leading to removed node
                        if (!edge.getStart().equals(edge.getEnd())) {
                            edgeSize--; // Reduce non-reflexive edge count
                        }
                    }
                }
            }
            return node; // Node that was removed
        }
        return null; // No node was removed
    }

    /**
     * Removes a directed edge to the graph.
     * @param edge that is to be removed from the graph
     * @spec.requires edge != null
     * @spec.effects Removes the directed edge from the graph, if in the graph.
     * Otherwise, does not.  Note that, since it's a directed edge, only the edge with
     * direction startNode to endNode is removed from the graph.  Both nodes connected
     * by this edge still remain after removal.
     * @return the edge that was removed
     */
    @Override
    public Edge<NodeType, EdgeType> removeEdge(Edge<NodeType, EdgeType> edge) {
        Node<NodeType> startNode = edge.getStart();
        if (graph.containsKey(startNode)) { // If edge has startNode in graph
            if (graph.get(startNode).contains(edge)) { // If edge is in graph
                graph.get(startNode).remove(edge); // Remove edge requested for removal
                if (!edge.getStart().equals(edge.getEnd())) {
                    edgeSize--;
                }
                return edge; // Edge that was removed
            }
        }
        return null; // No edge was removed
    }

    /**
     * Returns an unordered unmodifiable set of nodes associated with this graph
     * @return an unmodifiable set of nodes of this graph
     */
    @Override
    public Set<Node<NodeType>> listNodes() {
        return Collections.unmodifiableSet(graph.keySet());
    }

    /**
     * Returns an unsorted unmodifiable set of children edges of this node
     * @param parentNode that is in this graph
     * @param includeSelf boolean to include parent as a child, true if considering reflexive edges
     * @spec.requires node is in graph, boolean is not null
     * @return an unmodifiable set of edges in this graph, of this parent node
     */
    @Override
    public Set<Edge<NodeType, EdgeType>> listChildren(Node<NodeType> parentNode, boolean includeSelf) {
        Set<Edge<NodeType, EdgeType>> edges = new HashSet<>();
        for (Edge<NodeType, EdgeType> child : graph.get(parentNode)) {
            if (!child.getEnd().equals(parentNode) || includeSelf) { // considering reflexive
                edges.add(child);
            }
        }
        return Collections.unmodifiableSet(edges);
    }

    /**
     * Returns a string representation of this graph in the form of all the nodes, and
     * then the children nodes of each parent node.  The list of nodes and
     * each parent node and their children are separated by a new line.
     * Nodes, parent nodes and child nodes and edges are listed in alphabetical order.
     * Ex:
     *     graph contains: n0 n1 n2
     *     the children of n0 are:
     *     the children of n1 are: n2(e8)
     *     the children of n2 are:
     * @return a String representation of this graph.
     */
    @Override
    public String toString() {
        String graphString = "graph contains:";
        Set<Node<NodeType>> sortedNodes = listNodes();
        List<Node<NodeType>> parentNodes = new ArrayList<>();
        List<Set<Edge<NodeType, EdgeType>>> sortedChildren = new ArrayList<>();
        // Saves all parent nodes in graph
        for (Node<NodeType> node : sortedNodes) {
            graphString += " " + node.toString();
            parentNodes.add(node);
            sortedChildren.add(listChildren(node, true));
        }
        graphString += "\n";
        int parentIndex = 0;
        // Then, saves all children of those parent nodes
        for (Set<Edge<NodeType, EdgeType>> childrenSet : sortedChildren) {
            graphString += "the children of " + parentNodes.get(parentIndex) + " are:";
            for (Edge<NodeType, EdgeType> child : childrenSet) {
                graphString += " " + child.getEnd().toString() + "(" + child.getLabel() + ")";
            }
            graphString += "\n";
            parentIndex++; // Go to next parent node
        }
        return graphString;
    }

    /**
     * Returns if graph contains node.
     * @param node the node in question
     * @return a boolean, true if node is in the graph.  False otherwise.
     */
    public boolean containsNode(Node<NodeType> node) {
        return graph.containsKey(node);
    }

    /**
     * Returns the size of this graph.
     * @return an int; the number of nodes in this graph
     */
    @Override
    public int size() {
        return graph.size();
    }

    /**
     * Returns the number of non-reflexive edges in this graph.
     * @return an int; the number of non-reflexive edges in this graph
     */
    public int getEdgeCount() {
        return edgeSize;
    }

    /**
     * Returns if graph is empty.
     * @return a boolean; true if graph has no nodes, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return graph.size() == 0;
    }

    /*
        Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert (graph != null); // graph cannot be null
        if (needsCheckRep) { // Only check expensive checks if needed
            Iterator<Map.Entry<Node<NodeType>, Set<Edge<NodeType, EdgeType>>>> itr = graph.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<Node<NodeType>, Set<Edge<NodeType, EdgeType>>> entry = itr.next();
                assert (entry.getKey() != null) : "Graph cannot have a null node";
                Iterator<Edge<NodeType, EdgeType>> eItr = entry.getValue().iterator();
                while (eItr.hasNext()) {
                    Edge<NodeType, EdgeType> e = eItr.next();
                    assert (e != null) : "Graph cannot have a null edge";
                }
            }
        }
    }
}
