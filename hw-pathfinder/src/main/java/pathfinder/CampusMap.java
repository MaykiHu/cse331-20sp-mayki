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

/**
 * This class represents an abstract map of the UW College Campus Map.  There are buildings
 * on campus, having short and long names, as well as locations that are traversable
 * on campus.  This class will let you know what is on campus, as it is a map of campus.
 */

public class CampusMap implements ModelAPI {
    // mapping locations to buildings, location to paths, and names of buildings
    private Map<Point, CampusBuilding> campusMap = new HashMap<>();
    private DirectedGraph<Point, Double> locGraph = new DirectedGraph<>();
    private Map<String, Node<CampusBuilding>> nameMap = new HashMap<>();

    /* Abstraction Function:
     *   A CampusMap m represents a map of campus.  There is a DirectedGraph locGraph
     *   which represents nodes of all locations that can be travelled upon on campus
     *   and their respective locations that can be reached from that node.
     *   There is a campusMap which maps a campus's location to its respective campus
     *   building information.  Lastly, there is a nameMap which maps a short name of
     *   the campus building to its campus building node.  All in all, the CampusMap m
     *   represents a mapping of campus information on its locations and its buildings.
     */

    // Representation invariant for every CampusMap m:
    // m.campusMap != null && m.locGraph != null && m.nameMap != null
    // forall i such that (0 <= i < m.locGraph.size()), m.locGraph.get(i) != null) &&
    // forall i such that (0 <= i < m.locGraph.size() - 1), m.locGraph.get(i) != m.locGraph.get(i+1)) &&
    // forall j such that (0 <= j < m.locGraph.get(i).size()), m.locGraph.get(i).get(j) != null) &&
    // forall j such that (0 <= j < m.locGraph.get(i).size() - 1),
    //      m.locGraph.get(i).get(j) != m.locGraph.get(i).get(i+1)) &&
    // forall i such that (0 <= i < m.campusMap.size()), m.campusMap.get(i) != null) &&
    // forall i such that (0 <= i < m.nameMap.size()), m.nameMap.get(i) != null)
    // In other words,
    //   * the locGraph, campusMap, and nameMap fields always points to some usable object
    //   * no element in locGraph, campusMap, and nameMap is null
    //   * there are no duplicate nodes in the locGraph, campusMap, and nameMap (by definition)
    //   * no outgoing edge in the locGraph for any node is null
    //   * there are no duplicate outgoing edges in the locGraph for any node (DirectedGraph rep)

    // Change this to run expensive methods in checkRep() if set to true, otherwise does not run.
    private final boolean needsCheckRep = false;

    /*
     *  Stores data on campus buildings and paths from specified file name
     *  @param buildingsFile the file to get buildings from
     *  @param pathsFile the file to get paths from
     *  @spec.requires buildingsFile and pathsFile be under data folder and valid
     *  @return a DirectedGraph of location paths
     */
    public void initializeData(String buildingsFile, String pathsFile) {
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
        checkRep();
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

    /*
    Throws an exception if the representation invariant is violated.
 */
    private void checkRep() {
        assert (locGraph != null && campusMap != null && nameMap != null); // fields are not null
        if (needsCheckRep) { // Only check expensive checks if needed
            // DirectedGraph checkRep is checked implicitly if enabled in DirectedGraph.java
            // Checks campusMap mapping valid location to valid building
            Iterator<Map.Entry<Point, CampusBuilding>> buildingMapItr = campusMap.entrySet().iterator();
            while (buildingMapItr.hasNext()) {
                Map.Entry<Point, CampusBuilding> entry = buildingMapItr.next();
                assert (entry.getKey() != null) : "Cannot have a null location mapping";
                assert(entry.getValue() != null) : "Cannot map location to a null building";
            }
            // Checks nameMap mapping valid name to valid building node
            Iterator<Map.Entry<String, Node<CampusBuilding>>> nameMapItr = nameMap.entrySet().iterator();
            while (nameMapItr.hasNext()) {
                Map.Entry<String, Node<CampusBuilding>> entry = nameMapItr.next();
                assert (entry.getKey() != null) : "Cannot have a null name mapping";
                assert(entry.getValue().getData() != null) : "Cannot map name to a null building";
            }
        }
    }
}
