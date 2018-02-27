package mas.agents;


import env.Attribute;
import env.Couple;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.*;

import java.util.*;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

public class ExplorerAgent extends abstractAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1784844593772918359L;

	//private List<Couple<String,Boolean>> nodes= new ArrayList<Couple<String,Boolean>>();
	private List<Couple<String,String>> edges = new ArrayList<Couple<String,String>>();
	
	Map<String, Boolean> nodes;
	
	//private graph my_graph;
	
	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	
	public Map<String, Boolean> getNodes() {
		return nodes;
	}
	
	public void addVisited(String name, Boolean bool) {
		this.nodes.put(name, bool);
	}
	
	public List<Couple<String, String>> getEdges() {
		return edges;
	}

	public void addEdges(Couple<String, String> edge) {
		this.edges.add(edge);
	}
	
	public void setVisited(String name, Boolean bool) {
		this.nodes.put(name, bool);
	}
	
	protected void setup(){

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType( "explorer"); /* donner un nom aux services qu'on propose */
		sd.setName(getLocalName());
		dfd.addServices(sd);
		
		try {
			DFService.register(this, dfd);
		}	catch (FIPAException fe) { fe.printStackTrace();}
		
		super.setup();

		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args[0]!=null){

			deployAgent((Environment) args[0]);

		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		
		//Add the behaviours
		addBehaviour(new ExplorerWalk(this));
		addBehaviour(new SayVisited(this));

		System.out.println("the agent "+this.getLocalName()+ " is started");

	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}


}
