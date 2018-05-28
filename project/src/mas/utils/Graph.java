package mas.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import env.Attribute;
import env.Couple;

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
	final private HashMap<String, MyCouple> treasureHashmap;
	final private HashMap<String, Long> timeStampHashmap;
	final private HashMap<String, String> isVisitedHashmap;
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
        this.treasureHashmap = new HashMap<>();
        this.timeStampHashmap = new HashMap<>();
        this.isVisitedHashmap = new HashMap<>();
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
    	
    	// Merging the Vertexes and the Edges
    	Iterable<T> allVertices = receivedGraph.getAllVertices();
    	for (T node : allVertices) {
    		addVertex(node);
    		for (T adjacentNode : receivedGraph.getNeighbors(node)) {
    			addVertex(adjacentNode);
    			addEdge(node, adjacentNode);
    		}
    	}
    	
    	// Merging the Treasure Hashmaps
    	Long myTime, receivedTime;
    	for (String node : receivedGraph.getTreasureHashmap().keySet()) {
			MyCouple value = receivedGraph.getTreasureHashmap().get(node);

			// Take the last update
			receivedTime = receivedGraph.timeStampHashmap.get(node);
    		if (this.treasureHashmap.containsKey(node)) {
    			myTime = this.timeStampHashmap.get(node);
    			if (myTime < receivedTime) {
    				this.treasureHashmap.put(node, value);
    			}
    		}
    		else {
    			this.treasureHashmap.put(node, value);
    			this.timeStampHashmap.put(node, receivedTime);
    		}
    	}
    	
    	// Merging the isVisitedHashmaps
    	
    	for (String node : receivedGraph.getIsVisitedHashmap().keySet()) {
    		this.isVisitedHashmap.put(node, "discovered");
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
    public T closestNode(T node, Stack<T> S, String lastMove) {
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
    				if (this.degree(adjacentNode) > 2) {
    					// Check if not neighbor to original node
    					if (adjacentNode.equals(lastMove)) {
    						return (T) "GO RANDOM";
    					}
    					
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
		return node;
    }
    
    public boolean isThereDesiredTreasure(T node, String treasureType, int backPackFreeSpace) {
    	// Check that there is indeed treasure on the node
    	if (!this.treasureHashmap.containsKey(node)) {
    		return false;
    	}
    	
    	MyCouple couple = this.treasureHashmap.get((String)node);
    
    	// How much treasure of the type I want?
    	int value = 0;
    	if (treasureType.equals("Diamonds")) {value = couple.getDiamonds();}
    	if (treasureType.equals("Treasure")) {value = couple.getTreasure();}
    	
    	//System.err.println("value is : " + value);
//    	if (value > 0 && value < backPackFreeSpace)
//    		System.out.println("result is true");
//    	else
//    		System.out.println("result is false");
		return (value > 0 && value < backPackFreeSpace);
    }
    
    //Same algorithm as closestNode above, but stop condition is "is there a treasure of my type"
    public T closestTreasure(T node, Stack<T> S, String treasureType, int backPackFreeSpace) {
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
    				if (isThereDesiredTreasure(adjacentNode, treasureType, backPackFreeSpace)) {
        				//System.out.println("in second if");
    					updateStack(parent, old, adjacentNode, S);
    					System.out.println("closest treasure of my type is : " + (String)adjacentNode);
    					return adjacentNode;
    				}
    				f.add(adjacentNode);
    				h.put(adjacentNode, "visited");
    			}
    		}
		}
		//updateStack(parent,old, node, S); NO UPDATE STACK IF NOTHING WAS FOUND
		//System.out.println("(NOT REALLY BUT)closest node is : " + (String)node);
		return (T) "NOT FOUND TREASURE TYPE";
    }
    
	public boolean isTankerAtPosition(String position) {
		if (!this.getTreasureHashmap().containsKey(position)) {
			return false;
		}
		return this.getTreasureHashmap().get(position).equals(-1, -1);
	}
    
    //Same algorithm as closestNode above, but stop condition is "is there a Tanker"
    public T closestTanker(T node, Stack<T> S) {
    	System.err.println("Doing Tanker BFS");
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
    				if (isTankerAtPosition((String)adjacentNode)) {
        				// We update stack from old to node and not adjacent node because Tanker is in adjacentNode
    					updateStack(parent, old, adjacentNode, S);
    					System.out.println("closest Tanker is at node: " + (String)adjacentNode);
    					return adjacentNode;
    				}
    				f.add(adjacentNode);
    				h.put(adjacentNode, "visited");
    			}
    		}
		}
		//updateStack(parent,old, node, S); NO UPDATE STACK IF NOTHING WAS FOUND
		//System.out.println("(NOT REALLY BUT)closest node is : " + (String)node);
		return (T) "TANKER NOT FOUND";
    }

	public HashMap<String, MyCouple> getTreasureHashmap() {
		return treasureHashmap;
	}

	public HashMap<String, Long> getTimeStampHashmap() {
		return timeStampHashmap;
	}
	
	public void printTreasureHashmap() {
		// Print the Treasure Hashmap
		HashMap<String, MyCouple> myHashMap = this.treasureHashmap;
		for (String name : myHashMap.keySet()) {
			System.out.print('{');
			String key = name.toString();
			String treasureValue = myHashMap.get(name).getTreasure().toString();
			String diamondValue = myHashMap.get(name).getDiamonds().toString();
			System.out.println('[' + key + ": (" + treasureValue + ", " + diamondValue + ")], ");
		}
	}

	public HashMap<String, String> getIsVisitedHashmap() {
		return isVisitedHashmap;
	}
	
	public boolean isThereTankerAround(T myPosition, List<Couple<String, List<Attribute>>> lobs) {
		//System.err.println("position is |" + (String) myPosition + "|");
		for (int i = 0; i < lobs.size(); i++) {
			String adjacentNode = lobs.get(i).getLeft();
			//System.out.println("considering node " + adjacentNode + "( i = " + i + " )");
			if (this.getTreasureHashmap().containsKey(adjacentNode)) {
				//System.out.println("first if");
				if (this.isTankerAtPosition((String)adjacentNode)) {
					System.out.println("Tanker is around");
					return true;
				}
			}
		}
		System.out.println("Tanker is not around");
		return false;
	}
    
}