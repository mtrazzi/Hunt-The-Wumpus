package mas.behaviours;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.ExplorerAgent2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.lang.Thread;

/**************************************
 * 
 * 
 * BEHAVIOUR
 * 
 * 
 **************************************/

// TO DO :

// Behaviour that add observed nodes into graph and mark nodes as "to visit OR
// visited"

// If a node to be visited is neighbour, go to this node

// Else, compute a Djikstra to know which is the neirest neighbor, and add to
// stack the path to this node to go there

public class ExplorerWalk2 extends SimpleBehaviour {
	/**
	 * When an agent choose to move
	 * 
	 */
	private static final long serialVersionUID = 9088209402507795289L;

	public ExplorerWalk2(final mas.abstractAgent myagent) {
		super(myagent);
	}

	public abstractAgent getAbstractAgent() {
		return (mas.abstractAgent)this.myAgent;
	}

	public ExplorerAgent2 getExplorerAgent() {
		return (mas.agents.ExplorerAgent2)this.myAgent;
	}

	public void printCurrentHashmap() {
		// Print the current Hashmap
		HashMap<String, String> myHashMap = getExplorerAgent().getHashmap();
		for (String name : myHashMap.keySet()) {
			System.out.print('{');
			String key = name.toString();
			String value = myHashMap.get(name).toString();
			System.out.print('[' + key + ": " + value + "], ");
		}
	}

	public void printStack() {
		System.out.println("Stack: " + Arrays.toString(getExplorerAgent().getStack().toArray()));
	}
	
	public void littlePause() {
		// Little pause to allow you to follow what is going on
		try {
			System.out.println("Press Enter in the console to allow the agent " + this.myAgent.getLocalName()
					+ " to execute its next move");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void UpdateHashmapAndStack(String myPosition, List<Couple<String, List<Attribute>>> lobs) {
		for (int i = 0; i < lobs.size(); i++) {
			// Add Edge
			String adjacentNode = lobs.get(i).getLeft();
			getExplorerAgent().getGraph().addEdge(myPosition, adjacentNode);
			// Add adjacent node to stack if not in stack and not already visited
			if (!getExplorerAgent().getHashmap().containsKey(adjacentNode)
					&& !(getExplorerAgent().containsStack(adjacentNode))) {
				getExplorerAgent().getStack().push(adjacentNode);
			}
		}
	}

	public void action() {
		// Set verbose to true do debug the behaviour
		boolean verbose = false;
		
		//Get current position
		String myPosition = getAbstractAgent().getCurrentPosition();

		// Prints the current position
		System.out.println("myPosition: " + myPosition);

		if (myPosition != "") {
			
			// List of observable from the agent's current position
			List<Couple<String, List<Attribute>>> lobs = getAbstractAgent().observe();
			if (verbose)
				System.out.println(this.myAgent.getLocalName() + " -- list of observables: " + lobs);
			
			/////////////////////////////////
			//// Update Graph
			
			// Add myPosition to Graph
			getExplorerAgent().getGraph().addVertex(myPosition);

			// Set Node as discovered
			getExplorerAgent().getHashmap().put(myPosition, "discovered");

			/////////////////////////////////
			//// Update the Hashmap and the Stack
			UpdateHashmapAndStack(myPosition, lobs);

			/////////////////////////////////
			//// Pick the next Move and do it
			
			// Pick the next Node to go
			String myMove = getExplorerAgent().getStack().pop();

			// Push current position to be sure to come back safe
			if (!getExplorerAgent().getHashmap().containsKey(myMove)) {
				getExplorerAgent().getStack().push(myPosition);
			}
			
			// Move to the picked location
			// Must the last action of your behaviour
			getAbstractAgent().moveTo(myMove);
		}

	}

	public boolean done() {
		littlePause();
		
		// if done, do this
		if (getExplorerAgent().getStack().empty()) {
			System.out.println(this.myAgent.getLocalName() + " is Done!");
			this.printCurrentHashmap();
		}

		// Explorer Agent is done if and only his stack is empty
		return (getExplorerAgent().getStack().empty());
	}

}