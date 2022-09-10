package marvel.junitTests;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import graph.*;

import java.util.ArrayList;
import java.util.Set;

import static marvel.MarvelPaths.constructGraph;
import static marvel.MarvelPaths.findShortestPath;
import static org.junit.Assert.*;

public class MarvelTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    private Graph<String, String> graph = new Graph<String, String>();

    @Test
    public void testInvalidFileName() {
        assertThrows(IllegalArgumentException.class, () -> constructGraph("NoFile"));
    }

    @Test
    public void testInvalidGraphAndNode() {
        assertThrows(IllegalArgumentException.class, () ->
                findShortestPath("n1", "n2", null));
        assertThrows(IllegalArgumentException.class, () ->
                findShortestPath("n1", "n2", graph));
    }

    /**
     * check that the edge is present in the given set of edges
     */
    private boolean hasEqualPath(ArrayList<Graph.Edge> arr1, ArrayList<Graph.Edge> arr2) {
        if (arr1.size() != arr2.size()) {
            return false;
        }
        for (int i = 0; i < arr1.size(); i++) {
            if (!arr1.get(i).equals(arr2.get(i))) {
                return false;
            }
        }
        return true;
    }


    @Test
    public void testParserConstruct() {
        constructGraph("empty.csv");
        constructGraph("oneNodeNoEdge.csv");
        constructGraph("staffSuperheroes.csv");
        constructGraph("threeNodesTwoEdges.csv");
        constructGraph("twoNodesNoEdge.csv");
        constructGraph("twoNodesOneEdge.csv");
        constructGraph("marvel.csv");
    }

    @Test
    public void testFindPathIsTheShortestByDistance() {
        graph = new Graph<String, String>();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addEdge("n1", "n2", "1");
        graph.addEdge("n2", "n3", "2");
        graph.addEdge("n1", "n3", "3");
        ArrayList<Graph.Edge> temp = new ArrayList<Graph.Edge>();
        temp.add(graph.new Edge("3", "n1", "n3"));
        assertTrue(hasEqualPath(findShortestPath("n1", "n3", graph), temp));
    }

    @Test
    public void testFindPathIsTheShortestByNodeName() {
        graph = new Graph<String, String>();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addNode("n4");
        graph.addEdge("n1", "n2", "1");
        graph.addEdge("n2", "n4", "2");
        graph.addEdge("n3", "n4", "3");
        graph.addEdge("n1", "n3", "4");
        ArrayList<Graph.Edge> temp = new ArrayList<Graph.Edge>();
        temp.add(graph.new Edge("1", "n1", "n2"));
        temp.add(graph.new Edge("2", "n2", "n4"));
        assertTrue(hasEqualPath(findShortestPath("n1", "n4", graph), temp));
    }

    @Test
    public void testFindPathIsTheShortestByLabelName() {
        graph = new Graph<String, String>();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addEdge("n1", "n2", "1");
        graph.addEdge("n1", "n2", "2");
        ArrayList<Graph.Edge> temp = new ArrayList<Graph.Edge>();
        temp.add(graph.new Edge("1", "n1", "n2"));
        assertTrue(hasEqualPath(findShortestPath("n1", "n2", graph), temp));
    }
}
