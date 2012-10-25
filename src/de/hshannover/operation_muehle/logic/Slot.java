package de.hshannover.operation_muehle.logic;

public class Slot implements de.hshannover.inform.muehle.strategy.Slot {
	private int status;
	private int row;
	private int column;
	
	public Slot(int row, int column, int status) {
		if(row<0) throw new IllegalArgumentException("Slot.Row ungueltig!");
		if(column<0) throw new IllegalArgumentException("Slot.Column ungueltig!");
		if(status<0 | status>2) throw new IllegalArgumentException("Slot.Status ungueltig!");
		this.status= status;
		this.row= row;
		this.column= column;
	}
	
	public void setStatus(int status) {
		if(status<0 | status>2) throw new IllegalArgumentException("Slot.Status ungueltig!");
		this.status= status;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	@Override
	public char getColumn() {
		return (char)(this.column+64);
	}
	@Override
	public int getRow() {
		return this.row;
	}

}
