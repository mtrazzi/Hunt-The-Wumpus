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
import mas.behaviours.ReceiveMap;
import mas.behaviours.SendMap;
import mas.behaviours.TankerBehaviour;
import mas.utils.Graph;
import mas.utils.MyCouple;

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

		//general setup for every agent (from GeneralAgent class)
		generalSetup("tanker");
		
		//Add the behaviours
		addBehaviour(new TankerBehaviour(this)); //To explore the map
		addBehaviour(new SendMap(this)); //To communicate the map
		addBehaviour(new ReceiveMap(this));//To receive a map, ,

		System.out.println("the agent "+this.getLocalName()+ " is started");


	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}