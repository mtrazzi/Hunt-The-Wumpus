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

public class ExplorerWalk2 extends GeneralSimpleBehaviour {

	private static final long serialVersionUID = 9088209402507795289L;

	public ExplorerWalk2(final mas.abstractAgent myagent) {
		super(myagent);
	}

	public void action() {
		//define agent = agent from the behavior casted to general agent
		mas.agents.GeneralAgent agent = getGeneralAgent();

		// Set verbose to true do debug the behaviour
		boolean verbose = false;
		
		//Get current position
		String myPosition = agent.getCurrentPosition();

		// Prints the current position
		System.out.println("myPosition: " + myPosition);

		if (myPosition != "") {
			
			// List of observable from the agent's current position
			List<Couple<String, List<Attribute>>> lobs = agent.observe();
			if (verbose)
				System.out.println(agent.getLocalName() + " -- list of observables: " + lobs);
			
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
			//// STEP 3) Update the Stack
			
			agent.getGraph().bfs(myPosition, agent.getHashmap(),agent.getStack());

			/////////////////////////////////
			//// STEP 4) Pick the next Move and do it
			
			// Pick the next Node to go
			String myMove = agent.getStack().pop();
			
			// Move to the picked location (must be last action)
			agent.moveTo(myMove);
		}

	}

	public boolean done() {
		this.sleep(200);
		
		getGeneralAgent().printStack();
		// if done, do this
		if (getGeneralAgent().getStack().empty()) {
			System.out.println(this.myAgent.getLocalName() + " is Done!");
			getGeneralAgent().printCurrentHashmap();
		}

		// Explorer Agent is done if and only his stack is empty
		return (getGeneralAgent().getStack().empty());
	}

}