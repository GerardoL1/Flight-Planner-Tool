import java.util.List;
import java.util.Set;

/**
 * A snapshot of a partially built route, used as one stack entry during the depth-first search.
 */
class PathState {

    /** The city the route has reached so far. */
    CityNode currentCity;

    /** Ordered list of city names visited on this route. */
    List<String> pathSoFar;

    /** The same cities as {@code pathSoFar}, for constant-time repeat-visit checks. */
    Set<String> visited;

    /** Accumulated travel time of the route so far. */
    int totalTime;

    /** Accumulated cost of the route so far. */
    int totalCost;

    /**
     * Creates a route snapshot.
     *
     * @param currentCity the city the route has reached
     * @param pathSoFar   ordered city names visited so far
     * @param visited     set of city names visited so far
     * @param totalTime   accumulated travel time
     * @param totalCost   accumulated cost
     */
    public PathState(CityNode currentCity, List<String> pathSoFar, Set<String> visited,
                     int totalTime, int totalCost) {
        this.currentCity = currentCity;
        this.pathSoFar = pathSoFar;
        this.visited = visited;
        this.totalTime = totalTime;
        this.totalCost = totalCost;
    }
}