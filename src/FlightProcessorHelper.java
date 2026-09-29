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
     * Appends a formatted results table for one request to the output file.
     *
     * <p>Each request is written as a titled block with a ranked table of up to three routes.
     * If no route exists, the block says so instead of showing a table.
     *
     * @param validPaths      routes for this request, already sorted
     * @param originCity      the departure city
     * @param destinationCity the arrival city
     * @param sortCriterion   {@code "T"} for time or {@code "C"} for cost
     * @param flightNumber    the request's position in the input file, starting at 1
     */
    public static void print(List<FlightPath> validPaths, String originCity,
                             String destinationCity, String sortCriterion, int flightNumber) {
        int numToPrint = Math.min(3, validPaths.size());
        String criterion = sortCriterion.equals("T") ? "time" : "cost";
        try {
            File obj = new File("sample_output/output.txt");
            FileWriter out = new FileWriter(obj, true);
            out.write("Request " + flightNumber + ": " + originCity + " to " + destinationCity
                    + "\n");
            out.write("Ranked by " + criterion + " | Showing " + numToPrint + " of "
                    + validPaths.size() + " routes\n");
            if (validPaths.isEmpty()) {
                out.write("No route available.\n\n");
                System.out.println("No valid flight path available between " + originCity
                        + " and " + destinationCity);
            } else {
                // Size the route column to the longest route shown so the table stays aligned.
                int routeWidth = "Route".length();
                for (int g = 0; g < numToPrint; g++) {
                    routeWidth = Math.max(routeWidth,
                            String.join(" -> ", validPaths.get(g).path).length());
                }
                String rowFormat = "%-6s%-" + (routeWidth + 3) + "s%7s%7s\n";
                String divider = repeat('-', 6 + routeWidth + 3 + 14);
                out.write(divider + "\n");
                out.write(String.format(rowFormat, "Rank", "Route", "Time", "Cost"));
                out.write(divider + "\n");
                for (int g = 0; g < numToPrint; g++) {
                    FlightPath fp = validPaths.get(g);
                    out.write(String.format(rowFormat, g + 1, String.join(" -> ", fp.path),
                            fp.totalTime, fp.totalCost));
                }
                out.write(divider + "\n\n");
            }
            out.close();
            System.out.println("Flight paths written to sample_output/output.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    /**
     * Builds a string by repeating a character.
     *
     * @param ch    the character to repeat
     * @param count how many times to repeat it
     * @return the repeated string
     */
    private static String repeat(char ch, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        return sb.toString();
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