package mas.behaviours;

import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;

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
				//System.out.println("Value of the treasure on the current position: "+a.getName() +": "+ a.getValue());
				//System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
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

	@Override
	public void action() {
		
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		mas.agents.GeneralAgent agent = getGeneralAgent();
		
		boolean verbose = false;
		
		//Start by Picking Treasure!
		pickMyType(agent);

		if (myPosition != "") {
			String myMove;

			// List of observable from the agent's current position
			List<Couple<String, List<Attribute>>> lobs = agent.observe();
			if (verbose)
				System.out.println(agent.getLocalName() + " -- list of observables: " + lobs);

			/////////////////////////////////
			//// INTERBLOCKING
			if (agent.getLastMove() != "" && !myPosition.equals(agent.getLastMove())) {
				myMove = choseMoveInterblocking(myPosition);
			}

			/////////////////////////////////
			//// NO INTERBLOCKING
			else {
				/////////////////////////////////
				//// STEP 1) Updating Graph and Hashmaps
				updatingGraph(myPosition,lobs);

				/////////////////////////////////
				//// STEP 2) Update the Stack if empty
				if (agent.getStack().empty())
					agent.getGraph().bfs(myPosition, agent.getHashmap(),agent.getStack());
				
				//TODO: For Tanker
				//TODO: agent.emptyMyBackPack("Agent5");
				
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
				agent.UpdateTreasureHashmap(lobs, myPosition);
			}

			// Set last move to the next move, for next iteration
			agent.setLastMove(myMove);

			// Move to the picked location (must be last action)
			agent.moveTo(myMove);
		}

	}
	
	public boolean done() {
		this.defaultsleep();

		return (this.getGeneralAgent().areAllNodesVisited());
	}
}
