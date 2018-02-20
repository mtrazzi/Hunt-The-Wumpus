package mas.behaviours;

import jade.core.AID;

import java.util.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import mas.abstractAgent;

public class SayVisited extends TickerBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2058134622078521998L;

	/**
	 * An agent tries to contact its friend and to give him its current position
	 * @param myagent the agent who posses the behaviour
	 *  
	 */
	public SayVisited (final Agent myagent) {
		super(myagent, 3000);
		//super(myagent);
	}

	@Override
	public void onTick() {
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
		System.out.println(result.length + " results");
		if (result.length > 0) {
			for (int i = 0; i < result.length; i++) {
				System.out.println(" " + result[i].getName());

			}
		}
			
		if (myPosition!=""){
			System.out.println("Agent "+this.myAgent.getLocalName()+ " is trying to reach its friends");
			msg.setContent("Hello World, I'm at "+myPosition);
			
			for (int i = 0; i < result.length; i++) {
				msg.addReceiver(result[i].getName());
			}
			
//			if (!myAgent.getLocalName().equals("Agent1")){
//				msg.addReceiver(new AID("Agent1",AID.ISLOCALNAME));
//			}else{
//				msg.addReceiver(new AID("Agent2",AID.ISLOCALNAME));
//			}

			((mas.abstractAgent)this.myAgent).sendMessage(msg);

		}

	}

}