package mas.agents;


import env.EntityType;
import env.Environment;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import env.Attribute;
import env.Couple;
import mas.behaviours.GeneralSimpleBehaviour;
import mas.graph.Graph;

public class DummyCollectorAgent extends GeneralAgent{

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
		addBehaviour(new RandomWalkExchangeBehaviour(this)); //To explore the map
		
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


/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


class RandomWalkExchangeBehaviour extends GeneralSimpleBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;

	public RandomWalkExchangeBehaviour (final mas.abstractAgent myagent) {
		super(myagent);
	}
	
	public void printAndPick(String myPosition, List<Couple<String,List<Attribute>>> lobs) {
		List<Attribute> lattribute= lobs.get(0).getRight();
		
		//example related to the use of the backpack for the treasure hunt
		Boolean b=false;
		for(Attribute a:lattribute){
			switch (a) {
			case TREASURE : case DIAMONDS :
				System.out.println("My treasure type is :"+((mas.abstractAgent)this.myAgent).getMyTreasureType());
				System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
				System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
				//System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				//System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
				b=true;
				break;

			default:
				break;
			}
		}

		//If the agent picked (part of) the treasure
		if (b){
			List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println("lobs after picking "+lobs2);
		}
	}
	
	//TODO: REMEMBER: to give to tanker = emptybackpack (cf. moodle)
	
	public boolean isObjectMyType(mas.agents.GeneralAgent agent) {
		List<Couple<String, List<Attribute>>> lobs = agent.observe();
		List<Attribute> lattribute= lobs.get(0).getRight();
		
		boolean b = false;
		for (Attribute a:lattribute) {
			System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
			System.err.println(agent.getLocalName() + "--> My type is " + agent.getMyTreasureType());
			switch (a) {
				case DIAMONDS:
					System.err.println(agent.getLocalName() + "-->I am on DIAMONDS");
					b = b || agent.getMyTreasureType().equals("Diamonds");
				break;
				case TREASURE:
					System.err.println(agent.getLocalName() + "-->I am on TREASURE");
					b = b || agent.getMyTreasureType().equals("Treasure");
				break;
			default:
				break;
			}
		}
		return b;
	}
	
	public void pickMyType(mas.agents.GeneralAgent agent) {
		System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
		if (isObjectMyType(agent)) {
			agent.pick();
		}
		System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
	}
	
	//public boolean pickWhileYouCan()

	@Override
	public void action() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		mas.agents.GeneralAgent agent = getGeneralAgent();
		
		boolean verbose = false;
		
		

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


			//TODO : factorize in General Agent
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
				//For Tanker
				agent.emptyMyBackPack("Agent5");
				/////////////////////////////////
				//// STEP 4) Pick the next Move and do it

				// Pick the next Node to go
				if (!agent.getStack().empty())
					myMove = agent.getStack().pop();
				else
					myMove = myPosition;
			}
			
			//Pick Treasure
			pickMyType(agent);

			// Set last move to the next move, for next iteration
			agent.setLastMove(myMove);

			// Move to the picked location (must be last action)
			agent.moveTo(myMove);
		}

	}
	
	public boolean done() {
	//	this.sleep(300);
		this.littlePause();

		return (this.getGeneralAgent().areAllNodesVisited());
	}
}

}

