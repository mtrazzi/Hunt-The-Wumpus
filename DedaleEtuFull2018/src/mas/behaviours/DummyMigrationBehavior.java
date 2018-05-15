package mas.behaviours;

import java.io.IOException;

import jade.core.ContainerID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import java.util.*;

public class DummyMigrationBehavior extends GeneralSimpleBehaviour{

	private static final long serialVersionUID = -2058134622078521998L;

	public DummyMigrationBehavior(final mas.abstractAgent myagent) {
		super(myagent);
	}
	
	private static Integer n = 0;

	
	private boolean verbose=true;
	
	public void action() {
		
		if (this.n == 0) {
//			ContainerID cID= new ContainerID();
//			cID.setName("SimonContainer");
//			cID.setPort("8888");
//			cID.setAddress("132.227.113.196"); //IP of the host of the targeted container
//			
			
			ContainerID cID1= new ContainerID();
			cID1.setName("NOM_COURT");
			cID1.setPort("8888");
			cID1.setAddress("132.227.113.195"); //IP of the host of the targeted container
			
			
			ContainerID cID2= new ContainerID();
			cID2.setName("08-RaoulPlatform");
			cID2.setPort("8888");
			cID2.setAddress("132.227.113.200"); //IP of the host of the targeted container
			
			ContainerID cID3= new ContainerID();
			cID3.setName("MyDistantContainer0");
			cID3.setPort("8888");
			cID3.setAddress("132.227.113.205"); //IP of the host of the targeted container
			
			java.util.List<ContainerID> ids = new java.util.ArrayList<>();
			
			//ids.add(cID);
			ids.add(cID1);
			ids.add(cID2);
			ids.add(cID3);
			//java.util.Collections.shuffle(ids);
			
			//this.myAgent.doMove(cID);// last method to call in the behaviour
			//this.myAgent.doMove(cID1);// last method to call in the behaviour
			//this.myAgent.doMove(cID2);// last method to call in the behaviour
			this.myAgent.doMove(cID3);// last method to call in the behaviour
			
			this.n = this.n + 1;
		}
		

	}

	public boolean done() {
		this.sleep(2000);
		
		return false;
	}

}