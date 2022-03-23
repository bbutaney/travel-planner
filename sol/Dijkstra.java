package sol;

import src.IDijkstra;
import src.IGraph;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

public class Dijkstra<V, E> implements IDijkstra<V, E> {

    /**
     * returns the path with the lowest total edge weight between
     * two vertices
     * @param graph       the IGraph including the vertices
     * @param source      the source vertex
     * @param destination the destination vertex
     * @param edgeWeight - A Function object that extracts the weight
     *                   (a double) from an edge
     * @return - a list of edges corresponding to the path with the lowest
     * total edge weight between two vertices
     */
    @Override
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight) {
        /*
        makes a HashMap with vertices as both keys and values in which the
        edge from the value to the key is the final edge in the smallest total
        edge-weight path from the source to the key
         */
        HashMap<V, V> cameFromMap = this.dijkstra(graph, source, edgeWeight);
        V currentSpot = destination;
        LinkedList<E> path = new LinkedList<>();
        /*
        traces the smallest weight path from the destination back to the source,
        adding each edge to the path list along the way
         */
        try {
            while (!source.equals(currentSpot)) {
                PriorityQueue<E> edges = this.getConnectingEdges(graph,
                        cameFromMap.get(currentSpot),
                        currentSpot,
                        edgeWeight);
                path.add(edges.peek());
                currentSpot = cameFromMap.get(currentSpot);
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Entered city does not exist");
        } catch (RuntimeException e) {
            List<E> empt = new LinkedList<>();
            return empt;
        }
        return path;
    }

    /**
     * a method for implementing the dijkstra algorithm
     * @param graph - an IGraph containing the source vertex
     * @param source - the source vertex
     * @param edgeWeight - A Function object that extracts the weight
     *                   (a double) from an edge
     * @return - a HashMap with vertices as both keys and values in
     * which the edge from the value
     * to the key is the final edge in the smallest total edge-weight
     * path from the source to the key.
     */
    public HashMap<V, V> dijkstra(IGraph<V, E> graph, V source,
                                  Function<E, Double> edgeWeight) {
        /*
        initiates a HashMap with vertices as both keys and values in which
        the edge from the value to the key is the final edge in the smallest
        total edge-weight path from the source to the key
         */
        HashMap<V, V> cameFromMap = new HashMap<>();
        /*
        Initiates a HashMap in which the keys are vertices and
        the values are doubles representing the total edge-weight
        in the smallest edge-weight path from the source to
        the key
         */
        HashMap<V, Double> routeDist = new HashMap<>();
        /*
        sets up a priority queue to order vertices that need to be checked based
        on the smallest known edge-weight path from the source to the vertex.
         */
        Comparator<V> compareVertices = (vert1, vert2) -> {
            return Double.compare(routeDist.get(vert1), routeDist.get(vert2));
        };
        PriorityQueue<V> toCheckQueue = new PriorityQueue<V>(compareVertices);
        /*
        initally places all vertices in graph in routeDist, toCheckQueue,
        and cameFromMap. the route distance from the source to itself
        is 0 so it is considered first in toCheckQueue, and the route
        distance for all other vertices is initialized to infinity.
        In cameFromMap, all vertices are initially linked to
        the source.
         */
        for (V v : graph.getVertices()) {
            if (v.equals(source)) {
                routeDist.put(v, 0.0);
            }
            else {
                routeDist.put(v, Double.POSITIVE_INFINITY);
            }
            toCheckQueue.add(v);
            cameFromMap.put(v, source);
        }

        while (!toCheckQueue.isEmpty()) {
            // removes the highest priority vertex from toCheckQueues
            V checkingV = toCheckQueue.poll();
            /*
            iterates through the outgoing edges of checkingV and determines whether
            the route distance of the path from the source to checkingV plus the edge weight of an
            edge connecting checkingV to one of its neighbors is less than the route distance from
            the source to the neighbor. If this is true, the route distance from the source to the
            neighbor is updated, the previous vertex of the neighbor is updated to checkingV in
            the cameFromMap, and the position of the neighbor is updated in toCheckQueue.
             */
            for (E edge : graph.getOutgoingEdges(checkingV)) {
                double temp = routeDist.get(checkingV);
                V neighbor = graph.getEdgeTarget(edge);
                if (!neighbor.equals(checkingV)) {
                    temp = routeDist.get(checkingV) + edgeWeight.apply(edge);
                }
                if (temp < routeDist.get(neighbor)) {
                    routeDist.replace(neighbor, temp);
                    cameFromMap.replace(neighbor, checkingV);
                    toCheckQueue.remove(neighbor);
                    toCheckQueue.add(neighbor);
                }
            }
        }
        return cameFromMap;
    }

    /**
     * a method for returning a priority queue of edges connecting two vertices in a graph
     * @param graph - an IGraph within which the two vertices reside
     * @param source - the first vertex
     * @param destination - the second vertex
     * @param edgeWeight - a Function that extracts the weight from an edge. used to sort
     *                   edges in the priority queue
     * @return - a priority queue of edges
     */
    private PriorityQueue<E> getConnectingEdges(IGraph<V, E> graph, V source, V destination,
                                                Function<E, Double> edgeWeight) {
        Comparator<E> lowerWeight = (edge1, edge2) -> {
            return Double.compare(edgeWeight.apply(edge1), edgeWeight.apply(edge2));
        };
        PriorityQueue<E> edges = new PriorityQueue<E>(lowerWeight);
        if (source.equals(destination)) {
            return null;
        }
        for (E e : graph.getOutgoingEdges(source)) {
            if (graph.getEdgeTarget(e).equals(destination)) {
                edges.add(e);
            }
        }
        if (!edges.isEmpty()) {
            return edges;
        }
        throw new RuntimeException (
                "No route exists between the two entered cities");
    }
}
