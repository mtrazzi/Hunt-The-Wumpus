package agents.example5;

import behaviours.example5.SumNbReceivedValuesBehaviour;
import behaviours.example5.ReceiveMessageBehaviour;
import behaviours.example5.SendNbValuesBehaviour;
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
	private static final long serialVersionUID = 3499482209671348272L;

	protected void setup(){

		super.setup();

		//get the parameters given into the object[]
		final Object[] args = getArguments();
		if(args.length!=0){
			System.err.println("Error while creating the sum agent");
			System.exit(-1);

		}else{

			//Add the behaviours
			addBehaviour(new SumNbReceivedValuesBehaviour(this, "AgentSUM"));

			System.out.println("the receiver agent "+this.getLocalName()+ " is started");
		}
	}

	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){

	}
}