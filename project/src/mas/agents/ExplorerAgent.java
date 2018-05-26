package mas.agents;


import env.Environment;
import mas.behaviours.*;
import mas.utils.Graph;
import mas.agents.GeneralAgent;

import java.util.*;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

import java.util.HashMap;
import env.EntityType;
import mas.utils.MyCouple;

public class ExplorerAgent extends GeneralAgent{
	
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

		//general setup for every agent (from GeneralAgent class)
		generalSetup("explorer");
		
		//Add the behaviours
		addBehaviour(new ExplorerWalk(this)); //To explore the map
		addBehaviour(new SendMap(this)); //To communicate the map
		addBehaviour(new ReceiveMap(this));//To receive a map

		System.out.println("the agent "+this.getLocalName()+ " is started");

	}

}
