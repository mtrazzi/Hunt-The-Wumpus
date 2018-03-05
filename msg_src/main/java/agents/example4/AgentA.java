package agents.example4;

import behaviours.example4.ReceiveMessageBehaviour;
import behaviours.example4.SendNbValuesBehaviour;
import jade.core.Agent;


/**
 *  AgentA sends 10 values to agent B (whose name on the platform is AgentSUM)
 *  
 * 
 * @author hc
 *
 */
public class AgentA extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1410401284578853709L;

	protected void setup(){

		super.setup();

		//get the parameters given into the object[]
		final Object[] args = getArguments();
		if(args.length!=0){
			System.err.println("Malfunction - no parameter expected");
			System.exit(-1);
		}else{
			
		//Add the behaviours
			
		addBehaviour(new SendNbValuesBehaviour(this,10,"AgentSUM"));// Both the name of the agent to contact and the number of values to send are hardcoded, its bad.
		addBehaviour(new ReceiveMessageBehaviour(this));

		System.out.println("the sender agent "+this.getLocalName()+ " is started");
		
		}
	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}

}
