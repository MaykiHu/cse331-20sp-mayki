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

package marvel.scriptTestRunner;

import graph.DirectedGraph;
import graph.Edge;
import graph.Node;
import marvel.MarvelPaths;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts from
 * files for testing Graph, the Marvel parser, and your BFS algorithm.
 */
public class MarvelTestDriver {

    // ***************************
    // ***  JUnit Test Driver  ***
    // ***************************

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, DirectedGraph> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @spec.requires r != null && w != null
     * @spec.effects Creates a new GraphTestDriver which reads command from
     * {@code r} and writes results to {@code w}
     **/
    // Leave this constructor public
    public MarvelTestDriver(Reader r, Writer w) {
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
                // Additional cases that were added to Marvel and Graphs as of HW6
                case "LoadGraph":
                    loadGraph(arguments);
                    break;
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
        DirectedGraph testGraph = graphs.get(graphName);
        nodeName = nodeName.replaceAll("_", " ");
        String outputString = graphName + " does ";
        if (testGraph.containsNode(new Node(nodeName))) {
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
        DirectedGraph testGraph = graphs.get(graphName);
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
        DirectedGraph testGraph = graphs.get(graphName);
        startChar = startChar.replaceAll("_", " ");
        endChar = endChar.replaceAll("_", " ");
        MarvelPaths.findPath(testGraph, startChar, endChar, output);
    }

    private void loadGraph(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to LoadGraph: " + arguments);
        }
        String graphName = arguments.get(0);
        String fileName = arguments.get(1);
        loadGraph(graphName, fileName);
    }

    private void loadGraph(String graphName, String fileName) {
        graphs.put(graphName, MarvelPaths.setupUniverse(fileName));
        output.println("loaded graph " + graphName);
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new DirectedGraph());
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
        DirectedGraph testGraph = graphs.get(graphName);
        Node newNode = new Node(nodeName);
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
        DirectedGraph testGraph = graphs.get(graphName);
        Node oldNode = new Node(nodeName);
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
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {

        DirectedGraph testGraph = graphs.get(graphName);
        Node parentNode = new Node(parentName);
        Node childNode = new Node(childName);
        Edge newEdge = new Edge(parentNode, childNode, edgeLabel);
        testGraph.addEdge(newEdge);
        output.println("added edge " + edgeLabel + " from " + parentName + " to " + childName +
                " in " + graphName);
    }

    private void removeEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to RemoveEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        removeEdge(graphName, parentName, childName, edgeLabel);
    }

    private void removeEdge(String graphName, String parentName, String childName,
                            String edgeLabel) {

        DirectedGraph testGraph = graphs.get(graphName);
        Node parentNode = new Node(parentName);
        Node childNode = new Node(childName);
        Edge oldEdge = new Edge(parentNode, childNode, edgeLabel);
        testGraph.removeEdge(oldEdge);
        output.println("removed edge " + edgeLabel + " from " + parentName + " to " + childName +
                " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        DirectedGraph testGraph = graphs.get(graphName);
        Set<Node> listOfNodes = testGraph.listNodes();
        String outputString = graphName + " contains:";
        for (Node node : listOfNodes) {
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

    private void listChildren(String graphName, String parentName) {
        DirectedGraph testGraph = graphs.get(graphName);
        parentName = parentName.replaceAll("_", " ");
        Set<Edge> listOfChildren = testGraph.listChildren(new Node(parentName), false);
        String outputString = "the children of " + parentName + " in " + graphName + " are:";
        for (Edge child : listOfChildren) {
            outputString += " " + child.getEnd().toString() + "(" + child.getLabel() + ")";
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
        DirectedGraph testGraph = graphs.get(graphName);
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
        DirectedGraph testGraph = graphs.get(graphName);
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
        DirectedGraph testGraph = graphs.get(graphName);
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
