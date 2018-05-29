package mas.behaviours;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.abstractAgent;
import mas.agents.ExplorerAgent;

import java.util.Arrays;
import java.util.Stack;

public class ExplorerWalk extends GeneralSimpleBehaviour {

	private static final long serialVersionUID = 9088209402507795289L;

	public ExplorerWalk(final mas.abstractAgent myagent) {
		super(myagent);
	}

	public void action() {

		// Set verbose to true do debug the behaviour
		boolean verbose = false;

		// define agent = agent from the behavior casted to general agent
		mas.agents.GeneralAgent agent = getGeneralAgent();

		if (verbose)
			System.out.println(agent.getLocalName() + " -> STARTING BEHAVIOUR!");

		// Get current position
		String myPosition = agent.getCurrentPosition();

		// Prints the current position
		if (verbose)
			System.out.println("Position of: " + agent.getLocalName() + ": " + myPosition);

		if (myPosition != "") {
			// Default move
			String myMove = myPosition;

			// List of observable from the agent's current position
			List<Couple<String, List<Attribute>>> lobs = agent.observe();
			if (verbose)
				System.out.println(agent.getLocalName() + " -- list of observables: " + lobs);

			/////////////////////////////////
			//// INTERBLOCKING

			if ((agent.getLastMove() != "" && !myPosition.equals(agent.getLastMove()))) {
				agent.emptyStack();
				String result = agent.getGraph().closestNode(myPosition, agent.getStack(), agent.getLastMove());
				if (result.equals("GO RANDOM")) {
					myMove = this.choseRandomMove(myPosition, lobs);
				} else
					myMove = agent.getStack().pop();
			}

			/////////////////////////////////
			//// NO INTERBLOCKING
			else {

				/////////////////////////////////
				//// STEP 1) Updating Graph and Hashmaps
				updatingGraph(myPosition, lobs);

				/////////////////////////////////
				//// STEP 2) Update the Stack if empty

				// If there are still some nodes to visit
				if (agent.getStack().empty() && !agent.areAllNodesVisited())
					agent.getGraph().bfs(myPosition, agent.getHashmap(), agent.getStack());

				// If there are still some treasure to update
				if (agent.getStack().empty() && agent.areAllNodesVisited()) {

					// Check if there are still treasures left ("all" types, size < INT_MAX)
					if (agent.getGraph().closestTreasure(myPosition, agent.getStack(), "all", 2147483647)
							.equals("NOT FOUND TREASURE TYPE")) {
						// random move if no more node to visit and no more treasure
						myMove = this.choseRandomMove(myPosition, lobs);
					}

				}
				/////////////////////////////////
				//// STEP 3) Pick the next Move and do it
				// Pick the next Node to go
				if (!agent.getStack().empty()) // avoid EmptyStackException
					myMove = agent.getStack().pop();
			}

			// Set last move to the next move, for next iteration
			agent.setLastMove(myMove);

			// Move to the picked location (must be last action)
			agent.moveTo(myMove);
		}
	}

	public boolean done() {
		this.defaultsleep();

		return false;
	}

}