package marvel;

import graph.DirectedGraph;
import graph.Edge;
import graph.Node;

import java.io.PrintWriter;
import java.util.*;

/*
 *  This app allows a user to model (a graph of) the marvel universe
 */
public class MarvelPaths {
    private static final String fileName = "marvel.tsv"; // The data file on marvel heroes

    /*
     *  Where the AF would go, but this isn't an ADT bc we are using as a client program
     *  Where the rep invariant would go
     */

    // Commands to communicate from client to the model universe
    public static void main(String[] args) {

    }

    /*
     *  Sets up graph with data on heroes and comics from specified file name
     *  and returns the set up graph
     *  @param fileName, String name of the file to set up graph data from
     *  @spec.requires fileName is a valid file within Marvel's data folder
     *  @return a DirectedGraph; a graph of the marvel universe specified from the file
     */
    public static DirectedGraph setupUniverse(String fileName) {
        DirectedGraph universe = new DirectedGraph();
        // data is in the form: comic books and heroes associated
        Map<String, Set<String>> data = MarvelParser.parseData(fileName);
        // Titles of comics and heroes associated with them

        Iterator<Map.Entry<String, Set<String>>> titleItr = data.entrySet().iterator();
        while (titleItr.hasNext()) { // For each title
            Map.Entry<String, Set<String>> titleEntry = titleItr.next();
            Set<String> commonHeroesList = titleEntry.getValue();
            Iterator<String> commonHeroesItr = commonHeroesList.iterator();
            while (commonHeroesItr.hasNext()) {
                Node startHero = new Node(commonHeroesItr.next());
                universe.addNode(startHero);
                for (String commonHero : commonHeroesList) { // For every hero in common with title
                    String label = titleEntry.getKey(); // Label of edge is title of comic
                    Node endHero = new Node(commonHero);
                    universe.addNode(endHero);
                    Edge toEdge = new Edge(startHero, endHero, label);
                    Edge fromEdge = new Edge(endHero, startHero, label);
                    universe.addEdge(toEdge);
                    universe.addEdge(fromEdge);
                }
            }
        }
        return universe;
    }

    /*
     *  Given the names of two characters, searches and returns a path through the graph
     *  connecting them.  Returns the lexicographically least path, null if no path
     *  and prints output if specified to a printer, null meaning otherwise not printed.
     *  If printed, one expects below behavior:
     *  Cases:
     *      If no path found, we get:
     *      path from CHAR 1 to CHAR N:
     *      no path found
     *
     *      If a character name CHAR was not in the original dataset, we get:
     *      unknown character CHAR
     *
     *      Path from the CHAR to itself:
     *      path from CHAR to CHAR:
     *
     *      If path found:
     *      path from CHAR 1 to CHAR N:
     *      CHAR 1 to CHAR 2 via BOOK 1
     *      CHAR 2 to CHAR 3 via BOOK 2
     *      ...
     *      CHAR N-1 to CHAR N via BOOK N-1
     *  @param startChar, the String starting character/hero of the path
     *  @param endChar, the String ending character/hero of the path
     *  @param output, the PrintWriter where output is printed
     *  @spec.requires startChar, endChar, and output are not null, output is valid destination
     */
    public static List<Edge> findPath(DirectedGraph universe, String startChar,
                                      String endChar, PrintWriter output) {
        Node start = new Node(startChar);
        Node dest = new Node(endChar);
        // Case(s) where characters aren't in the graph
        if (!universe.containsNode(start) || !universe.containsNode(dest)) {
            String unknownFormat = "unknown character ";
            if (!universe.containsNode(start)) {
                output.println(unknownFormat + startChar);
            }
            if (!universe.containsNode(dest)) {
                output.println(unknownFormat + endChar);
            }
            return null;
        }

        String pathFormat = "path from " + startChar + " to " + endChar + ":";
        Queue<Node> nodesToVisit = new LinkedList<>();
        Map<Node, List<Edge>> nodePaths = new HashMap<>();
        nodesToVisit.add(start);
        nodePaths.put(start, new ArrayList<>());
        if (output != null)
            output.println(pathFormat); // Prints to tell client path is searchable

        // Start/Keep searching through applicable nodes
        while (!nodesToVisit.isEmpty()) { // Nodes still to be checked
            Node currNode = nodesToVisit.remove();
            if (currNode.equals(dest)) { // Reached destination node
                List<Edge> destPath = nodePaths.get(currNode);
                for (Edge edge : destPath) { // Print path for client
                    String currChar = edge.getStart().toString();
                    String nextChar = edge.getEnd().toString();
                    String book = edge.getLabel();
                    if (output != null)
                        output.println(currChar + " to " + nextChar + " via " + book);
                }
                return destPath;
            } // Continue searching through edges of this node
            Set<Edge> sortedEdges = universe.listChildren(currNode, false); // no reflexive
            for (Edge currEdge : sortedEdges) {
                Node nextNode = currEdge.getEnd();
                if (!nodePaths.containsKey(nextNode)) { // nextNode has not been visited
                    List<Edge> currPath = nodePaths.get(currNode);
                    List<Edge> nextPath = new ArrayList<>(currPath);
                    nextPath.add(currEdge); // nextPath to include current path and current edge
                    nodePaths.put(nextNode, nextPath); // Update path to include next node
                    nodesToVisit.add(nextNode); // Add a new node for searching
                }
            }
        }

        // Never got to destination, so no path
        if (output != null)
            output.println("no path found");
        return null;
    }
}