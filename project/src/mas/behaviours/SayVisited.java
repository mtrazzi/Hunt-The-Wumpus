package mas.behaviours;

import java.io.IOException;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class SayVisited extends GeneralSimpleBehaviour {

	private static final long serialVersionUID = -2058134622078521998L;

	public SayVisited(final mas.abstractAgent myagent) {
		super(myagent);
	}

	private boolean verbose = true;

	public void action() {

		String myPosition = ((mas.abstractAgent) this.myAgent).getCurrentPosition();

		ACLMessage msg = new ACLMessage(7);
		msg.setSender(this.myAgent.getAID());

		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("explorer");
		dfd.addServices(sd);
		DFAgentDescription[] result = null;
		try {
			result = DFService.search(((mas.abstractAgent) this.myAgent), dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (myPosition != "") {
			// Sends the Graph

			try {
				msg.setContentObject(((mas.agents.ExplorerAgent2) this.myAgent).getGraph());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (verbose)
				System.out.println(this.myAgent.getLocalName() + " --> Sending message to");
			for (int i = 0; i < result.length; i++) {
				if (!this.myAgent.getLocalName().equals(result[i].getName().getLocalName())) {// do not send message to
																								// yourself!
					msg.addReceiver(result[i].getName());
					if (verbose)
						System.out.println("\t" + result[i].getName().getLocalName());
				}
			}
			((mas.abstractAgent) this.myAgent).sendMessage(msg);

		}
	}

	public boolean done() {
		this.defaultsleep();

		return false;
	}

}
