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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.*;

import static pathfinder.parser.CampusPathsParser.parseCampusBuildings;
import static pathfinder.parser.CampusPathsParser.parseCampusPaths;

/**
 * <b>CampusMap</b> represents a <b>immutable</b> model of the app which construct and operate the uw
 * campus map based on the Graph ADT by provided csv file. It is constructed by a list of buildings and
 * a graph. It is a class involving four methods, shortNameExists, longNameForShort, buildingNames,and
 * findShortestPath, which using Dijkstra algorithm.
 *
 * Specification fields:
 *  @spec.specfield buildings: List of CampusBuilding storing building information
 *  @spec.specfield graph : Graph of Point and Double
 *
 *  Abstract Invariant:
 *   graph should not be null; building should not be null; all double in graph representing distance
 *   cannot be negative; all buildings' locations(Points) should be present in graph
 */
public class CampusMap implements ModelAPI {

    /**
     * The list of Campus Buildings
     */
    private final List<CampusBuilding> buildings;

    /**
     * The graph of points and paths
     */
    private final Graph<Point, Double> graph;

    public static final boolean DEBUG = false;

    // Abstraction Function:
    //  A CampusMap such that
    //      a list of CampusBuilding storing building data
    //      a graph constructed by Point and Double storing trivial path data

    // Representation Invariant:
    //  graph != null, buildings != null; any distance(double) >= 0; buildings does not
    //  contain null

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert(graph != null);
        assert(buildings != null);
        if (!DEBUG) {
            return;
        }
        assert(!buildings.contains(null));
        for (Graph.Edge e :graph.listEdges()) {
            assert((double) e.getLabel() >= 0.0);
        }
    }

    /**
     * Constructor a CampusMap based on the csv files
     */
    public CampusMap() {
        buildings = parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> paths = parseCampusPaths("campus_paths.csv");
        graph = new Graph<Point, Double>();
        for (CampusPath cp : paths) {
            Point start = new Point(cp.getX1(), cp.getY1());
            Point end = new Point(cp.getX2(), cp.getY2());
            graph.addNode(start);
            graph.addNode(end);
            graph.addEdge(start, end, cp.getDistance());
        }
        checkRep();
    }

    /**
     * @param shortName The short name of a building to query.
     * @return {@literal true} iff the short name provided exists in this campus map.
     */
    @Override
    public boolean shortNameExists(String shortName) {
        for (CampusBuilding cb : buildings) {
            if (cb.getShortName().equals(shortName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param shortName The short name of a building to look up.
     * @return The long name of the building corresponding to the provided short name.
     * @throws IllegalArgumentException if the short name provided does not exist.
     */
    @Override
    public String longNameForShort(String shortName) {
        for (CampusBuilding cb : buildings) {
            if (cb.getShortName().equals(shortName)) {
                return cb.getLongName();
            }
        }
        throw new IllegalArgumentException("The short name provided does not exist");
    }

    /**
     * @return A mapping from all the buildings' short names to their long names in this campus map.
     */
    @Override
    public Map<String, String> buildingNames() {
        Map<String, String> map = new HashMap<String, String>();
        for (CampusBuilding cb : buildings) {
            map.put(cb.getShortName(), cb.getLongName());
        }
        return map;
    }

    /**
     * Finds the shortest path, by distance, between the two provided buildings.
     *
     * @param startShortName The short name of the building at the beginning of this path.
     * @param endShortName   The short name of the building at the end of this path.
     * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
     * if none exists.
     * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
     *                                  {@literal null}, or not valid short names of buildings in
     *                                  this campus map.
     */
    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        if (startShortName == null || endShortName == null ||
        !shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException();
        }
        Point start = null;
        Point end = null;
        for (CampusBuilding cb : buildings) {
            if (cb.getShortName().equals(startShortName)) {
                start = new Point(cb.getX(), cb.getY());
            }
            if (cb.getShortName().equals(endShortName)) {
                end = new Point(cb.getX(), cb.getY());
            }
        }
        return CampusPathDijkstra.findPathDijkstra(start, end, graph);
    }

}
