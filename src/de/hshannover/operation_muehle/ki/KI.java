package de.hshannover.operation_muehle.ki;

import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.operation_muehle.logic.Slot;
import de.hshannover.operation_muehle.logic.SlotStatus;

import de.hshannover.inform.muehle.strategy.Strategy;

/**
 * 
 * @author zyklos
 * 
 */
public class KI implements Strategy {
	
	private MoveGenerator moveGen = new MoveGenerator();

	/**
	 * NIKI for 'Nicht Intelligente KI'
	 */
	private String strategyName = "NIKI";
	/**
	 * we are the second group
	 */
	private int group = 2;
	private Gameboard boardIPlayWith;

	/**
	 * Required for creating Slots and Moves First we think we are black
	 */
	private SlotStatus color = SlotStatus.BLACK;

	/**
	 * Time-Management
	 */
	private long time;
	private long timeStamp;

	/**
	 * Initialize the own Gameboard
	 */
	public KI() {
		this.boardIPlayWith = new Gameboard();
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

	private SlotStatus getOpponentColor() {
		SlotStatus result = SlotStatus.WHITE;
		if (this.color == SlotStatus.WHITE) {
			result = SlotStatus.BLACK;
		}
		return result;
	}

	/**
	 * The Slot-Interface uses chars to specify columns. The address-space for
	 * columns is between 'A' and 'G' like in common Muehle game.
	 * 
	 * @params col Muehle column-address, that won't be checked, if it lies in
	 *         the valid address-space
	 * @return returns the equivalent int value
	 * 
	 */
	private int charToIntCoord(char col) {
		return (int) col - 64;
	}

	private void apply(de.hshannover.inform.muehle.strategy.Move last) {
		if (last != null) {
			this.boardIPlayWith.applyMove(new Move(

			new Slot(last.fromSlot().getRow(), this.charToIntCoord(last
					.fromSlot().getColumn()), this.getOpponentColor()),

			new Slot(last.toSlot().getRow(), this.charToIntCoord(last.toSlot()
					.getColumn()), this.getOpponentColor())

			));
		}
	}

	private void remove(de.hshannover.inform.muehle.strategy.Slot removed) {
		if (removed != null) {
			this.boardIPlayWith.removeStone(new Slot(removed.getRow(), this
					.charToIntCoord(removed.getColumn()), this.color));
		}
	}

	@Override
	public int getGroup() {
		return this.group;
	}

	@Override
	public String getStrategyName() {
		return this.strategyName;
	}

	@Override
	public Move doMove(de.hshannover.inform.muehle.strategy.Move last,
			de.hshannover.inform.muehle.strategy.Slot removed, int thinktime) {

		Move result;

		this.initStamp(thinktime);

		apply(last);
		remove(removed);

		this.stamp();

		/*
		 * TODO Here the algorithm for choosing and creating a move
		 */
		result = new Move(new Slot(6,6,this.color));

		return result;
	}

	@Override
	public Slot placeStone(de.hshannover.inform.muehle.strategy.Slot last,
			de.hshannover.inform.muehle.strategy.Slot removed, int thinktime) {

		Slot result;

		/*
		 * TODO Man braeuchte noch ein applySlot bei Gameboard, jedenfalls kann
		 * man dafuer das derzeitige applyMove nicht benutzen
		 */
		if (last != null) {

			// geht nicht, da NullPointerException in applyMove
			this.boardIPlayWith.applyMove(new Move(null,

			new Slot(last.getRow(), this.charToIntCoord(last.getColumn()), this
					.getOpponentColor())

			));
		} else {
			// this case should only come once:
			// this is the first move or place in that game and we are in action
			// this means we are white not black
			this.color = SlotStatus.WHITE;
		}

		remove(removed);

		/*
		 * TODO ... choosing the Stone, that has to be removed ...
		 */
		result = new Slot(5,5,this.color);

		return result;
	}

	@Override
	public Slot removeStone(int thinktime) {

		Slot result;
		/*
		 * TODO Hier wird der Algorithmus fuer Auswaehlen berechnet
		 */

		result = new Slot(4, 4, this.getOpponentColor());

		return result;
	}

}
