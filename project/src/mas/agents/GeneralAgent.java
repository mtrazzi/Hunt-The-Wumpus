package mas.agents;

import mas.abstractAgent;
import mas.utils.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Stack;

import env.Attribute;
import env.Couple;
import env.EntityType;
import env.Environment;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas.utils.MyCouple;

public class GeneralAgent extends abstractAgent{
	private static final long serialVersionUID = -1784844593772918359L;

	private Graph<String> graph;
	
	private HashMap<String,String> hashmap;
	
	private Stack<String> stack;
	
	private String lastMove;
		
	protected void takeDown(){

	}

	public Graph<String> getGraph() {
		return graph;
	}

	public void setGraph(Graph<String> graph) {
		this.graph = graph;
	}

	public HashMap<String,String> getHashmap() {
		return hashmap;
	}

	public void setHashmap(HashMap<String,String> hashmap) {
		this.hashmap = hashmap;
	}

	public Stack<String> getStack() {
		return stack;
	}

	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}

	public boolean containsStack(String element) {
		return Arrays.asList(this.stack.toArray()).contains(element);
	}

	public String getLastMove() {
		return lastMove;
	}

	public void setLastMove(String lastMove) {
		this.lastMove = lastMove;
	}
	
	public abstractAgent getAbstractAgent() {
		return (mas.abstractAgent)this;
	}
	
	public void printCurrentHashmap() {
		// Print the current Hashmap
		System.err.println(this.getLocalName() + " #######");
		HashMap<String, String> myHashMap = this.getHashmap();
		for (String name : myHashMap.keySet()) {
			System.out.print('{');
			String key = name.toString();
			String value = myHashMap.get(name).toString();
			System.out.println('[' + key + ": " + value + "], ");
		}
	}
	
	public void UpdateEdges(String myPosition, List<Couple<String, List<Attribute>>> lobs) {
		for (int i = 0; i < lobs.size(); i++) {
			// Add Edge
			String adjacentNode = lobs.get(i).getLeft();
			this.getGraph().addVertex(adjacentNode);
			this.getGraph().addEdge(myPosition, adjacentNode);
		}
	}
	
	public void UpdateTreasureHashmap(List<Couple<String, List<Attribute>>> lobs, String myPosition) {
		//For every adjacent node
		for (int i = 0; i < lobs.size(); i++) {
			String adjacentNode = lobs.get(i).getLeft();
			
			//Only add the treasure value of the node you're visiting
			if (adjacentNode.equals(myPosition)) {
				List<Attribute> lattribute = lobs.get(i).getRight();
				MyCouple couple = new MyCouple(0,0);
				
				//Creating a couple with both the treasure and diamond value
				for (Attribute a:lattribute) {
					Integer val = Integer.valueOf(a.getValue().toString());
					System.out.println("val is " + Integer.toString(val));
					switch (a) {
						case DIAMONDS:
							couple.setDiamonds(val);
						break;
						case TREASURE:
							couple.setTreasure(val);
						break;
					default:
						break;
					}
				}

				//Adding the couple
				//System.out.println("adding to the key " + myPosition + " the couple: (" + + couple.getTreasure() + ", " + couple.getDiamonds() + ")");
				getGraph().getTreasureHashmap().put(myPosition, couple);
				
				//Updating the timestamp of current node
				getGraph().getTimeStampHashmap().put(myPosition, System.currentTimeMillis());
			}
			
		}
	}
	
	public void printTreasureHashmap() {
		// Print the current Treasure Hashmap
		System.err.println(this.getLocalName() + " #######");
		HashMap<String, MyCouple> myHashMap = this.getGraph().getTreasureHashmap();
		for (String name : myHashMap.keySet()) {
			System.out.print('{');
			String key = name.toString();
			String treasureValue = myHashMap.get(name).getTreasure().toString();
			String diamondValue = myHashMap.get(name).getDiamonds().toString();
			System.out.println('[' + key + ": (" + treasureValue + ", " + diamondValue + ")], ");
		}
	}
	
	public void printStack() {
		System.out.println("Stack: " + Arrays.toString(this.getStack().toArray()));
	}
	
	public boolean areAllNodesVisited() {
		if (this.getGraph().isVisited(this.getHashmap())) {
			this.printCurrentHashmap();
			System.err.println(this.getLocalName() + " -->EVERYTHING IS VISITED, HOURRA!!!");
		}
		return this.getGraph().isVisited(this.getHashmap());
	}
	
	public void generalSetup(String service) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType( service);
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
		
		//Initialize attributes
		this.setGraph(new Graph<String>());
		this.setHashmap(new HashMap<String, String>());
		this.setStack(new Stack<String>());
		this.setLastMove("");
	}
}
