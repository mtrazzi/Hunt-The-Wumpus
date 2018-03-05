package princ;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;


import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Using the Jade's documentation, you can hardly find how to launch a platform from source code.  
 * I hope these few classes will be useful.
 * 
 * 
 * 
 * You can decide in the main class with example you want to be executed
 * example 1 :
 * 1 main container, 3 containers, 4 agents
 * Agent0 on container1
 * Agent1 and Agent3 on container2
 * Agent2 on container3
 * 
 * Agent0 will send one message to agent1 that's all :)
 * 
 * example 2 : 
 * 1 main container, 3 containers, 2 agents
 * AgentA and AgentSUM on container2
 * 
 * AgentA will send 10 values to agentSUM, which will compute the sum and return the result to agentA
 * 
 * example 3 :
 * 1 main container, 3 containers, 2 agents
 * Agent Oscillator on container2
 * 
 * Agent Oscillator moves from container2 to container 1 then oscillates between container1 and 3
 * It does nothing else then migrating between the containers
 * 
 * In all examples, the "pause" when launching the platform  is just here to give you the possibility to activate the sniffer agent from its GUI 
 * in order to sniff the agents and to graphically see the message passing process (if any).
 * 
 * In any case, I chose here to print the sent/received on the standard output.
 * 
 * For sake of simplicity, the creation of the containers and of the agents is hardcoded, and I do not activate the sniffer by default.  
 * It is bad.
 * 
 * 
 * Tested with Jade from 3.7 to 4.4.0
 * 
 * 
 * @author hc
 *
 */


public class Principal {

	/*
	 * The values of the 3 following variables must be udpated if you want to work with a distributed platform. Otherwise, do not touch them.
	 */

	/**
	 * IP (or host) of the main container
	 */
	private static String PLATFORM_IP = "127.0.0.1"; 

	/**
	 * Port to use to conctact the AMS
	 */
	private static int PLATFORM_PORT=8888;

	/**
	 * ID (name) of the platform instance
	 */
	private static String PLATFORM_ID="Ithaq";


	/**
	 * This variable is used to select the example you want to run (1, 2 or 3).
	 */
	private static int EXAMPLE_TO_RUN = 5;

	private static HashMap<String, ContainerController> containerList=new HashMap<String, ContainerController>();// container's name - container's ref
	private static List<AgentController> agentList;// agents's ref
	private static Runtime rt;	
	private static ArrayList<String> rec = new ArrayList (); 

	public static void main(String[] args){

		//1), create the platform (Main container (DF+AMS) + containers + monitoring agents : RMA and SNIFFER)
		rt=emptyPlatform(containerList);

		//2) create agents and add them to the platform.
		agentList=createAgents(containerList);
		

		try {
			System.out.println("Press a key to start the agents -- this action is here only to let you activate the sniffer (see documentation)");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//3) launch agents
		startAgents(agentList);

	}



	/**********************************************
	 * 
	 * Methods used to create an empty platform
	 * 
	 **********************************************/

	/**
	 * Create an empty platform composed of 1 main container and several containers.
	 * 
	 * @param containerList the HashMap of (container's name,container's ref)
	 * @return a ref to the platform and update the containerList
	 */
	private static Runtime emptyPlatform(HashMap<String, ContainerController> containerList){

		Runtime rt = Runtime.instance();

		// 1) create a platform (main container+DF+AMS)
		Profile pMain = new ProfileImpl(PLATFORM_IP, PLATFORM_PORT, PLATFORM_ID);
		System.out.println("Launching a main-container..."+pMain);
		AgentContainer mainContainerRef = rt.createMainContainer(pMain); //DF and AMS are include

		// 2) create the containers
		containerList.putAll(createContainers(rt));

		// 3) create monitoring agents : rma agent, used to debug and monitor the platform; sniffer agent, to monitor communications; 
		createMonitoringAgents(mainContainerRef);

		System.out.println("Plaform ok");
		return rt;

	}

	/**
	 * Create the containers used to hold the agents 
	 * @param rt The reference to the main container
	 * @return an Hmap associating the name of a container and its object reference.
	 * 
	 * note: there is a smarter way to find a container with its name, but we go straight to the goal here. Cf jade's doc.
	 */
	private static HashMap<String,ContainerController> createContainers(Runtime rt) {
		String containerName;
		ProfileImpl pContainer;
		ContainerController containerRef;
		HashMap<String, ContainerController> containerList=new HashMap<String, ContainerController>();//bad to do it here.


		System.out.println("Launching containers ...");

		//create the container1	
		containerName="Mycontainer1";
		pContainer = new ProfileImpl(PLATFORM_IP, PLATFORM_PORT, PLATFORM_ID);
		pContainer.setParameter(Profile.CONTAINER_NAME,containerName);

		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.
		containerList.put(containerName, containerRef);

		//create the container2	
		containerName="Mycontainer2";
		pContainer = new ProfileImpl(PLATFORM_IP, PLATFORM_PORT, PLATFORM_ID);
		pContainer.setParameter(Profile.CONTAINER_NAME,containerName);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.
		containerList.put(containerName, containerRef);

		//create the container3	
		containerName="Mycontainer3";
		pContainer = new ProfileImpl(PLATFORM_IP, PLATFORM_PORT, PLATFORM_ID);
		pContainer.setParameter(Profile.CONTAINER_NAME,containerName);
		System.out.println("Launching container "+pContainer);
		containerRef = rt.createAgentContainer(pContainer); //ContainerController replace AgentContainer in the new versions of Jade.
		containerList.put(containerName, containerRef);

		System.out.println("Launching containers done");
		return containerList;
	}


	/**
	 * create the monitoring agents (rma+sniffer) on the main-container given in parameter and launch them.
	 *  - RMA agent's is used to control, debug and monitor the platform;
	 *  - Sniffer agent is used to monitor communications
	 * @param mc the main-container's reference
	 */
	private static void createMonitoringAgents(ContainerController mc) {

		System.out.println("Launching the rma agent on the main container ...");
		AgentController rma;

		try {
			rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("Launching of rma agent failed");
		}

		System.out.println("Launching  Sniffer agent on the main container...");
		AgentController snif=null;

		try {
			snif= mc.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer",new Object[0]);
			snif.start();

		} catch (StaleProxyException e) {
			e.printStackTrace();
			System.out.println("launching of sniffer agent failed");

		}		


	}



	/**********************************************
	 * 
	 * Methods used to create the agents and to start them
	 * 
	 **********************************************/


	/**
	 *  Creates the agents and add them to the agentList. The agents are NOT started.
	 *@param containerList :Name and container's ref
	 *@return the agentList
	 */
	private static List<AgentController> createAgents(HashMap<String, ContainerController> containerList) {
		System.out.println("Launching agents for example "+EXAMPLE_TO_RUN+" ...");
		ContainerController c;
		String agentName;
		String containerName;
		List<AgentController> agentList=new ArrayList<AgentController>();

		switch (EXAMPLE_TO_RUN) {
		case 1:
			//Agent0 on container1
			containerName="Mycontainer1";
			c = containerList.get(containerName);
			agentName="Agent0";
			try {

				List<String> data=new ArrayList<String>();
				data.add("1");data.add("2");data.add("3");
				Object[] objtab=new Object[]{data};// Example regarding how to give information to an agent at creation. These "data" will be processed in the setup() method of agent0 

				AgentController	ag=c.createNewAgent(agentName,agents.example1.AgentA.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//Agent1 and Agent3 on container2
			containerName="Mycontainer2";
			c = containerList.get(containerName);
			agentName="Agent1";

			try {					
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example1.AgentB.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			agentName="Agent3";
			try {
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example1.AgentEmpty.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

			//Agent2 on container3
			containerName="Mycontainer3";
			c = containerList.get(containerName);
			agentName="Agent2";
			try {					
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example1.AgentEmpty.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case 2:

			//AgentA and AgentSUM on container2
			containerName="Mycontainer2";
			c = containerList.get(containerName);
			agentName="AgentA";
			try {					
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example2.AgentA.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			agentName="AgentSUM";
			try {
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example2.AgentB.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

			break;
		case 3:
			//Agent0 on container1
			containerName="Mycontainer2";
			c = containerList.get(containerName);
			agentName="Oscillator";
			try {
				Object[] objtab=new Object[]{};// Example regarding how to give information to an agent at creation. These "data" will be processed in the setup() method of agent0 
				AgentController	ag=c.createNewAgent(agentName,agents.example3.DummyMovingAgent.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:

			//AgentA , AgentC and AgentSUM on container2
			containerName="Mycontainer2";
			c = containerList.get(containerName);
			agentName="AgentA";
			try {					
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example4.AgentA.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				agentName="AgentC";
			try {					
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example4.AgentC.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			agentName="AgentSUM";
			try {
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example4.AgentB.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			break;
		case 5:

			//AgentA (multiple) , AgentB on container2
			containerName="Mycontainer2";
			c = containerList.get(containerName);
			for(int i=0;i<4;i++){
			agentName="AgentA"+ i;
			try {					
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example4.AgentA.class.getName(),objtab);
				agentList.add(ag);
				rec.add(agentName);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			agentName="AgentSUM";
			try {
				Object[] objtab=new Object[]{};//used to give informations to the agent (in that case, nothing)
				AgentController	ag=c.createNewAgent(agentName,agents.example5.AgentB.class.getName(),objtab);
				agentList.add(ag);
				System.out.println(agentName+" launched on "+containerName);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			break;	
		default:
			System.err.println("This example do not exist");
			System.exit(-1);
			break;
		}



		System.out.println("Agents launched...");
		return agentList;
	}

	/**
	 * Start the agents
	 * @param agentList
	 */
	private static void startAgents(List<AgentController> agentList){

		System.out.println("Starting agents...");


		for(final AgentController ac: agentList){
			try {
				ac.start();
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		System.out.println("Agents started...");
	}

}







