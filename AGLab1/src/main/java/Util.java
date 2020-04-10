import java.util.*;

public class Util {
    /**
     * Searches for the shortest path between 2 vertices given by their IDs.
     * Performs a BFS on the graph starting at @vertexStartID and throws an exception if it cannot
     *  find a path to @VertexEndID.
     * If it can, then it performs a reverse iteration on the parsed vertices in order to construct
     *  the path.
     * @param vertexStartID - id of the starting Vertex of the path
     * @param vertexEndID - id of the final Vertex of the path
     * @return List<Vertex> - ordered list of the vertices that form the path
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

        Queue<Vertex> queue = new LinkedList<>();
        Map<Vertex, ArrayList<Vertex>> visited = new LinkedHashMap<>();
        Vertex preStartingPoint = new Vertex(-1);
        queue.add(vertexStart);
        visited.put(preStartingPoint, new ArrayList<>());
        visited.get(preStartingPoint).add(vertexStart);

        boolean pathFound = false;
        while(!pathFound && !queue.isEmpty()) {
            Vertex current = queue.poll();
            //ArrayList<Vertex> arayList = new ArrayList<>();
            for(Edge e : current.getOutEdges()) {
                if(visited.get(current) == null || !visited.get(current).contains(e.getV2())) {
                    queue.add(e.getV2());
                    visited.computeIfAbsent(current, k -> new ArrayList<>());
                    visited.get(current).add(e.getV2());
                    //visited.put(current, e.getV2());
                    if(e.getV2().equals(vertexEnd)) {
                        pathFound = true;
                        break;
                    }
                }
            }
        }

        if(!pathFound) {
            throw new IllegalStateException("No such path!");
        }

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
        Collections.reverse(path);

        return path;

    }
}
