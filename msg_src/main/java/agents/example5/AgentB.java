package agents.example5;

import behaviours.example5.RequestValues;
import behaviours.example5.ReceiveMessageBehaviour;
import behaviours.example5.Stop;
import jade.core.Agent;
import princ.Principal;


/**
 * 
 * This agent is expects to receive K values from agentA, sum them and reply with the result
 * @author hc
 *
 */
public class AgentB extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3499482209671348272L;
	
	private static int K = 5;
	

	protected void setup(){

		super.setup();

		//get the parameters given into the object[]
		final Object[] args = getArguments();
		if(args.length!=0){
			System.err.println("Error while creating the sum agent");
			System.exit(-1);

		}else{

			//Add the behaviours
			addBehaviour(new RequestValues(this));// both the number of values and the name of the agent to reply are hardcoded, its bad
			addBehaviour(new Stop(this));
			//addBehaviour(new SumNbReceivedValuesBehaviour(this, 15,"AgentC"));// both the number of values and the name of the agent to reply are hardcoded, its bad

			System.out.println("the receiver agent "+this.getLocalName()+ " is started");
		}
	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}
