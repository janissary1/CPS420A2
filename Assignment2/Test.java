import java.util.Scanner;

/**
 * Main test program
 * offers the ability to test randomly generated matrices
 * as well as matrices read from Scanner
 * @author Sophie Quigley
 * <br>You can modify this testing program as you wish to perform your tests
 */
public class Test {
    /**
     * Test graph
     */
    private static Graph graph;
    /**
     * Input stream
     */
    private static Scanner in = new Scanner(System.in);
    /**
     * Main test program either calls testRandom, testInput, or both.
     * @param args the command line arguments
     */     
    public static void main(String[] args) {
        //testRandom();
        String input = "6\n3 2 0 0 0 4\n2 4 0 3 3 2\n0 0 4 1 2 3\n0 3 1 4 2 0\n0 3 2 2 2 3\n4 2 3 0 3 4\n";
        in = new Scanner(input);
        testInput();
    }
   /**
    * Loops generating a random graph and looking for its Euler circuit if there is one
    */ 
    private static void testRandom() {
        for (int max=1; max<=5; max++) {
            graph = new Graph(6,max);
            processGraph();
        }        
    }
    
    /**
    * Loops reading graph from scanner and looking for its Euler circuit if there is one
    */ 
    private static void testInput() {
        while (in.hasNext())    {
            graph = new Graph(in);
            processGraph();
        }

    }
    
    /**
     * Prints graph, then looks for Euler circuit and print it out if one is found.
     */
    private static void processGraph() {
        // Print graph
        System.out.println("Graph has " + graph.getVertices() 
            + " vertices, and " + graph.getEdges() + " edges.");
        System.out.print(graph);
                    
        // Try to find an Euler circuit
        if (graph.hasEulerCircuit(true)) {
            Walk Euler = graph.getEulerCircuit();
            System.out.println(
                "Graph has the following Euler Circuit:\n" 
                    + Euler);  
        }
    }
    

}
