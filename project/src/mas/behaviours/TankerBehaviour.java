package mas.behaviours;

import java.util.List;
import java.util.Random;

import env.Attribute;
import env.Couple;
import mas.utils.MyCouple;

public class TankerBehaviour extends GeneralSimpleBehaviour{

	private static final long serialVersionUID = 9088209402507795289L;

	public TankerBehaviour(final mas.abstractAgent myagent) {
		super(myagent);
	}
	
	

	public void action() {
		// Set verbose to true do debug the behaviour
		boolean verbose = false;
		
		//define agent = agent from the behavior casted to general agent
		mas.agents.GeneralAgent agent = getGeneralAgent();
		
		if (verbose)
			System.out.println(agent.getLocalName() + " -> STARTING BEHAVIOUR!");
		
		//Get current position
		String myPosition = agent.getCurrentPosition();

		// Prints the current position
		if (verbose)
			System.out.println("Position of: " + agent.getLocalName() + ": " + myPosition);

		if (myPosition != "") {
			
			// List of observable from the agent's current position
			List<Couple<String, List<Attribute>>> lobs = agent.observe();
			if (verbose)
				System.out.println(agent.getLocalName() + " -- list of observables: " + lobs);
			
			this.updatingGraph(myPosition, lobs);

			MyCouple couple = new MyCouple(-1,-1);
			agent.getGraph().getTreasureHashmap().put(myPosition, couple);
			agent.getGraph().getTimeStampHashmap().put(myPosition, System.currentTimeMillis());
		}
		
	}

	public boolean done() {
		this.defaultsleep();

		return false;
	}
}
