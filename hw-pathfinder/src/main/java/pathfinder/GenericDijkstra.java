package pathfinder;

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

import graph.DirectedGraph;
import graph.Edge;
import graph.Node;
import pathfinder.datastructures.Path;

import java.util.*;

/*
 * This class is a creation just to test the algorithm generically of the Dijkstra algo
 */
public class GenericDijkstra<PathType> {
    private DirectedGraph<PathType, Double> graph = new DirectedGraph<>();

    /*
     *  Where the AF would go, but this isn't an ADT bc we are using as a program
     *  Where the rep invariant would go
     */

    /*
     * Sets the DirectedGraph being used to find data from
     * @param newGraph
     */
    public void setCampusGraph(DirectedGraph<PathType, Double> newGraph) {
        graph = newGraph;
    }

    /*
     * @spec.requires nodes given to be valid in graph and non null
     * @param startNode the node where the path starts
     * @param endNode the node where the path ends
     * @return
     */
    public Path<Node<PathType>> findShortestPath(Node<PathType> startNode, Node<PathType> endNode) {
        boolean notValidName = (startNode == null || endNode == null);
        if (notValidName) {
            throw new IllegalArgumentException();
        } else { // Is valid, can try to find shortest path
            Node<PathType> start = startNode;
            Node<PathType> dest = endNode;
            PriorityQueue<Path<Node<PathType>>> nodeActivePaths =
                    new PriorityQueue<>(new PathComp());
            Set<Node<PathType>> finishedMinNodes = new HashSet<>();

            nodeActivePaths.add(new Path<Node<PathType>>(start)); // Path to itself, start

            while (!nodeActivePaths.isEmpty()) { // While still have paths to find
                // minPath is the lowest-cost path in active and,
                // if minDest isn't already 'finished,' is the
                // minimum-cost path to the node minDest -- from spec algorithm
                Path<Node<PathType>> minPath = nodeActivePaths.remove();
                Node<PathType> minDest = minPath.getEnd();
                if (minDest.equals(dest)) { // Reached min path dest
                    return minPath;
                }
                if (finishedMinNodes.contains(minDest)) { // min dest is in finished
                    continue;
                }
                // For all children edges of minDest
                for (Edge<PathType, Double> edge : graph.listChildren(minDest, false)) {
                    Node<PathType> child = edge.getEnd();
                    if (!finishedMinNodes.contains(child)) { // If child not in finished min nodes
                        Path<Node<PathType>> newPath = // minPath + this child's edge
                                minPath.extend(child, edge.getLabel());
                        nodeActivePaths.add(newPath);
                    }
                }
                finishedMinNodes.add(minDest);
            }
            // loop terminated
            return null; // no path exists
        }
    }

    // Comparator to work with at least Doubles
    private class PathComp implements Comparator<Path<Node<PathType>>> {
        public int compare(Path<Node<PathType>> node1, Path<Node<PathType>> node2) {
            return Double.compare(node1.getCost(), node2.getCost());
        }
    }

}
