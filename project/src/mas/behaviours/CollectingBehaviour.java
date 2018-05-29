package mas.behaviours;

import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class CollectingBehaviour extends GeneralSimpleBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;
	
	private boolean verbose = false;

	public CollectingBehaviour (final mas.abstractAgent myagent) {
		super(myagent);
	}
	
	public void printAndPick(String myPosition, List<Couple<String,List<Attribute>>> lobs) {
		List<Attribute> lattribute= lobs.get(0).getRight();
		
		//example related to the use of the backpack for the treasure hunt
		Boolean b=false;
		for(Attribute a:lattribute){
			switch (a) {
			case TREASURE : case DIAMONDS :
				if (verbose) {
					System.out.println("My treasure type is :"+((mas.abstractAgent)this.myAgent).getMyTreasureType());
					System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
					System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
					System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
					//System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
					//System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
				}
				b=true;
				break;

			default:
				break;
			}
		}

		//If the agent picked (part of) the treasure
		if (b && verbose){
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
			//System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
			//System.err.println(agent.getLocalName() + "--> My type is " + agent.getMyTreasureType());
			switch (a) {
				case DIAMONDS:
					if (verbose) {
						System.out.println(agent.getLocalName() + "is on DIAMONDS");
					}
					b = b || agent.getMyTreasureType().equals("Diamonds");
				break;
				case TREASURE:
					if (verbose) {
						System.out.println(agent.getLocalName() + "is on TREASURE");
					}
					b = b || agent.getMyTreasureType().equals("Treasure");
				break;
			default:
				break;
			}
		}
		return b;
	}
	
	public void pickMyType(mas.agents.GeneralAgent agent) {
		if (verbose) {
			System.out.println(agent.getLocalName() + "current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
		}
		if (isObjectMyType(agent)) {
			Integer tmp = agent.pick();
			if (verbose) {
				System.err.println(agent.getLocalName() + " was able to pick: " + tmp.toString());
				System.err.println(agent.getLocalName() + " remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
			}
		}
	}
	
	public boolean canPick(String myPosition, List<Couple<String,List<Attribute>>> lobs) {
		List<Attribute> lattribute= lobs.get(0).getRight();
		
		//TEST
		if (!isObjectMyType(getGeneralAgent())) {
			return false;
		}
		
		for(Attribute a:lattribute){
			switch (a) {
			case TREASURE : case DIAMONDS :
				//System.out.println("My treasure type is :"+((mas.abstractAgent)this.myAgent).getMyTreasureType());
				//System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
				System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
				//System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				//System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
				
				if (((mas.abstractAgent)this.myAgent).getBackPackFreeSpace() >= Integer.valueOf(a.getValue().toString())) {
					System.err.println("I can pick more!");
					return true;
				}
				break;

			default:
				break;
			}
		}
		
		return false;
	}
	
	//
	public String getTankerName() {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("tanker");
		dfd.addServices(sd);

		// Searching for agents with desired qualities
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(this.getGeneralAgent(), dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("name of the Tanker: |" + result[0].getName().getLocalName() + "|");
		return result[0].getName().getLocalName();
	}
	
	public void giveTreasureToTanker() {
		System.out.println("Trying to give my treasures to Tanker");
		if (this.getGeneralAgent().emptyMyBackPack(getTankerName()))
			System.err.println("IT WORKED!");
		else
			System.err.println("FAILURE");
	}

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

			//Start by Picking Treasure!
			pickMyType(agent);

			/////////////////////////////////
			//// INTERBLOCKING
			if (agent.getNbRandomMoves() > 0 ||
				(agent.getLastMove() != "" && !myPosition.equals(agent.getLastMove()))) {
				myMove = choseMoveInterblocking(myPosition, lobs);
			}

			/////////////////////////////////
			//// NO INTERBLOCKING
			else {

				/////////////////////////////////
				//// STEP 1) Updating Graph and Hashmaps
				updatingGraph(myPosition,lobs);

				/////////////////////////////////
				//// STEP 2) Update the Stack to go for the closest treasure if there is one.
				//// 		 Otherwise, if backpack is full or no treasure around, go for the closest Tanker to give treasure
				////		 If no treasure and no tanker, explore the map
				if (agent.getStack().empty()) {
					// if there is no treasure (for current capacity) around, it will enter the if. If there is one around, the function "closestTreasure" already changed the "Move-Stack"
					if (agent.getGraph().closestTreasure(myPosition, agent.getStack(), agent.getMyTreasureType(), agent.getBackPackFreeSpace()).equals("NOT FOUND TREASURE TYPE")) {
						// if your backpack is not empty, you should look for the Tanker
						if (!((mas.agents.CollectorAgent)agent).isBackPackEmpty()) {
							if (agent.getGraph().closestTanker(myPosition, agent.getStack()).equals("TANKER NOT FOUND")) {
								// if no tanker, you should do a bfs to explore the map and go talk with explorers
								agent.getGraph().bfs(myPosition, agent.getHashmap(),agent.getStack());
								System.err.println("Stack post BFS");
								agent.printStack();
							}							
						}
						else {
							agent.getGraph().bfs(myPosition, agent.getHashmap(),agent.getStack());
							System.err.println("Stack post BFS");
							agent.printStack();
						}
							// if your backpack is empty you should still do a bfs to explore the map and go talk with explorers
					}						
				}
				
				System.out.println("My current backpack free space: " + agent.getBackPackFreeSpace() );
				
				/////////////////////////////////
				//// STEP 3) Pick the next Move and do it
				// Pick the next Node to go
				if (!agent.getStack().empty() && !canPick(myPosition, lobs))
					myMove = agent.getStack().pop();
				else if (canPick(myPosition, lobs)) {
					System.err.println("Staying to pick more!");
					myMove = myPosition;
				}
				else
					myMove = myPosition;
				
				/////////////////////////////////
				// STEP 4) Because Agent Collector picked something, he might want to update his TreasureHashmap
				agent.UpdateTreasureHashmap(agent.observe(), myPosition);
				
			}
			
			// Try to empty the backpack if there is a Tanker around (after having updated graph)
			if (agent.getGraph().isThereTankerAround(myPosition, lobs)) {
				System.err.println("TANKER IS AROUND! YAY!");
				if (!((mas.agents.CollectorAgent)agent).isBackPackEmpty())
					giveTreasureToTanker();
				//agent.emptyMyBackPack("Agent6");
			}
			
			// If agent wants to stay at the same spot forever, introduce some random
			if (myMove.equals(myPosition)) {
				agent.emptyStack();
				myMove = this.choseRandomMove(myPosition, lobs);
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
