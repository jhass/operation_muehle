package de.hshannover.operation_muehle.airichard;

import java.util.ArrayList;

import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Slot;

public class AIGameBoard extends Gameboard {
	private static final long serialVersionUID = 1L;
	
	public AIGameBoard() {
		super();
	}

	public ArrayList<de.hshannover.operation_muehle.logic.Slot> getFreeSlots() {
		return getStonesOfStatus(Slot.Status.EMPTY);
		
	}

	public ArrayList<de.hshannover.operation_muehle.logic.Slot> getMyStones() {
		return getStonesOfStatus(Slot.Status.WHITE);
	}

	public ArrayList<de.hshannover.operation_muehle.logic.Slot> getMoveableStones() {
		
		ArrayList<de.hshannover.operation_muehle.logic.Slot> myStones = getMyStones();
		ArrayList<de.hshannover.operation_muehle.logic.Slot> movableStones 
				= new ArrayList<de.hshannover.operation_muehle.logic.Slot>();
		
		for(int i = 0; i < myStones.size(); i++) {
			if(freeSlotsaround(myStones.get(i))) {
				movableStones.add(myStones.get(i));
			}
		}
		return movableStones;
	}

	private boolean freeSlotsaround(Slot slot) {
		
		Slot[] neighbours = getNeighbours(slot);
		
		for(int i = 0; i < neighbours.length; i++) {
			if(neighbours[i] != null) {
				if(returnSlot(neighbours[i]).getStatus() == Slot.Status.EMPTY) {
				return true;
				}
			}
		}
		return false;
	}

	public ArrayList<de.hshannover.operation_muehle.logic.Slot> getOppositeStones() {
		return getStonesOfStatus(Slot.Status.BLACK);
		
	}

}
