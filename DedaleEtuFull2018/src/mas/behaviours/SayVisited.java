package mas.behaviours;

import java.io.IOException;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class SayVisited extends SimpleBehaviour{

	private static final long serialVersionUID = -2058134622078521998L;

	public SayVisited(final Agent myagent) {
		super(myagent);
	}
	
	public void action() {
		//Little pause to allow you to follow what is going on
		try {
			System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move (message)");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
//			Sends the Graph
			
			
			try {
				msg.setContentObject(((mas.agents.ExplorerAgent2)this.myAgent).getGraph());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(this.myAgent.getLocalName()+" --> Sending message to");
			for (int i = 0; i < result.length; i++) {
				if (!this.myAgent.getLocalName().equals(result[i].getName().getLocalName())) {//do not send message to yourself!
					msg.addReceiver(result[i].getName());
					System.out.println("\t" + result[i].getName().getLocalName());
				}
			}
			((mas.abstractAgent)this.myAgent).sendMessage(msg);

		}
	}

	public boolean done() {
		
		return false;
	}

}
