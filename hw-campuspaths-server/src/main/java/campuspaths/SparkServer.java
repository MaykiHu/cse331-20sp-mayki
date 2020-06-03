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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import spark.Spark;

public class SparkServer {

    // Files used for campus information
    private static final String buildingsFile = "campus_buildings.tsv";
    private static final String pathsFile = "campus_paths.tsv";

    // GSon to convert to JSon
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        // TODO: Create all the Spark Java routes you need here.
        // Stores the campus map
        CampusMap map = new CampusMap();
        map.initializeData(buildingsFile, pathsFile);

        // Returns the buildings within campus map
        Spark.get("/buildings", (req, res) -> gson.toJson(map.buildingNames()));

        // Returns the shortest path between two campus paths
        //(?startName=...&endName=...)
        Spark.get("/path", (req, res) -> {
            String startName = req.queryParams("startName");
            String endName = req.queryParams("endName");
            return gson.toJson(map.findShortestPath(startName, endName));
        });
    }

}
