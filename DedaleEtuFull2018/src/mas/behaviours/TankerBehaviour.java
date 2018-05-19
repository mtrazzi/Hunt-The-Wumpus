package mas.behaviours;

import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;

public class TankerBehaviour extends GeneralSimpleBehaviour{

	private static final long serialVersionUID = 9088209402507795289L;

	public TankerBehaviour(final mas.abstractAgent myagent) {
		super(myagent);
	}

	public void action() {
		// Set verbose to true do debug the behaviour
		boolean verbose = true;
		
		//define agent = agent from the behavior casted to general agent
		mas.agents.GeneralAgent agent = getGeneralAgent();
		
		if (verbose)
			System.out.println(agent.getLocalName() + " -> STARTING BEHAVIOUR!");
		
		//Get current position
		String myPosition = agent.getCurrentPosition();

		// Prints the current position
		System.out.println("Position of: " + agent.getLocalName() + ": " + myPosition);

		if (myPosition != "") {
			String myMove;
			
			// List of observable from the agent's current position
			List<Couple<String, List<Attribute>>> lobs = agent.observe();
			if (verbose)
				System.out.println(agent.getLocalName() + " -- list of observables: " + lobs);
			
			/////////////////////////////////
			//// INTERBLOCKING
			@SuppressWarnings("unused")
			String lastMove = agent.getLastMove();
			

			
			// WITH RANDOM
			if (agent.getLastMove() != "" && !myPosition.equals(agent.getLastMove())) {
				System.err.println("MOVE DIDN'T WORK");
				while (!agent.getStack().empty())
					agent.getStack().pop();
				Random r = new Random();
				Integer moveId=r.nextInt(lobs.size());
				myMove = lobs.get(moveId).getLeft();
			}
			
			/////////////////////////////////
			//// NO INTERBLOCKING
			else {
				/////////////////////////////////
				//// STEP 1) Update Graph
				
				// Add myPosition to Graph
				agent.getGraph().addVertex(myPosition);
	
				// Set Node as discovered
				agent.getHashmap().put(myPosition, "discovered");
	
				/////////////////////////////////
				//// STEP 2) Update the Hashmap
				agent.UpdateEdges(myPosition, lobs);
				
				/////////////////////////////////
				//// STEP 3) Update the Stack if empty
				
				if (agent.getStack().empty())
					agent.getGraph().bfs(myPosition, agent.getHashmap(),agent.getStack());
	
				/////////////////////////////////
				//// STEP 4) Pick the next Move and do it
				
				// Pick the next Node to go
				myMove = agent.getStack().pop();
			}
			
			//Debug next move
			if (verbose) {
				System.out.println(agent.getLocalName() +"  -> Next Move: " + myMove);
				System.out.print(agent.getLocalName() +"  ->");
				getGeneralAgent().printStack();
			}
			
			// Set last move to the next move, for next iteration
			agent.setLastMove(myMove);
			
			// Move to the picked location (must be last action)
			agent.moveTo(myMove);
		}

	}

	public boolean done() {
		//this.sleep(100);
		this.littlePause();

		return (this.getGeneralAgent().areAllNodesVisited());
	}
}
