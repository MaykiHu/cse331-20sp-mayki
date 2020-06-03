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

    // Commands to communicate from client to the marvel universe
    public static void main(String[] args) {
        PrintWriter output = new PrintWriter(System.out, true);
        Scanner userInput = new Scanner(System.in);
        introPrompt(output); // Tells user how to use program
        interact(output, userInput); // User interacts with program
    }

    /*
     *  Introduction to user on how to use program
     */
    private static void introPrompt(PrintWriter output) {
        output.println("Hey there, I'm Marvel Path.");
        output.println("I can help you find a path between two characters!");
        output.println("The catch is, I've only studied at Marvel University..");
        output.println("So I only am familiar with Marvel characters. Yay~ Marvel-U!!");
        output.println("You can try me with other characters though.");
        output.println("I'll still try to find them, but no guarantees that I'll find them.  :P");
        output.println("I'm kinda sassy and prefer you know the correct Marvel name format");
        output.println("So at least capitalize how the Marvel characters would be addressed.");
        output.println("I'll be nice and handle spacing or underscores for you.  Your choice.");
        output.println("If you use spaces, use spaces.  Don't mix spaces with underscores.");
        output.println("For example: ANCIENT ONE or ANCIENT_ONE is ok, but not aNcIeNt OnE!");
        output.println("Leave your meme formatting elsewhere or I won't find them for you!  >:(");
    }

    /*
     * Interacts with the user through console to find a path between two characters
     * @param output where the results are printed to
     * @param userInput where the user responds through
     */
    private static void interact(PrintWriter output, Scanner userInput) {
        DirectedGraph<String, String> marvelUniverse = setupUniverse(fileName);
        // Obtain user input
        output.println("Who's the first character leading the path?");
        String startChar = userInput.nextLine();
        output.println("Who's the second character at the end of the path?");
        String endChar = userInput.nextLine();
        output.println("Thanks.  Lemme look back into my Marvel-U history notes.");

        // Format user input and search
        startChar = format(startChar);
        endChar = format(endChar);
        findPath(marvelUniverse, startChar, endChar, output);

        // Keep interacting until quit
        output.println("Would you require any more assistance?");
        boolean keepRunning = true;
        String response = null;
        while (keepRunning) {
            output.println("Type case-sensitive Y or y for more path help, N or n if you're done.");
            response = userInput.nextLine();
            boolean respondedYes = response.equalsIgnoreCase("Y");
            boolean respondedNo = response.equalsIgnoreCase("N");
            if (respondedYes) {
                keepRunning = false;
                interact(output, userInput);
            } else if (respondedNo) {
                output.println("Well, have a good day.  I'll get going then.  See ya next time.");
                output.println("You can call me again when you need me.");
                keepRunning = false;
                System.exit(0); // Quit program
            } else { // Invalid response, not yes or no to keep program running.. so keep running
                output.println("Hey!!  I said type Y/y or N/n.  Like, c'mon!  >:(");
            }
        }
    }

    /*
     * Formats the charName string into one accepted by marvel parsing
     * @param charName the string of the marvel character
     * @return a String; the formatted string of a marvel character
     * @spec.requires charName must only have underscores, letters and no spaces
     *                or spaces, letters, and no underscores
     */
    private static String format(String charName) {
        if (charName.contains("_")) {
            return charName.replaceAll("_", " "); // proper lookup format for " " as "_"
        }
        return charName;
    }

    /*
     *  Sets up graph with data on heroes and comics from specified file name
     *  and returns the set up graph
     *  @param fileName, String name of the file to set up graph data from
     *  @spec.requires fileName is a valid file within Marvel's data folder
     *  @return a DirectedGraph; a graph of the marvel universe specified from the file
     */
    public static DirectedGraph<String, String> setupUniverse(String fileName) {
        DirectedGraph<String, String> universe = new DirectedGraph<>();
        // data is in the form: comic books and heroes associated
        Map<String, Set<String>> data = MarvelParser.parseData(fileName);
        // Titles of comics and heroes associated with them

        Iterator<Map.Entry<String, Set<String>>> titleItr = data.entrySet().iterator();
        while (titleItr.hasNext()) { // For each title
            Map.Entry<String, Set<String>> titleEntry = titleItr.next();
            Set<String> commonHeroesList = titleEntry.getValue();
            Iterator<String> commonHeroesItr = commonHeroesList.iterator();
            while (commonHeroesItr.hasNext()) {
                Node<String> startHero = new Node<String>(commonHeroesItr.next());
                universe.addNode(startHero);
                for (String commonHero : commonHeroesList) { // For every hero in common with title
                    String label = titleEntry.getKey(); // Label of edge is title of comic
                    Node<String> endHero = new Node<String>(commonHero);
                    universe.addNode(endHero);
                    Edge<String, String> toEdge = new Edge<String, String>(startHero, endHero, label);
                    Edge<String, String> fromEdge = new Edge<String, String>(endHero, startHero, label);
                    universe.addEdge(toEdge);
                    universe.addEdge(fromEdge);
                }
            }
        }
        return universe;
    }

    /*
     *  Given the names of two characters, searches and returns a path through the graph
     *  connecting them.  Returns the lexicographically least path; null if no or invalid path,
     *  and prints output if specified to a printer; null for printer means no printing.
     *  If printed, one expects below behavior:
     *  Cases:
     *      (return null)
     *      If no path found, we get:
     *      path from CHAR 1 to CHAR N:
     *      no path found
     *
     *      (return null)
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
     *  @spec.requires if using PrintWriter of System.out, remember to enter true to flush
     */
    public static List<Edge<String, String>> findPath(DirectedGraph<String, String> universe, String startChar,
                                      String endChar, PrintWriter output) {
        Node<String> start = new Node<String>(startChar);
        Node<String> dest = new Node<String>(endChar);
        // Case(s) where characters aren't in the graph
        if (!universe.containsNode(start) || !universe.containsNode(dest)) {
            String unknownFormat = "unknown character ";
            if (!universe.containsNode(start) && output != null) {
                output.println(unknownFormat + startChar);
            }
            if (!universe.containsNode(dest) && output != null) {
                output.println(unknownFormat + endChar);
            }
            return null;
        }

        String pathFormat = "path from " + startChar + " to " + endChar + ":";
        Queue<Node<String>> nodesToVisit = new LinkedList<>();
        Map<Node<String>, List<Edge<String, String>>> nodePaths = new HashMap<>();
        nodesToVisit.add(start);
        nodePaths.put(start, new ArrayList<>());
        if (output != null)
            output.println(pathFormat); // Prints to tell client path is searchable

        // Start/Keep searching through applicable nodes
        while (!nodesToVisit.isEmpty()) { // Nodes still to be checked
            Node<String> currNode = nodesToVisit.remove();
            if (currNode.equals(dest)) { // Reached destination node
                List<Edge<String, String>> destPath = nodePaths.get(currNode);
                for (Edge<String, String> edge : destPath) { // Print path for client
                    String currChar = edge.getStart().toString();
                    String nextChar = edge.getEnd().toString();
                    String book = edge.getLabel().toString();
                    if (output != null)
                        output.println(currChar + " to " + nextChar + " via " + book);
                }
                return destPath;
            } // Continue searching through edges of this node
            Set<Edge<String, String>> sortedEdges = new TreeSet<>(new EdgeComp());
            sortedEdges.addAll(universe.listChildren(currNode, false)); // no reflexive
            for (Edge<String, String> currEdge : sortedEdges) {
                Node<String> nextNode = currEdge.getEnd();
                if (!nodePaths.containsKey(nextNode)) { // nextNode has not been visited
                    List<Edge<String, String>> currPath = nodePaths.get(currNode);
                    List<Edge<String, String>> nextPath = new ArrayList<>(currPath);
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

    /*
        Computes how to compare between two edges
     */
    private static class EdgeComp implements Comparator<Edge<String, String>> {
        public int compare(Edge<String, String> edge1, Edge<String, String> edge2) {
            // Since toString() represents each edge/node distinctly, we can compare via toString()
            // We have to rearrange the toString() such that it evaluates
            // startNode -> endNode -> label
            String e1 = edge1.getStart().toString() + edge1.getEnd().toString() + edge1.getLabel();
            String e2 = edge2.getStart().toString() + edge2.getEnd().toString() + edge2.getLabel();
            return e1.compareTo(e2);
        }
    }
}