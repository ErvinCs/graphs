import javafx.util.Pair;

import java.util.*;

public class Util {
    /**
     * Searches for the shortest path between 2 vertices given by their IDs.
     * Performs a BFS on the graph starting at {@code vertexStartID} and throws an exception if it cannot
     *  find a path to {@code VertexEndID}.
     * If it can, then it performs a reverse iteration on the parsed vertices in order to construct
     *  the path.
     * @param vertexStartID - id of the starting Vertex of the path
     * @param vertexEndID - id of the final Vertex of the path
     * @return List<Vertex> - ordered list of the vertices that form the shortest path
     * @throws IllegalStateException
     *  If there is no path between the vertices or if there are no vertices with the given IDs.
     */
    public static List<Vertex> shortestPath(Graph graph, int vertexStartID, int vertexEndID) throws IllegalStateException {
        Optional<Vertex> vertexStartOptional = graph.getVertexById(vertexStartID);
        Optional<Vertex> vertexEndOptional = graph.getVertexById(vertexEndID);
        if (vertexEndOptional.isEmpty() || vertexStartOptional.isEmpty()) {
            throw new IllegalStateException("Given vertices do not exist!");
        }
        Vertex vertexStart = vertexStartOptional.get();
        Vertex vertexEnd = vertexEndOptional.get();

        // Prepare for BFS
        Queue<Vertex> queue = new LinkedList<>();
        Map<Vertex, ArrayList<Vertex>> visited = new LinkedHashMap<>();
        Vertex preStartingPoint = new Vertex(-1);
        queue.add(vertexStart);
        visited.put(preStartingPoint, new ArrayList<>());
        visited.get(preStartingPoint).add(vertexStart);

        // Perform BFS
        boolean pathFound = false;
        while(!pathFound && !queue.isEmpty()) {
            Vertex current = queue.poll();
            for(Edge e : current.getOutEdges()) {
                if(visited.get(current) == null || !visited.get(current).contains(e.getV2())) {
                    queue.add(e.getV2());
                    visited.computeIfAbsent(current, k -> new ArrayList<>());
                    visited.get(current).add(e.getV2());
                    if(e.getV2().equals(vertexEnd)) {
                        pathFound = true;
                        break;
                    }
                }
            }
        }

        // If the BFS did not find a path throw an exception
        if(!pathFound) {
            throw new IllegalStateException("No such path!");
        }

        // Construct the path by going backwards from vertexEnd
        List<Vertex> path = new ArrayList<>();
        Iterator<Vertex> mapKeyReverseIterator = new LinkedList<Vertex>(visited.keySet()).descendingIterator();
        Vertex key, value, prevKey;

        key = mapKeyReverseIterator.next();
        value = vertexEnd;
        prevKey = key;
        path.add(value);
        path.add(key);
        while (mapKeyReverseIterator.hasNext()) {
            key = mapKeyReverseIterator.next();
            if (key.getvID() == -1) {
                break;
            }

            ArrayList<Vertex> entryValues = visited.get(key);
            value = null;
            for(Vertex v : entryValues)
                if (v == prevKey)
                    value = v;

            if (value == null)
                continue;

            path.add(key);
            prevKey = key;
        }
        // Reverse the path such that it has the proper order
        Collections.reverse(path);

        return path;
    }

    /* Problem Statement:
        Write a program that, given a graph with positive costs and two vertices,
            finds a lowest cost walk between the given vertices,
            using a "backwards" Dijkstra algorithm
            (Dijkstra algorithm that searches backwards, from the ending vertex)
    */
    /**
     * Searches for the lowest cost path between 2 vertices given by their IDs.
     * Performs a Dijkstra's algorithm on the graph starting at {@code vertexEndID} and
     *  going backwards to  {@code vertexStartID}
     * Throws an exception if it cannot find a path to {@code vertexStartID}.
     * If it can, then it performs a reverse iteration on the parsed vertices
     * in order to construct the path.
     * @param vertexStartID - id of the starting Vertex of the path
     * @param vertexEndID - id of the final Vertex of the path
     * @return List<Vertex> - ordered list of the vertices that form the lowest cost path
     * @throws IllegalStateException
     *  If there is no path between the vertices or if there are no vertices with the given IDs.
     */
    public static List<Vertex> dijkstraLowestCostPath(Graph graph, Integer vertexStartID, Integer vertexEndID) {
        Optional<Vertex> vertexStartOptional = graph.getVertexById(vertexStartID);
        Optional<Vertex> vertexEndOptional = graph.getVertexById(vertexEndID);
        if (vertexEndOptional.isEmpty() || vertexStartOptional.isEmpty()) {
            throw new IllegalStateException("Given vertices do not exist!");
        }
        Vertex vertexStart = vertexStartOptional.get();
        Vertex vertexEnd = vertexEndOptional.get();

        Map<Vertex, Vertex> prev = new LinkedHashMap<>();
        Map<Vertex, Integer> dist = new LinkedHashMap<>();
        PathComparator pathComparator = new PathComparator(dist);
        PriorityQueue<Vertex> queue = new PriorityQueue<>(pathComparator);

        dist.put(vertexEnd, 0);
        queue.add(vertexEnd);
        boolean found = false;

        while(!queue.isEmpty() && !found) {
            Vertex current = queue.poll();
            for(Edge inEdge : current.getInEdges()) {
                Vertex other = inEdge.getV1();
                int costOtherToCurrent = inEdge.getWeight();
                if (!dist.containsKey(other) || dist.get(current) + costOtherToCurrent < dist.get(other)) {
                    dist.put(other, dist.get(current) + costOtherToCurrent);
                    pathComparator.distCopy = dist;
                    queue.add(other);
                    prev.put(other, current);
                }
            }
            if (current == vertexStart) {
                found = true;
            }
        }

        if (!found) {
            throw new IllegalStateException("No such path!");
        }

        List<Vertex> lowestCostPath = new ArrayList<>();
        Vertex current = vertexEnd;
        lowestCostPath.add(current);
        while(current != vertexStart) {
            final Vertex currentCopy = current;
            current = prev.entrySet().stream()
                    .filter(entry -> entry.getValue() == currentCopy)
                    .findFirst()
                    .get()
                    .getKey();
            lowestCostPath.add(current);
        }

        Collections.reverse(lowestCostPath);

        return lowestCostPath;
    }

    /*
     * Problem Statement:
     *  Given an undirected graph, find a Hamiltonian cycle (if it exists).
     */
    //public static List<Vertex> FindHamiltonianCycle(Graph graph) {
    //
    //}
}