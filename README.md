# Flight Route Planner

A Java program that finds every possible route between two cities and ranks the top three itineraries by **travel time** or **cost**. Built from scratch with a custom graph, a custom stack, and a hand-written sort, with no graph or sorting libraries.

## Features

- Builds an **undirected weighted graph** from a flight data file (13 cities, 25 routes in the included dataset)
- Uses an **iterative depth-first search** with a custom stack to enumerate every simple path (no repeated cities) between an origin and destination
- Ranks the results with **insertion sort** by total time or total cost, per request
- Processes a batch of requests from a file and writes the top 3 itineraries for each to an output file

## Data Structures and Concepts

| Concept | Where it appears |
| --- | --- |
| Adjacency list (linked list of cities, each with a linked list of flights) | `GraphManager`, `CityNode`, `FlightNode` |
| Custom stack (LIFO) | `Stack` |
| Depth-first search with path tracking | `FlightProcessor.findFlightPaths` |
| Insertion sort | `FlightProcessorHelper.insertionSort` |
| File I/O and input parsing | `GraphBuilder`, `FlightProcessor`, `FlightProcessorHelper` |

## Project Structure

```
flight-route-planner/
├── src/
│   ├── Main.java                   Entry point
│   ├── CityNode.java               Graph vertex (a city)
│   ├── FlightNode.java             Graph edge (a flight with time and cost)
│   ├── GraphManager.java           Adjacency list: add/find cities, add flights
│   ├── GraphBuilder.java           Builds the graph from the data file
│   ├── PathState.java              Snapshot of a partial route during the search
│   ├── Stack.java                  Custom stack of PathState objects
│   ├── FlightPath.java             A completed route with its total time and cost
│   ├── FlightProcessor.java        Reads requests, runs the search, ranks results
│   └── FlightProcessorHelper.java  Sorting, output writing, branch creation
├── data/
│   ├── flights.txt                 Available flights
│   └── requests.txt                Route requests to process
├── sample_output/
│   └── output.txt                  Output from the included data
├── .gitignore
└── README.md
```

## Requirements

- Java Development Kit (JDK) 8 or newer

Check your version with `java -version` and `javac -version`.

## How to Run

From the project root folder:

```
javac -d out src/*.java
java -cp out Main
```

The first command compiles the source into an `out` folder. The second runs the program. Results are written to `sample_output/output.txt`, replacing the file each time the program runs.

## Input Format

**`data/flights.txt`**: the first line is the number of flights. Each following line is one flight:

```
Origin|Destination|Time|Cost
```

Every flight is treated as bidirectional, so `Dallas|Chicago|108|147` also creates a Chicago to Dallas flight with the same time and cost.

**`data/requests.txt`**: the first line is the number of requests. Each following line is one request:

```
Origin|Destination|Criterion
```

The criterion is `T` to rank by time or `C` to rank by cost.

## Example

Given the requests `Dallas|New York|T` and `Dallas|Charlotte|C`, the program writes:

```
Request 1: Dallas to New York
Ranked by time | Showing 3 of 87 routes
------------------------------------------------------
Rank  Route                                Time   Cost
------------------------------------------------------
1     Dallas -> New York                    209    189
2     Dallas -> Chicago -> New York         233    300
3     Dallas -> Charlotte -> New York       255    380
------------------------------------------------------

Request 2: Dallas to Charlotte
Ranked by cost | Showing 3 of 49 routes
------------------------------------------------------
Rank  Route                                Time   Cost
------------------------------------------------------
1     Dallas -> Charlotte                   137    209
2     Dallas -> Chicago -> Charlotte        223    321
3     Dallas -> New York -> Charlotte       327    360
------------------------------------------------------
```

The complete output for the included dataset is in [`sample_output/output.txt`](sample_output/output.txt).

## How It Works

1. `GraphBuilder` reads `flights.txt` and builds the adjacency list, adding each flight in both directions.
2. For each request, `FlightProcessor` starts a depth-first search from the origin. Each stack entry (`PathState`) stores the current city, the path so far, the set of visited cities, and the running time and cost.
3. Popping a state either completes a route (the current city is the destination) or pushes a new state for every unvisited neighboring city.
4. When the stack is empty, every simple route has been found. They are sorted with insertion sort by the requested criterion, and the top three are written to the output file.