package sol;

import src.City;
import src.IBFS;
import src.IGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class BFS<V, E> implements IBFS<V, E> {

    /**
     * returns the path with the fewest number of connecting edges
     * between two vertices
     * @param graph the IGraph including the vertices
     * @param start the start vertex
     * @param end   the end vertex
     * @return - A list of edges corresponding to the path with the
     * fewest number of connecting edges between the two input
     * vertices
     */
    @Override
    public List<E> getPath(IGraph<V, E> graph, V start, V end) {
        LinkedList<V> toCheck = new LinkedList<>();
        /*
        visited will store the path from the start vertex to the
        vertex serving as the key for every vertex that has been
        visited by bfs
         */
        HashMap<V, LinkedList<E>> visited = new HashMap<>();
        /*
        the value of each KVPair in record corresponds to the previous vertex
        with respect to the key
         */
        HashMap<V, V> record = new HashMap<>();
        /*
        put the start vertex in the toCheck list and
         */
        toCheck.addLast(start);
        LinkedList<E> emptyList = new LinkedList<>();
        /*
        the previous vertex of the start vertex should be
        the start vertex, and the path from the start vertex
        to the start vertex should be empty
         */
        record.put(start, start);
        visited.put(start, emptyList);

        while (!toCheck.isEmpty()) {
            V checkingVertex = toCheck.removeFirst();
            LinkedList<E> newPath = new LinkedList<>();
            /*
            The path associated with checkingVertex in visited will be the
            path from the start vertex to the previous vertex of checkingVertex
            plus the edge from the previous vertex to checkingVertex
             */
            for (E edge : visited.get(record.get(checkingVertex))) {
                newPath.addLast(edge);
            }
            visited.put(checkingVertex, newPath);
            /*
            the if condition is necessary to ensure that null is not added to
            the path from the source vertex to itself. this adds the edge from
            the previous vertex to checkingVertex to the path associated with
            checkingVertex in the visited HashMap
             */
            if (visited.size() > 1) {
                visited.get(checkingVertex).addLast(this.getConnectingEdge(graph,
                        record.get(checkingVertex),
                        checkingVertex));
            }
            /*
            checks whether checkingVertex is equal to the end. If so, the path
            associated with checkingVertex in visited is returned.
             */
            try {
                if (end.equals(checkingVertex)) {
                    return visited.get(checkingVertex);
                }
            } catch(NullPointerException e) {
                throw new IllegalArgumentException(
                        "Entered city does not exist");
            }
            /*
            adds the neighbors of checkingVertex to the toCheck list and assigns
            checkingVertex as the previous node of each neighbor in record. both operations
            are only performed if the neighbor has not been visited yet.
             */
            for (E edge : graph.getOutgoingEdges(checkingVertex)) {
                V edgeTarget = graph.getEdgeTarget(edge);
                if (!visited.containsKey(edgeTarget) && !record.containsKey(edgeTarget)) {
                    toCheck.addLast(edgeTarget);
                    record.put(edgeTarget, checkingVertex);
                }
            }
        }

        List<E> empt = new LinkedList<>();
        return empt;
    }

    /**
     * a method for returning the edge connecting two vertices in a graph
     * @param graph - an IGraph within which the two vertices reside
     * @param source - the first vertex
     * @param destination - the second vertex
     * @return - the edge connecting the two vertices
     */
    private E getConnectingEdge(IGraph<V, E> graph, V source, V destination) {
        if (source.equals(destination)) {
            return null;
        }
        for (E e : graph.getOutgoingEdges(source)) {
            if (graph.getEdgeTarget(e).equals(destination)) {
                return e;
            }
        }
        throw new RuntimeException(
                "No route exists between the two entered cities");
    }
}
