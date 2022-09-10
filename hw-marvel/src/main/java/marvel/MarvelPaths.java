package marvel;

import graph.Graph;

import java.util.*;

/**
 * MarvelPaths is a class involving two methods, constructGraph and findShortestPath. The former
 * method is to construct using csv data from parser and build a graph with Graph ADT. THe latter
 * is a method using BFS to find the shortest path from two nodes based on node name and label.
 */
public class MarvelPaths {

    /**
     * @param filename the name of the file
     * @return a graph with nodes and edges data from the file using parser
     */
    public static Graph<String, String> constructGraph(String filename) {
        Graph<String, String> graph = new Graph<>();
        Map<String, ArrayList<String>> data = MarvelParser.parseData(filename);
        for (String label : data.keySet()) {
            List<String> nodes = data.get(label);
            for (String node : nodes) {
                graph.addNode(node);
            }
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = i + 1; j < nodes.size(); j++) {
                    graph.addEdge(nodes.get(i), nodes.get(j), label);
                    graph.addEdge(nodes.get(j), nodes.get(i), label);
                }
            }
        }
        return graph;
    }

    /**
     *
     * @param s the parent node
     * @param e the child node
     * @param graph the graph data
     * @spec.requires the Nodes and the graph are not null
     * @return an arraylist of Graph.Edge with order that store the shortest path from s to e
     */
    public static ArrayList<Graph.Edge> findShortestPath(String s, String e, Graph<String, String> graph){
        if (graph == null || s == null || e == null) {
            throw new IllegalArgumentException("query cannot be null");
        }

        if (!(graph.listNodes().contains(s)) || !(graph.listNodes().contains(e))) {
            throw new IllegalArgumentException("Either of Nodes is not present in the graph");
        }

        Queue<String> queue = new LinkedList<String>();
        Map<String, ArrayList<Graph.Edge>> map = new HashMap<String, ArrayList<Graph.Edge>>();
        queue.add(s);
        map.put(s, new ArrayList<Graph.Edge>());
        while(!queue.isEmpty()) {
            String n = queue.remove();
            if (n.equals(e)) {
                return map.get(n);
            } else {
                Set<Graph.Edge> set = new TreeSet<Graph.Edge>(new Comparator<Graph.Edge>() {
                    @Override
                    public int compare(Graph.Edge e1, Graph.Edge e2 ) {
                        if (e1.getChild().equals(e2.getChild())) {
                            return ((String) e1.getLabel()).compareTo((String) e2.getLabel());
                        }
                        return ((String) e1.getChild()).compareTo((String) e2.getChild());
                    }
                });
                set.addAll(graph.listNodeEdges((String) n));
                for (Graph.Edge edge : set) {
                    if (!map.keySet().contains(edge.getChild())) {
                        ArrayList<Graph.Edge> list = new ArrayList<Graph.Edge>();
                        for (Graph.Edge e3 : map.get(n)) {
                            list.add(e3);
                        }
                        list.add(edge);
                        map.put((String) edge.getChild(), list);
                        queue.add((String) edge.getChild());
                    }
                }
            }
        }
        return null;
    }

    /**
     * A main method that can let users interact with the class
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        Graph<String, String> graph = constructGraph("marvel.csv");
        System.out.println("marvel graph data created.");
        boolean play = true;
        while (play) {

            System.out.println("Please type in two characters that you want to find a shortest path " +
                    "between them one by one");
            System.out.println("The first character's name:");
            Scanner reader = new Scanner(System.in);
            String start = reader.nextLine();
            while (!graph.listNodes().contains(start)) {
                System.out.println("It is not a valid character name. Please try again..");
                start = reader.nextLine();
            }
            System.out.println("The second character's name:");
            String end = reader.nextLine();
            while (!graph.listNodes().contains(end)) {
                System.out.println("It is not a valid character name. Please try again..");
                end = reader.nextLine();
            }
            System.out.println("Please wait a second..");
            ArrayList<Graph.Edge> result = findShortestPath(start, end, graph);
            if (result == null) {
                System.out.println("Sorry, there is no path found connecting these two characters.");
            } else if (result.size() == 0){
                System.out.println("They are the same character");
            } else {

                System.out.println("The shortest path is:");
                for (Graph.Edge e : result) {
                    System.out.println(e.getParent() + " to " + e.getChild() + " via " + e.getLabel());
                }
            }
            System.out.println("Want to find again? (Y/N)");
            String ans = reader.nextLine();
            while (!ans.equals("Y") && !ans.equals("N")) {
                System.out.println("Please type in either Y or N");
                ans = reader.nextLine();
            }
            if (ans.equals("Y")) {
                play = true;
            } else if (ans.equals("N")) {
                System.out.println("Thanks for playing!");
                play = false;
                reader.close();
            }
        }
    }
}
