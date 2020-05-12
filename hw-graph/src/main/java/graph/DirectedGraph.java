package graph;

import java.util.*;

/**
 * This class represents the concept of a directed graph, which have may have Nodes and Edges.
 * In this directed graph, no same startNode to endNode edge will have the same label.
 */

public class DirectedGraph implements Graph {

    private final Map<Node, Set<Edge>> graph;

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
    public void addNode(Node node) {
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
    public void addEdge(Edge edge) {
        Node startNode = edge.getStart();
        Node endNode = edge.getEnd();
        // If edge has valid arguments to be added
        if (graph.containsKey(startNode) && graph.containsKey(endNode)) {
            if (!graph.get(startNode).contains(edge)) { // And edge not in graph already
                graph.get(startNode).add(edge);
                if (!graph.get(endNode).contains(edge)) { // Bidirectional edge counted once
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
    public Node removeNode(Node node) {
        if (node != null && graph.containsKey(node)) { // graph has this node
            graph.remove(node); // Removes this node and all its outgoing edges
            Iterator<Map.Entry<Node, Set<Edge>>> itr = graph.entrySet().iterator();
            while (itr.hasNext()) { // Traverses all remaining nodes
                Map.Entry<Node, Set<Edge>> entry = itr.next();
                Iterator<Edge> eItr = entry.getValue().iterator();
                while (eItr.hasNext()) { // Traverses outgoing edges of remaining node
                    Edge edge = eItr.next();
                    if (edge.getEnd().equals(node)) { // If outgoing edge is to this removed node
                        removeEdge(edge); // Removes this outgoing edge leading to removed node
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
    public Edge removeEdge(Edge edge) {
        Node startNode = edge.getStart();
        if (graph.containsKey(startNode)) { // If edge has startNode in graph
            if (graph.get(startNode).contains(edge)) { // If edge is in graph
                graph.get(startNode).remove(edge); // Remove edge requested for removal
                return edge; // Edge that was removed
            }
        }
        return null; // No edge was removed
    }

    /**
     * Returns an unmodifiable set of edges associated with this node
     * @param parentNode that is in this graph
     * @spec.requires node is in graph
     * @return an unmodifiable set of edges
     */
    public Set<Edge> getEdges(Node parentNode) {
        return Collections.unmodifiableSet(graph.get(parentNode));
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
        Map<Node, Set<Edge>> sortedGraph = new TreeMap<>(new NodeComp());
        sortedGraph.putAll(graph);
        String nodeList = "graph contains:";
        Iterator<Map.Entry<Node, Set<Edge>>> itr = sortedGraph.entrySet().iterator();
        while (itr.hasNext()) { // Traverses nodes in the graph (alphabetical already b/c sorted)
            Node node = itr.next().getKey();
            nodeList += " " + node.toString(); // Append node as string
        }
        return nodeList;
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
        String childrenList = "the children of " + parentNode.toString() + " are:";
        Set<Edge> sortedEdges = new TreeSet<>(new EdgeComp());
        sortedEdges.addAll(graph.get(parentNode));
        Iterator<Edge> eItr = sortedEdges.iterator();
        while (eItr.hasNext()) { // Traverses through edges of parentNode
            Edge childEdge = eItr.next();
            childrenList += " " + childEdge.getEnd() + "(" + childEdge.getLabel() + ")";
        }
        return childrenList;
    }

    /**
     * Returns a string representation of this graph in the form of all the nodes, and
     * then the children nodes of each parent node.  The list of nodes and
     * each parent node and their children are separated by a new line.
     * Nodes, parent nodes and child nodes are listed in alphabetical order.
     * Ex:
     *     graph contains: n0 n1 n2
     *     the children of n0 are:
     *     the children of n1 are: n2(e8)
     *     the children of n2 are:
     * @return a String representation of this graph.
     */
    @Override
    public String toString() {
        String graphList = listNodes() + "\n";
        Map<Node, Set<Edge>> sortedGraph = new TreeMap<>(new NodeComp());
        sortedGraph.putAll(graph);
        Iterator<Map.Entry<Node, Set<Edge>>> itr = sortedGraph.entrySet().iterator();
        while (itr.hasNext()) { // Traverses each node in graph
            graphList += listChildren(itr.next().getKey()) + "\n"; // Append children of this node
        }
        return graphList;
    }

    /**
     * Returns if graph contains node.
     * @param node the node in question
     * @return a boolean, true if node is in the graph.  False otherwise.
     */
    public boolean containsNode(Node node) {
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
     * Returns the number of edges in this graph.
     * @return an int; the number of edges in this graph
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
            Iterator<Map.Entry<Node, Set<Edge>>> itr = graph.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<Node, Set<Edge>> entry = itr.next();
                assert (entry.getKey() != null) : "Graph cannot have a null node";
                Iterator<Edge> eItr = entry.getValue().iterator();
                while (eItr.hasNext()) {
                    Edge e = eItr.next();
                    assert (e != null) : "Graph cannot have a null edge";
                }
            }
        }
    }

    /*
        Computes how to compare between two nodes
     */
    private class NodeComp implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            // Since toString() represents each node distinctly, we can compare via toString()
            return node1.toString().compareTo(node2.toString());
        }
    }

    /*
        Computes how to compare between two edges
     */
    private class EdgeComp implements Comparator<Edge> {
        public int compare(Edge edge1, Edge edge2) {
            // Since toString() represents each edge/node distinctly, we can compare via toString()
            // We have to rearrange the toString() such that it evaluates
            // startNode -> endNode -> label
            String e1 = edge1.getStart().toString() + edge1.getEnd().toString() + edge1.getLabel();
            String e2 = edge2.getStart().toString() + edge2.getEnd().toString() + edge2.getLabel();
            return e1.compareTo(e2);
        }
    }
}
