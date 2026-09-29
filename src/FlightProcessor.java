import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Handles route requests: reads them from a file, finds every route for each one, ranks the
 * routes, and writes the best results to the output file.
 *
 * <p>Expected request file format: the first line is the number of requests, followed by one
 * request per line as {@code Origin|Destination|Criterion}, where the criterion is
 * {@code T} (rank by time) or {@code C} (rank by cost).
 */
class FlightProcessor {

    /**
     * Processes every request in a file and writes the top routes for each to the output file.
     *
     * <p>Requests that name a city not present in the graph are skipped.
     *
     * @param graph    the flight graph to search
     * @param filename path to the request file
     */
    public void processFileAndFlightRequest(GraphManager graph, String filename) {
        List<String[]> requestedFlights = readFileWithRequestedFlights(filename);
        int flightCounter = 1;
        for (String[] request : requestedFlights) {
            String originCity = request[0];
            String destinationCity = request[1];
            String var = request[2];
            if (graph.findCity(originCity) != null && graph.findCity(destinationCity) != null) {
                List<FlightPath> validPaths = findFlightPaths(graph, originCity, destinationCity);
                boolean sortByTime = var.equals("T");
                FlightProcessorHelper.insertionSort(validPaths, sortByTime);
                FlightProcessorHelper.print(validPaths, originCity, destinationCity, var,
                        flightCounter++);
            }
        }
    }

    /**
     * Reads route requests from a file.
     *
     * <p>If the file cannot be found, a message is printed and an empty list is returned.
     *
     * @param filename path to the request file
     * @return one {@code String[]} per request: {@code {origin, destination, criterion}}
     */
    public List<String[]> readFileWithRequestedFlights(String filename) {
        List<String[]> flights = new ArrayList<>();
        try {
            File obj = new File(filename);
            Scanner infile = new Scanner(obj);
            int numLines = Integer.parseInt(infile.nextLine());
            for (int i = 0; i < numLines; i++) {
                if (infile.hasNextLine()) {
                    String line = infile.nextLine();
                    String[] split = line.split("\\|");
                    flights.add(split);
                } else {
                    System.out.println("File was read incorrectly");
                    break;
                }
            }
            infile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
        }
        return flights;
    }

    /**
     * Finds every route between two cities that does not visit any city more than once.
     *
     * <p>Uses an iterative depth-first search. Each stack entry is a {@link PathState} holding
     * a partial route, so branching creates a new state rather than mutating a shared one.
     * The returned routes are in discovery order and are not sorted.
     *
     * @param graph           the flight graph to search
     * @param originCity      the departure city (must exist in the graph)
     * @param destinationCity the arrival city
     * @return all routes from origin to destination, each with its total time and cost
     */
    public List<FlightPath> findFlightPaths(GraphManager graph, String originCity,
                                            String destinationCity) {
        CityNode originNode = graph.findCity(originCity);
        List<String> pathSoFar = new ArrayList<>();
        pathSoFar.add(originCity);
        Set<String> visited = new HashSet<>();
        visited.add(originCity);
        PathState firstCity = new PathState(originNode, pathSoFar, visited, 0, 0);
        Stack allFlights = new Stack();
        List<FlightPath> validPaths = new ArrayList<>();
        allFlights.push(firstCity);
        while (!allFlights.isEmpty()) {
            PathState current = allFlights.pop();
            if (current.currentCity.name.equals(destinationCity)) {
                // Destination reached: record the route and stop extending it.
                FlightPath complete = new FlightPath(current.pathSoFar, current.totalTime,
                        current.totalCost);
                validPaths.add(complete);
                continue;
            } else {
                // Branch into every neighboring city that this route has not visited yet.
                FlightNode neighbor = current.currentCity.destinations;
                while (neighbor != null) {
                    String nextCity = neighbor.destination;
                    if (!current.visited.contains(nextCity)) {
                        PathState newBranch = FlightProcessorHelper.createNewBranch(graph,
                                current, neighbor, nextCity);
                        allFlights.push(newBranch);
                    }
                    neighbor = neighbor.nextDestination;
                }
            }
        }

        return validPaths;
    }
}