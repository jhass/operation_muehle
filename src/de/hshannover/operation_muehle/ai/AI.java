package de.hshannover.operation_muehle.ai;

import java.util.HashMap;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;
import de.hshannover.inform.muehle.strategy.Strategy;

/**
 * 
 * @author zyklos
 * 
 */
public class AI implements Strategy {

	private MoveGenerator moveGen = new MoveGenerator();

	/**
	 * NIKI for 'Nicht Intelligente KI'
	 */
	private String strategyName = "NIKI";
	/**
	 * we are the second group
	 */
	private int group = 2;
	private AIBoard boardIPlayWith;

	/**
	 * Time-Management
	 */
	private long time;
	private long timeStamp;

	/**
	 * Initialize the own Gameboard
	 */
	public AI() {
		this.boardIPlayWith = new AIBoard();
	}

	private void initStamp(long time) {
		this.timeStamp = System.currentTimeMillis();
		this.time = time;
	}

	private long stamp() {
		long current = System.currentTimeMillis();
		long diff = current - timeStamp;
		this.timeStamp = current;
		this.time -= diff;
		return this.time;
	}

	@Override
	public int getGroup() {
		return this.group;
	}

	@Override
	public String getStrategyName() {
		return this.strategyName;
	}
	
	public AIBoard getBoard(){
		return this.boardIPlayWith;
	}

	@Override
	public Move doMove(Move last, Slot removed, int thinktime) {

		Move result = null;

		this.initStamp(thinktime);

		this.boardIPlayWith.moveStone(last,Status.FOREIGNSTONE);
		this.boardIPlayWith.removeStone(removed);

		this.stamp();

		HashMap<Integer,Status> myStones = this.boardIPlayWith.getFieldsWith(Status.MYSTONE);

		if(myStones == null){
		throw new IllegalArgumentException(
				"I have no more Stones to move.");
		}
		
		for (int key : myStones.keySet()) {
			HashMap<Integer,Status> neighbours = this.boardIPlayWith.getNeighbours(
					this.boardIPlayWith.generateSlotByAddress(key));
			for (int neigh: neighbours.keySet()) {
				if (neighbours.get(neigh) == Status.EMPTY) {
					result = new G2Move(this.boardIPlayWith.generateSlotByAddress(key),
							this.boardIPlayWith.generateSlotByAddress(neigh));
				}
			}
		}
		
		this.boardIPlayWith.moveStone(result,Status.MYSTONE);

		if(result == null){
		throw new IllegalArgumentException(
				"I have no more places to move.");
		}

		return result;
	}

	@Override
	public Slot placeStone(Slot last,Slot removed, int thinktime) {

		Slot result = null;

		this.initStamp(thinktime);

		this.boardIPlayWith.placeStone(last, Status.FOREIGNSTONE);
		this.boardIPlayWith.removeStone(removed);

		this.stamp();

		HashMap<Integer,Status> emptyPlaces = this.boardIPlayWith.getFieldsWith(Status.EMPTY);

		for (int key : emptyPlaces.keySet()) {
			result = this.boardIPlayWith.generateSlotByAddress(key);
			break;
		}
		
		this.boardIPlayWith.placeStone(result, Status.MYSTONE);
		

		if(result == null){
		throw new IllegalArgumentException(
				"There are no empty Fields available!");
		}

		return result;
	}

	@Override
	public Slot removeStone(int thinktime) {

		Slot result = null;

		this.initStamp(thinktime);

		this.stamp();

		HashMap<Integer,Status> opponentStones = 
				this.boardIPlayWith.getFieldsWith(Status.FOREIGNSTONE);
		
		if(opponentStones == null){
		throw new IllegalArgumentException(
				"There are no stones from opponent to remove.");
		}
		
		for (int key : opponentStones.keySet()) {
			result = this.boardIPlayWith.generateSlotByAddress(key);
			break;
		}
		
		this.boardIPlayWith.removeStone(result);

		
		return result;
	}

}
