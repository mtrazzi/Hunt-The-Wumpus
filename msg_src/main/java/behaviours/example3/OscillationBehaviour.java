package behaviours.example3;

import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.TickerBehaviour;

/**
 * Illustrating example of intra-platform migration.
 * The agent who uses this behaviour periodically moves between two HARDCODED containers.
 * 
 * @author hc
 *
 */
public class OscillationBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;

	private boolean oscillator;
	private ContainerID cID;
	
	/**
	 * The agent periodically migrates between container Mycontainer1 and Mycontainer3 
	 * 
	 * @param a Ref to the agent we are adding the behaviour 
	 * @param period (in ms), the period
	 * @param ip
	 * @param port
	 */
	public OscillationBehaviour(Agent a, String ip,String port,long period) {
		super(a, period);
		this.oscillator=true;
		
		this.cID= new ContainerID();
		this.cID.setPort(port);
		this.cID.setAddress(ip); //IP of the host of the targeted container		
	}
	
	@Override
	protected void onTick() {
		
		if (oscillator){
			cID.setName("Mycontainer1");
		}else{
			cID.setName("Mycontainer3");	
		}
		this.oscillator=!this.oscillator;
		
		this.myAgent.doMove(cID);// LAST method to call in a behaviour, ALWAYS
	}

	
	
}
