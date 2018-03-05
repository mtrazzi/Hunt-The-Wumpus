package behaviours.example5;

import java.util.ArrayList;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;


/***
 * This behaviour allows the agent who possess it to send nb random int within [0-100[ to another agent whose local name is given in parameters
 * 
 * There is not loop here in order to reduce the duration of the behaviour (an action() method is not preemptive)
 * The loop is made by the behaviour itslef
 * 
 * @author Cédric Herpson
 *
 */

public class RequestValues extends SimpleBehaviour{
	
	private static final long serialVersionUID = 9088209402507795289L;

	private boolean finished=false;
	/**
	 * number of values to send
	 */
	private int nbValues;
	
	/**
	 * number of values already sent
	 */
	private int nbMessagesSent=0;
	
	/**
	 * Name of the agent that should receive the values
	 */
	private String receiverName;
	
	Random r;

	private ArrayList rec;

	/**
	 * 
	 * @param myagent the Agent this behaviour is linked to
	 * @param nbValues the number of messages that should be sent to the receiver
	 * @param receiverName The local name of the receiver agent
	 */
	public RequestValues(final Agent myagent) {
		super(myagent);
		this.nbValues=nbValues;
		this.receiverName=receiverName;
		this.r= new Random();
		this.rec = rec;
	}


	public void action() {
		final ACLMessage msgResult = new ACLMessage(ACLMessage.INFORM);
		msgResult.setSender(this.myAgent.getAID());  	
		msgResult.setContent("Please send me numbers");

		for (int i = 0; i < 4; i++) {
			msgResult.addReceiver(new AID("AgentA"+i, AID.ISLOCALNAME));
	    }
		this.myAgent.send(msgResult);
		this.finished = true;
		
		System.out.println(this.myAgent.getLocalName()+" ----> Message number "+this.nbMessagesSent+" sent to "+this.receiverName+" ,content= "+msgResult.getContent());

	}

	public boolean done() {
		return finished;
	}

}
