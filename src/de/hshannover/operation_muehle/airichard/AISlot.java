package de.hshannover.operation_muehle.airichard;

import de.hshannover.operation_muehle.logic.Slot;

public class AISlot extends Slot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AISlot(int column, int row) {
		super(column, row);
	}
	
	public AISlot(int column, char row) {
		super(column, (int) row - 64 );
	}

}
