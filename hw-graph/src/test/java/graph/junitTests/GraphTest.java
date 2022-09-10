package graph.junitTests;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import graph.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public final class GraphTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    // this graph
    private Graph<String, String> graph = new Graph<String, String>();

    // some basic Nodes
    private String a = "a";
    private String b = "b";
    private String c = "c";
    private String d = "d";
    private String empty = "";

    // some basic edges
    private Graph<String, String>.Edge aToBLabel1 = graph.new Edge("1", a, b);
    private Graph<String, String>.Edge aToBLabel2 = graph.new Edge("2", a, b);
    private Graph<String, String>.Edge bToALabel3 = graph.new Edge("3", b, a);
    private Graph<String, String>.Edge aToALabel0 = graph.new Edge("0", a, a);


    @Test // test getName method
    public void testGetName() {
        assertEquals(a, "a");
        assertEquals(b, "b");
        assertEquals(empty, "");
    }

    @Test
    public void testEmptyName() {
        assertFalse(a.equals(""));
        assertTrue(empty.equals(""));
    }

    // Edge Test
    @Test
    public void testEdgeConstructor() {
        graph.new Edge("1", a, b);
        graph.new Edge("2", a, b);
        graph.new Edge("3", b, a);
        graph.new Edge("0", a, a);
    }

    @Test
    public void testGetLabel() {
        assertEquals(aToBLabel1.getLabel(), "1");
        assertEquals(aToBLabel2.getLabel(), "2");
        assertEquals(bToALabel3.getLabel(), "3");
        assertEquals(aToALabel0.getLabel(), "0");
    }

    @Test
    public void testGetParent() {
        assertEquals(aToBLabel1.getParent(), a);
        assertEquals(aToBLabel2.getParent(), a);
        assertEquals(bToALabel3.getParent(), b);
        assertEquals(aToALabel0.getParent(), a);
    }

    @Test
    public void testGetChild() {
        assertEquals(aToBLabel1.getChild(), b);
        assertEquals(aToBLabel2.getChild(), b);
        assertEquals(bToALabel3.getChild(), a);
        assertEquals(aToALabel0.getChild(), a);
    }


    @Test
    public void testEquals() {
        assertFalse(aToBLabel1.equals(aToBLabel2));
        assertFalse(aToBLabel2.equals(aToBLabel1));
        assertFalse(aToALabel0.equals(aToBLabel1));
        assertFalse(aToBLabel1.equals(aToALabel0));
        Graph.Edge temp = graph.new Edge("1", a, b);
        assertTrue(aToBLabel1.equals(temp));
        assertFalse(aToBLabel2.equals(temp));

    }

    // Graph Test
    @Test
    public void testGraphConstructor() {
        new Graph();
    }


    @Test
    public void testAddNode() {
        graph = new Graph<String, String>();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(new String("a"));
        Set<String> expected = new HashSet<String>();
        expected.add(a);
        expected.add(b);
        assertEquals(expected, graph.listNodes());
    }


    /**
     * check that the edge is present in the given set of edges
     */
    private boolean containEqualEdge(Set<Graph<String, String>.Edge> set, Graph<String, String>.Edge e) {
        for (Graph.Edge cur : set) {
            if (cur.equals(e)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testClearGraph() {
        graph = new Graph<String, String>();
        graph.addNode(a);
        graph.addNode(b);
        graph.addEdge(a, b, "1");
        graph.addEdge(b, a, "2");
        graph.clearGraph();
        // check node
        assertTrue(graph.listNodes().size() == 0);
        // check edge
        assertTrue(graph.listEdges().size() == 0);
    }

    @Test
    public void testAddEdge() {
        graph = new Graph<String, String>();
        graph.addNode(a);
        graph.addNode(b);
        graph.addEdge(a, b, "1");
        // check node
        Set<String> expectedNodes = new HashSet<String>();
        expectedNodes.add(a);
        expectedNodes.add(b);
        assertEquals(expectedNodes, graph.listNodes());
        // check children
        Set<String> expectedChildren = new HashSet<String>();
        expectedChildren.add(b);
        assertEquals(expectedChildren, graph.listChildren(a));
        // check edge
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("1", a, b)));
    }

    @Test
    public void testAddEdge2() {
        graph = new Graph<String, String>();
        graph.addNode(a);
        graph.addNode(b);
        graph.addEdge(new String("a"), new String("b"), "1");
        // check node
        Set<String> expectedNodes = new HashSet<String>();
        expectedNodes.add(a);
        expectedNodes.add(b);
        assertEquals(expectedNodes, graph.listNodes());
        // check children
        Set<String> expectedChildren = new HashSet<String>();
        expectedChildren.add(b);
        assertEquals(expectedChildren, graph.listChildren(a));
        // check edge
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("1", a, b)));
    }

    @Test
    public void testBidirectionEdge() {
        graph = new Graph<String, String>();
        graph.addNode(a);
        graph.addNode(b);
        graph.addEdge(a, b, "1");
        graph.addEdge(b, a, "2");
        // check node
        Set<String> expectedNodes = new HashSet<String>();
        expectedNodes.add(a);
        expectedNodes.add(b);
        assertEquals(expectedNodes, graph.listNodes());
        // check children
        Set<String> expectedChildren1 = new HashSet<String>();
        expectedChildren1.add(a);
        Set<String> expectedChildren2 = new HashSet<String>();
        expectedChildren2.add(b);
        assertEquals(expectedChildren2, graph.listChildren(a));
        assertEquals(expectedChildren1, graph.listChildren(b));
        // check edge
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("1", a, b)));
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("2", b, a)));
    }

    @Test
    public void testEmptyGraph() {
        graph = new Graph<String, String>();
        // check node
        assertTrue(graph.listNodes().size() == 0);
        // check edge
        assertTrue(graph.listEdges().size() == 0);
    }



    @Test
    public void testOneNodeGraph() {
        graph = new Graph<String, String>();
        graph.addNode(a);
        // check node
        Set<String> expectedNodes = new HashSet<String>();
        expectedNodes.add(a);
        assertEquals(expectedNodes, graph.listNodes());
        // check children
        assertTrue(graph.listChildren(a).size() == 0);
        // check edge
        assertTrue(graph.listEdges().size() == 0);
    }

    @Test
    public void testSelfEdge() {
        graph = new Graph<String, String>();
        graph.addNode(a);
        graph.addEdge(a, a,"0");
        // check node
        Set<String> expectedNodes = new HashSet<String>();
        expectedNodes.add(a);
        assertEquals(expectedNodes, graph.listNodes());
        // check children
        Set<String> expectedChildren = new HashSet<String>();
        expectedChildren.add(a);
        assertEquals(expectedChildren, graph.listChildren(a));
        // check edge
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("0", a, a)));
    }

    @Test
    public void testSampleGraph() {
        graph = new Graph<String, String>();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addEdge(a, a, "0");
        graph.addEdge(a, b, "1");
        graph.addEdge(b, a, "2");
        graph.addEdge(b, c, "3");

        // check node
        Set<String> expectedNodes = new HashSet<String>();
        expectedNodes.add(a);
        expectedNodes.add(b);
        expectedNodes.add(c);
        expectedNodes.add(d);
        assertEquals(expectedNodes, graph.listNodes());
        // check children
        Set<String> expectedChildren1 = new HashSet<String>();
        expectedChildren1.add(a);
        expectedChildren1.add(b);
        Set<String> expectedChildren2 = new HashSet<String>();
        expectedChildren2.add(a);
        expectedChildren2.add(c);
        assertEquals(expectedChildren1, graph.listChildren(a));
        assertEquals(expectedChildren2, graph.listChildren(b));
        // check edge
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("0", a, a)));
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("1", a, b)));
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("2", b, a)));
        assertTrue(containEqualEdge(graph.listEdges(), graph.new Edge("3", b, c)));
    }


}
