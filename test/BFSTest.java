package test;

import org.junit.Assert;
import org.junit.Test;
import sol.BFS;
import sol.TravelController;
import sol.TravelGraph;
import src.Transport;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BFSTest {

    private static final double DELTA = 0.001;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;
    private SimpleVertex f;
    private SimpleGraph graph;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test
     * may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     */
    public void makeSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");
        this.f = new SimpleVertex("f");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);
        this.graph.addVertex(this.f);

        this.graph.addEdge(this.a,
                new SimpleEdge(1, this.a, this.b));
        this.graph.addEdge(this.b,
                new SimpleEdge(1, this.b, this.c));
        this.graph.addEdge(this.c,
                new SimpleEdge(1, this.c, this.e));
        this.graph.addEdge(this.d,
                new SimpleEdge(1, this.d, this.e));
        this.graph.addEdge(this.a,
                new SimpleEdge(100, this.a, this.f));
        this.graph.addEdge(this.f,
                new SimpleEdge(100, this.f, this.e));

        this.graph.addEdge(this.b,
                new SimpleEdge(50, this.b, this.d));
        this.graph.addEdge(this.d,
                new SimpleEdge(50, this.d, this.f));

    }

    @Test
    public void testBasicBFS() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        List<SimpleEdge> path = bfs.getPath(this.graph, this.a, this.e);
        System.out.println(path);
        assertEquals(SimpleGraph.getTotalEdgeWeight(path), 200.0, DELTA);
        assertEquals(path.size(), 2);

        System.out.println(bfs.getPath(this.graph, this.a, this.d));
        System.out.println(bfs.getPath(this.graph, this.a, this.f));
    }

    @Test
    public void testBFS() {
        TravelController controller1 = new TravelController();
        controller1.load("data/cities3.csv", "data/transport3.csv");
        List<Transport> path1 = controller1.mostDirectRoute("Chicago",
                "New York");
        System.out.println(path1);
        // tests that bfs finds the shortest path from Chicago to New York
        assertEquals(2, path1.size());
        assertEquals(1000, controller1.getTotalEdgeWeightTime(path1), 0.001);

        // tests that finding the most direct route from a city to itself returns an empty path
        List<Transport> path2 = controller1.mostDirectRoute("Chicago",
                "Chicago");
        assertEquals(0, path2.size());

        // tests that finding the most direct route from a City with no outgoing edges returns empty
        controller1.mostDirectRoute("New York", "Chicago");
        Assert.assertEquals(controller1.mostDirectRoute("New York", "Chicago").isEmpty(), true);

        // tests that entering a city that does not exist throws an exception
        Exception e1 = Assert.assertThrows(IllegalArgumentException.class,
                () -> controller1.mostDirectRoute("Atlantis", "Narnia"));
        Assert.assertEquals("Entered city does not exist", e1.getMessage());


    }
}
