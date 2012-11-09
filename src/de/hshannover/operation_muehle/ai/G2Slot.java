package de.hshannover.operation_muehle.ai;

import de.hshannover.inform.muehle.strategy.Slot;

public class G2Slot implements Slot{
	
	private char col;
	private int row;
	
	public G2Slot(char col,int row){
		this.row = row;
		this.col = col;
	}

	@Override
	public char getColumn() {
		return this.col;
	}

	@Override
	public int getRow() {
		return this.row;
	}
	
	@Override
	public String toString(){
		return String.valueOf(this.col)+String.valueOf(this.row);
	}

}
