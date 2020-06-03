/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.DirectedGraph;
import graph.Edge;
import graph.Node;
import pathfinder.GenericDijkstra;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, DirectedGraph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @spec.requires r != null && w != null
     * @spec.effects Creates a new GraphTestDriver which reads command from
     * {@code r} and writes results to {@code w}
     **/
    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests()
            throws IOException {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch (command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "RemoveNode":
                    removeNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "RemoveEdge":
                    removeEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "IsEmpty":
                    isEmpty(arguments);
                    break;
                case "GetSize":
                    getSize(arguments);
                    break;
                case "ToString":
                    toString(arguments);
                    break;
                // Additional cases that were added to Marvel, Graphs, and CampusMap as of HW6
                case "FindPath":
                    findPath(arguments);
                    break;
                case "GetEdgeCount":
                    getEdgeCount(arguments);
                    break;
                case "ContainsNode":
                    containsNode(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void containsNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to ContainsNode: " + arguments);
        }
        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);
        containsNode(graphName, nodeName);
    }

    private void containsNode(String graphName, String nodeName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        String outputString = graphName + " does ";
        Node<String> node = new Node<>(nodeName);
        if (testGraph.containsNode(node)) {
            outputString += "contain " + nodeName;
        } else { // Does not contain node
            outputString += "not contain " + nodeName;
        }
        output.println(outputString);
    }

    private void getEdgeCount(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to GetEdgeCount: " + arguments);
        }
        String graphName = arguments.get(0);
        getEdgeCount(graphName);
    }

    private void getEdgeCount(String graphName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        output.println("Number of edges in " + graphName + " is: " + testGraph.getEdgeCount());
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        }
        String graphName = arguments.get(0);
        String startChar = arguments.get(1);
        String endChar = arguments.get(2);
        findPath(graphName, startChar, endChar);
    }

    private void findPath(String graphName, String startChar, String endChar) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        GenericDijkstra<String> testMap = new GenericDijkstra<>();
        testMap.setCampusGraph(testGraph);
        // Given startChar/endChar do not exist
        Node<String> startNode = new Node<>(startChar);
        Node<String> endNode = new Node<>(endChar);
        if (!testGraph.containsNode(startNode) || !testGraph.containsNode(endNode)) {
            if (!testGraph.containsNode(startNode)) {
                output.println("unknown node " + startChar);
            }
            if (!testGraph.containsNode(endNode)) {
                output.println("unknown node " + endChar);
            }
        } else { // Can search bc valid names
            Path<Node<String>> path = testMap.findShortestPath(new Node<>(startChar), new Node<>(endChar));
            output.println("path from " + startChar + " to " + endChar + ":");
            if (path == null) { // No path was found
                output.println("no path found");
            } else { // Path was found
                Iterator<Path<Node<String>>.Segment> segmentIterator = path.iterator();
                while (segmentIterator.hasNext()) { // Each segment in path
                    Path<Node<String>>.Segment segment = segmentIterator.next();
                    String startName = segment.getStart().toString();
                    String endName = segment.getEnd().toString();
                    double weight = segment.getCost();
                    output.println(startName + " to " + endName + String.format(" with weight %.3f", weight));
                }
                output.println(String.format("total cost: %.3f", path.getCost()));
            }
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new DirectedGraph<String, Double>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        Node<String> newNode = new Node<>(nodeName);
        testGraph.addNode(newNode);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void removeNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to RemoveNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        removeNode(graphName, nodeName);
    }

    private void removeNode(String graphName, String nodeName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        Node<String> oldNode = new Node<>(nodeName);
        testGraph.removeNode(oldNode);
        output.println("removed node " + nodeName + " from " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        Double edgeLabel = Double.valueOf(arguments.get(3));

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {

        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        Node<String> parentNode = new Node<>(parentName);
        Node<String> childNode = new Node<>(childName);
        Edge<String, Double> newEdge = new Edge<>(parentNode, childNode, edgeLabel);
        testGraph.addEdge(newEdge);
        output.println(String.format("added edge %.3f", edgeLabel) + " from " + parentName + " to " + childName +
                " in " + graphName);
    }

    private void removeEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to RemoveEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        Double edgeLabel = Double.valueOf(arguments.get(3));

        removeEdge(graphName, parentName, childName, edgeLabel);
    }

    private void removeEdge(String graphName, String parentName, String childName,
                            Double edgeLabel) {

        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        Node<String> parentNode = new Node<>(parentName);
        Node<String> childNode = new Node<>(childName);
        Edge<String, Double> oldEdge = new Edge<>(parentNode, childNode, edgeLabel);
        testGraph.removeEdge(oldEdge);
        output.println(String.format("removed edge %.3f", edgeLabel) + " from " + parentName + " to " + childName +
                " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    /*
     * Computes how to compare between two nodes
     */
    private class NodeComp implements Comparator<Node<String>> {
        public int compare(Node<String> node1, Node<String> node2) {
            // Compare their respective data
            return node1.getData().compareTo(node2.getData());
        }
    }

    private void listNodes(String graphName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        GenericDijkstra<String> testMap = new GenericDijkstra<>();
        testMap.setCampusGraph(testGraph);
        Set<Node<String>> listOfNodes = new TreeSet<>(new NodeComp());
        listOfNodes.addAll(testGraph.listNodes());
        String outputString = graphName + " contains:";
        for (Node<String> node : listOfNodes) {
            outputString += " " + node.toString();
        }
        output.println(outputString);
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    /*
     * Computes how to compare between two edges
     */
    private static class EdgeComp implements Comparator<Edge<String, Double>> {
        public int compare(Edge<String, Double> edge1, Edge<String, Double> edge2) {
            // Since toString() represents each edge/node distinctly, we can compare via toString()
            // We have to rearrange the toString() such that it evaluates
            // startNode -> endNode -> label
            String e1 = edge1.getStart().toString() + edge1.getEnd().toString() + edge1.getLabel();
            String e2 = edge2.getStart().toString() + edge2.getEnd().toString() + edge2.getLabel();
            return e1.compareTo(e2);
        }
    }

    private void listChildren(String graphName, String parentName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        GenericDijkstra<String> testMap = new GenericDijkstra<>();
        testMap.setCampusGraph(testGraph);
        Set<Edge<String, Double>> listOfChildren = new TreeSet<>(new EdgeComp());
        listOfChildren.addAll(testGraph.listChildren(new Node<>(parentName), false));
        String outputString = "the children of " + parentName + " in " + graphName + " are:";
        for (Edge<String, Double> child : listOfChildren) {
            outputString += " " + child.toString() + String.format("(%.3f", child.getLabel()) + ")";
        }
        output.println(outputString);
    }

    private void isEmpty(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to IsEmpty: " + arguments);
        }

        String graphName = arguments.get(0);
        isEmpty(graphName);
    }

    private void isEmpty(String graphName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        String emptyStatus = " is not empty";
        if (testGraph.isEmpty()) {
            emptyStatus = " is empty";
        }
        output.println(graphName + emptyStatus);
    }

    private void getSize(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to GetSize: " + arguments);
        }

        String graphName = arguments.get(0);
        getSize(graphName);
    }

    private void getSize(String graphName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        output.println("size of " + graphName + " is " + testGraph.size());
    }

    private void toString(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        toString(graphName);
    }

    private void toString(String graphName) {
        DirectedGraph<String, Double> testGraph = graphs.get(graphName);
        output.println(testGraph.toString());
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
