import java.util.*;
import java.util.stream.Stream;

/**
 * Represents a vertex identified by an ID in a directed graph.
 * Holds references to all its inbound & outbound edges.
 */
public class Vertex {
    private int vID;
    private Set<Edge> in;
    private Set<Edge> out;
    private int degreeIn;
    private int degreeOut;

    /**
     * Creates a new vertex with the given id and no associated edges
     * @param id - integer
     */
    public Vertex(int id) {
        vID = id;
        in = new HashSet<>();
        degreeIn = 0;
        out = new HashSet<>();
        degreeOut = 0;
    }

    /**
     * @return Stream of inbound edges
     */
    public Stream<Edge> inEdges(){
        return in.stream();
    }

    /**
     * @return Stream of outbound edges
     */
    public Stream<Edge> outEdges(){
        return out.stream();
    }

    /**
     * Adds an edge to the set of inbound edges
     * @param edge - inbound Edge
     */
    public void addInEdge(Edge edge) {
        in.add(edge);
        degreeIn += 1;
    }

    /**
     * Adds an edge to the set of outbound edges
     * @param edge - outbound edge
     */
    public void addOutEdge(Edge edge) {
        out.add(edge);
        degreeOut += 1;
    }

    /**
     * Removes an edge from the set of inbound edges
     * @param edge - inbound edge
     */
    public void removeInEdge(Edge edge) {
        in.remove(edge);
        degreeIn -= 1;
    }

    /**
     * Removes an edge from the set of outbound edges
     * @param edge = outbound edge
     */
    public void removeOutEdge(Edge edge) {
        out.remove(edge);
        degreeOut -= 1;
    }

    /**
     * @return int - ID of the vertex
     */
    public int getvID() {
        return this.vID;
    }

    /**
     * @return int - the inbound degree of the vertex (number of inbound edges)
     */
    public int getInDegree() {
        return degreeIn;
    }

    /**
     * @return int - the outbound degree of the vertex (number of outbound edges)
     */
    public int getOutDegree() {
        return degreeOut;
    }

    /**
     * @return Set - the set of inbound edges
     */
    public Set<Edge> getInEdges() {
        return in;
    }

    /**
     * @return Set - the set of outbound edges
     */
    public Set<Edge> getOutEdges() {
        return out;
    }

    @Override
    public String toString() {
        return "Vertex{ID=" + vID + ", " +
                "In-Degree=" + degreeIn + ", " +
                "Out-Degree=" + degreeOut +  ", " +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        Vertex other = (Vertex)obj;
        return this.getvID() == other.getvID();
    }
}
