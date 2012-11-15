package de.hshannover.operation_muehle.ai;

import de.hshannover.inform.muehle.strategy.Slot;

public class G2Slot implements Slot{
	
	private char col;
	private int row;
	public static final int UPPERCASEDIFF = (int)('a'-'A');  
	
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
	
	public void setColumn(char col){
		this.col = col;
	}
	
	@Override
	public String toString(){
		return String.valueOf(this.col)+String.valueOf(this.row);
	}

}
