package graph;

import java.util.*;

/**
 * This class represents the concept of a directed graph, which have may have Nodes and Edges.
 * In this directed graph, no same startNode to endNode edge will have the same label.
 */

public class DirectedGraph implements Graph {

    private final Map<Node, Set<Edge>> graph;

    /**
     * Constructs a DirectedGraph.
     * @spec.effects Constructs a new DirectedGraph.
     */
    public DirectedGraph() {
        throw new RuntimeException("DirectedGraph constructor is not yet implemented.");
    }

    /**
     * Adds a node to the graph.
     * @param node that is added to the graph
     * @spec.requires node != null
     * @spec.effects Adds a node to the graph, if not in the graph.  Otherwise, does not add.
     */
    @Override
    public void addNode(Node node) {
        throw new RuntimeException("DirectedGraph.addNode() is not yet implemented.");
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
    public void addEdge(Edge edge) {
        throw new RuntimeException("DirectedGraph.addEdge() is not yet implemented.");
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
    public Node removeNode(Node node) {
        throw new RuntimeException("DirectedGraph.removeNode() is not yet implemented.");
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
    public Edge removeEdge(Edge edge) {
        throw new RuntimeException("DirectedGraph.removePath() is not yet implemented.");
    }

    /**
     * Returns the string representation of the nodes in this graph.  Its output starts with:
     * graph contains:
     * and is followed, on the same line, by a space-separated list of the node data contained
     * in each node of the graph.  The nodes appear in alphabetical order.  There is a single
     * space between the colon, the first node name, and any subsequent, but no space if no nodes.
     * [Description from Homework 5 Spec]
     * @return a String representation of this graph.
     */
    @Override
    public String listNodes() {
        throw new RuntimeException("DirectedGraph.getNodes() is not yet implemented.");
    }

    /**
     * Returns the string representation of the children nodes of this parent node.  Its output is:
     * the children of 'parentNode' are:
     * and is followed, on the same line, by a space-separated list of entries of the form
     * node(edgeLabel), where node is a node in graphName to which there is an edge from parentNode
     * and edgeLabel is the label on that edge. If there are multiple edges between two nodes,
     * there should be a separate node(edgeLabel) entry for each edge.
     * The nodes should appear in lexicographical (alphabetical) order by node name and secondarily
     * by edge label, e.g. firstNode(someEdge) secondNode(edgeA) secondNode(edgeB) secondNode(edgeC)
     * There is a single space between the colon and the first node name, but no space if there are
     * no children.  [Description from Homework 5 Spec]
     * @param parentNode a node on the graph
     * @return a String representation of the children node(edgeLabel) of this parent node
     */
    public String listChildren(Node parentNode) {
        throw new RuntimeException("DirectedGraph.getChildren() is not yet implemented.");
    }

    /**
     * Returns a string representation of this graph in the form of the children nodes
     * of each parent node.  Each parent node and their children are separated by a new line.
     * Parent nodes and child nodes are listed in alphabetical order.
     * Ex: the children of n0 are:
     *     the children of n1 are: n2(e8)
     * @return a String representation of this graph.
     */
    @Override
    public String toString() {
        throw new RuntimeException("DirectedGraph.toString() is not yet implemented.");
    }


    /**
     * Returns the size of this graph.
     * @return an int; the number of nodes in this graph
     */
    @Override
    public int size() {
        throw new RuntimeException("DirectedGraph.size() is not yet implemented.");
    }

    /**
     * Returns if graph is empty.
     * @return a boolean; true if graph has no nodes, false otherwise
     */
    @Override
    public boolean isEmpty() {
        throw new RuntimeException("DirectedGraph.isEmpty() is not yet implemented.");
    }

    private void checkRep() {
        throw new RuntimeException("DirectedGraph.checkRep() is not yet implemented.");
    }

    private class NodeComp implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            throw new RuntimeException("NodeComp.compare() is not yet implemented.");
        }
    }

    private class EdgeComp implements Comparator<Edge> {
        public int compare(Edge edge1, Edge edge2) {
            throw new RuntimeException("EdgeComp.compare() is not yet implemented.");
        }
    }
}
