package sol;

import src.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class TravelController implements ITravelController<City, Transport> {

    private TravelGraph graph;

    public TravelController() {
    }

    /**
     * A method for creating a TravelGraph object from a csv of
     * cities and transports
     * @param citiesFile    the filename of the cities csv
     * @param transportFile the filename of the transports csv
     * @return - A string indicating whether the input files were
     * successfully parsed or there was an error
     */
    @Override
    public String load(String citiesFile, String transportFile) {

        this.graph = new TravelGraph();
        TravelCSVParser parser = new TravelCSVParser();

        // a function to serve as the input to parseLocations
        Function<Map<String, String>, Void> addVertex = map -> {
            this.graph.addVertex(new City(map.get("name")));
            return null; // need explicit return null to account for Void type
        };

        // a function to serve as the input to parseTransportation
        Function<Map<String, String>, Void> addEdge = map -> {
            City origin = this.graph.getCities().get(map.get("origin"));
            City dest = this.graph.getCities().get(map.get("destination"));
            TransportType transport =
                    TransportType.fromString(map.get("type"));
            // can I use parseDouble ???????????????????
            double price = Double.parseDouble(map.get("price"));
            double duration = Double.parseDouble(map.get("duration"));
            this.graph.addEdge(origin,
                    new Transport(origin, dest, transport, price, duration));
            return null;
        };

        try {
            /* pass in string for CSV and function to
            create City (vertex) using city name */
            parser.parseLocations(citiesFile, addVertex);
            parser.parseTransportation(transportFile, addEdge);
        } catch (IOException e) {
            return "Error parsing file: " + citiesFile;
        }

        return "Successfully loaded cities and transportation files.";
    }

    /**
     * returns the fastest route between two cities
     * @param source      the string representation of the source city
     * @param destination the string representation the destination city
     * @return - A list of transports corresponding to the path with the
     * lowest total travel time
     */
    @Override
    public List<Transport> fastestRoute(String source, String destination) {
        Function<Transport, Double> getDuration = transport -> {
            return transport.getMinutes();
        };
        Dijkstra<City, Transport> dijkstra = new Dijkstra<>();
        return dijkstra.getShortestPath(graph, graph.getCityFromName(source),
                graph.getCityFromName(destination), getDuration);
    }

    /**
     * returns the cheapest route between two cities
     * @param source      the string representation of the source city
     * @param destination the string representation of the destination city
     * @return - A list of transports corresponding to the path with the
     * lowest total price
     */
    @Override
    public List<Transport> cheapestRoute(String source, String destination) {
        Function<Transport, Double> getCost = transport -> {
            return transport.getPrice();
        };
        Dijkstra<City, Transport> dijkstra = new Dijkstra<>();
        return dijkstra.getShortestPath(graph, graph.getCityFromName(
                source), graph.getCityFromName(destination), getCost);
    }

    /**
     * returns the most direct route between two cities
     * @param source      the string representation of the source city
     * @param destination the string representation of the destination city
     * @return - A list of transports corresponding to the path with the
     * fewest number of connections
     */
    @Override
    public List<Transport> mostDirectRoute(String source, String destination) {
        BFS<City, Transport> bfs = new BFS<>();
        return bfs.getPath(graph, graph.getCityFromName(source),
                graph.getCityFromName(destination));
    }

    /**
     * only used for testing. determines the total travel time
     * of a given path
     * @param edges - a list of transports representing a path
     * @return - a double representing the total travel time of
     * the input path
     */
    public double getTotalEdgeWeightTime(List<Transport> edges) {
        double total = 0;
        for (Transport edge : edges) {
            total = edge.getMinutes() + total;
        }
        return total;
    }

    /**
     * only used for testing. determines the total travel price
     * of a given path
     * @param edges - a list of transports representing a path
     * @return - a double representing the total travel price of
     * the input path
     */
    public double getTotalEdgeWeightPrice(List<Transport> edges) {
        double total = 0;
        for (Transport edge : edges) {
            total = edge.getPrice() + total;
        }
        return total;
    }

    /**
     * only used for testing. a getter for the graph field
     * @return - the graph field of this TravelController
     */
    public TravelGraph getGraph() {
        return this.graph;
    }
}

