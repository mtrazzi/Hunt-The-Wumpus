package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import java.util.Random;


public class GeneralSimpleBehaviour extends SimpleBehaviour {
	/*
	 * Methods useful for all kind of agents. Available for every behavior inheriting from GeneralSimpleBehaviour.
	 */

	public GeneralSimpleBehaviour(final mas.abstractAgent myagent) {
		super(myagent);
	}

	public void action() {
	}

	public boolean done() {
		return true;
	}

	public mas.agents.GeneralAgent getGeneralAgent() {
		return (mas.agents.GeneralAgent) this.myAgent;
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

	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void debugStack(boolean verbose) {
		if (verbose) {
			System.out.print(getGeneralAgent().getLocalName() + "  ->");
			getGeneralAgent().printStack();
		}

	}

	public void debugNextMove(boolean verbose, String myMove) {
		if (verbose)
			System.out.println(getGeneralAgent().getLocalName() + "  -> Next Move: " + myMove);
	}

	public void defaultsleep() {
		this.sleep(100);
	}
	
	public String choseRandomMove(String myPosition, List<Couple<String, List<Attribute>>> lobs) {
		mas.agents.GeneralAgent agent = this.getGeneralAgent();
		String myMove = myPosition;
		Integer counter = new Integer(0);
		while (counter < 100 && (myMove.equals(myPosition) || myMove.equals(this.getGeneralAgent().getLastMove()))) {
			System.out.println("entering while loop with counter = " + counter);
			Random r = new Random();
			Integer moveId=r.nextInt(lobs.size());
			myMove = lobs.get(moveId).getLeft();
			counter += 1;
		}
		// Decrease nb of random moves (still) to do
		if (agent.getNbRandomMoves() > 0)
			agent.setNbRandomMoves(agent.getNbRandomMoves() - 1);
		return myMove;
	}
	
	public String choseMoveInterblocking(String myPosition, List<Couple<String, List<Attribute>>> lobs) {
		mas.agents.GeneralAgent agent = this.getGeneralAgent();
		
		// Counter counts number of moves that did not work
		agent.setCounter(agent.getCounter() + 1);
		
		// If to many moves didn't work (for instance two agents going random in front of one another)
		if (agent.getCounter() >= 5) {
			agent.setNbRandomMoves(10);
			agent.setCounter(0);
		}
		
		agent.emptyStack();
		if (agent.getGraph().isThereTankerAround(myPosition, lobs)
			|| agent.getNbRandomMoves() > 0 
			|| agent.getGraph().closestNode(myPosition, agent.getStack(), agent.getLastMove()).equals("GO RANDOM")) {
			return this.choseRandomMove(myPosition, lobs);
		}
		// if everything above is false, that means closestNode updated the Stack to the closes highest degree
		// So pop will give the next node of interest
		String result = agent.getStack().pop();
		while (result.equals(myPosition) || result.equals(this.getGeneralAgent().getLastMove())
				 && !agent.getStack().isEmpty())
			result = agent.getStack().pop();
		agent.setNbRandomMoves(agent.getNbRandomMoves() - 1);
		return result;
	}
	
	public void updatingGraph(String myPosition, List<Couple<String, List<Attribute>>> lobs) {
		mas.agents.GeneralAgent agent = this.getGeneralAgent();
		
		/////////////////////////////////
		//// STEP 1) Update Graph
		
		// Add myPosition to Graph
		agent.getGraph().addVertex(myPosition);

		// Set Node as discovered
		agent.getHashmap().put(myPosition, "discovered");

		/////////////////////////////////
		//// STEP 2) Update the Hashmaps
		
		agent.UpdateEdges(myPosition, lobs);
		agent.UpdateTreasureHashmap(lobs, myPosition);
	}
}
