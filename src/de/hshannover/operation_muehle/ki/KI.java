package de.hshannover.operation_muehle.ki;

import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.operation_muehle.logic.Slot;

import de.hshannover.inform.muehle.strategy.Strategy;

public class KI implements Strategy {

	public static final int WHITE = 1;
	public static final int BLACK = 2;

	/**
	 * NIKI for 'Nicht Intelligente KI'
	 */
	private String strategyName = "NIKI";
	private Gameboard boardIPlayWith;

	private Move lastKnownMove;
	private Slot lastKnownRemoved;

	/**
	 * Required for creating Slots and Moves
	 */
	private int color;

	/**
	 * Initialize the own Gameboard und set color to WHITE
	 */
	public KI() {
		this.boardIPlayWith = new Gameboard();
		this.color = WHITE;
	}

	public KI(int color) {
		this();
		this.color = color;
	}

	private int getOpponentColor() {
		int result = WHITE;
		if (this.color == WHITE) {
			result = BLACK;
		}
		return result;
	}

	/**
	 * The Slot-Interface uses chars to specify columns.
	 * The address-space for the columns is between 'A' and 'G' 
	 * like in common Muehle game.
	 * @params col Muehle column-address, that won't be checked,
	 * 				if it lies in the valid address-space
	 * @return 		returns the equivalent int value 
	 * 				
	 */
	private int charToIntCoord(char col) {
		return (int) col - 64;
	}

	@Override
	public int getGroup() {
		// We are the second group
		return 2;
	}

	@Override
	public String getStrategyName() {
		return this.strategyName;
	}

	@Override
	public de.hshannover.inform.muehle.strategy.Move doMove(
			de.hshannover.inform.muehle.strategy.Move last,
			de.hshannover.inform.muehle.strategy.Slot removed, int thinktime) {
		
		de.hshannover.inform.muehle.strategy.Move result; 
		
		long time = thinktime;
		long lastTimeStamp = System.currentTimeMillis();
		long timeDiff;
		
		if (last != null) {
			this.boardIPlayWith.applyMove(
					new Move(
							
							new Slot(last.fromSlot().getRow(),
		 this.charToIntCoord(last.fromSlot().getColumn()), 
		 this.getOpponentColor()), 
		 
		 					new Slot(last.toSlot().getRow(), 
		 this.charToIntCoord(last.toSlot().getColumn()),
		 this.getOpponentColor())
							
					)
			);
		}
		if (removed != null) {
			this.boardIPlayWith.removeStone(
		 		new Slot(removed.getRow(), 
		 				this.charToIntCoord(removed.getColumn()),
		 				this.getOpponentColor())
			);
		}
		timeDiff = System.currentTimeMillis() - lastTimeStamp;
		time -= timeDiff;
		
		/*
		 * TODO Here the algorithm for choosing and creating a
		 * move
		 */
		result = null;

		return result;
	}

	@Override
	public de.hshannover.inform.muehle.strategy.Slot placeStone(
			de.hshannover.inform.muehle.strategy.Slot last,
			de.hshannover.inform.muehle.strategy.Slot removed, int thinktime) {
		
		de.hshannover.inform.muehle.strategy.Slot result;

		/*
		 *  TODO Man braeuchte noch ein applySlot bei Gameboard,
		 *  jedenfalls kann man dafuer das derzeitige applyMove 
		 *  nicht benutzen  
		 */
		if (last != null) {
			// geht, da NullPointerException in applyMove
			this.boardIPlayWith.applyMove(
					new Move(	
							null,
		 
		 					new Slot(last.getRow(), 
		 				this.charToIntCoord(last.getColumn()),
		 				this.getOpponentColor())
							
					)
			);
		}

		if (removed != null) {
			this.boardIPlayWith.removeStone(
			 		new Slot(removed.getRow(), 
			 				this.charToIntCoord(removed.getColumn()),
			 				this.getOpponentColor())
				);
		}

		/*
		 * TODO ... choosing the Stone, that has to be removed ... 
		 */
		result = null;
		
		return result;
	}

	@Override
	public de.hshannover.inform.muehle.strategy.Slot removeStone(int thinktime) {
		
		de.hshannover.inform.muehle.strategy.Slot result;
		/*
		 * TODO Hier wird der Algorithmus fuer Auswaehlen
		 * berechnet
		 */
		result = null;
		
		return result;
	}

}
