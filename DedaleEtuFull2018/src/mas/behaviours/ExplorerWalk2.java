package mas.behaviours;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import mas.agents.ExplorerAgent2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.lang.Thread;

/**************************************
 * 
 * 
 * 				BEHAVIOUR
 * 
 * 
 **************************************/

// TO DO :

// LONG TERM
// #########

// Behaviour that add observed nodes into graph and mark nodes as "to visit OR visited"

// If a node to be visited is neighbour, go to this node

// Else, compute a Djikstra to know which is the neirest neighbor, and add to stack the path to this node to go there

//SHORT TERM
//##########

// Refactor the code into methods, such as the printCurrentHashmap() one

// For example : one method for the press enter, one method to replace "((mas.agents.ExplorerAgent2)this.myAgent)"

public class ExplorerWalk2 extends SimpleBehaviour{
	/**
	 * When an agent choose to move
	 *  
	 */
	private static final long serialVersionUID = 9088209402507795289L;

	public ExplorerWalk2 (final mas.abstractAgent myagent) {
		super(myagent);
		
		//super(myagent);
	}
	
	public void printCurrentHashmap() {
		//Print the current Hashmap
		HashMap<String,String> myHashMap = ((mas.agents.ExplorerAgent2)this.myAgent).getHashmap();
		for (String name: myHashMap.keySet()){
			System.out.print('{');
            String key =name.toString();
            String value = myHashMap.get(name).toString();  
            System.out.print('['+ key + ": " + value + "], ");  
		}
	}
	
	@Override
	public void action() {
		//Set verbose to true do debug the behaviour
		boolean verbose = false;
		
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
		//Prints the current position
		System.out.println("myPosition: "+myPosition);
		
		//If last move was ineffective (other agent on next node)
		String myLastMove = ((mas.agents.ExplorerAgent2)this.myAgent).getLastMove();
		if (myPosition !="" && myLastMove !="" && !myLastMove.equals(myPosition)) {
			Random r = new Random();
			int result = r.nextInt(2);
			if (result == 0) { //continue, try hard, with the same move
				if (verbose) //prints Stack
					System.out.println("Stack: " + Arrays.toString(((mas.agents.ExplorerAgent2)this.myAgent).getStack().toArray()));
				
				//1) Set the last move to the move you will make
				((mas.agents.ExplorerAgent2)this.myAgent).setLastMove(((mas.agents.ExplorerAgent2)this.myAgent).getLastMove());
				
				//2) Do the move
				((mas.abstractAgent)this.myAgent).moveTo(((mas.agents.ExplorerAgent2)this.myAgent).getLastMove());
				
			} else { //pick a random move
				List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();
				Random r2 = new Random();
				//1) get a couple <Node ID,list of percepts> from the list of observables
				int moveId=r2.nextInt(lobs.size());
				
				int counter = 0;
				String nextMove = lobs.get(moveId).getLeft();
				while (counter < 100 || nextMove.equals(myPosition) || nextMove.equals(myLastMove)) {
					moveId=r2.nextInt(lobs.size());
					nextMove = lobs.get(moveId).getLeft();
					counter += 1;
				}
				
				//3) push your position so you can come back safe and sound later
				((mas.agents.ExplorerAgent2)this.myAgent).getStack().push(myPosition);
				
				//2) push next position to stack to stay there for a while
				
				if (!nextMove.equals(myPosition)) {
					for (int i=0; i<5; i++)
						((mas.agents.ExplorerAgent2)this.myAgent).getStack().push(nextMove);
				}
				else
					((mas.agents.ExplorerAgent2)this.myAgent).getStack().push(nextMove);
				
				
				if (verbose) //prints Stack
					System.out.println("Stack: " + Arrays.toString(((mas.agents.ExplorerAgent2)this.myAgent).getStack().toArray()));
				
				//3)Set the last move to the move you will make
				((mas.agents.ExplorerAgent2)this.myAgent).setLastMove(lobs.get(moveId).getLeft());
				
				//4) Move to the picked location. The move action (if any) MUST be the last action of your behaviour
				((mas.abstractAgent)this.myAgent).moveTo(lobs.get(moveId).getLeft());
			}
		} else if (myPosition!="" && !((mas.agents.ExplorerAgent2)this.myAgent).getStack().empty() &&
				((mas.agents.ExplorerAgent2)this.myAgent).getStack().peek().equals(myPosition)) {
			((mas.agents.ExplorerAgent2)this.myAgent).getStack().pop();
			((mas.abstractAgent)this.myAgent).moveTo(myPosition);
		} else if (myPosition!=""){
			
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();
			System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);
			
			//Add myPosition to Graph
			((mas.agents.ExplorerAgent2)this.myAgent).getGraph().addVertex(myPosition);
			
			//Set Node as discovered
			((mas.agents.ExplorerAgent2)this.myAgent).getHashmap().put(myPosition,"discovered");

			//list of attribute associated to the currentPosition
			List<Attribute> lattribute= lobs.get(0).getRight();

			//Iterate over all adjacent node
			for (int i = 0; i < lobs.size();i++) {
				//Add Edge
				String adjacentNode = lobs.get(i).getLeft();
				((mas.agents.ExplorerAgent2)this.myAgent).getGraph().addEdge(myPosition, adjacentNode);
				//Add adjacent node to stack if not in stack and not already visited
				if (!((mas.agents.ExplorerAgent2)this.myAgent).getHashmap().containsKey(adjacentNode)
					&& !( ( (mas.agents.ExplorerAgent2)this.myAgent).containsStack(adjacentNode) )	) {
					((mas.agents.ExplorerAgent2)this.myAgent).getStack().push(adjacentNode);
					if (verbose)
						System.out.println("Node: " + adjacentNode+ " added to Stack");
				}				
			}
			
			//Pick the next Node to go
			String myMove = ((mas.agents.ExplorerAgent2)this.myAgent).getStack().pop();
			
			//Push current position to be sure to come back safe, if you need to explore more
			if (!((mas.agents.ExplorerAgent2)this.myAgent).getHashmap().containsKey(myMove)) {
				((mas.agents.ExplorerAgent2)this.myAgent).getStack().push(myPosition);
				if (verbose)
					System.out.println("Node: " + myPosition+ " added to Stack to come back safe");
			}
			
			//System.out.println("visited:"+((mas.agents.ExplorerAgent2)this.myAgent).getNodes());
			if (verbose)
				System.out.println("my move:"+myMove);
			//Set the last move to the move you will make
			((mas.agents.ExplorerAgent2)this.myAgent).setLastMove(myMove);
			
			//Prints the stack
			if (verbose)
				System.out.println("Stack: " + Arrays.toString(((mas.agents.ExplorerAgent2)this.myAgent).getStack().toArray()));
			
			//2) Move to the picked location. The move action (if any) MUST be the last action of your behaviour
			((mas.abstractAgent)this.myAgent).moveTo(myMove);
		}
		
	}
	public boolean	done() {
		//Little pause to allow you to follow what is going on
		try {
			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//if done, do this
		if (((mas.agents.ExplorerAgent2)this.myAgent).getStack().empty()) {
			System.out.println(this.myAgent.getLocalName() + " is Done!");
			
			this.printCurrentHashmap();
		}
		
		//Explorer Agent is done if and only his stack is empty
		return (((mas.agents.ExplorerAgent2)this.myAgent).getStack().empty());
	}

}