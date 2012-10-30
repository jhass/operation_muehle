package de.hshannover.operation_muehle.logic;

/** Helper object to represent the user choosen options for a player.
 * 
 * @author Jonne Haß
 *
 */
public class PlayerOptions {
	private boolean isAI;
	private String name;
	private int thinkTime;
	
	/** Create a new AI-representing object
	 * 
	 * @param klass the full class name to pass to StrategyLoader
	 * @param thinkTime 
	 */
	public PlayerOptions(String klass, int thinkTime) {
		this.isAI = true;
		this.name = klass;
		this.thinkTime = thinkTime;
	}
	
	/** Create a new a human player representing object
	 * 
	 * @param name the human readable name
	 */
	public PlayerOptions(String name) {
		this.isAI = false;
		this.name = name;
	}
	
	/** Whether the player should be an AI or a human 
	 * 
	 * @return isAI
	 */
	public boolean isAI() {
		return isAI;
	}
	
	/** Depending on the player being an AI or a human either the the full class name
	 *  that should be passed to StrategyLoader or a human readable name.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/** If the player is an AI giving the thinkTime, for a human player the result is
	 *  undefined and should not be used.
	 * 
	 * @return
	 */
	public int getThinkTime() {
		return thinkTime;
	}
}
