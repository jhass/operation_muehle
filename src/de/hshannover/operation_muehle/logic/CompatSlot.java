package de.hshannover.operation_muehle.logic;

public class CompatSlot extends Slot {

	public CompatSlot(de.hshannover.inform.muehle.strategy.Slot slot) {
		super(slot);
	}
	
	@Override
	public char getColumn() {
		return Character.toLowerCase(super.getColumn());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
