import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Builds a {@link GraphManager} from a flight data file.
 *
 * <p>Expected file format: the first line is the number of flights, followed by one flight
 * per line as {@code Origin|Destination|Time|Cost}.
 */
class GraphBuilder {

    /** The graph being built. */
    GraphManager graph = new GraphManager();

    /**
     * Reads flights from a file and adds them to the graph as bidirectional routes.
     *
     * <p>If the file cannot be found, a message is printed and the graph is returned empty.
     *
     * @param filename path to the flight data file
     * @return the populated graph
     */
    public GraphManager buildGraphUsingFile(String filename) {
        try {
            File obj = new File(filename);
            Scanner infile = new Scanner(obj);
            int numLines = Integer.parseInt(infile.nextLine());
            for (int i = 0; i < numLines; i++) {
                if (infile.hasNextLine()) {
                    String line = infile.nextLine();
                    String split[] = line.split("\\|");
                    String originCity = split[0];
                    String destinationCity = split[1];
                    int time = Integer.parseInt(split[2]);
                    int cost = Integer.parseInt(split[3]);
                    graph.addCity(originCity);
                    graph.addCity(destinationCity);
                    // Add both directions so every route can be flown either way.
                    graph.addFlight(originCity, destinationCity, time, cost);
                    graph.addFlight(destinationCity, originCity, time, cost);
                } else {
                    System.out.println("File was read incorrecly");
                    break;
                }
            }
            infile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
        }
        return graph;
    }
}