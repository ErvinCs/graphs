import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static void printMenu() {
        String string = "";
        //TODO
        System.out.println(string);
    }

    public static void main(String[] args) {
        try {
            Graph graph = new Graph("res/res1.txt");
            boolean isRunning = true;
            int option;
            Scanner scanner = new Scanner(System.in);

            while(isRunning) {
                printMenu();
                System.out.println("Enter option: ");
                option = scanner.nextInt();
                switch (option) {
                    case 0:
                        isRunning = false;
                        break;
                    case 1:
                        System.out.println("Number of Vertices: " + graph.getNoVertices());
                    case 2:
                        graph.vertices()
                                .forEach(v -> System.out.println(v.toString()));
                    case 3:
                        int v1, v2;
                        System.out.println("Give vertex #1 id: ");
                        v1 = scanner.nextInt();
                        System.out.println("Give vertex #2 id: ");
                        v2 = scanner.nextInt();
                        Optional<Edge> optionalEdge = graph.existsEdge(v1, v2);
                        if (optionalEdge.isEmpty()) {
                            System.out.println("No such Edge exists!");
                        } else {
                            System.out.println("Edge found: " + optionalEdge.get().toString());
                        }
                    case 4:
                        System.out.println("Give vertex id: ");
                        int vid = scanner.nextInt();
                        Vertex v = graph.getVertexById(vid);
                        System.out.println("In-degree=" + v.getInDegree() + " | " + "Out-degree=" + v.getOutDegree());
                    case 5:
                        //TODO
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }



    }
}
