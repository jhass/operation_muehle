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

	protected MoveGenerator moveGen = new FirstValidGenerator();

	/**
	 * NIKI for 'Nicht Intelligente KI'
	 */
	protected String strategyName = "NIKI2.0";
	/**
	 * we are the second group
	 */
	private int group = 2;
	private AIBoard boardIPlayWith;

	private int stonesPlaced = 0;
	public static final int MAXSTONES = 9;
	public static final int MINSTONES = 3;

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
		moveGen = new FirstValidGenerator();
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

	public AIBoard getBoard() {
		return this.boardIPlayWith;
	}

	@Override
	public Move doMove(Move last, Slot removed, int thinktime) {
		Move result = null;

		this.initStamp(thinktime);

		last = this.normalizeMove(last);
		removed = this.normalizeSlot(removed);

		this.boardIPlayWith.moveStone(last, Status.FOREIGNSTONE);
		this.boardIPlayWith.removeStone(removed);

		this.stamp();

		HashMap<Integer, Status> myStones = this.boardIPlayWith
				.getFieldsWith(Status.MYSTONE);

		if (myStones == null) {
			throw new IllegalArgumentException("I have no more Stones to move.");
		}

		// condition for Jumping. Else AI will move a Stone
		if (this.stonesPlaced >= AI.MAXSTONES
				&& myStones.size() <= AI.MINSTONES) {

			result = this.moveGen.generateJump(this.boardIPlayWith);

		} else {

			result = this.moveGen.generateMove(this.boardIPlayWith);

		}

		this.boardIPlayWith.moveStone(result, Status.MYSTONE);

		if (result == null) {
			throw new IllegalArgumentException("I have no more places to move.");
		}

		return result;
	}

	@Override
	public Slot placeStone(Slot last, Slot removed, int thinktime) {
		Slot result = null;

		this.initStamp(thinktime);

		last = this.normalizeSlot(last);
		removed = this.normalizeSlot(removed);

		this.stonesPlaced++;

		this.boardIPlayWith.placeStone(last, Status.FOREIGNSTONE);
		this.boardIPlayWith.removeStone(removed);

		this.stamp();

		result = this.moveGen.generatePlace(this.boardIPlayWith);
		
		this.boardIPlayWith.placeStone(result, Status.MYSTONE);

		if (result == null) {
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

		result = this.moveGen.generateRemove(this.boardIPlayWith);

		this.boardIPlayWith.removeStone(result);

		return result;
	}

	public Slot normalizeSlot(Slot slot) {
		Slot result = slot;

		if (slot != null) {
			char col = slot.getColumn();
			if (col > 'Z') {
				result = new G2Slot((char) (col - G2Slot.UPPERCASEDIFF),
						slot.getRow());
			}
		}

		return result;
	}

	public Move normalizeMove(Move move) {
		Move result = move;

		if (move != null) {
			Slot from = move.fromSlot();
			Slot to = move.toSlot();
			Slot fromNormal = this.normalizeSlot(from);
			Slot toNormal = this.normalizeSlot(to);

			if (from != fromNormal || to != toNormal) {
				result = new G2Move(fromNormal, toNormal);
			}
		}

		return result;
	}

}
