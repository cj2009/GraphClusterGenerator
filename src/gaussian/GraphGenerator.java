/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gaussian;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author hduser
 */
public class GraphGenerator {
    
    static Random r;
    
    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {
        int NUM_CLUSTERS = 70;
        r = new Random();
        int beginIndex = 0;
        ArrayList<String> edges = new ArrayList();
        
        // Create 10 clusters, each of size 5-20 nodes.
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            int V = r.nextInt(20 - 5 + 1) + 5;
            int matrix[][] = GenerateGraph(V);
            
            
            // Parse this matrix and create strings for each edge, adding it to
            // the arraylist:
            for (int j = 0; j < matrix.length; j++) {
                for (int k = j+1; k < matrix.length; k++) {
                    if (matrix[j][k] == 1) {
                        String edge = (j+beginIndex) + "\t" + (k+beginIndex);
                        edges.add(edge);
                    }
                }
            }
            beginIndex += V;
        }
        System.out.println("Done generating clusters.");
        
        
        
        // Now add some sparse edges between these clusters.
        // The total range of all nodes in this graph is between 0 and beginIndex.
        // Therefore, E = beginIndex * (beginIndex - 1) / 2 (max number of possible edges)
        int E = beginIndex * (beginIndex - 1) / 2;
        
        // Let's add about x% of the total possible number of edges:
        E *= 0.03;
        int edgesAdded = 0;
        while (edgesAdded < E) {
            int v1 = r.nextInt(beginIndex);
            int v2 = r.nextInt(beginIndex);
            if (v1 != v2) {
                String edge;
                if (v1 < v2)
                    edge = v1 + "\t" + v2;
                else
                    edge = v2 + "\t" + v1;                
                if (!edges.contains(edge)) {
                    edgesAdded++;
                    edges.add(edge);
                }
                    
            }
        }
        System.out.println("Done adding inter-cluster edges.");
        
        
        PrintWriter writer = new PrintWriter(beginIndex + "-vertices_" + E + "-edges.txt", "UTF-8");
        for (String s : edges)
            writer.println(s);
        writer.close();
        
    }
    
    public static int[][] GenerateGraph(int V) {
        int matrix[][] = new int[V][V];
       
        // Fill in with all 1s initially:
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                matrix[i][j] = 1;
            }
        }
        
        // Now remove some edges randomly. Remove anywhere from 
        // 0.1 * (V*V - V) / 2 to 0.3 * (V*V - V) / 2 edges.
        int removeLimit1 = (int) (0.1 * (V*V - V) / 2);
        int removeLimit2 = (int) (0.3 * (V*V - V) / 2);
        int toRemove = r.nextInt(removeLimit2 - removeLimit1 + 1) - removeLimit1;
        
        int edgesRemoved = 0;
        
        while (edgesRemoved < toRemove) {
            int v1 = r.nextInt(V);
            int v2 = r.nextInt(V);
            if (v1 != v2) {
                if (matrix[v1][v2] == 1) {
                    // remove this edge:
                    matrix[v1][v2] = 0;
                    matrix[v2][v1] = 0;
                    edgesRemoved++;
                }
            }
        }
        return matrix;
    }
}
