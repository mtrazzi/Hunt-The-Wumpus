package mas.agents;


import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import env.Attribute;
import env.Couple;
import env.EntityType;
import env.Environment;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas.abstractAgent;
import mas.agents.DummyCollectorAgent.RandomWalkExchangeBehaviour;
import mas.behaviours.GeneralSimpleBehaviour;
import mas.graph.Graph;


public class DummyTankerAgent extends GeneralAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784844593772918359L;



	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType( "collector"); /* donner un nom aux services qu'on propose */
		sd.setName(getLocalName());
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		}	catch (FIPAException fe) { fe.printStackTrace();}
		
		super.setup();

		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args[0]!=null){

			deployAgent((Environment) args[0],(EntityType)args[1]);

		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		
		//Add the behaviours
		addBehaviour(new RandomTankerBehaviour(this)); //To explore the map
		
		//Initialize attributes
		this.setGraph(new Graph<String>());
		this.setHashmap(new HashMap<String, String>());
		this.setStack(new Stack<String>());
		this.setLastMove("");

		System.out.println("the agent "+this.getLocalName()+ " is started");


	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}


/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/

class RandomTankerBehaviour extends GeneralSimpleBehaviour{

	private static final long serialVersionUID = 9088209402507795289L;

	public RandomTankerBehaviour(final mas.abstractAgent myagent) {
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
		this.sleep(100);

		return (this.getGeneralAgent().areAllNodesVisited());
	}
}