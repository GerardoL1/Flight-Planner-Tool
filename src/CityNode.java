/**
 * A vertex in the flight graph, representing a single city.
 *
 * <p>Cities are kept in a singly linked list. Each city also holds the head of a
 * linked list of {@link FlightNode} objects describing its outgoing flights.
 */
class CityNode {

    /** Name of the city. */
    String name;

    /** Head of this city's list of outgoing flights, or {@code null} if it has none. */
    FlightNode destinations;

    /** Next city in the graph's list of cities, or {@code null} at the tail. */
    CityNode nextCity;

    /**
     * Creates a city with no flights and no successor.
     *
     * @param name the city name
     */
    public CityNode(String name) {
        this.name = name;
        this.destinations = null;
        this.nextCity = null;
    }
}