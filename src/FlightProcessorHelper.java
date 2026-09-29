import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Static helpers used by {@link FlightProcessor}: sorting routes, writing results, and
 * extending a partial route by one flight.
 */
class FlightProcessorHelper {

    /**
     * Sorts routes in ascending order by total time or total cost, in place.
     *
     * <p>Insertion sort: stable, O(n^2) comparisons in the worst case.
     *
     * @param validPaths the routes to sort
     * @param sortByTime {@code true} to sort by total time, {@code false} to sort by total cost
     */
    public static void insertionSort(List<FlightPath> validPaths, boolean sortByTime) {
        for (int i = 1; i < validPaths.size(); i++) {
            FlightPath key = validPaths.get(i);
            int j = i - 1;
            while (j >= 0 && ((sortByTime && validPaths.get(j).totalTime > key.totalTime)
                    || (!sortByTime && validPaths.get(j).totalCost > key.totalCost))) {
                // Shift larger elements one position right to open a slot for the key.
                validPaths.set(j + 1, validPaths.get(j));
                j--;
            }
            validPaths.set(j + 1, key);
        }
    }

    /**
     * Appends the best routes for one request to the output file.
     *
     * <p>Writes up to three routes, or fewer if fewer exist. If there are none, a message is
     * printed to the console.
     *
     * @param validPaths      routes for this request, already sorted
     * @param originCity      the departure city
     * @param destinationCity the arrival city
     * @param sortCriterion   {@code "T"} for time or {@code "C"} for cost
     * @param flightNumber    the request's position in the input file, starting at 1
     */
    public static void print(List<FlightPath> validPaths, String originCity,
                             String destinationCity, String sortCriterion, int flightNumber) {
        int numToPrint = 3;
        if (validPaths.size() < 3) {
            numToPrint = validPaths.size();
        }
        try {
            File obj = new File("sample_output/output.txt");
            FileWriter out = new FileWriter(obj, true);
            out.write("Flight " + flightNumber + ": " + originCity + ", " + destinationCity + " ("
                    + (sortCriterion.equals("T") ? "Time" : "Cost") + ")\n");
            for (int g = 0; g < numToPrint; g++) {
                FlightPath fp = validPaths.get(g);
                out.write("Path " + (g + 1) + ": " + String.join(" -> ", fp.path)
                        + ". | Time: " + fp.totalTime + " | Cost: " + fp.totalCost + "\n");
            }
            if (validPaths.isEmpty()) {
                System.out.println("No valid flight path available between " + originCity
                        + " and " + destinationCity);
            }

            out.close();
            System.out.println("Flight paths written to output.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    /**
     * Extends a partial route by one flight, returning a new state and leaving the original
     * untouched so other branches of the search are unaffected.
     *
     * @param graph    the flight graph
     * @param current  the partial route being extended
     * @param neighbor the flight to add to the route
     * @param nextCity the city that flight arrives in
     * @return a new {@link PathState} with the extended path and updated totals
     */
    public static PathState createNewBranch(GraphManager graph, PathState current,
                                            FlightNode neighbor, String nextCity) {
        CityNode nextCityNode = graph.findCity(neighbor.destination);
        List<String> newPathSoFar = new ArrayList<>(current.pathSoFar);
        Set<String> newVisited = new HashSet<>(current.visited);
        newPathSoFar.add(nextCity);
        newVisited.add(nextCity);
        int newTime = current.totalTime + neighbor.time;
        int newCost = current.totalCost + neighbor.cost;
        PathState newBranch = new PathState(nextCityNode, newPathSoFar, newVisited, newTime,
                newCost);
        return newBranch;
    }
}