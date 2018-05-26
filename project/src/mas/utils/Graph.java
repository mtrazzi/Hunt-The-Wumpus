package mas.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.io.*;

/**
 * A simple undirected and unweighted graph implementation.
 * 
 * @param <T> The type that would be used as vertex.
 */
public class Graph<T> implements Serializable  {
    /**
	 * 
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
    		for (T adjacentNode : receivedGraph.getNeighbors(node)) {
    			addVertex(adjacentNode);
    			addEdge(node, adjacentNode);
    		}
    	}
    }
    
    public T bfs(T node, HashMap<String, String> H, Stack<T> S) {
    	T old = node;
    	Queue<T> f = new LinkedList<T>();
    	HashMap<T, String> h = new HashMap<T, String>();
    	HashMap<T,T> parent = new HashMap<T,T>();
    	f.add(node);
    	h.put(node, "visited");
		while (!f.isEmpty()) {
			node = f.remove();
			for (T adjacentNode : getNeighbors(node)) {
    			if (!h.containsKey(adjacentNode)) {
    				parent.put(adjacentNode,node);
    				if (!H.containsKey(adjacentNode)) {
    					updateStack(parent, old, adjacentNode, S);
    					return adjacentNode;
    				}
    				f.add(adjacentNode);
    				h.put(adjacentNode, "visited");
    			}
    		}
		}
		return node; //to make eclipse happy
    }
    
    void updateStack(HashMap<T,T> parent, T node, T result, Stack<T> S) {
    	while (!parent.get(result).equals(node)) {
    		S.push(result);
    		result = parent.get(result);
    	}
    	S.push(result);
    };

    //Check if all Nodes from Graph are visited in Hashmap
    public boolean isVisited(HashMap<String, String> H) {
    	Iterable<T> allVertices = this.getAllVertices();
    	for (T node : allVertices) {
    		String nodeName = (String) node;
    		if (!H.containsKey(nodeName) || !H.get(nodeName).equals("discovered"))
    			return false;
    	}
    	return true;
    }
    
    //Returns degree of node
    public int degree(T node) {
    	this.adjacencyList.get(node).remove(node);
    	return this.adjacencyList.get(node).size();
    }
    
    //BFS but the stop condition is to be a node of degree > 2
    public T closestNode(T node, Stack<T> S) {
    	T old = node;
    	Queue<T> f = new LinkedList<T>();
    	HashMap<T, String> h = new HashMap<T, String>();
    	HashMap<T,T> parent = new HashMap<T,T>();
    	f.add(node);
    	h.put(node, "visited");
		while (!f.isEmpty()) {
			node = f.remove();
			//System.out.println("node is " + node);
			for (T adjacentNode : getNeighbors(node)) {
				//System.out.println("in for with node = " + (String)adjacentNode );
    			if (!h.containsKey(adjacentNode)) {
    				//System.out.println("in first if");
    				parent.put(adjacentNode,node);
    				if (this.degree(adjacentNode) > 2) {
        				//System.out.println("in second if");
    					updateStack(parent, old, adjacentNode, S);
    					System.out.println("closest node is : " + (String)adjacentNode);
    					return adjacentNode;
    				}
    				f.add(adjacentNode);
    				h.put(adjacentNode, "visited");
    			}
    		}
		}
		updateStack(parent,old, node, S);
		System.out.println("(NOT REALLY BUT)closest node is : " + (String)node);
		return node; //to make eclipse happy
    }
    
}