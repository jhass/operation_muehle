package de.hshannover.operation_muehle.advancedAI;

import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Slot;

public class AdvAI implements Strategy {

	public AdvAI() {
		AIState.phase = AIState.PLACE_PHASE;
		AIState.board = new Gameboard();
		AIState.availableStones = 9;
		AIState.stones = 0;
	}
	
	@Override
	public Move doMove(de.hshannover.inform.muehle.strategy.Move lastMove, de.hshannover.inform.muehle.strategy.Slot removed, int thinkTime) {
		removeOwnStone(removed);
		if (lastMove != null) AIState.board.applyMove(new Move(lastMove));
		AIState.removalRequested = false;
		return generateMove(thinkTime);
	}

	private Move generateMove(int thinkTime) {
		MoveCreator moveCreator = new MoveCreator();
		moveCreator.start();
		try {
			moveCreator.join((long) thinkTime);
		} catch (InterruptedException e) {}
		return moveCreator.getBestMove();
	}

	@Override
	public int getGroup() {
		return 2;
	}

	@Override
	public String getStrategyName() {
		return "G2_Easy_AI";
	}

	@Override
	public Slot placeStone(de.hshannover.inform.muehle.strategy.Slot placed, 
						   de.hshannover.inform.muehle.strategy.Slot removed,
						   int thinkTime) {
		if (AIState.color == null) {
			if (placed == null) AIState.color = AIState.Color.WHITE;
			else AIState.color = AIState.Color.BLACK;
		}
		removeOwnStone(removed);
		
		if (placed != null) {
			AIState.board.applySlot(new Slot(placed), 
								 AIState.color.getOpponentSlotStatus());
		}
		AIState.increaseStones();
		AIState.removalRequested = false;
		return generateMove(thinkTime).toSlot();
	}

	private void removeOwnStone(de.hshannover.inform.muehle.strategy.Slot removed) {
		if (removed != null) {
			AIState.board.applySlot(new Slot(removed), Slot.Status.EMPTY);
			AIState.decreaseNumberOfStones();
		}
	}

	@Override
	public Slot removeStone(int thinkTime) {
		// TODO Auto-generated method stub
		AIState.removalRequested = true;
		return generateMove(thinkTime).fromSlot();
	}	
}
