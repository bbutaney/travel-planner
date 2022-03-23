package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sol.TravelController;
import src.City;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your Graph method tests should all go in this class!
 * The test we've given you will pass, but we still expect you to write more tests using the
 * City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 * TODO: Recreate the test below for the City and Transport classes
 * TODO: Expand on your tests, accounting for basic cases and edge cases
 */
public class GraphTest {
    private SimpleGraph graph;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;

    private SimpleEdge edgeAB;
    private SimpleEdge edgeBC;
    private SimpleEdge edgeCA;
    private SimpleEdge edgeAC;
    private SimpleEdge edgeAA;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     * TODO: create more setup methods!
     */
    private void createSimpleGraph() {

        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("A");
        this.b = new SimpleVertex("B");
        this.c = new SimpleVertex("C");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);

        // create and insert edges
        this.edgeAB = new SimpleEdge(1, this.a, this.b);
        this.edgeBC = new SimpleEdge(1, this.b, this.c);
        this.edgeCA = new SimpleEdge(1, this.c, this.a);
        this.edgeAC = new SimpleEdge(1, this.a, this.c);
        this.edgeAA = new SimpleEdge(1, this.a, this.a);

        this.graph.addEdge(this.a, this.edgeAB);
        this.graph.addEdge(this.b, this.edgeBC);
        this.graph.addEdge(this.c, this.edgeCA);
        this.graph.addEdge(this.a, this.edgeAC);
    }

    @Test
    public void testGetVertices() {
        this.createSimpleGraph();

        // test getVertices to check this method AND insertVertex
        assertEquals(this.graph.getVertices().size(), 3);
        assertTrue(this.graph.getVertices().contains(this.a));
        assertTrue(this.graph.getVertices().contains(this.b));
        assertTrue(this.graph.getVertices().contains(this.c));

        this.graph.addVertex(this.a);
        System.out.println(this.graph.getVertices());
        this.graph.addEdge(this.a, this.edgeAB);
        System.out.println(this.graph.getOutgoingEdges(this.a));
    }

    @Test
    public void testAddEdge() {
        TravelController controller1 = new TravelController();
        controller1.load("data/cities3.csv", "data/transport3.csv");
        /* test that adding an edge in which the origin and destination
         are the same throws an exception
         */
        Transport sillyEdge = new
                Transport(controller1.getGraph().getCityFromName("Chicago"),
                controller1.getGraph().getCityFromName("Chicago"),
                TransportType.TRAIN, 60, 120);
        Exception e = Assert.assertThrows(IllegalArgumentException.class,
                () -> controller1.getGraph().addEdge(
                        controller1.getGraph().getCityFromName("Chicago"),
                        sillyEdge));
        Assert.assertEquals(

                "Transport origin and destination cannot be the same.",
                e.getMessage());
        /* test that adding an edge in which the origin and edge source are
        not the same throws an exception */
        City atlantis = new City("Atlantis");
        City beaverville = new City("Beaverville");
        Transport doofusEdge = new Transport(atlantis, beaverville,
                TransportType.BUS, 4000, 10);
        Exception e1 = Assert.assertThrows(IllegalArgumentException.class,
                () -> controller1.getGraph().addEdge(
                        controller1.getGraph().getCityFromName("Chicago"),
                        doofusEdge));
        Assert.assertEquals(
                "Transport origin and edge source must be the same",
                e1.getMessage());

        // tests that adding an edge for a city that is not in the graph throwws an exception
        Exception e2 = Assert.assertThrows(IllegalArgumentException.class,
                () -> controller1.getGraph().addEdge(atlantis, doofusEdge));
        Assert.assertEquals(
                "Cannot add edge with a city that is not in the graph",
                e2.getMessage());

    }

    @Test
    public void testAddVertex() {
        TravelController controller1 = new TravelController();
        controller1.load("data/cities3.csv", "data/transport3.csv");
        City newChicago = new City("Chicago");

        // test that adding a city that is already in the graph throws an exception
        Exception e1 = Assert.assertThrows(IllegalArgumentException.class,
                () -> controller1.getGraph().addVertex(newChicago));
        Assert.assertEquals("City already in graph", e1.getMessage());
    }

    @Test
    public void testGetEdgeSource() {
        this.createSimpleGraph();
        // basic test for getEdgeSource
        assertEquals(this.a, this.graph.getEdgeSource(this.edgeAC));
    }

    @Test
    public void testGetEdgeTarget() {
        this.createSimpleGraph();
        assertEquals(this.c, this.graph.getEdgeTarget(this.edgeAC));
    }

    @Test
    public void testGetOutgoingEdges() {
        this.createSimpleGraph();
        // basic tests for getOutgoingEdges
        assertTrue(this.graph.getOutgoingEdges(this.a).contains(this.edgeAB));
        assertTrue(this.graph.getOutgoingEdges(this.a).contains(this.edgeAC));
    }

    // TODO: write more tests + make sure you test all the cases in your testing plan!
}
