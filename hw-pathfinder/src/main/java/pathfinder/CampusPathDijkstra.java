package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A class that involve Dijkstra algorithm
 */
public class CampusPathDijkstra {

    /**
     * Find the shortest paths using Dijkstra
     *
     * @param start starting N
     * @param end ending N
     * @param graph the graph that path takes place
     * @param <N> representing Node
     * @param <E> representing Edge Label
     * @return Path of N that has the shortest path between start and end
     */
    public static <N, E> Path<N> findPathDijkstra(N start, N end, Graph<N, E> graph) {
        if (start == null) {
            throw new IllegalArgumentException("the start cannot be null");
        }
        if (end == null) {
            throw new IllegalArgumentException("the end cannot be null");
        }
        if (graph == null) {
            throw new IllegalArgumentException("the end cannot be null");
        }
        PriorityQueue<Path<N>> active = new PriorityQueue<>(Comparator.comparingDouble(Path::getCost));
        Set<N> finished = new HashSet<N>();
        active.add(new Path<N>(start));
        while (!active.isEmpty()) {
            Path<N> minPath = active.remove();
            N minDest = minPath.getEnd();
            if (minDest.equals(end)) {
                return minPath;
            }
            if (finished.contains(minDest)) {
                continue;
            }
            for (Graph<N, E>.Edge e : graph.listNodeEdges(minDest)) {
                if (!finished.contains(e.getChild())) {
                    Path<N> newPath = minPath.extend(e.getChild(), (double) e.getLabel());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        return null;
    }

    public static void test() {
        return;
    }
}
