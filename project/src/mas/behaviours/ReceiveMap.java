package mas.behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.utils.Graph;


public class ReceiveMap extends GeneralSimpleBehaviour{

	private static final long serialVersionUID = 9088209402507795289L;

	private boolean verbose=false;
	
	public ReceiveMap(final mas.abstractAgent myagent) {
		super(myagent);
	}


	@SuppressWarnings("unchecked")
	public void action() {
		

		
		// Initialization
		mas.agents.GeneralAgent agent = getGeneralAgent();
		
		if (verbose) {
			System.out.println(this.myAgent.getLocalName() +  " --> Entering behavior |ReceiveVisited|");
		}
		
		
		//1) receive the message
		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);			

		final ACLMessage msg = agent.receive(msgTemplate);
		if (msg != null) {
			
			if (this.myAgent.getLocalName().equals("Agent1")
				&& msg.getSender().getLocalName().equals("Agent6"))	{ //TODO : delete this debug
				verbose = false;
			}
			
			if (verbose)
				System.out.println(agent.getLocalName()+"<----Result received from "+msg.getSender().getLocalName());
			
			
			try {
				Graph<String> ReceivedGraph = new Graph<String>();
				ReceivedGraph = (Graph<String>) msg.getContentObject();
				if (verbose) {
					System.out.println("My treasureHashmap");
					agent.getGraph().printTreasureHashmap();
				}
				agent.getGraph().mergeGraph(ReceivedGraph);
				//Print received graph
				if (verbose) {
					System.out.println("Received treasureHashmap");
					ReceivedGraph.printTreasureHashmap();
					System.out.println("Merged graph");
					agent.getGraph().printTreasureHashmap();
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
		this.defaultsleep();

		return false;
	}

}

