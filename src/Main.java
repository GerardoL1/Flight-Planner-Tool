import java.io.FileWriter;
import java.io.IOException;

/**
 * Entry point for the flight route planner.
 *
 * <p>Builds a flight graph from {@code data/flights.txt}, processes the route requests in
 * {@code data/requests.txt}, and writes the top itineraries for each request to
 * {@code sample_output/output.txt}.
 */
public class Main {

    /**
     * Runs the planner.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        // Truncate the output file so results from a previous run are not kept.
        try {
            new FileWriter("sample_output/output.txt", false).close();
        } catch (IOException e) {
            System.out.println("Failed to clear output.txt at start.");
            e.printStackTrace();
        }
        GraphManager graph = new GraphManager();
        GraphBuilder buildGraph = new GraphBuilder();
        graph = buildGraph.buildGraphUsingFile("data/flights.txt");
        FlightProcessor processorOfFlights = new FlightProcessor();
        processorOfFlights.processFileAndFlightRequest(graph, "data/requests.txt");
    }
}
