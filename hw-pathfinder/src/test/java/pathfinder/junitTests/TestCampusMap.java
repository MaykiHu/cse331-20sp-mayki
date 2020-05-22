package pathfinder.junitTests;

import org.junit.Test;
import pathfinder.CampusMap;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * To test the stub method in CampusMap: initializeData(String buildingFile, String pathsFile)
 */
public class TestCampusMap {
    private final String buildingFile = "campus_buildings.tsv"; // building file
    private final String pathsFile = "campus_paths.tsv"; // paths file
    private final int numOfBuildings = 51; // Total number of buildings
    private final Path current = Paths.get("");
    private final String absoluteBuildingPath = current.toAbsolutePath().toString() +
            "/src/main/resources/data/campus_buildings.tsv";

    /*
     * Test to see parsed and stored number of buildings correctly
     */
    @Test
    public void testBuildingSize() {
        System.out.println(current.toAbsolutePath());
        CampusMap testMap = new CampusMap();
        testMap.initializeData(buildingFile, pathsFile);
        Map<String, String> buildingNames = testMap.buildingNames();
        assertEquals(buildingNames.size(), numOfBuildings);
    }

    /*
     * Test to see if parsed and stored building information correctly
     * @spec.requires file path to be in /data and tab separated
     * as short_name longName x y
     */
    @Test
    public void testBuildingNames() throws IOException { // throw bc using file
        List<String> shortList = new ArrayList<>(); // List of all short names
        List<String> longList = new ArrayList<>(); // List of all long names
        Scanner parser = new Scanner(new File(absoluteBuildingPath));
        parser.nextLine(); // Skip the tab information about short_name long_name etc.
        while (parser.hasNextLine()) { // Scan line-by-line
            String[] buildingInfo = parser.nextLine().split("\t"); //[short, long]
            shortList.add(buildingInfo[0]); // add short name
            longList.add(buildingInfo[1]); // add long name
        }
        CampusMap testMap = new CampusMap();
        testMap.initializeData(buildingFile, pathsFile);
        Map<String, String> buildingNames = testMap.buildingNames();
        for (int i = 0; i < shortList.size(); i++) {
            String shortName = shortList.get(i);
            assertTrue(buildingNames.containsKey(shortName)); // buildingNames has this short name
            assertEquals(buildingNames.get(shortName), longList.get(i)); // buildingNames has long name
        }
    }
}