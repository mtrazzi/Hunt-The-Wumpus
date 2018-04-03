package mas.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.io.*;

/**
 * A simple undirected and unweighted graph implementation.
 * 
 * @param <T> The type that would be used as vertex.
 */
public class Graph<T> implements Serializable  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final private HashMap<T, Set<T>> adjacencyList;
    private int nbEdges;
    /**
     * Create new Graph object.
     */
    
    public int getNbEdges() {
    	return this.nbEdges;
    }
    
    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.nbEdges = 0;
    }
    
    /**
     * Add new vertex to the graph.
     * 
     * @param v The vertex object. 
     */
    public void addVertex(T v) {
        if (this.adjacencyList.containsKey(v)) {
        	return;
            //throw new IllegalArgumentException("Vertex already exists.");
        }
        this.adjacencyList.put(v, new HashSet<T>());
        this.nbEdges += 1;
    }
    
    /**
     * Remove the vertex v from the graph.
     * 
     * @param v The vertex that will be removed.
     */
    public void removeVertex(T v) {
        if (!this.adjacencyList.containsKey(v)) {
            //throw new IllegalArgumentException("Vertex doesn't exist.");
        	return;
        }
        
        this.adjacencyList.remove(v);
        this.nbEdges -= 1;
        
        for (T u: this.getAllVertices()) {
            this.adjacencyList.get(u).remove(v);
        }
    }
    
    /**
     * Add new edge between vertex. Adding new edge from u to v will
     * automatically add new edge from v to u since the graph is undirected.
     * 
     * @param v Start vertex.
     * @param u Destination vertex.
     */
    public void addEdge(T v, T u) {
        if (!this.adjacencyList.containsKey(v) || !this.adjacencyList.containsKey(u)) {
            //throw new IllegalArgumentException();
        	return;
        }
        
        this.adjacencyList.get(v).add(u);
        this.adjacencyList.get(u).add(v);
    }
    
    /**
     * Remove the edge between vertex. Removing the edge from u to v will 
     * automatically remove the edge from v to u since the graph is undirected.
     * 
     * @param v Start vertex.
     * @param u Destination vertex.
     */
    public void removeEdge(T v, T u) {
        if (!this.adjacencyList.containsKey(v) || !this.adjacencyList.containsKey(u)) {
            throw new IllegalArgumentException();
        }
        
        this.adjacencyList.get(v).remove(u);
        this.adjacencyList.get(u).remove(v);
    }
    
    /**
     * Check adjacency between 2 vertices in the graph.
     * 
     * @param v Start vertex.
     * @param u Destination vertex.
     * @return <tt>true</tt> if the vertex v and u are connected.
     */
    public boolean isAdjacent(T v, T u) {
        return this.adjacencyList.get(v).contains(u);
    }
    
    /**
     * Get connected vertices of a vertex.
     * 
     * @param v The vertex.
     * @return An iterable for connected vertices.
     */
    public Iterable<T> getNeighbors(T v) {
        return this.adjacencyList.get(v);
    }
    
    /**
     * Get all vertices in the graph.
     * 
     * @return An Iterable for all vertices in the graph.
     */
    public Iterable<T> getAllVertices() {
        return this.adjacencyList.keySet();
    }
    
    public void printNodes() {
    	Iterable<T> allVertices = getAllVertices();
    	for (T node : allVertices) {
    		System.out.print((String)node + " ");
    	}
    	System.out.println("");
    }
    
    public void printEdges() {
    	for (T node : getAllVertices()) {
    		System.out.println("Neighbor of " + (String)node);
    		for (T adjacentNode : getNeighbors(node)) {
    			System.out.println("-->" + adjacentNode);
    		}
    	}
    }
    
    //Merge the received graph with current graph
    public void mergeGraph(Graph<T> receivedGraph) {
    	Iterable<T> allVertices = receivedGraph.getAllVertices();
    	for (T node : allVertices) {
    		addVertex(node);
    		for (T adjacentNode : getNeighbors(node)) {
    			addEdge(node, adjacentNode);
    		}
    	}
    }
    
}