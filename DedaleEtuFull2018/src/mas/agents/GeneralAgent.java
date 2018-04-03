package mas.agents;

import env.Environment;
import mas.abstractAgent;
import mas.behaviours.*;

import java.util.*;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import mas.graph.Graph;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Stack;

import env.EntityType;

public class GeneralAgent extends abstractAgent{
	private static final long serialVersionUID = -1784844593772918359L;

	private Graph<String> graph;
	
	private HashMap<String,String> hashmap;
	
	private Stack<String> stack;
	
	private String lastMove;
	
	protected void takeDown(){

	}

	public Graph<String> getGraph() {
		return graph;
	}

	public void setGraph(Graph<String> graph) {
		this.graph = graph;
	}

	public HashMap<String,String> getHashmap() {
		return hashmap;
	}

	public void setHashmap(HashMap<String,String> hashmap) {
		this.hashmap = hashmap;
	}

	public Stack<String> getStack() {
		return stack;
	}

	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}

	public boolean containsStack(String element) {
		return Arrays.asList(this.stack.toArray()).contains(element);
	}

	public String getLastMove() {
		return lastMove;
	}

	public void setLastMove(String lastMove) {
		this.lastMove = lastMove;
	}
	
	public abstractAgent getAbstractAgent() {
		return (mas.abstractAgent)this;
	}
	
	public void printCurrentHashmap() {
		// Print the current Hashmap
		HashMap<String, String> myHashMap = this.getHashmap();
		for (String name : myHashMap.keySet()) {
			System.out.print('{');
			String key = name.toString();
			String value = myHashMap.get(name).toString();
			System.out.print('[' + key + ": " + value + "], ");
		}
	}
}
