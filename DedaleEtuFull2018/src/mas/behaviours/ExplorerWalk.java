package mas.behaviours;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import env.Attribute;

import env.Couple;

import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.SimpleBehaviour;

import mas.agents.ExplorerAgent;

import mas.graph.Pair;
import java.util.Arrays;
import java.util.HashMap;

/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


public class ExplorerWalk extends SimpleBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;

	public ExplorerWalk (final mas.abstractAgent myagent) {
		super(myagent);
		
		//super(myagent);
	}
	
	
	@Override
	public void action() {
		System.out.println("YES, I AM EXECUTING THIS PART AT THE BEGINNING OF ACTION");
		
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
		//Prints the current position
		System.out.println("myPosition: "+myPosition);
		
		//Print the current Hashmap
		HashMap<String,String> myHashMap = ((mas.agents.ExplorerAgent)this.myAgent).getHashmap();
		for (String name: myHashMap.keySet()){

            String key =name.toString();
            String value = myHashMap.get(name).toString();  
            System.out.println(key + " " + value);  
		}
		System.out.println("YES, I AM EXECUTING THIS PART JUST BEFORE THE IF");
		if (myPosition!=""){
			//Initialize the stack with current position if Hashmap is empty (first step)
			if (((mas.agents.ExplorerAgent)this.myAgent).getHashmap().isEmpty()) {
				((mas.agents.ExplorerAgent)this.myAgent).getStack().push(myPosition);
			}
			
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);
			
			//Add myPosition to Graph
			((mas.agents.ExplorerAgent)this.myAgent).getGraph().addVertex(myPosition);
			
			//Set Node as discovered
			((mas.agents.ExplorerAgent)this.myAgent).getHashmap().put(myPosition,"discovered");

			//while (!((mas.agents.ExplorerAgent)this.myAgent).getHashmap().containsKey(adjacentNode))

			//list of attribute associated to the currentPosition
			List<Attribute> lattribute= lobs.get(0).getRight();
			
			//example related to the use of the backpack for the treasure hunt
			Boolean b=false;
			
			//TO DO: TO DEAL WITH ATTRIBUTES
			
//			for(Attribute a:lattribute){
//				switch (a) {
//				case TREASURE:
////					System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
////					System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
////					System.out.println("Value of the treasure on the current position: "+a.getValue());
////					System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
////					System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
////					System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
//					b=true;
//					//Little pause to allow you to follow what is going on
//					try {
//						System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
//						System.in.read();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					break;
//			case DIAMONDS:
////				System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
////				System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
////				System.out.println("Value of the diamonds on the current position: "+a.getValue());
////				System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
////				System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
////				System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
//				b=true;
//				//Little pause to allow you to follow what is going on
//				try {
//					System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
//					System.in.read();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				default:
//					break;
//				}
//			}

			//If the agent picked (part of) the treasure
			if (b){
				List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
				System.out.println("list of observables after picking "+lobs2);
			}
			System.out.println("YES, I AM EXECUTING THIS PART");
			//Iterate over all adjacent nodes
			for (int i = 0; i < lobs.size();i++) {
				//Add Edge
				String adjacentNode = lobs.get(i).getLeft();
				((mas.agents.ExplorerAgent)this.myAgent).getGraph().addEdge(myPosition, adjacentNode);
				//Add adjacent node to stack if has never been discovered
				if (!((mas.agents.ExplorerAgent)this.myAgent).getHashmap().containsKey(adjacentNode)) {
					System.out.println("Node: " + adjacentNode+ " added to Stack");
					((mas.agents.ExplorerAgent)this.myAgent).getStack().push(adjacentNode);
				}				
			}
			
			//Prints the stack
			System.out.println("Stack: " + Arrays.toString(((mas.agents.ExplorerAgent)this.myAgent).getStack().toArray()));
			
			//Pick the next Node to go
			String myMove = ((mas.agents.ExplorerAgent)this.myAgent).getStack().pop();
			
			//Push current position to be sure to come back safe
			((mas.agents.ExplorerAgent)this.myAgent).getStack().push(myPosition);
			
			//System.out.println("visited:"+((mas.agents.ExplorerAgent)this.myAgent).getNodes());
			System.out.println("my move:"+myMove);
			//2) Move to the picked location. The move action (if any) MUST be the last action of your behaviour
			((mas.abstractAgent)this.myAgent).moveTo(myMove);
			
		}
		
	}
	public boolean	done() {
		//Little pause to allow you to follow what is going on
//		try {
//			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//Explorer Agent is done if and only his stack is empty
		return (((mas.agents.ExplorerAgent)this.myAgent).getStack().empty());
	}

}