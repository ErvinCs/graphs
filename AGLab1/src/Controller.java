import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class Controller {
    private Graph graph;

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Controller(Graph graph) {
        this.graph = graph;
    }

    public Controller(String filename) throws FileNotFoundException {
        this.graph = new Graph(filename);
    }

    public Controller(int noVertices, int noEdges) {
        this.graph = Graph.generateGraph(noVertices, noEdges);
    }

    public Set<Vertex> getVertices() {
        return graph.getVertices();
    }

    public Set<Edge> getEdges() {
        return graph.getEdges();
    }

    public int getNoVertices() {
        return graph.getNoVertices();
    }

    public int getNoEdges() {
        return graph.getNoEdges();
    }

    public Vertex getVertexById(int id) throws IllegalStateException {
        Optional<Vertex> vertex = graph.getVertexById(id);
        if (vertex.isEmpty()) {
            throw new IllegalStateException();
        }
        return vertex.get();
    }

    public Edge getEdge(int v1id, int v2id) {
        Optional<Edge> edge = graph.getEdge(v1id, v2id);
        if (edge.isEmpty()) {
            throw new IllegalStateException();
        }
        return edge.get();
    }

    public Stream<Vertex> vertices(){
        return graph.vertices();
    }

    public Stream<Edge> edges(){
        return graph.edges();
    }

    public Edge existsEdge(int v1id, int v2id) {
        Optional<Vertex> v1optional = graph.vertices()
                .filter(vertex -> vertex.getvID() == v1id)
                .findFirst();
        Optional<Vertex> v2optional = graph.vertices()
                .filter(vertex -> vertex.getvID() == v2id)
                .findFirst();
        if(v1optional.isEmpty() || v2optional.isEmpty())
            throw new NullPointerException("Invalid Vertex!");

        Vertex v1 = v1optional.get();
        Vertex v2 = v2optional.get();
        Optional<Edge> edge = graph.existsEdge(v1, v2);

        if (edge.isEmpty()) {
            throw new IllegalStateException("No such edge exists!");
        }
        return edge.get();
    }

    public void addEdge(Edge edge) throws IllegalStateException {
        Optional<Edge> edgeOptional = graph.edges().filter(e -> e.equals(edge)).findAny();
        if (edgeOptional.isEmpty()) {
            graph.addEdge(edge);
        } else {
            throw new IllegalStateException("Edge already exists!");
        }
    }

    public void removeEdge(Edge edge) {
        graph.removeEdge(edge);
    }

    public void addVertex(Vertex vertex) {
        graph.addVertex(vertex);
    }

    public void removeVertex(Vertex vertex) {
        if (!graph.existsVertex(vertex.getvID())) {
            throw new IllegalStateException("No such vertex!");
        }
        graph.removeVertex(vertex);
    }
}
