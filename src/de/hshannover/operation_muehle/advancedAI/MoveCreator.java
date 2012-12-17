package de.hshannover.operation_muehle.advancedAI;

import java.util.ArrayList;
import java.util.Random;

import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.operation_muehle.logic.MoveValidator;
import de.hshannover.operation_muehle.logic.Slot;

public class MoveCreator extends Thread{
	private Move bestMove;

	@Override
	public void run() {
		if (AIState.removalRequested) {
			computeRemoval();
		} else {
			switch (AIState.phase) {
			case AIState.PLACE_PHASE: computePlacement();
			case AIState.MOVE_PHASE: computeMove();
			case AIState.JUMP_PHASE: computeJump();
			}
		}
	}
	
	private void computeRemoval() {
		ArrayList<Slot> canRemove = AIState.board.getStonesOfStatus(
							  AIState.color.getOpponentSlotStatus());
		//TODO: Liste kopieren/ Steine innerhalb von Mühlen ausschließen ...
		MoveValidator moveVal = new MoveValidator(AIState.board, null);
		
		for (int i=canRemove.size(); i>0; i--) {
		if (moveVal.isInMill(canRemove.get(i))) canRemove.remove(i);
		}
		
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(canRemove.size());
		this.bestMove = new Move(null, canRemove.get(index));
	}
		

	private void computeJump() {
		ArrayList<Slot> freeSlots = AIState.board.getStonesOfStatus(Slot.Status.EMPTY);
		ArrayList<Slot> colorSlots = AIState.board.getStonesOfStatus(AIState.color.getSlotStatus());
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(colorSlots.size());
		Slot jumpSlot = colorSlots.get(index);
		index = slotNumber.nextInt(freeSlots.size());
		this.bestMove = new Move(jumpSlot, freeSlots.get(index));
	}

	private void computeMove() {
		ArrayList<Slot> freeSlots = AIState.board.getStonesOfStatus(AIState.color.getSlotStatus());
		ArrayList<Move> avaMoves = new ArrayList<Move>();
		
		for (Slot slot: freeSlots) {
			Slot[] neighbours = AIState.board.getNeighbours(slot);
			for (Slot nSlot: neighbours) {
				if (nSlot != null) avaMoves.add(new Move(slot,nSlot));
			}
		}
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(freeSlots.size());
		this.bestMove = new Move(null, freeSlots.get(index));
	}

	private void computePlacement() {
		ArrayList<Slot> freeSlots = AIState.board.getStonesOfStatus(Slot.Status.EMPTY);
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(freeSlots.size());
		this.bestMove = new Move(null, freeSlots.get(index));
	}

	public Move getBestMove() {
		return this.bestMove;
	}
}