package mas.behaviours;

import jade.core.AID;

import java.io.IOException;
import java.util.*;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mas.abstractAgent;

public class SayVisited extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622078521998L;

	private boolean finished=false;

	public SayVisited(final Agent myagent) {
		super(myagent);
	}

	public void action() {
		boolean verbose = false;
		
		//Little pause to allow you to follow what is going on
//		try {
//			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move (message)");
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		ACLMessage msg=new ACLMessage(7);
		msg.setSender(this.myAgent.getAID());
		
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("explorer");
		dfd.addServices(sd);
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(((mas.abstractAgent)this.myAgent), dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		if (myPosition!=""){
//			System.out.println("Agent "+this.myAgent.getLocalName()+ " is trying to reach its friends");
			msg.setContent("Hello World, I'm at "+myPosition + ". Can you tell me where I am?");
			
			for (int i = 0; i < result.length; i++) {
				if (!this.myAgent.getLocalName().equals(result[i].getName().getLocalName())) {//do not send message to yourself!
					msg.addReceiver(result[i].getName());
				}
			}
			
//			if (!myAgent.getLocalName().equals("Agent1")){
//				msg.addReceiver(new AID("Agent1",AID.ISLOCALNAME));
//			}else{
//				msg.addReceiver(new AID("Agent2",AID.ISLOCALNAME));
//			}

			((mas.abstractAgent)this.myAgent).sendMessage(msg);

		}
	}

	public boolean done() {
		try {
			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move (send message)");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return finished;
	}

}
