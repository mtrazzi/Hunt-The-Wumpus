package mas.agents;


import env.EntityType;
import env.Environment;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas.abstractAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import env.Attribute;
import env.Couple;
import mas.behaviours.ExplorerWalk2;
import mas.behaviours.GeneralSimpleBehaviour;
import mas.behaviours.ReceiveVisited;
import mas.behaviours.SayVisited;
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

	/*protected void setup(){

		super.setup();

		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}

		//Add the behaviours
		addBehaviour(new RandomWalkExchangeBehaviour(this));


		System.out.println("the agent "+this.getLocalName()+ " is started");

	}
	*/

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

	@Override
	public void action() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);

			//list of attribute associated to the currentPosition
			List<Attribute> lattribute= lobs.get(0).getRight();

			//example related to the use of the backpack for the treasure hunt
			Boolean b=false;
			for(Attribute a:lattribute){
				switch (a) {
				case TREASURE : case DIAMONDS :
					//System.out.println("My treasure type is :"+((mas.abstractAgent)this.myAgent).getMyTreasureType());
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
			//System.out.println(this.myAgent.getLocalName()+" - My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
			//System.out.println(this.myAgent.getLocalName()+" - The agent tries to transfer is load into the Silo (if reachable); succes ? : "+((mas.abstractAgent)this.myAgent).emptyMyBackPack("Agent5"));
			//System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());

		/*	//Random move from the current position
			Random r= new Random();
			int moveId=1+r.nextInt(lobs.size()-1);

			//The move action (if any) should be the last action of your behaviour
			((mas.abstractAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
			*/
			//define agent = agent from the behavior casted to general agent
			mas.agents.GeneralAgent agent = getGeneralAgent();

			// Set verbose to true do debug the behaviour
			boolean verbose = false;
			
			
			// Prints the current position
			System.out.println("myPosition: " + myPosition);

			if (myPosition != "") {
				
				
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

	}
	
	public boolean done() {
		this.littlePause();
		
		// if done, do this
		if (getGeneralAgent().getStack().empty()) {
			System.out.println(this.myAgent.getLocalName() + " is Done!");
			getGeneralAgent().printCurrentHashmap();
		}

		// Explorer Agent is done if and only his stack is empty
		return (getGeneralAgent().getStack().empty());
	}
	}

}

