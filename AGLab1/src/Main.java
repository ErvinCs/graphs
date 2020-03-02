import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static void printMenu() {
        StringBuilder strBuilder = new StringBuilder("Menu\n");
        strBuilder.append("\t0. Exit\n");
        strBuilder.append("\t1. Number of vertices\n");
        strBuilder.append("\t2. Set of vertices\n");
        strBuilder.append("\t3. Check Edge existence between 2 vertices\n");
        strBuilder.append("\t4. In-Out degree of a vertex\n");
        strBuilder.append("\t5. Set of edges\n");
        strBuilder.append("\t6. Inbound edges of a vertex\n");
        strBuilder.append("\t7. Update edge information\n");
        strBuilder.append("\t8. Add edge\n");
        strBuilder.append("\t9. Add vertex\n");
        strBuilder.append("\t10. Remove edge\n");
        strBuilder.append("\t11. Remove vertex\n");
        strBuilder.append("\t12. Save graph\n");
        strBuilder.append("\t13. Load graph\n");
        strBuilder.append("\t14. Generate graph\n");
        System.out.println(strBuilder);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Graph graph = new Graph("res/res1.txt");
            boolean isRunning = true;
            int option;

            int v1, v2, answer, weight, noVertices, noEdges;
            Optional<Edge> optionalEdge;
            Vertex vertex1, vertex2;
            Edge edge;
            String filename;
            while(isRunning) {
                printMenu();
                System.out.println("Enter option: ");
                option = scanner.nextInt();
                try {
                    switch (option) {
                        case 0:
                            //Exit
                            isRunning = false;
                            break;
                        case 1:
                            //Number of vertices
                            System.out.println("Number of Vertices: " + graph.getNoVertices());
                            break;
                        case 2:
                            // Parse set of vertices
                            graph.vertices()
                                    .forEach(v -> System.out.println(v.toString()));
                            break;
                        case 3:
                            // Check edge existence
                            System.out.println("Give vertex #1 id: ");
                            v1 = scanner.nextInt();
                            System.out.println("Give vertex #2 id: ");
                            v2 = scanner.nextInt();
                            optionalEdge = graph.existsEdge(v1, v2);
                            if (optionalEdge.isEmpty()) {
                                System.out.println("No such Edge exists!");
                            } else {
                                System.out.println("Edge found: " + optionalEdge.get().toString());
                            }
                            break;
                        case 4:
                            // In-Out degree of a vertex
                            System.out.println("Give vertex id: ");
                            v1 = scanner.nextInt();
                            vertex1 = graph.getVertexById(v1);
                            System.out.println("In-degree=" + vertex1.getInDegree() + " | " + "Out-degree=" + vertex1.getOutDegree());
                            break;
                        case 5:
                            // Parse set of edges
                            graph.getEdges()
                                    .forEach(e -> System.out.println(e.toString()));
                            break;
                        case 6:
                            // Inbound edges of a verte
                            System.out.println("Give vertex id: ");
                            v1 = scanner.nextInt();
                            graph.getVertexById(v1)
                                    .inEdges()
                                    .forEach(e -> System.out.println(e.toString()));
                            break;
                        case 7:
                            // Update edge information
                            System.out.println("Give edge vertex1: ");
                            v1 = scanner.nextInt();
                            System.out.println("Give edge vertex2: ");
                            v2 = scanner.nextInt();
                            edge = graph.getEdge(v1, v2);
                            System.out.println(edge.toString());
                            System.out.println("Update(1/0): ");
                            answer = scanner.nextInt();
                            if (answer == 1) {
                                System.out.println("Give new weight: ");
                                weight = scanner.nextInt();
                                edge.setWeight(weight);
                            }
                            break;
                        case 8:
                            // Add edge
                            System.out.println("Give vertex1 id: ");
                            v1 = scanner.nextInt();
                            vertex1 = graph.getVertexById(v1);
                            System.out.println("Give vertex2 id: ");
                            v2 = scanner.nextInt();
                            vertex2 = graph.getVertexById(v2);
                            System.out.println("Give weight");
                            weight = scanner.nextInt();
                            edge = new Edge(vertex1, vertex2, weight);
                            graph.addEdge(edge);
                            break;
                        case 9:
                            // Add vertex
                            System.out.println("Give vertex id: ");
                            v1 = scanner.nextInt();
                            vertex1 = new Vertex(v1);
                            graph.addVertex(vertex1);
                            break;
                        case 10:
                            // Remove edge
                            System.out.println("Give edge vertex1: ");
                            v1 = scanner.nextInt();
                            System.out.println("Give edge vertex2: ");
                            v2 = scanner.nextInt();
                            edge = graph.getEdge(v1, v2);
                            graph.removeEdge(edge);
                            break;
                        case 11:
                            // Remove vertex
                            System.out.println("Give vertex id: ");
                            v1 = scanner.nextInt();
                            vertex1 = graph.getVertexById(v1);
                            graph.removeVertex(vertex1);
                            break;
                        case 12:
                            // Save graph
                            System.out.println("Name fle: ");
                            filename = scanner.next();
                            Graph.saveGraph(graph, filename);
                            break;
                        case 13:
                            System.out.println("Enter file name: ");
                            filename = "res/";
                            filename += scanner.next();
                            graph = Graph.readGraph(filename);
                            break;
                        case 14:
                            // Generate graph
                            System.out.println("Enter number of vertices: ");
                            noVertices = scanner.nextInt();
                            if (noVertices <= 0) {
                                System.out.println("Invalid number of vertices!");
                                break;
                            }
                            System.out.println("Enter number of edges: ");
                            noEdges = scanner.nextInt();
                            if (noEdges < 0) {
                                System.out.println("Invalid number of edges! ");
                            }
                            graph = Graph.generateGraph(noVertices, noEdges);
                            break;
                    }
                } catch (IllegalStateException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
