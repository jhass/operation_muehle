package de.hshannover.operation_muehle.ai;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;

public interface MoveGenerator {
	
	public Move generateMove(AIBoard board);
	public Move generateJump(AIBoard board);
	public Slot generatePlace(AIBoard board);
	public Slot generateRemove(AIBoard board);
	
	
}
