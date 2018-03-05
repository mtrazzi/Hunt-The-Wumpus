package behaviours.example5;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * This behaviour receives nb integer values, sum them and then send the result back to the agent whose name is given in parameters
 * 
 * In order to stay relatively simple for now, this behaviour is not fully generic (it is not declared as being part of a protocol) 
 * and do both the receiving and the sending processes.
 * 
 * @author hc
 *
 */
public class SumNbReceivedValuesBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 9088209402507795289L;
	private boolean finished=false;
	private boolean is_done=false;
	
	/**
	 * Number of int to receive
	 */
	private int nbValues;
	
	/**
	 * Number of int received until now
	 */
	private int nbMessagesReceived;
	
	/**
	 * Name of the agent to send the result back
	 */
	private String senderName="";
	
	/**
	 * sum
	 */
	private int sum;
	
	Random r;

	/**
	 * 
	 * This behaviour receives nb integer values, sum them and then send the result back to the agent whose name is given in parameters.
	 * 
	 * In order to stay relatively simple for now, this behaviour is not fully generic (it is not declared as being part of a protocol) 
	 * and do both the receiving and the sending processes
	 *	 
	 * @param myagent
	 * @param nbValues
	 * @param resultReceiver
	 */
	public SumNbReceivedValuesBehaviour(final Agent myagent,String resultReceiver) {
		super(myagent);
		this.nbValues=nbValues;
		this.nbMessagesReceived=0;
		this.senderName=resultReceiver;
		this.sum=0;
		this.r = new Random();
	}


	public void action() {
		
		//1) create the reception template (inform + name of the sender)
		final MessageTemplate msgTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
																MessageTemplate.MatchSender(new AID(this.senderName, AID.ISLOCALNAME)));

		//2) get the message
		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		
		if (msg != null) {		
			
			System.out.println(this.myAgent.getLocalName()+ "<----Message received from "+msg.getSender().getLocalName()+" ,content= "+msg.getContent());
			
			final ACLMessage msgResult = new ACLMessage(ACLMessage.INFORM);
			msgResult.setSender(this.myAgent.getAID());
			msgResult.addReceiver(new AID(this.senderName, AID.ISLOCALNAME));  	
			msg.setContent(((Integer)r.nextInt(100)).toString());
			this.myAgent.send(msgResult);
			System.out.println("SUM computed");
			
		}else{
			//block the behaviour until the next message
			System.out.println("No message received, the behaviour "+this.getBehaviourName()+ "goes to sleep");
			block();
		}
	}
	
	public boolean is_stop() {
		return is_stop();
	}

	public boolean done() {
		return finished;
	}

}


