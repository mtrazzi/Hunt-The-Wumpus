package mas.agents;


import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import env.Attribute;
import env.Couple;
import env.EntityType;
import env.Environment;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mas.behaviours.GeneralSimpleBehaviour;
import mas.graph.Graph;
import mas.behaviours.TankerBehaviour;


public class TankerAgent extends GeneralAgent{

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
		sd.setType( "Tanker"); /* donner un nom aux services qu'on propose */
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
		addBehaviour(new TankerBehaviour(this)); //To explore the map
		
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
}