package test;

import org.junit.Assert;
import org.junit.Test;
import sol.Dijkstra;
import sol.TravelController;
import src.IDijkstra;
import src.Transport;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DijkstraTest {

    private static final double DELTA = 0.001;

    private SimpleGraph graph;
    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);

        this.graph.addEdge(this.a,
                new SimpleEdge(100, this.a, this.b));
        this.graph.addEdge(this.a,
                new SimpleEdge(3, this.a, this.c));
        this.graph.addEdge(this.a,
                new SimpleEdge(1, this.a, this.e));
        this.graph.addEdge(this.c,
                new SimpleEdge(6, this.c, this.b));
        this.graph.addEdge(this.c,
                new SimpleEdge(2, this.c, this.d));
        this.graph.addEdge(this.d,
                new SimpleEdge(1, this.d, this.b));
        this.graph.addEdge(this.d,
                new SimpleEdge(5, this.d, this.e));
    }

    @Test
    public void testSimple() {
        this.createSimpleGraph();

        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        // a -> c -> d -> b
        List<SimpleEdge> path =
                dijkstra.getShortestPath(
                        this.graph, this.a, this.b, edgeWeightCalculation);
        assertEquals(6, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(3, path.size());

        // c -> d -> b
        path = dijkstra.getShortestPath(
                this.graph, this.c, this.b, edgeWeightCalculation);
        assertEquals(3, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(2, path.size());
    }

    @Test
    public void testComplex() {
        TravelController tc = new TravelController();
        tc.load("data/cities3.csv",
                "data/transport3.csv");
        List<Transport> path = tc.cheapestRoute("Chicago",
                "Scottsdale");
        System.out.println(path);

        // tests general cheapest path case
        assertEquals(2, path.size());
        assertEquals(200, tc.getTotalEdgeWeightPrice(path), 0.001);

        // tests general fastest path case
        path = tc.fastestRoute("Chicago", "Scottsdale");
        assertEquals(1, path.size());
        assertEquals(250, tc.getTotalEdgeWeightPrice(path), 0.001);

        // tests fastest when source = destination
        path = tc.fastestRoute("Chicago", "Chicago");
        assertEquals(0, path.size());

        // tests cheapest when source = destination
        path = tc.cheapestRoute("Chicago", "Chicago");
        assertEquals(0, path.size());

        /* tests fastest when source = destination but there is a loop that
         allows travel back to the source (LA -> San Fran -> LA) */
        tc.load("data/cities5.csv", "data/transport5.csv");
        path = tc.fastestRoute("LA", "LA");
        assertEquals(0, path.size());

        /* tests cheapest when source = destination but there is a loop that
         allows travel back to the source (LA -> San Fran -> LA) */
        path = tc.cheapestRoute("LA", "LA");
        assertEquals(0, path.size());

        // Test Errors Below
        tc.load("data/cities3.csv", "data/transport3.csv");
        // tests that finding the cheapest route from a City with no outgoing edges returns empty
        Assert.assertEquals(tc.cheapestRoute(
                "New York", "Chicago").isEmpty(), true);

        // tests that finding the fastest route from a City with no outgoing edges returns empty
        Assert.assertEquals(tc.fastestRoute("New York", "Chicago").isEmpty(), true);

        // tests that entering a city that does not exist throws an exception (cheap)
        Exception e3 = Assert.assertThrows(IllegalArgumentException.class,
                () -> tc.cheapestRoute("Atlantis", "Narnia"));
        Assert.assertEquals("" +
                "Entered city does not exist", e3.getMessage());

        // tests that entering a city that does not exist throws an exception (fast)
        Exception e4 = Assert.assertThrows(IllegalArgumentException.class,
                () -> tc.fastestRoute("Atlantis", "Narnia"));
        Assert.assertEquals(
                "Entered city does not exist", e4.getMessage());
    }
}


