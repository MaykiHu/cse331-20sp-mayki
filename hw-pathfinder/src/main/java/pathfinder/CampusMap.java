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

public class CampusMap extends GenericDijkstra<Point> implements ModelAPI<Node<Point>> {
    // mapping locations to buildings, location to paths, and names of buildings
    private Map<Point, CampusBuilding> campusMap = new HashMap<>();
    private DirectedGraph<Point, Double> locGraph = new DirectedGraph<>();
    private Map<String, Node<CampusBuilding>> nameMap = new HashMap<>();

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
            nameMap.put(building.getShortName(), new Node<CampusBuilding>(building)); // Save name map
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
            return nameMap.get(shortName).getData().getLongName();
        }
    }

    @Override
    public Map<String, String> buildingNames() {
        Map<String, String> shortLongNames = new HashMap<>();
        Iterator<Map.Entry<String, Node<CampusBuilding>>> buildingItr = nameMap.entrySet().iterator();
        while (buildingItr.hasNext()) { // for each building
            CampusBuilding building = buildingItr.next().getValue().getData();
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
            CampusBuilding startBuilding = nameMap.get(startShortName).getData();
            CampusBuilding endBuilding = nameMap.get(endShortName).getData();
            Node<Point> start = new Node<>(new Point
                    (startBuilding.getX(), startBuilding.getY()));
            Node<Point> dest = new Node<>(new Point
                    (endBuilding.getX(), endBuilding.getY()));
            GenericDijkstra<Point> superAlgo = new GenericDijkstra<>();
            superAlgo.setCampusGraph(locGraph);
            return superAlgo.findShortestPath(start, dest);
        }
    }
}
