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

package pathfinder;

import graph.DirectedGraph;
import graph.Edge;
import graph.Node;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.*;

public class DijkstraAlgo {
    private DirectedGraph<CampusBuilding, Double> campusGraph = new DirectedGraph<>();

    /*
     *  Where the AF would go, but this isn't an ADT bc we are using as a program
     *  Where the rep invariant would go
     */

    /*
     *  Stores data on campus buildings and paths from specified file name
     *  @param buildingsFile the file to get buildings from
     *  @param pathsFile the file to get paths from
     *  @spec.requires buildingsFile and pathsFile be under data folder and valid
     *  @return a DirectedGraph from this data
     */
    public DirectedGraph<CampusBuilding, Double> initializeData(String buildingsFile, String pathsFile) {
        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings(buildingsFile);
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths(pathsFile);
        Map<Point, CampusBuilding> locMap = new HashMap<>(); // For eventual path-graph-mapping
        for (CampusBuilding building : buildings) { // Add each building as a node
            campusGraph.addNode(new Node<CampusBuilding>(building)); // Buildings are nodes
            locMap.put(new Point(building.getX(), building.getY()), building); // Save location map
        }
        for (CampusPath path : paths) { // Add each path as an edge to their respective start nodes
            Point pathStart = new Point(path.getX1(), path.getY1());
            Point pathEnd = new Point(path.getX2(), path.getY2());
            Node<CampusBuilding> startBuilding = new Node<>(locMap.get(pathStart));
            Node<CampusBuilding> endBuilding = new Node<>(locMap.get(pathEnd));
            campusGraph.addEdge(new Edge<CampusBuilding, Double>(startBuilding, endBuilding, path.getDistance()));
        }
        return campusGraph;
    }

    /*
     * Sets the DirectedGraph being used for the map, mainly for testing purposes only
     * @param newGraph
     */
    public void setCampusGraph(DirectedGraph<CampusBuilding, Double> newGraph) {
        campusGraph = newGraph;
    }

    /*
     * Returns a string array of the building's info, split up
     * to: [short name, long name, location]
     * @param building containing info to be split
     * @return a String[] of the building's info, split up
     */
    public String[] splitBuildingInfo(Node<CampusBuilding> building) {
        String[] buildingInfo = building.toString().split(";");
        String[] notInfoList = {"[Short: ", " Long: ", " Location: "};
        for (int i = 0; i < buildingInfo.length; i++) { // Traverse each building
            String notInfoText = notInfoList[i];
            String unparsedText = buildingInfo[i];
            // Update to contain only needed info
            buildingInfo[i] = unparsedText.substring(notInfoText.length(), unparsedText.length() - 1);
        }
        return buildingInfo;
    }

    public boolean shortNameExists(String shortName) {
        Set<Node<CampusBuilding>> buildings = campusGraph.listNodes();
        for (Node<CampusBuilding> building : buildings) {
            String name = building.getData().getShortName();
            if (name.equals(shortName)) { // building matches shortName
                return true;
            }
        }
        return false; // No shortName match in campus's graph
    }

    public String longNameForShort(String shortName) {
        if (!shortNameExists(shortName)) { // shortName does not exist
            throw new IllegalArgumentException();
        } else { // shortName exists in graph
            // Get the matching building info from shortName-node map
            Node<CampusBuilding> matchingBuilding = getNameNodeMap().get(shortName);
            String[] buildingInfo = splitBuildingInfo(matchingBuilding);
            return buildingInfo[1]; // Return the long name
        }
    }

    public Map<String, String> buildingNames() {
        Map<String, String> nameMap = new HashMap<>();
        Set<Node<CampusBuilding>> buildings = campusGraph.listNodes();
        for (Node<CampusBuilding> building : buildings) {
            String[] nameInfo = splitBuildingInfo(building);
            nameMap.put(nameInfo[0], nameInfo[1]); // put(short, long)
        }
        return nameMap;
    }

    /*
     * Returns a map matching the short name of a building to its
     * matching building
     * @return a map of the string to its building
     */
    private Map<String, Node<CampusBuilding>> getNameNodeMap() {
        Map<String, Node<CampusBuilding>> nameMap = new HashMap<String, Node<CampusBuilding>>();
        Set<Node<CampusBuilding>> buildings = campusGraph.listNodes();
        for (Node<CampusBuilding> building : buildings) {
            String name = building.getData().getShortName();
            nameMap.put(name, building); // put(short, node)
        }
        return nameMap;
    }

    public Path<Node<CampusBuilding>> findShortestPath(String startShortName, String endShortName) {
        boolean notValidName = (startShortName == null || endShortName == null ||
                !shortNameExists(startShortName) || !shortNameExists(endShortName));
        if (notValidName) {
            throw new IllegalArgumentException();
        } else { // Is valid, can try to find shortest path
            Map<String, Node<CampusBuilding>> nameNodeMap = getNameNodeMap(); // map shortName-node
            Node<CampusBuilding> start = nameNodeMap.get(startShortName);
            Node<CampusBuilding> dest = nameNodeMap.get(endShortName);
            PriorityQueue<Path<Node<CampusBuilding>>> nodeActivePaths =
                    new PriorityQueue<>(new PathComp());
            Set<Node<CampusBuilding>> finishedMinNodes = new HashSet<>();

            nodeActivePaths.add(new Path<Node<CampusBuilding>>(start)); // Path to itself, start

            while (!nodeActivePaths.isEmpty()) { // While still have paths to find
                // minPath is the lowest-cost path in active and,
                // if minDest isn't already 'finished,' is the
                // minimum-cost path to the node minDest -- from spec algorithm
                Path<Node<CampusBuilding>> minPath = nodeActivePaths.remove();
                Node<CampusBuilding> minDest = minPath.getEnd();
                if (minDest.equals(dest)) { // Reached min path dest
                    return minPath;
                }
                if (finishedMinNodes.contains(minDest)) { // min dest is in finished
                    continue;
                }
                // For all children edges of minDest
                for (Edge<CampusBuilding, Double> edge : campusGraph.listChildren(minDest, false)) {
                    Node<CampusBuilding> child = edge.getEnd();
                    if (!finishedMinNodes.contains(child)) { // If child not in finished min nodes
                        Path<Node<CampusBuilding>> newPath = // minPath + this child's edge
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
    private class PathComp implements Comparator<Path<Node<CampusBuilding>>> {
        public int compare(Path<Node<CampusBuilding>> node1, Path<Node<CampusBuilding>> node2) {
            return Double.compare(node1.getCost(), node2.getCost());
        }
    }

}