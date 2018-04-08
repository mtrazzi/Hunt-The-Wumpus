package mas.behaviours;

import java.io.IOException;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.graph.*;


public class ReceiveVisited extends GeneralSimpleBehaviour{

	private static final long serialVersionUID = 9088209402507795289L;

	private boolean verbose=false;
	/**
	 * 
	 * This behaviour is a one Shot.
	 * It receives a message tagged with an inform performative, print the content in the console and destroy itlself
	 * @param myagent
	 */
	public ReceiveVisited(final mas.abstractAgent myagent) {
		super(myagent);
	}


	public void action() {

		
		//1) receive the message
		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);			

		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		if (msg != null) {
			if (verbose)
				System.out.println(this.myAgent.getLocalName()+"<----Result received from "+msg.getSender().getLocalName());
			try {
				Graph<String> ReceivedGraph = new Graph<String>();
				ReceivedGraph = (Graph<String>) msg.getContentObject();
				((mas.agents.ExplorerAgent2)this.myAgent).getGraph().mergeGraph(ReceivedGraph);
				//Print received graph
				if (verbose) {
					System.out.println("Received graph");
					System.out.println("Printing nodes");
					ReceivedGraph.printNodes();
					System.out.println("Printing edges");
					ReceivedGraph.printEdges();
					//Print merged graph
					System.out.println("Merged graph");
					System.out.println("Printing nodes");
					((mas.agents.ExplorerAgent2)this.myAgent).getGraph().printNodes();
					System.out.println("Printing edges");
					((mas.agents.ExplorerAgent2)this.myAgent).getGraph().printEdges();
				}
					
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			block();// the behaviour goes to sleep until the arrival of a new message in the agent's Inbox.
		}
	}

	public boolean done() {
		this.sleep(200);
		
		return false;
	}

}

