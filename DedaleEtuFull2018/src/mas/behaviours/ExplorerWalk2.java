package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.ExplorerAgent2;

import java.util.Arrays;
import java.util.Stack;

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
	
	public void UpdateEdges(String myPosition, List<Couple<String, List<Attribute>>> lobs) {
		for (int i = 0; i < lobs.size(); i++) {
			// Add Edge
			String adjacentNode = lobs.get(i).getLeft();
			getExplorerAgent().getGraph().addVertex(adjacentNode);
			getExplorerAgent().getGraph().addEdge(myPosition, adjacentNode);
		}
	}
	
	//Hardcoded, to change
	public void UpdateStackDFS(String myPosition, List<Couple<String, List<Attribute>>> lobs) {
		for (int i = 0; i < lobs.size(); i++) {
			String adjacentNode = lobs.get(i).getLeft();

			// Add adjacent node to stack if not in stack and not already visited
			if (!getExplorerAgent().getHashmap().containsKey(adjacentNode) //node not open
					&& !(getExplorerAgent().containsStack(adjacentNode))) { //push only if not in stack
				getExplorerAgent().getStack().push(adjacentNode);
			}
		}
	}
	
	//TO DO
	public void UpdateStackNeirest(String myPosition, List<Couple<String, List<Attribute>>> lobs) {
		//Check the neirest node not visited (node still open but not closed)
		//Stack <- path to neirest node (example : [1, 3, 4])
	}
	
	//TO DO
	Stack<String> PathToNeirestUnseen() {	//Called in UpdateStackNeirest
		return getExplorerAgent().getStack(); //TO CHANGE, only for eclipse warning
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
			//// STEP 1) Update Graph
			
			// Add myPosition to Graph
			getExplorerAgent().getGraph().addVertex(myPosition);

			// Set Node as discovered
			getExplorerAgent().getHashmap().put(myPosition, "discovered");

			/////////////////////////////////
			//// STEP 2) Update the Hashmap
			UpdateEdges(myPosition, lobs);
			
			/////////////////////////////////
			//// STEP 3) Update the Stack
			
			String s = getExplorerAgent().getGraph().bfs(myPosition, 
					getExplorerAgent().getHashmap(), getExplorerAgent().getStack());
			System.out.println("result of bfs" + s);
			//UpdateStackDFS(myPosition, lobs);

			/////////////////////////////////
			//// STEP 4) Pick the next Move and do it
			
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
//		try {
//			Thread.sleep(300);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// if done, do this
		if (getExplorerAgent().getStack().empty()) {
			System.out.println(this.myAgent.getLocalName() + " is Done!");
			getExplorerAgent().printCurrentHashmap();
		}

		// Explorer Agent is done if and only his stack is empty
		return (getExplorerAgent().getStack().empty());
	}

}