/**
 * An edge in the flight graph, representing a single one-way flight.
 *
 * <p>Flights leaving the same city are kept in a singly linked list.
 */
class FlightNode {

    /** Name of the departure city. */
    String origin;

    /** Name of the arrival city. */
    String destination;

    /** Travel time of this flight. */
    int time;

    /** Cost of this flight. */
    int cost;

    /** Next flight departing from the same origin, or {@code null} at the tail. */
    FlightNode nextDestination;

    /**
     * Creates a flight with no successor in the list.
     *
     * @param origin      the departure city
     * @param destination the arrival city
     * @param time        the travel time
     * @param cost        the cost
     */
    public FlightNode(String origin, String destination, int time, int cost) {
        this.origin = origin;
        this.destination = destination;
        this.nextDestination = null;
        this.time = time;
        this.cost = cost;
    }
}