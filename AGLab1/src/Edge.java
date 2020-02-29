public class Edge {
    private Vertex v1;
    private Vertex v2;
    private int weight;

    public Edge(Vertex v1, Vertex v2, int weight) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{Vertex1=" + v1.getvID() + ", Vertex2=" + v2.getvID() + ", Weight=" + weight + "}";
    }

    @Override
    public boolean equals(Object obj) {
        Edge other = (Edge)obj;
        return other.getV1() == this.getV1() && other.getV2() == this.getV2();
    }
}
