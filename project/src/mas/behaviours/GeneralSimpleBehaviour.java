package mas.behaviours;

import java.io.IOException;
import java.util.List;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;

public class GeneralSimpleBehaviour extends SimpleBehaviour {

	public GeneralSimpleBehaviour(final mas.abstractAgent myagent) {
		super(myagent);
	}

	public void action() {
	}

	public boolean done() {
		return true;
	}

	public mas.agents.GeneralAgent getGeneralAgent() {
		return (mas.agents.GeneralAgent) this.myAgent;
	}

	public void littlePause() {
		// Little pause to allow you to follow what is going on
		try {
			System.out.println("Press Enter in the console to allow the agent " + this.myAgent.getLocalName()
					+ " to execute its next move");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void debugStack(boolean verbose) {
		if (verbose) {
			System.out.print(getGeneralAgent().getLocalName() + "  ->");
			getGeneralAgent().printStack();
		}

	}

	public void debugNextMove(boolean verbose, String myMove) {
		if (verbose)
			System.out.println(getGeneralAgent().getLocalName() + "  -> Next Move: " + myMove);
	}

	public void defaultsleep() {
		this.sleep(500);
	}
}
