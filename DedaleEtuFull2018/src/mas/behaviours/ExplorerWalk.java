package mas.behaviours;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import env.Attribute;

import env.Couple;

import jade.core.behaviours.TickerBehaviour;

import mas.agents.ExplorerAgent;

import mas.graph.Pair;


/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/


public class ExplorerWalk extends TickerBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;

	public ExplorerWalk (final mas.abstractAgent myagent) {
		super(myagent, 1000);
		
		//super(myagent);
	}
	

	@Override
	public void onTick() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		//List<String> visited=((mas.agents.ExplorerAgent)this.myAgent).getNodes();
		//if (!visited.contains(myPosition)) {
		//	((mas.agents.ExplorerAgent)this.myAgent).addVisited(myPosition);
		//}
		
		//((mas.agents.ExplorerAgent)this.myAgent).setVisited(myPosition, true); **
		
		
		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);
			
			//for (int i=0; i<lobs.size();i++) {
			//	if (!visited.contains(lobs.get(i).getLeft())) {
			//		moveId = i;
			//	}
			//}
<<<<<<< HEAD
			
			//((mas.agents.ExplorerAgent)this.myAgent).addEdges(myPosition, true); **
			Pair<String,Integer> myPair = new Pair(myPosition, 2);
			((mas.agents.ExplorerAgent)this.myAgent).getGraph().addVertex(myPair);
			
=======
			//((mas.agents.ExplorerAgent)this.myAgent).addEdges(myPosition, true);
>>>>>>> 228da0b7ae4e42ab6c8bfd98c08aaa5c64d219ac

//			//Little pause to allow you to follow what is going on
			try {
				System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}


			//list of attribute associated to the currentPosition
			List<Attribute> lattribute= lobs.get(0).getRight();

			
			//example related to the use of the backpack for the treasure hunt
			Boolean b=false;
			
			for(Attribute a:lattribute){
				switch (a) {
				case TREASURE:
					System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
					System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
					System.out.println("Value of the treasure on the current position: "+a.getValue());
					System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
					System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
					System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
					b=true;
					//Little pause to allow you to follow what is going on
					try {
						System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
						System.in.read();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
			case DIAMONDS:
				System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
				System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println("Value of the diamonds on the current position: "+a.getValue());
				System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
				System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
				b=true;
				//Little pause to allow you to follow what is going on
				try {
					System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
					System.in.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				default:
					break;
				}
			}

			//If the agent picked (part of) the treasure
			if (b){
				List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
				System.out.println("list of observables after picking "+lobs2);
			}
			
			for (int i = 0; i < lobs.size();i++) {
				String nextPosition = lobs.get(i).getLeft();
				System.out.println("nextPosition: " + nextPosition);
				Pair<String,Integer> nextPair = new Pair(myPosition, 1);
				((mas.agents.ExplorerAgent)this.myAgent).getGraph().addVertex(nextPair);
				((mas.agents.ExplorerAgent)this.myAgent).getGraph().addEdge(myPair, nextPair);
			}
			
			Iterable<Pair<String,Integer>> Vertices = ((mas.agents.ExplorerAgent)this.myAgent).getGraph().getAllVertices();
			
			
//			//Random move from the current position
			Random r= new Random();
//			//1) get a couple <Node ID,list of percepts> from the list of observables
//			
//			int moveId=r.nextInt(lobs.size());
////			System.out.println("My position is" + myPosition);
////			for (int i = 0; i < lobs.size();i++) {
////				System.out.println("I can go to " + lobs.get(i).getLeft());
////			}
			int moveId = r.nextInt(lobs.size()); //default value
			
			//for (int i=0; i<lobs.size();i++) {
			//	if (!visited.contains(lobs.get(i).getLeft())) {
			//		moveId = i;
			//	}
			//}
			
			
			
			//System.out.println("visited:"+((mas.agents.ExplorerAgent)this.myAgent).getNodes());
			//System.out.println("my move:"+lobs.get(moveId).getLeft());
			//2) Move to the picked location. The move action (if any) MUST be the last action of your behaviour
			((mas.abstractAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
		}

	}

}