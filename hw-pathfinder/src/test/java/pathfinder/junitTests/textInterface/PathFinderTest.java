package pathfinder.junitTests.textInterface;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import graph.*;
import pathfinder.datastructures.Point;

import static org.junit.Assert.*;
import static pathfinder.CampusPathDijkstra.findPathDijkstra;

public class PathFinderTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    //private Graph<String, String> graph = new Graph<String, String>();

    @Test
    public void testStringGraph() {
        Graph<String, String> graph = new Graph<String, String>();
    }

    @Test
    public void testPointGraph() {
        Graph<Point, Double> graph = new Graph<Point, Double>();
    }

    @Test
    public void invalidParamDijkstra() {
        Graph<Point, Double> graph = new Graph<Point, Double>();
        assertThrows(IllegalArgumentException.class, () ->
                findPathDijkstra(null, new Point(1.0, 1.0), graph));
        assertThrows(IllegalArgumentException.class, () ->
                findPathDijkstra(new Point(1.0, 1.0), null, graph));
        assertThrows(IllegalArgumentException.class, () ->
                findPathDijkstra(new Point(1.0, 1.0), new Point(1.0, 1.0), null));

    }
}

