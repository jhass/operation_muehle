package de.hshannover.operation_muehle.advancedAI;

import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.logic.Slot;

public class AdvAI implements Strategy {
	private AIState state;
	
	public AdvAI() {
		state = new AIState();
	}
	
	@Override
	public Move doMove(de.hshannover.inform.muehle.strategy.Move lastMove, de.hshannover.inform.muehle.strategy.Slot removed, int thinkTime) {
		removeOwnStone(removed);
		if (lastMove != null) {
			Move move = new Move(lastMove);
			if (move.isPlacement()) {
				state.board.applySlot(move.toSlot(), state.color.getOpponentSlotStatus());
			} else {
				state.board.applyMove(move);
			}
		}
		state.removalRequested = false;
		return generateMove(thinkTime);
	}

	private Move generateMove(int thinkTime) {
		MoveCreator moveCreator = new MoveCreator(state);
		moveCreator.start();
		try {
			moveCreator.join((long) thinkTime);
		} catch (InterruptedException e) {}
		Move move = moveCreator.getBestMove();
		
		if (move.isRemoval()) {
			state.board.applySlot(move.fromSlot(), Slot.Status.EMPTY);
		} else if (move.isPlacement()) {
			state.board.applySlot(move.toSlot(), state.color.getSlotStatus());
		} else {
			state.board.applyMove(move);
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
		if (state.color == null) {
			if (placed == null) state.color = AIState.Color.WHITE;
			else state.color = AIState.Color.BLACK;
		}
		removeOwnStone(removed);
		
		if (placed != null) {
			Slot slot = new Slot(placed);
			state.board.applySlot(slot, 
								 state.color.getOpponentSlotStatus());
		}
		state.removalRequested = false;
		Move move = generateMove(thinkTime);
		state.increaseStones();
		return move.toSlot();
	}

	private void removeOwnStone(de.hshannover.inform.muehle.strategy.Slot removed) {
		if (removed != null) {
			Slot slot = new Slot(removed);
			state.board.applySlot(slot, Slot.Status.EMPTY);
			state.decreaseNumberOfStones();
		}
	}

	@Override
	public Slot removeStone(int thinkTime) {
		state.removalRequested = true;
		return generateMove(thinkTime).fromSlot();
	}	
}
