package marvel;

import graph.DirectedGraph;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

/*
 *  This app allows a user to model (a graph of) the marvel universe
 */
public class MarvelPaths {
    private static final String fileName = "marvel.tsv"; // The data file on marvel heroes
    private static Graph universe; // The marvel universe graph

    /*
     *  Where the AF would go, but this isn't an ADT bc we are using as a client program
     *  Where the rep invariant would go
     */

    // Commands to communicate from client to the model universe
    public static void main(String[] args) {
        setupUniverse();
    }

    /*
     *  Sets up graph with data on heroes and comics from specified file
     */
    public static void setupUniverse() {
        universe = new DirectedGraph();
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
    }

    /*
     *  Given the names of two characters, searches and returns a path through the graph
     *  connecting them.  Returns the lexicographically least path, null if no path
     *  and prints output below:
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
     */
    public List<Edge> findPath(String startChar, String endChar) {
        Node start = new Node(startChar);
        Node dest = new Node(endChar);
        // Case(s) where characters aren't in the graph
        if (!universe.containsNode(start) || !universe.containsNode(dest)) {
            String unknownFormat = "unknown character ";
            if (!universe.containsNode(start)) {
                System.out.println(unknownFormat + startChar);
            }
            if (!universe.containsNode(dest)) {
                System.out.println(unknownFormat + endChar);
            }
            return null;
        }

        String pathFormat = "path from " + startChar + " to " + endChar + ":";
        Queue<Node> nodesToVisit = new LinkedList<>();
        Map<Node, List<Edge>> nodePaths = new HashMap<>();
        nodesToVisit.add(start);
        nodePaths.put(start, new ArrayList<>());
        System.out.println(pathFormat); // Prints to tell client path is searchable

        // Start/Keep searching through applicable nodes
        while (!nodesToVisit.isEmpty()) { // Nodes still to be checked
            Node currNode = nodesToVisit.remove();
            if (currNode.equals(dest)) { // Reached destination node
                List<Edge> destPath = nodePaths.get(currNode);
                for (Edge edge : destPath) { // Print path for client
                    String currChar = edge.getStart().toString().substring(1); // Removes prefix
                    String nextChar = edge.getEnd().toString().substring(1);   // 'n' for node
                    String book = edge.getLabel();
                    System.out.println(currChar + " to " + nextChar + " via " + book);
                }
                return destPath;
            } // Continue searching through edges of this node
            Set<Edge> sortedEdges = new TreeSet<>(new EdgeComp());
            sortedEdges.addAll(((DirectedGraph) universe).getEdges(currNode));
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
        System.out.println("no path found");
        return null;
    }

    /*
        Computes how to compare between two edges
     */
    private class EdgeComp implements Comparator<Edge> {
        public int compare(Edge edge1, Edge edge2) {
            // Since toString() represents each edge/node distinctly, we can compare via toString()
            // We have to rearrange the toString() such that it evaluates
            // startNode -> endNode -> label
            String e1 = edge1.getStart().toString() + edge1.getEnd().toString() + edge1.getLabel();
            String e2 = edge2.getStart().toString() + edge2.getEnd().toString() + edge2.getLabel();
            return e1.compareTo(e2);
        }
    }
}