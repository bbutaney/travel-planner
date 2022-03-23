package sol;

import src.City;
import src.IGraph;
import src.Transport;

import java.util.*;
import java.util.function.Function;

public class TravelGraph implements IGraph<City, Transport> {
    private HashMap<String, City> cities;

    /**
     * a constructor for the TravelGraph class
     */
    public TravelGraph(){
        this.cities = new HashMap<String, City>();
    }

    /**
     * a method to return the cities hashMap
     * @return - the HashMap in the cities field of this TravelGraph
     */
    public HashMap<String, City> getCities() {
        return this.cities;
    }

    /**
     * adds a new City to the cities HashMap
     * @param vertex the vertex
     */
    @Override
    public void addVertex(City vertex) {
        if (this.cities.containsKey(vertex.toString())) {
            throw new IllegalArgumentException("City already in graph");
        }
        this.cities.put(vertex.toString(), vertex);
    }

    /**
     * adds a new Transport to the list of outgoing Transports
     * of a city
     * @param origin the origin of the edge.
     * @param edge - the Transport to be added
     */
    @Override
    public void addEdge(City origin, Transport edge) {
        if (!origin.equals(this.getEdgeSource(edge))) {
            throw new IllegalArgumentException(
                    "Transport origin and edge source must be the same");
        }
        if (origin.equals(this.getEdgeTarget(edge))) {
            throw new IllegalArgumentException(
                    "Transport origin and destination cannot be the same.");
        }
        try {
            this.cities.get(origin.toString()).getOutgoing().add(edge);
        } catch(NullPointerException e) {
            throw new IllegalArgumentException(
                    "Cannot add edge with a city that is not in the graph");
        }
    }

    /**
     * gets all of the vertices in this graph
     * @return - A set of cities representing all of the vertices
     * in this TravelGraph
     */
    @Override
    public Set<City> getVertices() {
        return new HashSet(this.cities.values());
    }

    /**
     * gets the Source city of a given Transport
     * @param edge A transport
     * @return - the source City of the input Transport
     */
    @Override
    public City getEdgeSource(Transport edge) {
        return edge.getSource();
    }

    /**
     * gets the destination (target) city of a given Transport
     * @param edge the transport
     * @return - the target city of the input Transport
     */
    @Override
    public City getEdgeTarget(Transport edge) {
        return edge.getTarget();
    }

    /**
     * gets all of the Transports stemming from a city
     * @param fromVertex a City
     * @return - a Set of all Transports with the input city
     * as the source
     */
    @Override
    public Set<Transport> getOutgoingEdges(City fromVertex) {
        try {
            return fromVertex.getOutgoing();
        } catch(Exception e){
            throw new IllegalArgumentException("No routes extend from " +
                    fromVertex.toString());
        }
    }

    /**
     * returns the City whose name corresponds to the given string
     * @param name - A string representing the name of the city
     * @return - the City whose name is the input string
     */
    public City getCityFromName(String name) {
        try {
            return cities.get(name);
        } catch(Exception e) {
            throw new IllegalArgumentException(name +
                    " is not available");
        }
    }
}