/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package marvel.scriptTestRunner;

import graph.Graph;

import java.io.*;
import java.util.*;

import static marvel.MarvelPaths.constructGraph;
import static marvel.MarvelPaths.findShortestPath;

/**
 * This class implements a testing driver which reads test scripts from
 * files for testing Graph, the Marvel parser, and your BFS algorithm.
 */
public class MarvelTestDriver {

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, Graph<String, String>> graphs = new HashMap<String, Graph<String, String>>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public MarvelTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "LoadGraph":
                    loadGraph(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<String, String>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, String> temp = graphs.get(graphName);
        temp.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        Graph<String, String> temp = graphs.get(graphName);
        temp.addEdge(new String(parentName), new String(childName), edgeLabel);
        output.println("added edge " + edgeLabel + " from " + parentName + " to " + childName + " in " +
                graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String, String> graph = graphs.get(graphName);
        Set<String> nodes = graph.listNodes();
        String temp = graphName + " contains:";
        for (String node : nodes) {
            temp += " " + node;
        }
        output.println(temp);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, String> graph = graphs.get(graphName);
        String node = new String(parentName);
        Set<String> nodes = graph.listNodes();
        String temp = "the children of " + parentName + " in " + graphName + " are:";
        for (String n : nodes) {
            if (n.equals(new String(parentName))) {
                node = n;
            }
        }
        Set<Graph.Edge> edges = new TreeSet<Graph.Edge>(new Comparator<Graph.Edge>() {
            @Override
            public int compare(Graph.Edge e1, Graph.Edge e2 ) {
                if (e1.getChild().equals(e2.getChild())) {
                    return ((String)e1.getLabel()).compareTo((String)e2.getLabel());
                }
                return ((String)e1.getChild()).compareTo((String)e2.getChild());
            }
        });
        edges.addAll(graph.listNodeEdges(node));
        for (Graph.Edge e : edges) {
            temp += " " + e.getChild() + "(" + e.getLabel() + ")";
        }
        output.println(temp);
    }

    private void loadGraph(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to loadGraph: " + arguments);
        }
        String graphName = arguments.get(0);
        String fileName = arguments.get(1);
        loadGraph(graphName, fileName);
    }

    private void loadGraph(String graphName, String fileName) {
        Graph<String, String> graph = constructGraph(fileName);
        graphs.put(graphName, graph);
        output.println("loaded graph " + graphName);
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to findGraph: " + arguments);
        }
        String graphName = arguments.get(0);
        String nodeA = arguments.get(1);
        String nodeB = arguments.get(2);
        findPath(graphName, nodeA, nodeB);
    }

    private void findPath(String graphName, String nodeA, String nodeB) {
        Graph<String, String> graph = graphs.get(graphName);
        if (!graph.listNodes().contains(nodeA)) {
            output.println("unknown: " + nodeA);
        }
        if (!graph.listNodes().contains(nodeB)) {
            output.println("unknown: " + nodeB);
        }
        if (graph.listNodes().contains(nodeA) && graph.listNodes().contains(nodeB)) {
            ArrayList<Graph.Edge> result = findShortestPath(nodeA, nodeB, graph);
            output.println("path from " + nodeA + " to " + nodeB + ":");
            if (result == null) {
                output.println("no path found");
            } else {
                for (Graph.Edge e : result) {
                    output.println(e.getParent() + " to " + e.getChild() + " via " + e.getLabel());
                }

            }
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }

}
