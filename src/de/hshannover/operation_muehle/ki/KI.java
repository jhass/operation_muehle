package de.hshannover.operation_muehle.ki;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

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
	
	private ArrayList<Slot> getSlotsWith(SlotStatus status){
		
		ArrayList<Slot> result = new ArrayList<Slot>();
		
		HashMap<Integer,Slot> slots = this.boardIPlayWith.getBoard();
		
		Iterator<Integer> iterator = slots.keySet().iterator();
		Integer i;
		Slot s;
		while(iterator.hasNext()){
			i = iterator.next();
			s = slots.get(i);
			if(s.getStatus()==status){
				result.add(s);
			}
		}
		
		return result;
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

		Move result = null;

		this.initStamp(thinktime);

		apply(last);
		remove(removed);

		this.stamp();


		ArrayList<Slot> myStones = this.getSlotsWith(this.color);
		for(int i=0;i<myStones.size();i++){
			Slot my = myStones.get(i);
			Slot[] neighbours = this.boardIPlayWith.getNeighbours(my);
			for(int j=0;j<neighbours.length;j++){
				Slot neigh = neighbours[j];
				if(neigh.getStatus()==SlotStatus.EMPTY){
					result = new Move(new Slot(my.getColumn(),my.getRow()),
							new Slot(neigh.getColumn(),neigh.getRow()));
				}
			}
		}

		return result;
	}

	@Override
	public Slot placeStone(de.hshannover.inform.muehle.strategy.Slot last,
			de.hshannover.inform.muehle.strategy.Slot removed, int thinktime) {

		Slot result = null;

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

		ArrayList<Slot> emptyPlaces = this.getSlotsWith(SlotStatus.EMPTY);
		
		if(emptyPlaces.size()>0){
			result = emptyPlaces.get(0);
		}

		return result;
	}

	@Override
	public Slot removeStone(int thinktime) {

		Slot result = null;
		
		ArrayList<Slot> hisStones = this.getSlotsWith(this.getOpponentColor());
		if(hisStones.size()>0){
			result = hisStones.get(0);
		}
		return result;
	}

}
