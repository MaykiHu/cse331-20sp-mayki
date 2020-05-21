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

public class CampusMap implements ModelAPI<Node<Point>> {
    // mapping locations to buildings, location to paths, and names of buildings
    private Map<Point, CampusBuilding> campusMap = new HashMap<>();
    private DirectedGraph<Point, Double> locGraph = new DirectedGraph<>();
    private Map<String, CampusBuilding> nameMap = new HashMap<>();

    /*
     *  Where the AF would go, but this isn't an ADT bc we are using as a program
     *  Where the rep invariant would go
     */

    /*
     *  Stores data on campus buildings and paths from specified file name
     *  @param buildingsFile the file to get buildings from
     *  @param pathsFile the file to get paths from
     *  @spec.requires buildingsFile and pathsFile be under data folder and valid
     *  @return a DirectedGraph of location paths
     */
    public DirectedGraph<Point, Double> initializeData(String buildingsFile, String pathsFile) {
        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings(buildingsFile);
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths(pathsFile);
        for (CampusBuilding building : buildings) { // Add each building as a node
            Point buildingLoc = new Point(building.getX(), building.getY());
            campusMap.put(buildingLoc, building); // Save location map
            nameMap.put(building.getShortName(), building); // Save name map
            locGraph.addNode(new Node<Point>(buildingLoc)); // Save building location
        }
        for (CampusPath path : paths) { // Add paths of locations
            Point pathStart = new Point(path.getX1(), path.getY1());
            locGraph.addNode(new Node<Point>(pathStart)); // Save starting path loc
            Point pathEnd = new Point(path.getX2(), path.getY2());
            locGraph.addNode(new Node<Point>(pathEnd)); // Save ending path loc
            Node<Point> startLoc = new Node<>(pathStart);
            Node<Point> endLoc = new Node<>(pathEnd);
            locGraph.addEdge(new Edge<Point, Double>
                    (startLoc, endLoc, path.getDistance()));
        }
        return locGraph;
    }

    @Override
    public boolean shortNameExists(String shortName) {
        return nameMap.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        if (!shortNameExists(shortName)) { // shortName does not exist
            throw new IllegalArgumentException();
        } else { // shortName exists in graph
            return nameMap.get(shortName).getLongName();
        }
    }

    @Override
    public Map<String, String> buildingNames() {
        Map<String, String> shortLongNames = new HashMap<>();
        Iterator<Map.Entry<String, CampusBuilding>> buildingItr = nameMap.entrySet().iterator();
        while (buildingItr.hasNext()) { // for each building
            CampusBuilding building = buildingItr.next().getValue();
            shortLongNames.put(building.getShortName(), building.getLongName());
        }
        return shortLongNames; // map of short to long names of all campus buildings
    }

    @Override
    public Path<Node<Point>> findShortestPath(String startShortName, String endShortName) {
        boolean notValidName = (startShortName == null || endShortName == null ||
                !shortNameExists(startShortName) || !shortNameExists(endShortName));
        if (notValidName) {
            throw new IllegalArgumentException();
        } else { // Is valid, can try to find shortest path
            Node<Point> start = new Node<>(new Point
                    (nameMap.get(startShortName).getX(), nameMap.get(startShortName).getY()));
            Node<Point> dest = new Node<>(new Point
                    (nameMap.get(endShortName).getX(), nameMap.get(endShortName).getY()));
            PriorityQueue<Path<Node<Point>>> nodeActivePaths =
                                                        new PriorityQueue<>(new PathComp());
            Set<Node<Point>> finishedMinNodes = new HashSet<>();

            nodeActivePaths.add(new Path<Node<Point>>(start)); // Path to itself, start
            while (!nodeActivePaths.isEmpty()) { // While still have paths to find
                // minPath is the lowest-cost path in active and,
                // if minDest isn't already 'finished,' is the
                // minimum-cost path to the node minDest -- from spec algorithm
                Path<Node<Point>> minPath = nodeActivePaths.remove();
                Node<Point> minDest = minPath.getEnd();
                if (minDest.equals(dest)) { // Reached min path dest
                    return minPath;
                }
                if (finishedMinNodes.contains(minDest)) { // min dest is in finished
                    continue;
                }
                // For all children edges of minDest
                for (Edge<Point, Double> edge : locGraph.listChildren(minDest, false)) {
                    Node<Point> child = edge.getEnd();
                    if (!finishedMinNodes.contains(child)) { // If child not in finished min nodes
                        Path<Node<Point>> newPath = // minPath + this child's edge
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
    private class PathComp implements Comparator<Path<Node<Point>>> {
        public int compare(Path<Node<Point>> node1, Path<Node<Point>> node2) {
            return Double.compare(node1.getCost(), node2.getCost());
        }
    }

}
