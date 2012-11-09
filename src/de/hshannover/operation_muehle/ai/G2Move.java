package de.hshannover.operation_muehle.ai;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;

public class G2Move implements Move{
	
	private Slot from,to;
	
	public G2Move(Slot from, Slot to){
		this.from = from;
		this.to = to;
	}

	@Override
	public Slot fromSlot() {
		return this.from;
	}

	@Override
	public Slot toSlot() {
		return this.to;
	}
	
	@Override
	public String toString(){
		return this.from + " => " + this.to;
	}

}
