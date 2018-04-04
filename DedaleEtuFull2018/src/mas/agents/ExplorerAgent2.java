package mas.agents;


import env.Environment;
import mas.behaviours.*;
import mas.agents.GeneralAgent;

import java.util.*;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

import mas.graph.Graph;
import java.util.HashMap;
import env.EntityType;

public class ExplorerAgent2 extends GeneralAgent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

			deployAgent((Environment) args[0],(EntityType)args[1]);

		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		
		//Add the behaviours
		addBehaviour(new ExplorerWalk2(this)); //To explore the map
		addBehaviour(new SayVisited(this)); //To communicate the map
		addBehaviour(new ReceiveVisited(this));//To receive a map
		
		//Initialize attributes
		this.setGraph(new Graph<String>());
		this.setHashmap(new HashMap<String, String>());
		this.setStack(new Stack<String>());
		this.setLastMove("");

		System.out.println("the agent "+this.getLocalName()+ " is started");

	}

}
