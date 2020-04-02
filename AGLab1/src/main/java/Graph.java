import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {
    private Set<Vertex> vertices;
    private Set<Edge> edges;
    private int noVertices;
    private int noEdges;

    // -------------------- Constructors --------------------
    /**
     * Creates an empty Graph object
     */
    public Graph() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
        this.noVertices = 0;
        this.noEdges = 0;
    }

    /**
     * Creates a graph with the given vertices and edges and sets their count accordingly
     * @param vertices - Set of Vertex
     * @param edges - Set of Edge
     */
    public Graph(Set<Vertex> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.noVertices = vertices.size();
        this.noEdges = edges.size();
    }

    /**
     * Creates a copy of the passed Graph
     * @param other - Graph to be copied
     */
    public Graph(Graph other) {
        vertices = other.vertices;
        edges = other.edges;
        noVertices = other.noVertices;
        noEdges = other.noEdges;
    }

    /**
     * Creates a graph read from the specified file
     * @param filename - String - path to the file
     * @throws FileNotFoundException
     *      Thrown if the given path does not specify a file
     */
    public Graph(String filename) throws FileNotFoundException {
        Graph graph = Graph.readGraph(filename);
        vertices = graph.vertices;
        edges = graph.edges;
        noVertices = graph.noVertices;
        noEdges = graph.noEdges;
    }

    // -------------------- Getters & Setters --------------------
    /**
     * @return Set of vertices of the graph
     */
    public Set<Vertex> getVertices() {
        return vertices;
    }

    /**
     * @return Set of edges of the graph
     */
    public Set<Edge> getEdges() {
        return edges;
    }

    /**
     * @return int - number of vertices of the graph
     */
    public int getNoVertices() {
        return noVertices;
    }

    /**
     * @return int - number of edges of the graph
     */
    public int getNoEdges() {
        return noEdges;
    }

    /**
     * Search for a Vertex in the graph, given the vertex ID
     * @param vid - ID of the Vertex
     * @return Optional<Vertex> - empty if a Vertex with the @vid does not exist; contains the Vertex otherwise
     */
    public Optional<Vertex> getVertexById(int vid) {
        Optional<Vertex> vertex = vertices.stream()
                .filter(v -> v.getvID() == vid)
                .findFirst();
        return vertex;
    }

    /**
     * Search for an Edge in the graph given its 2 vertices
     * @param v1id - Vertex 1 of the Edge (outbound)
     * @param v2id - Vertex 2 of the Edge (inbound)
     * @return Optional<Edge> - empty if the Edge does not exists; contains the Edge otherwise
     */
    public Optional<Edge> getEdge(int v1id, int v2id) {
        Optional<Edge> edge = edges.stream()
                .filter(e -> e.getV1().getvID() == v1id && e.getV2().getvID() == v2id)
                .findFirst();
        return edge;
    }

    // -------------------- Operations --------------------
    /**
     * Used to parse the set of vertices
     * @return Stream of Vertex for the graph
     */
    public Stream<Vertex> vertices(){
        return vertices.stream();
    }

    /**
     * Used to parse the set of edges
     * @return Stream of Edge for the graph
     */
    public Stream<Edge> edges(){
        return edges.stream();
    }

    /**
     * Checks the existence of a Vertex specified by its ID
     * @param vid - ID of the vertex to be checked
     * @return boolean - true if the Vertex exists; false otherwise
     */
    public boolean existsVertex(int vid) {
        return vertices.stream()
                .anyMatch(v -> v.getvID() == vid);
    }

    /**
     * Checks the existence of an Edge specified by its 2 vertices
     * @param v1 - Vertex of the Edge
     * @param v2 - Vertex of the Edge
     * @return Optional<Edge> - empty if the edge does not exists; otherwise contains the Edge
     */
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

    /**
     * Adds an edge to the graph
     * @param edge - Edge to be added
     */
    public void addEdge(Edge edge) {
        this.edges.add(edge);
        edge.getV1().addOutEdge(edge);
        edge.getV2().addInEdge(edge);
        this.noEdges++;
    }

    /**
     * Removes an edge from the graph
     * @param edge - Edge to be removed
     */
    public void removeEdge(Edge edge) {
        this.edges.remove(edge);
        edge.getV1().removeOutEdge(edge);
        edge.getV2().removeInEdge(edge);
        this.noEdges--;
    }

    /**
     * Adds a vertex to the graph
     * @param vertex - Vertex to be added
     */
    public void addVertex(Vertex vertex) {
        this.vertices.add(vertex);
        this.noVertices++;
    }

    /**
     * Removes a vertex from the graph and all its connected edges
     * @param vertex - Vertex to be removed
     */
    public void removeVertex(Vertex vertex) {
        this.edges = this.edges()
                .filter(edge -> !(edge.getV1() == vertex || edge.getV2() == vertex))
                .collect(Collectors.toSet());

        this.vertices.remove(vertex);
        this.vertices.stream()
                .filter(v -> v.getvID() > vertex.getvID())
                .forEach(v -> v.setvID(v.getvID() - 1));
        this.noVertices--;
        this.noEdges = this.edges.size();
    }

    // -------------------- Static --------------------

    /**
     * Generates a random directed graph with the specified number of vertices and edges
     * @param noVertices - int - the number of vertices
     * @param noEdges - int - the number of edges
     * @return Graph - generated graph
     */
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

    /**
     * Writes the passed graph to the specified file in the following manner:
     *      Number_of_vertices Number_of_Edges - on the first line
     *      Vertex_1(outbound) Vertex_2(inbound) Weight - until file end
     * @param graph - the Graph to be saved
     * @param filename - String - the name of the file to be written to
     * @throws IOException
     *      Thrown if the file could not be created succesfully
     */
    public static void saveGraph(Graph graph, String filename) throws IOException {
        File file = new File("res/" + filename);
        file.createNewFile();
        PrintWriter writer = new PrintWriter(new FileWriter(file));

        writer.println(graph.getNoVertices() + " " + graph.getNoEdges());
        Iterator<Edge> iterator = graph.edges().iterator();
        for(int i = 0; i < graph.noEdges-1; i++) {
            Edge edge = iterator.next();
            writer.println(edge.getV1().getvID() + " " + edge.getV2().getvID() + " " + edge.getWeight());
        }
        Edge edge = iterator.next();
        writer.print(edge.getV1().getvID() + " " + edge.getV2().getvID() + " " + edge.getWeight());

        writer.close();
    }

    /**
     * Read a directed graph from a file.
     * The file should be structured as:
     *      Number_of_vertices Number_of_Edges - on the first line
     *      Vertex_1(outbound) Vertex_2(inbound) Weight - until file end
     * The number of read vertices and edges should correspond to the values read on the 1st line
     * @param filename - String - path to the file that contains Graph data
     * @return Graph - containing the read vertices and edges
     * @throws FileNotFoundException
     *      Thrown if the file specified through @param filename does not exist
     * @throws IllegalStateException
     *      Thrown if the read values for vertex & edge count do not match the actual number of read edges and vertices
     */
    public static Graph readGraph(String filename) throws FileNotFoundException, IllegalStateException {
        Graph graph = new Graph();

        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        int noVertices, noEdges;
        noVertices = scanner.nextInt();
        noEdges = scanner.nextInt();

        for(int i = 0; i < noVertices; i++) {
            Vertex v = new Vertex(i);
            graph.addVertex(v);
        }

        int v1id, v2id, weight;
        while(scanner.hasNextLine()) {
            v1id = scanner.nextInt();
            v2id = scanner.nextInt();
            weight = scanner.nextInt();

            //if (!graph.existsVertex(v1id))
            //    graph.addVertex(new Vertex(v1id));
            Vertex v1 = graph.getVertexById(v1id).get();

            //if (!graph.existsVertex(v2id))
            //    graph.addVertex(new Vertex(v2id));
            Vertex v2 = graph.getVertexById(v2id).get();

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
