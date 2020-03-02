import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
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

    public Optional<Vertex> getVertexById(int vid) throws IllegalStateException {
        Optional<Vertex> vertex = vertices.stream()
                .filter(v -> v.getvID() == vid)
                .findFirst();
        return vertex;
    }

    public Optional<Edge> getEdge(int v1id, int v2id) {
        Optional<Edge> edge = edges.stream()
                .filter(e -> e.getV1().getvID() == v1id && e.getV2().getvID() == v2id)
                .findFirst();
        return edge;
    }

    // Operations
    public Stream<Vertex> vertices(){
        return vertices.stream();
    }

    public Stream<Edge> edges(){
        return edges.stream();
    }

    public boolean existsVertex(int vid) {
        return vertices.stream()
                .anyMatch(v -> v.getvID() == vid);
    }

    public Optional<Edge> existsEdge(Vertex v1, Vertex v2) {
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
        if (edgeOptional.isPresent()) {
            return edgeOptional;
        }        
        return Optional.empty();
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
        edge.getV1().addOutEdge(edge);
        edge.getV2().addInEdge(edge);
        this.noEdges++;
    }

    public void removeEdge(Edge edge) {
        this.edges.remove(edge);
        edge.getV1().removeOutEdge(edge);
        edge.getV2().removeInEdge(edge);
        this.noEdges--;
    }

    public void addVertex(Vertex vertex) {
        this.vertices.add(vertex);
        this.noVertices++;
    }

    public void removeVertex(Vertex vertex) {
        this.edges = this.edges()
                .filter(edge -> !(edge.getV1() == vertex || edge.getV2() == vertex))
                .collect(Collectors.toSet());

        this.vertices.remove(vertex);
        this.noVertices--;
    }

    public static Graph generateGraph(int noVertices, int noEdges) {
        Graph g = new Graph();
        Random rand = new Random();

        for(int i = 0; i < noVertices; i++) {
            g.addVertex(new Vertex(i));
        }

        int i = 0;
        while (i < noEdges) {
            int v1id = rand.nextInt(noVertices);
            int v2id = rand.nextInt(noVertices);
            Vertex v1 = g.getVertexById(v1id).get();
            Vertex v2 = g.getVertexById(v2id).get();

            if (g.existsEdge(v1, v2).isPresent()) {
                continue;
            }

            int weight = rand.nextInt(500);
            g.addEdge(new Edge(v1, v2, weight));
            i++;
        }

        return g;
    }

    public static void saveGraph(Graph graph, String filename) throws IOException {
        File file = new File("res/" + filename);
        file.createNewFile();
        PrintWriter writer = new PrintWriter(new FileWriter(file));

        writer.println(graph.getNoVertices() + " " + graph.getNoEdges());
        for(Edge edge : graph.getEdges()) {
            writer.println(edge.getV1().getvID() + " " + edge.getV2().getvID() + " " + edge.getWeight());
        }

        writer.close();
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

            Vertex v1;
            if (graph.existsVertex(v1id)) {
                v1 = graph.getVertexById(v1id).get();
            } else {
                v1 = new Vertex(v1id);
                graph.addVertex(v1);
            }

            Vertex v2;
            if (graph.existsVertex(v2id)) {
                v2 = graph.getVertexById(v2id).get();
            } else {
                v2 = new Vertex(v2id);
                graph.addVertex(v2);
            }

            Edge edge = new Edge(v1, v2, weight);

            v1.addOutEdge(edge);
            v2.addInEdge(edge);
            graph.edges.add(edge);
        }
        graph.noVertices = graph.vertices.size();
        graph.noEdges = graph.edges.size();

        //Safety Check
        if (graph.noEdges != noEdges)
            throw new IllegalStateException("Read number of edges not equal to actual number of edges");
        if (graph.noVertices != noVertices)
            throw new IllegalStateException("Read number of vertices not equal to actual number of vertices: specified=" + noVertices + " & generated=" + graph.noVertices);

        scanner.close();

        return graph;
    }
}
