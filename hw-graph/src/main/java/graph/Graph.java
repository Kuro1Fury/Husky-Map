package graph;

import java.util.*;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>Graph</b> represents a <b>mutable</b> Graph that constructed by a HashMap where keys are nodes,
 * and values are HashSets of Edges. The keys record the current nodes, and the corresponding values
 * store the edges that start from this node. The Nodes are generic types.
 *
 * Specification fields:
 *  @spec.specfield graph : HashMap where key:N, value:HashSet of Edge   // The overall graph
 *
 * Abstract Invariant:
 *  graph should not be null; for all keys from graph, the nodes could not be null; the hashmap should
 *  store no null edges
 *  The graph CAN have a node with no edge.
 *  The graph CAN have a bi-direction edge.
 *  The graph CAN have multiple nodes with same name.
 *  The graph CAN have multiple edges with same label, parent, and child.
 * @param <N> the node
 * @param <E> the edge label
 */
public class Graph<N, E> {

    /**
     * Overall graph
     */
    private final Map<N, HashSet<Edge>> graph;

    public static final boolean DEBUG = false;

    // Abstraction Function:
    //  A graph such that
    //      a HashMap where the keys are the current Nodes, and value is a HashSet of current
    //      edges.

    // Representation Invariant:
    //  graph != null, graph.key does not contain null, graph.value does not contain null,
    //  graph.value cannot be null, all Edges must connect two current nodes

    /**
     * @spec.effects Construct an empty graph
     */
    public Graph() {
        graph = new HashMap<N, HashSet<Edge>>();
        checkRep();
    }

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert (graph != null): "graph cannot be null";
        assert (!graph.containsKey(null)): "key set of graph cannot contain null nodes";
        if (!DEBUG) {
            return;
        }
        assert (!graph.containsValue(null)): "Values of graph cannot be null";
        for (Set<Edge> set : graph.values()) {
            assert (!set.contains(null)): "Values of graph cannot contain null edges";
        }
        Iterator<HashSet<Edge>> iterator = graph.values().iterator();
        Set<N> nodes = graph.keySet();
        while(iterator.hasNext()) {
            Set<Edge> temp = iterator.next();
            for (Edge e : temp) {
                assert (nodes.contains(e.getChild()) && nodes.contains(e.getParent())): "All edges " +
                        "must connect two current nodes";
            }
        }
    }

    /**
     * Add a node to this graph
     *
     * @param n name of the node
     * @spec.requires n != null\
     * @spec.modifies graph
     * @spec.effects add the given Node to the end of the graph
     * @throws IllegalArgumentException if n == null
     */
    public void addNode(N n) {
        checkRep();
        if (n == null) {
            throw new IllegalArgumentException("Node to be added cannot be null");
        }
        if (!graph.containsKey(n)) {
            graph.put(n, new HashSet<Edge>());
        }
        checkRep();
    }

    /**
     * Add an edge to this graph
     *
     * @param s the parent node
     * @param e the child node
     * @param n the edge label
     * @spec.requires both nodes are already contained by the graph
     * @spec.modifies graph
     * @spec.effects add an edge to the graph with given information
     * @throws IllegalArgumentException if either of the nodes is not contained
     * by the graph
     */
    public void addEdge(N s, N e, E n) {
        checkRep();
        if (s == null || e == null || !graph.containsKey(s) || !graph.containsKey(e)) {
            throw new IllegalArgumentException("The Node is not contained by the graph");
        }
        Edge temp = new Edge(n, s, e);
        graph.get(s).add(temp);
        checkRep();
    }

    /**
     * Delete all nodes and edges of this graph
     *
     * @spec.modifies graph
     * @spec.effects clear the whole graph
     */
    public void clearGraph() {
        checkRep();
        graph.clear();
        checkRep();
    }

    /**
     * Get a set of all Nodes from this graph
     *
     * @return a set of all Nodes from this graph
     */
    public Set<N> listNodes() {
        Set<N> set = new HashSet<N>();
        set.addAll(graph.keySet());
        return set;
    }

    /**
     * Get a set of all children from a node
     *
     * @param n node information
     * @return a set of all children from a node, return an empty set if it does not have any
     * child
     * @spec.requires given node n != null
     * @throws IllegalArgumentException if n == null
     */
    public Set<N> listChildren(N n) {
        if (n == null) {
            throw new IllegalArgumentException("The node does not exist");
        }
        Set<N> temp = new HashSet<N>();
        if (graph.get(n).size() != 0) {
            for (Edge e : graph.get(n)) {
                temp.add((N) e.getChild());
            }
        }
        return temp;
    }

    /**
     * Get a set of all edges
     *
     * @return a set of all edges
     */
    public Set<Edge> listEdges() {
        Set<Edge> temp = new HashSet<Edge>();
        for (HashSet<Edge> sets : graph.values()) {
            temp.addAll(sets);
        }
        return temp;
    }

    /**
     * Get a set of all edges from a particular node
     *
     * @param n the name of node
     * @spec.requires n != null
     * @return a set of all edges from a particular node
     * @throws IllegalArgumentException if n == null
     * @throws IllegalArgumentException if n is not present
     */
    public Set<Edge> listNodeEdges(N n) {
        if (n == null) {
            throw new IllegalArgumentException("n cannot be null");
        }
        if (!graph.containsKey(n)) {
            throw new IllegalArgumentException("n is not present");
        }
        return graph.get(n);
    }


    /**
     * <b>Edge</b> represents an <b>immutable</b> Edge that constructed by two Nodes representing the
     * starting Node, or parent, and the ending Node, or child, as well as a generic type representing the
     * edge label. Edge represents the connection between two Nodes and marked by a label.
     *
     * Specification fields:
     *  @spec.specfield label : E     // The label of the edge
     *  @spec.specfield start : N     // The parent node
     *  @spec.specfield end : N       // The child node
     *
     * Abstract Invariant:
     *  Either label, start, and end could not be null.
     *  A Node CAN be both the start and the end.
     */
    public class Edge {

        /**
         * Label of this edge
         */
        private final E label;

        /**
         * Parent node
         */
        private final N start;

        /**
         * Child node
         */
        private final N end;

        // Abstraction Function:
        //  A Edge such that
        //      label = any non-null E
        //      two Nodes = any non-null Nodes
        //      Edges with same starting and ending node are allowed

        // Representation Invariant:
        //  start != null, end != null, label != null

        /**
         * @param l the label of the new Edge
         * @param s the parent node of the new Edge
         * @param e the child node of the new Edge
         * @spec.requires l != null and s != null and e != null
         * @spec.effects Constructs a new edge with starting point s, ending point e, and label l
         */
        public Edge(E l, N s, N e) {
            if (l == null) {
                throw new IllegalArgumentException("The label of edge cannot be null");
            }
            if (s == null) {
                throw new IllegalArgumentException("The parent node of edge cannot be null");
            }
            if (e == null) {
                throw new IllegalArgumentException("The child node of edge cannot be null");
            }
            label = l;
            start = s;
            end = e;
            checkRep();
        }

        /**
         * Throws an exception if the representation invariant is violated.
         */
        private void checkRep() {

            assert (label != null): "Edge name cannot be null";
            assert (start != null): "Parent node cannot be null";
            assert (end != null): "Child node cannot be null";
        }

        /**
         * Gets the label of this Edge.
         *
         * @return the label of this Edge
         */
        public E getLabel() {
            return label;
        }

        /**
         * Gets the parent node of this Edge.
         *
         * @return the parent node of this Edge
         */
        public N getParent() {
            return start;
        }

        /**
         * Gets the child node of this Edge.
         *
         * @return the child node of this Edge
         */
        public N getChild() {
            return end;
        }


        /**
         * Compare two Edges to determine whether they are equal
         *
         * @param e the Edge to be compared
         * @return true if the label, the parent, and the child node are the same, false otherwise
         */
        @Override
        public boolean equals(Object e) {
            if (!(e instanceof Graph<?,?>.Edge)) {
                return false;
            }
            Graph<?,?>.Edge o = (Graph<?,?>.Edge) e;
            return this.start.equals(o.start) &&
                    this.end.equals(o.end) &&
                    this.label.equals(o.label);
        }

        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also return
         */
        @Override
        public int hashCode() {
            return label.hashCode() + start.hashCode() * end.hashCode();
        }

    }
}
