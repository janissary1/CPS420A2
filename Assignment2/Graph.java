import java.util.Scanner;
import java.util.Random;

/**
  * Graph objects can be used to work with undirected graphs.
  * They are internally represented using adjacency matrices.
  * @author Sophie Quigley
  * @author PUT YOUR NAMES HERE
  * <BR>THE ONLY METHODS THAT SHOULD BE MODIFIED ARE:
  * <BR>hasEulerCircuit, getEulerCircuit, 
  * and whichever helper function they need, such as findEuler.
  * 
  */
public class Graph {
    /**
     * Total number of vertices.
     */
    int vertices = 0;
    /**
     * Adjacency matrix of graph.
     * <br>edges[x][y] is the number of edges from vertex x to vertex y.
     */
    int[][] edges;
    /**
     * Total number of edges in graph
     */
    int totaledges = 0;
    /**
     * Used by graph visitors to keep track of visited vertices.
     */

    boolean[] visitedV;
    /**
     * Used by graph visitors to keep track of visited edges.
     */
    int[][] visitedE;
    /**
     * Used by graph visitors to keep track of unvisited edges
     * as an alternative to using visitedE.
     */
    int[][] unvisitedE;
    /**
     * Used to generate edges randomly
     */
    Random rand = new Random();   
    
    /**
     * 
     * Creates a new undirected Graph whose content will be read from the Scanner.
     * <br>Input format consists of non-negative integers separated by white space as follows:
     * <ul>
     * <li>First positive integer specifies the number of vertices n
     * <li>Next n*n integers specify the edges, listed in adjacency matrix order
     * </ul>
     * The graph information will be rejected when incorrect data is read.
     * @param in Scanner used to read graph description
     */
    Graph(Scanner in) {
        int i, j;
        boolean inputMistake = false;
        
        // Read number of vertices
        vertices = in.nextInt();
        if (vertices <= 0) {
            System.out.println("Error: number of vertices must be positive");
            vertices = 0;
            return;
        }
        
        // Read adjacency matrix        
        edges = new int[vertices][vertices];
        for (i=0; i<vertices; i++) {
            for (j=0; j<vertices; j++) {
                edges[i][j]=in.nextInt();
                if (j>=i)   totaledges += edges[i][j];
                if (edges[i][j] <0){
                    System.out.println("Error: number of edges cannot be negative");
                    inputMistake = true;            
                }
            }
        }
        
        // Verify that adjacency matrix is symmetric
        for (i=0; i<vertices; i++)
            for (j=i+1; j<vertices; j++)
                if (edges[i][j] != edges[j][i]) {
                    System.out.println(
                        "Error: adjacency matrix is not symmetric");
                    inputMistake = true;
                }
        
        if (inputMistake) 
            this.vertices = 0;
        
        // Prepare visitation status arrays
        else {
            visitedV = new boolean[vertices];
            visitedE = new int[vertices][vertices];
            unvisitedE = new int[vertices][vertices];   
            clearVisited();
        }
    }
    /**
     * Creates a randomly generated graph according to specifications,
     * or an empty graph if the specifications are faulty.
     * @param vertices Number of vertices in graph - must be positive
     * @param maxParallelEdges Maximum number of parallel edges between any two vertices - must be non-negative
     */
     Graph( int vertices, int maxParallelEdges) {
        if (vertices <= 0) {
            System.out.println("Error: number of vertices must be positive");
            return;
        }
        if (maxParallelEdges <0) {
            System.out.println("Error: maxVertexDegree cannot be negative");
            return;           
        }
        this.vertices = vertices;
        edges = new int[vertices][vertices]; 
         
        // Populate edges randomly        
        int randmax = maxParallelEdges+1;
        for (int i=0; i<vertices; i++)
            for (int j=i; j<vertices; j++) {
                edges[i][j] = rand.nextInt(randmax); 
                edges[j][i] = edges[i][j];
                totaledges += edges[i][j];
            }
        
        // Prepare visitation status arrays
        visitedV = new boolean[vertices];
        visitedE = new int[vertices][vertices];
        unvisitedE = new int[vertices][vertices];   
        clearVisited();
 
    }
    
    /**
     * Resets visitedV, visitedE, and unvisitedE matrices for a new visitation
     */
     private void clearVisited() {
        for (int i=0; i<vertices; i++) {
            visitedV[i] = false;
            for (int j=0; j<vertices; j++) {
                visitedE[i][j] = 0;
                unvisitedE[i][j] = edges[i][j];
            }
        }
    }
    
   /**
    * Returns a String representation of the graph
    * which is a 2-D representation of the adjacency matrix of that graph.
    * @return The 2-D representation of the adjacency matrix of that graph.
    * 
    */    
    @Override
    public String toString() {
        return matrixtoString(edges);
    }

    /**
     * Returns a String representation of 2 dimensional matrix
     * of size vertices X vertices.  
     * This can be used to visualize edges, visitedE, and unvisitedE
     * @param matrix matrix to be represented
     * @return 2D string representation of matrix
     */
    private String matrixtoString(int[][] matrix) {
        String result = "";
        for (int i=0; i<vertices; i++) {
            for (int j=0; j<vertices; j++) {
                result += matrix[i][j];
                result += " ";
            }
            result += "\n";
        }
        return result;
         
    }
    
    /**
    * Returns the number of vertices in the graph.
    * @return The number of vertices in the graph.
    *
    */  
    public int getVertices() {
        return vertices;
    }
    
    /**
    * Returns the number of edges in the graph.
    * @return The number of edges in the graph.
    *
    */  
    public int getEdges() {
        return totaledges;
    }   
    
    /**
     * Returns the adjacency matrix of the graph
     * @return The adjacency matrix of the graph 
     */
    public int[][] getMatrix() {
        return edges;
    }
    
    /**
     * Returns the number of edges from sourceV to destV 
     * @param sourceV The source vertex
     * @param destV The destination vertex
     * @return the number associated with edges from sourceV to destV
     */
    public int getEdges(int sourceV, int destV) {
        if (sourceV>=0 && sourceV<vertices && destV>=0 && destV<vertices)
            return edges[sourceV][destV];
        else
            return 0;
    }  
      
    /**
     * Verifies whether graph is connected
     * @return True iff graph is connected
     */
    public boolean isConnected() {
        clearVisited();
        DFSvisit(0);
        for (int i=0; i<vertices; i++)  
            if (!visitedV[i]) {
                clearVisited();
                return false;
            }
        clearVisited();
        return true;        
    }
    
    /**
     * Conducts a Depth First Search visit of the unvisited vertices 
     * of the graph starting at vertex.  
     * <br>Ties between vertices are broken in numeric order.
     * <br>Used by isConnected()
     * @param vertex First vertex to be visited.
     */
    private void DFSvisit(int vertex) {
        visitedV[vertex] = true;
        for (int i=0; i<vertices; i++)
            if (edges[vertex][i]!=0 && !visitedV[i])
                DFSvisit(i);
    } 
    /**
     * Decides whether the graph contains an Euler circuit
     * This implements Euler's theorem about the existence of Euler circuits,
     * @param printexplanation when this is true, an explanation is also printed.
     * @return True iff the graph contains an Euler circuit
     */
    
    public boolean hasEulerCircuit(boolean printexplanation) {
        if (!this.isConnected()) {
            if (printexplanation) System.out.println("No Euler circuit: Graph is not connected.");
            return false;
        }
        for (int row = 0; row < vertices; row++){
            int degree = 0;
            for (int column = 0; column < vertices; column++)
            {
                if (row == column) {
                    degree += edges[row][column] * 2; // loop
                } else {
                    degree += edges[row][column];
                }
            }
            if (degree % 2 != 0) {
                if (printexplanation) System.out.println("No Euler circuit: Vertex " + row + " has an odd degree (" + degree + ").");
                return false;
            }
        }
        if (printexplanation) System.out.println("Graph has Euler circuit: Graph is connected and all vertex degrees are even.");
        return true;
    }
    
    private boolean rowDepleted(int row) {
        for (int column = 0; column < unvisitedE[row].length; column += 1) {
            if (unvisitedE[row][column] > 0) return false;
        }
        return true;
    }
    
    /**
     * Finds a Euler circuit in graph if there is one.
     * @return an Euler circuit for the graph if there is one, or null otherwise.
     */
    public Walk getEulerCircuit() {
        clearVisited();
        Walk walk = new Walk(totaledges);
        Walk speculativeWalk = new Walk(totaledges);
        
        int row = 0;
        while (!rowDepleted(row) || speculativeWalk.totalVertices > 0) {
            // System.out.print("The row for vertex " + row + " is ");
            if (rowDepleted(row)) {
                // System.out.print("depleted, so we add " + row + " to the final walk. ");
                walk.addVertex(row);
                // System.out.print("The walk is now " + walk + "and row went from " + row);
                assert speculativeWalk.getVertices() > 0;
                row = speculativeWalk.getVertex(speculativeWalk.getVertices() - 1);
                speculativeWalk.removeLastVertex();
                // System.out.println(" to " + row + ".");
            } else {
                // System.out.print("not depleted, so we push " + row + " onto the speculative walk and take an edge from ");
                speculativeWalk.addVertex(row);
                for (int column = 0; column < vertices; column += 1) {
                    if (unvisitedE[row][column] > 0) {
                        // System.out.print(row + " to " + column + ".");
                        unvisitedE[row][column]--;
                        if (row != column) unvisitedE[column][row]--;
                        row = column;
                        break;
                    }
                }
            }
            // System.out.println("\nThe speculative walk is now " + speculativeWalk + ".");
        }
        // System.out.println(walk);
        clearVisited();
        return walk;
    }
    
    /**
     * This function implements a backtracking algorithm to find an Euler circuit
     * in a graph for which this has already been determined to be true.
     * It is a recursive function that assumes that a trail has already been built 
     * from the starting vertex to the current vertex and attempts to complete an
     * Euler circuit back to the starting vertex using the remaining edges.
     * @param startV starting vertex in Euler circuit being built
     * @param currentV vertex at the end of trail built so far
     * @param remainingEs number of remaining edges
     * @param Euler Walk being built 
     * @return True iff the Euler circuit has been successfully built 
     */
    private boolean findEuler(int startV, int currentV, int remainingEs, Walk Euler) {
        return false;
        //return false;
    }
    
}