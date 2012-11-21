package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;

public class RemoveMoveValidator extends MoveValidator {

	public RemoveMoveValidator(Gameboard board, PlayerManager players) {
		super(board, players);
	}

	@Override
	public boolean isValidMove(Move move) {
		if (isNoOperationMove(move))
			return false;
		
		if (move.isPlacement())
			return false;
		
		if (move.isRemoval()) {
			return canRemove(gameboard.returnSlot(move.fromSlot()));
		}
		
		return false;
	}
	
	
	/**
	 * Checks if a Stone in a Slot is removable or not.
	 * @param slot The Slot that contains the Stone in question.
	 * @return If the Stone can be removed.
	 */
	private boolean canRemove(Slot slot) {
		ArrayList<Slot> removeableSlots = new ArrayList<Slot>();
		
		/*
		 * Pruefen, ob alle gegnerischen Steine innerhalb einer Muehle sind
		 * Wenn ein gegnerischer Stein nicht in einer Muehle ist, dann wird der
		 * Slot in die Liste hinzugefuegt. Wenn der gesuchte Slot nicht in einer
		 * Mühle ist kann er entfernt werden.
		 */
		for (Slot opponentSlot: gameboard.getStonesOfStatus(players.getOpponentsSlotStatus())) {
			if (!isInMill(opponentSlot)) {
				if (opponentSlot.hashCode() == slot.hashCode())
					return true;
				removeableSlots.add(opponentSlot);
			}
		}
		
		/*
		 * Gibt true zurueck, wenn der die Liste leer ist, da nun alle Steine
		 * innherhalb einer Muehle liegen und der Stein erfernt werden darf.
		 * Oder wenn in der Liste Elemente sind, gibt es Steine außerhalb
		 * der Muehle, dann darf der Stein nur entfernt werden, wenn er in der
		 * Liste enthalten ist.
		 */
		if (removeableSlots.size() == 0 || removeableSlots.contains(slot))
			return true;
		
		return false;
	}
}
