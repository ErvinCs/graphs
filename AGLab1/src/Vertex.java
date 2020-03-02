import java.util.*;
import java.util.stream.Stream;

public class Vertex {
    private int vID;
    private Set<Edge> in;
    private Set<Edge> out;
    private int degreeIn;
    private int degreeOut;

    public Vertex(int id) {
        vID = id;
        in = new HashSet<>();
        degreeIn = 0;
        out = new HashSet<>();
        degreeOut = 0;
    }

    public Stream<Edge> inEdges(){
        return in.stream();
    }

    public Stream<Edge> outEdges(){
        return out.stream();
    }

    public void addInEdge(Edge edge) {
        in.add(edge);
        degreeIn += 1;
    }

    public void addOutEdge(Edge edge) {
        out.add(edge);
        degreeOut += 1;
    }

    public void removeInEdge(Edge edge) {
        in.remove(edge);
        degreeIn -= 1;
    }

    public void removeOutEdge(Edge edge) {
        out.remove(edge);
        degreeOut -= 1;
    }

    public void setvId(int vID) {
        this.vID = vID;
    }

    public int getvID() {
        return this.vID;
    }

    public int getInDegree() {
        return degreeIn;
    }

    public int getOutDegree() {
        return degreeOut;
    }

    public Set<Edge> getInEdges() {
        return in;
    }

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
