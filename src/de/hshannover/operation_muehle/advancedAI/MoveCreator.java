package de.hshannover.operation_muehle.advancedAI;

import java.util.ArrayList;
import java.util.Random;

import de.hshannover.operation_muehle.logic.Logger;
import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.operation_muehle.logic.MoveValidator;
import de.hshannover.operation_muehle.logic.Slot;

public class MoveCreator extends Thread{
	private Move bestMove;

	@Override
	public void run() {
		Logger.logDebug("Meine Steine: ");
		for (Slot slot: AIState.board) {
			if (slot.getStatus() ==AIState.color.getSlotStatus())
				Logger.logDebug(slot.toString()+", ");
		}
		Logger.logDebug("Andere Steine: ");
		for (Slot slot: AIState.board) {
			if (slot.getStatus() ==AIState.color.getOpponentSlotStatus())
				Logger.logDebug(slot.toString()+", ");
		}
		if (AIState.removalRequested) {
			computeRemoval();
		} else {
			switch (AIState.phase) {
			case AIState.PLACE_PHASE: computePlacement();
				break;
			case AIState.MOVE_PHASE: computeMove();
				break;
			case AIState.JUMP_PHASE: computeJump();
			}
		}
	}
	
	public MoveCreator() {
		super();
		//new Throwable("").printStackTrace();
	}
	
	private void computeRemoval() {
		Logger.logDebug("AI: compute removal");
		ArrayList<Slot> enemyStones = AIState.board.getStonesOfStatus(
							  AIState.color.getOpponentSlotStatus());
		ArrayList<Slot> canRemove= new ArrayList<Slot>();
		
		MoveValidator moveVal = new MoveValidator(AIState.board, null);
		
		for (Slot slot : enemyStones) {
		if (!moveVal.isInMill(slot)) canRemove.add(slot);
		}
		if (canRemove.size() == 0) canRemove = enemyStones;
		
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(canRemove.size());
		this.bestMove = new Move(canRemove.get(index),null);
	}
		

	private void computeJump() {
		Logger.logDebug("AI: compute jump");
		ArrayList<Slot> freeSlots = AIState.board.getStonesOfStatus(Slot.Status.EMPTY);
		ArrayList<Slot> colorSlots = AIState.board.getStonesOfStatus(AIState.color.getSlotStatus());
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(colorSlots.size());
		Slot jumpSlot = colorSlots.get(index);
		index = slotNumber.nextInt(freeSlots.size());
		this.bestMove = new Move(jumpSlot, freeSlots.get(index));
	}

	private void computeMove() {
		Logger.logDebug("AI: compute move");
		ArrayList<Slot> freeSlots = AIState.board.getStonesOfStatus(AIState.color.getSlotStatus());
		ArrayList<Move> avaMoves = new ArrayList<Move>();
		
		for (Slot slot: freeSlots) {
			Slot[] neighbours = AIState.board.getNeighbours(slot);
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
		Logger.logDebug("AI: compute placement");
		ArrayList<Slot> freeSlots = AIState.board.getStonesOfStatus(Slot.Status.EMPTY);
		Random slotNumber = new Random();
		int index = slotNumber.nextInt(freeSlots.size());
		this.bestMove = new Move(null, freeSlots.get(index));
	}

	public Move getBestMove() {
		return this.bestMove;
	}
}