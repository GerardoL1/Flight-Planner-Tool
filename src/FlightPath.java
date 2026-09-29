import java.util.List;

/**
 * A completed route between two cities, with its total travel time and cost.
 */
class FlightPath {

    /** Ordered city names from origin to destination. */
    List<String> path;

    /** Total travel time across all flights on the route. */
    int totalTime;

    /** Total cost across all flights on the route. */
    int totalCost;

    /**
     * Creates a completed route.
     *
     * @param path      ordered city names from origin to destination
     * @param totalTime total travel time
     * @param totalCost total cost
     */
    public FlightPath(List<String> path, int totalTime, int totalCost) {
        this.path = path;
        this.totalTime = totalTime;
        this.totalCost = totalCost;
    }
}