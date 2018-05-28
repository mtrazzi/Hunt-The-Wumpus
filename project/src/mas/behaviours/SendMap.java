package mas.behaviours;

import java.io.IOException;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.Arrays;
import java.util.List;

public class SendMap extends GeneralSimpleBehaviour {

	private static final long serialVersionUID = -2058134622078521998L;

	public SendMap(final mas.abstractAgent myagent) {
		super(myagent);
	}

	private boolean verbose = false;

	public void action() {
		// Initialization
		mas.agents.GeneralAgent agent = getGeneralAgent();
		String myPosition = agent.getCurrentPosition();
		List<String> agentTypeList = Arrays.asList("collector", "explorer", "tanker");
		
		for (String agentType : agentTypeList) {
			// Message specification
			ACLMessage msg = new ACLMessage(7);
			msg.setSender(this.myAgent.getAID());
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType(agentType);
			dfd.addServices(sd);

			// Searching for agents with desired qualities
			DFAgentDescription[] result = null;
			try {
				result = DFService.search(agent, dfd);
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Sending the message
			if (myPosition != "") {
				// Sends the Graph

				try {
					msg.setContentObject(agent.getGraph());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (verbose && result.length > 0)
					System.out.println(agent.getLocalName() + " --> Sending message to");
				for (int i = 0; i < result.length; i++) {
					if (!agent.getLocalName().equals(result[i].getName().getLocalName())) {// do not send message to
																									// yourself!
						msg.addReceiver(result[i].getName());
						if (verbose)
							System.out.println("\t" + result[i].getName().getLocalName());
					}
				}
				agent.sendMessage(msg);	
			}
		}
	}

	public boolean done() {
		this.defaultsleep();

		return false;
	}

}
