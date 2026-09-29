/**
 * A flight graph stored as an adjacency list.
 *
 * <p>Cities are kept in a singly linked list of {@link CityNode} objects, and each city holds
 * a linked list of {@link FlightNode} objects for its outgoing flights. Flights are one-way
 * here; callers add the reverse flight separately to model a bidirectional route.
 */
class GraphManager {

    /** First city in the graph, or {@code null} if the graph is empty. */
    CityNode head;

    /** Creates an empty graph. */
    public GraphManager() {
        head = null;
    }

    /**
     * Adds a city to the end of the city list. Does nothing if the city already exists.
     *
     * @param name the city name
     */
    public void addCity(String name) {
        CityNode newCity = new CityNode(name);
        CityNode currCity;
        CityNode check = findCity(name);
        if (check == null) {
            if (head == null) {
                head = newCity;
            } else {
                currCity = head;
                while (currCity.nextCity != null) {
                    currCity = currCity.nextCity;
                }
                currCity.nextCity = newCity;
            }
        }
    }

    /**
     * Finds a city by name.
     *
     * @param name the city name to look up
     * @return the matching city, or {@code null} if it is not in the graph
     */
    public CityNode findCity(String name) {
        CityNode current = head;
        while (current != null) {
            if (current.name.equals(name)) {
                return current;
            } else {
                current = current.nextCity;
            }
        }
        return null;
    }

    /**
     * Adds a one-way flight from {@code origin} to {@code destination}.
     *
     * <p>Both cities must already exist in the graph. If a flight between the same pair
     * already exists, the call is ignored.
     *
     * @param origin      the departure city
     * @param destination the arrival city
     * @param time        the travel time
     * @param cost        the cost
     */
    public void addFlight(String origin, String destination, int time, int cost) {

        CityNode originNode = findCity(origin);
        if (originNode == null || findCity(destination) == null) {
            System.out.println("Either the origin or destination city does not exist");
            return;
        }
        FlightNode currFlight = originNode.destinations;
        FlightNode curr = originNode.destinations;
        // Skip the insert if this origin already has a flight to the destination.
        while (curr != null) {
            if (curr.destination.equals(destination)) {
                return;
            }
            curr = curr.nextDestination;
        }
        FlightNode newFlight = new FlightNode(origin, destination, time, cost);
        if (currFlight == null) {
            originNode.destinations = newFlight;
        } else {
            // Append to the tail of the origin's flight list.
            while (currFlight.nextDestination != null) {
                currFlight = currFlight.nextDestination;
            }
            currFlight.nextDestination = newFlight;
        }
    }
}