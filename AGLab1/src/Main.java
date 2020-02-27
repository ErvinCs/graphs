import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Graph graph = new Graph("res/res1.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        //TODO - Menu

    }
}
