import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class Graph {
    private Set<Vertex> vertices;
    private Set<Edge> edges;
    private int noVertices;
    private int noEdges;

    // Constructors
    public Graph() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
        this.noVertices = 0;
        this.noEdges = 0;
    }

    public Graph(Set<Vertex> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.noVertices = vertices.size();
        this.noEdges = edges.size();
    }

    public Graph(Graph other) {
        vertices = other.vertices;
        edges = other.edges;
        noVertices = other.noVertices;
        noEdges = other.noEdges;
    }

    public Graph(String filename) throws FileNotFoundException {
        Graph graph = Graph.readGraph(filename);
        vertices = graph.vertices;
        edges = graph.edges;
        noVertices = graph.noVertices;
        noEdges = graph.noEdges;
    }

    // Getters & Setters
    public Set<Vertex> getVertices() {
        return vertices;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public int getNoVertices() {
        return noVertices;
    }

    public int getNoEdges() {
        return noEdges;
    }

    public Vertex getVertexById(int id) throws IllegalStateException {
        Optional<Vertex> vertex = vertices.stream()
                .filter(v -> v.getvID() == id)
                .findFirst();
        if (vertex.isEmpty()) {
            throw new IllegalStateException();
        }
        return vertex.get();
    }

    public Edge getEdge(int v1id, int v2id) {
        Optional<Edge> edge = edges.stream()
                .filter(e -> e.getV1().getvID() == v1id && e.getV2().getvID() == v2id)
                .findFirst();
        if (edge.isEmpty()) {
            throw new IllegalStateException();
        }
        return edge.get();
    }

    // Operations
    public Stream<Vertex> vertices(){
        return vertices.stream();
    }

    public Stream<Edge> edges(){
        return edges.stream();
    }

    public Optional<Edge> existsEdge(int v1id, int v2id) {
        Optional<Vertex> v1optional = this.vertices.stream()
               .filter(vertex -> vertex.getvID() == v1id)
               .findFirst();

        Optional<Vertex> v2optional = this.vertices.stream()
                .filter(vertex -> vertex.getvID() == v2id)
                .findFirst();
        if(v1optional.isEmpty() || v2optional.isEmpty())
            throw new NullPointerException("Invalid Vertex!");

        Vertex v1 = v1optional.get();
        Vertex v2 = v2optional.get();
        Optional<Edge> edgeOptional;

        edgeOptional = v1.outEdges()
                .filter(edge -> edge.getV1().equals(v1) && edge.getV2().equals(v2))
                .findFirst();
        if (edgeOptional.isPresent()) {
            return edgeOptional;
        }

        edgeOptional = v2.inEdges()
                .filter(edge -> edge.getV1().equals(v2) && edge.getV2().equals(v1))
                .findFirst();

        return edgeOptional;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
        edge.getV1().addOutEdge(edge);
        edge.getV2().addInEdge(edge);
    }

    public void removeEdge(Edge edge) {
        this.edges.remove(edge);
        edge.getV1().removeOutEdge(edge);
        edge.getV2().removeInEdge(edge);
    }

    public void addVertex(Vertex vertex) {
        this.vertices.add(vertex);
    }

    public void removeVertex(Vertex vertex) {
        this.edges.forEach(edge -> {
                    if (edge.getV1() == vertex || edge.getV2() == vertex) {
                        edges.remove(edge);
                    }
                });
        this.vertices.remove(vertex);
    }

    public static Graph generateGraph() {
        //TODO
        return null;
    }

    public static void saveGraph(Graph graph) {
        //TODO
    }

    public static Graph readGraph(String filename) throws FileNotFoundException, IllegalStateException {
        Graph graph = new Graph();

        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        int noVertices, noEdges;
        noVertices = scanner.nextInt();
        noEdges = scanner.nextInt();

        int v1id, v2id, weight;
        while(scanner.hasNextLine()) {
            v1id = scanner.nextInt();
            v2id = scanner.nextInt();
            weight = scanner.nextInt();

            Vertex v1 = new Vertex(v1id);
            Vertex v2 = new Vertex(v2id);
            Edge edge = new Edge(v1, v2, weight);

            //TODO - Fix here
            v1.addOutEdge(edge);
            v2.addInEdge(edge);
            graph.vertices.add(v1);
            graph.vertices.add(v2);
            graph.edges.add(edge);
        }
        graph.noVertices = graph.vertices.size();
        graph.noEdges = graph.edges.size();

        graph.vertices().forEach(v -> System.out.println(v.toString()));

        //Safety Check
        if (graph.noEdges != noEdges)
            throw new IllegalStateException("Read number of edges not equal to actual number of edges");
        if (graph.noVertices != noVertices)
            throw new IllegalStateException("Read number of vertices not equal to actual number of vertices: specified=" + noVertices + " & generated=" + graph.noVertices);

        return graph;
    }
}
