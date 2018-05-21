package mas.agents;

import jade.domain.DFService;
import jade.domain.FIPAException;

import java.util.HashMap;
import java.util.Stack;

import env.EntityType;
import env.Environment;
import mas.abstractAgent;
import mas.behaviours.*;
import mas.graph.Graph;
import mas.agents.*;
import mas.agents.interactions.protocols.deployMe.R1_deployMe;
import mas.agents.interactions.protocols.deployMe.R1_managerAnswer;

public class MigrAgent extends GeneralAgent {
	private static final long serialVersionUID = -5686331366676803589L;
	protected void setup(){//Automatically called at agent’s creation
		addBehaviour(new R1_deployMe("GK",this));
		addBehaviour(new R1_managerAnswer("GK",this));
		
		addBehaviour(new DummyMigrationBehavior(this));
		addBehaviour(new ExplorerWalk3(this));
		this.setGraph(new Graph<String>());
		this.setHashmap(new HashMap<String, String>());
		this.setStack(new Stack<String>());
		this.setLastMove("");

		super.setup();
	}
	protected void beforeMove(){//Automatically called before doMove()
	super.beforeMove();
	
		System.out.println("knock-knock");

	}
	protected void afterMove(){//Automatically called after doMove()
		super.afterMove();
		addBehaviour(new R1_deployMe("GK",this));
		System.out.println("mi grat ed");
	}
}