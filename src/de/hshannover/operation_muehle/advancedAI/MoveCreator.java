package de.hshannover.operation_muehle.advancedAI;

import java.util.ArrayList;
import java.util.Random;

import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.operation_muehle.logic.MoveValidator;
import de.hshannover.operation_muehle.logic.Slot;

public class MoveCreator extends Thread {
	private AIState state;
	private Move bestMove;

	public MoveCreator(AIState state) {
		super();
		this.state = state;
	}
	
	@Override
	public void run() {
		if (state.removalRequested) {
			computeRemoval();
		} else {
			switch (state.phase) {
			case AIState.PLACE_PHASE: computePlacement();
				break;
			case AIState.MOVE_PHASE: computeMove();
				break;
			case AIState.JUMP_PHASE: computeJump();
			}
		}
	}
	
	private void computeRemoval() {
		ArrayList<Slot> enemyStones = state.board.getStonesOfStatus(
							  state.color.getOpponentSlotStatus());
		ArrayList<Slot> canRemove= new ArrayList<Slot>();
		
		MoveValidator moveVal = new MoveValidator(state.board, null);
		
		for (Slot slot : enemyStones) {
		if (!moveVal.isInMill(slot)) canRemove.add(slot);
		}
		if (canRemove.size() == 0) canRemove = enemyStones;
		
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(canRemove.size());
		this.bestMove = new Move(canRemove.get(index),null);
	}
		

	private void computeJump() {
		ArrayList<Slot> freeSlots = state.board.getStonesOfStatus(Slot.Status.EMPTY);
		ArrayList<Slot> colorSlots = state.board.getStonesOfStatus(state.color.getSlotStatus());
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(colorSlots.size());
		Slot jumpSlot = colorSlots.get(index);
		index = slotNumber.nextInt(freeSlots.size());
		this.bestMove = new Move(jumpSlot, freeSlots.get(index));
	}

	private void computeMove() {
		ArrayList<Slot> freeSlots = state.board.getStonesOfStatus(state.color.getSlotStatus());
		ArrayList<Move> avaMoves = new ArrayList<Move>();
		
		for (Slot slot: freeSlots) {
			Slot[] neighbours = state.board.getNeighbours(slot);
			for (Slot nSlot: neighbours) {
				if (nSlot != null && 
					nSlot.getStatus() == Slot.Status.EMPTY) 
					avaMoves.add(new Move(slot,nSlot));
			}
		}
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(avaMoves.size());
		this.bestMove = avaMoves.get(index);
	}

	private void computePlacement() {
		ArrayList<Slot> freeSlots = state.board.getStonesOfStatus(Slot.Status.EMPTY);
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(freeSlots.size());
		this.bestMove = new Move(null, freeSlots.get(index));
	}

	public Move getBestMove() {
		return this.bestMove;
	}
}