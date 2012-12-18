package de.hshannover.operation_muehle.advancedAI;

import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Logger;
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
		if (lastMove != null) {
			Move move = new Move(lastMove);
			if (move.isPlacement()) {
				AIState.board.applySlot(move.toSlot(), AIState.color.getOpponentSlotStatus());
			} else {
				AIState.board.applyMove(move);
			}
			Logger.logDebugf("AI received %s", move);
		}
		AIState.removalRequested = false;
		return generateMove(thinkTime);
	}

	private Move generateMove(int thinkTime) {
		MoveCreator moveCreator = new MoveCreator();
		moveCreator.start();
		try {
			moveCreator.join((long) thinkTime);
		} catch (InterruptedException e) {}
		Move move = moveCreator.getBestMove();
		
		if (move.isRemoval()) {
			AIState.board.applySlot(move.fromSlot(), Slot.Status.EMPTY);
		} else if (move.isPlacement()) {
			AIState.board.applySlot(move.toSlot(), AIState.color.getSlotStatus());
		} else {
			AIState.board.applyMove(move);
		}
		return move;
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
			Slot slot = new Slot(placed);
			Logger.logDebugf("AI received placement to %s", slot);
			AIState.board.applySlot(slot, 
								 AIState.color.getOpponentSlotStatus());
		}
		AIState.removalRequested = false;
		Move move = generateMove(thinkTime);
		AIState.increaseStones();
		return move.toSlot();
	}

	private void removeOwnStone(de.hshannover.inform.muehle.strategy.Slot removed) {
		if (removed != null) {
			Slot slot = new Slot(removed);
			Logger.logDebugf("AI received removal of %s", slot);
			AIState.board.applySlot(slot, Slot.Status.EMPTY);
			AIState.decreaseNumberOfStones();
		}
	}

	@Override
	public Slot removeStone(int thinkTime) {
		AIState.removalRequested = true;
		return generateMove(thinkTime).fromSlot();
	}	
}
