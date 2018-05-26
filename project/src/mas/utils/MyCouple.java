package mas.utils;
import java.io.Serializable;

import env.Couple;

public class MyCouple implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer treasure;
	private Integer diamonds;

	public MyCouple(Integer treasure, Integer diamonds) {
		this.treasure = treasure;
		this.diamonds = diamonds;
	}

	public Integer getTreasure() {
		return this.treasure;
	}
	
	public Integer getDiamonds() {
		return this.diamonds;
	}
	
	public void setTreasure(Integer treasure) {
		this.treasure = treasure;
	}
	
	public void setDiamonds(Integer diamonds) {
		this.diamonds = diamonds;
	}
}
