package de.hshannover.operation_muehle.logic;

public class MoveValidator {
	protected Gameboard gameboard;
	protected PlayerManager players;
	
	public MoveValidator(Gameboard board, PlayerManager players) {
		this.gameboard = board;
		this.players = players;
	}
	
	public void setPlayers(PlayerManager players) {
		this.players = players;
	}
	
	/**
	 * Checks if the Stone in the given Slot is closing a 
	 * row of three (mill).
	 * @param slot The Slot that contains the Stone to be checked.
	 * @return If the Stone is part of a Mill.
	 */
	public boolean isInMill(Slot slot) {
		if (slot.isEmpty())
			return false;
		
		Slot[] closeSlotNeighbours = gameboard.getNeighbours(slot);
		boolean isTopNull = (closeSlotNeighbours[0] == null);
		boolean isRightNull = (closeSlotNeighbours[1] == null);
		boolean isBottomNull = (closeSlotNeighbours[2] == null);
		boolean isLeftNull = (closeSlotNeighbours[3] == null);
		boolean isMill = false;
				 
		if (!(isTopNull || isBottomNull)) {
			/* Wenn die Felder oberhalb und unterhalb des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			isMill = hasTopAndBottomStatusOf(closeSlotNeighbours, slot.getStatus());
		} else if (isBottomNull) {
			/* Wenn der untere Nachbar leer ist, befindet sich der uebergebene Slot
			 * am unteren Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn oberhalb bestimmt werden.
			 */
			isMill = checkForMill(slot, closeSlotNeighbours[0],0);
		} else if (isTopNull) {
			/* Wenn der obere Nachbar leer ist, befindet sich der uebergebene Slot
			 * am oberen Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn unterhalb bestimmt werden.
			 */
			isMill = checkForMill(slot, closeSlotNeighbours[2],2);
		}
		if (isMill)
			return true;
		
		if (!(isLeftNull || isRightNull)) {
			/* Wenn die Felder links und rechts des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			isMill = hasLeftAndRightStatusOf(closeSlotNeighbours, slot.getStatus());
		} else if (isLeftNull) {
			/* Wenn der rechte Nachbar leer ist, befindet sich der uebergebene Slot
			 * am rechten Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn in linker Richtung bestimmt werden.
			 */
			isMill = checkForMill(slot, closeSlotNeighbours[1],1);
		} else if (isRightNull) {
			/* Wenn der linke Nachbar leer ist, befindet sich der uebergebene Slot
			 * am linken Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn in rechter Richtung bestimmt werden.
			 */
			isMill = checkForMill(slot, closeSlotNeighbours[3],3);
		}
		
		return isMill;
	}
	
	
	
	/**
	 * Die Methode nimmt zwei Slots entgegen (die in einer Reihe liegen), bestimmt den
	 * verbleibenden dritten Slot und prueft die drei Slots auf Statusgleichheit.
	 * Gibt true zurueck, wenn es eine Muehle ist, gibt falsche zurueck, wenn es keine
	 * Muehle ist
	 * @param firstSlot Urspruenglicher Slot
	 * @param secondSlot Erster Nachbar in Reihe
	 * @param index Index, an dem der dritte Reihenstein zu finden ist
	 * @return boolean
	 */
	private boolean checkForMill(Slot firstSlot, Slot secondSlot, int index) {
		return firstSlot.getStatus() == secondSlot.getStatus() &&
			   firstSlot.getStatus() == gameboard.getNeighbours(secondSlot)[index].getStatus();
	}

	/** Checks if a Move is a valid one or not.
	 * 
	 * @param move The Move to be checked.
	 * @return If the Move is true or not.
	 */
	public boolean isValidMove(Move move) {
		Slot startSlot, endSlot;;
		
		if (isPlacementAndNotInPlacementPhase(move))
			return false;
		if (isRemovalAndNotInRemovalMode(move))
			return false;
		if (isNoOperationMove(move))
			return false;
		
		// We don't do this earlier because this function is performance critical
		endSlot = gameboard.returnSlot(move.toSlot());
		
		if (isPlacementAndPlacementPhaseAndEmptyDestination(move, endSlot))
			return true;
		if (isMoveToUsedSlotAndNotRemovalMode(endSlot))
			return false;
		if (isMoveAndJumpPhaseAndEmptyDestination(move, endSlot))
			return true;
		
		// We don't do this earlier because this function is performance critical
		startSlot = gameboard.returnSlot(move.fromSlot());
		
		
		if (isMoveOfOpponentsStoneOrEmptySlot(startSlot))
			return false;
		// most expensive op, last one
		if (isMoveAndMovePhaseAndDestinationEmptyNeighbour(move, startSlot, endSlot))
			return true;
		
		return false; // In doubt refuse
	}
	
	private boolean isPlacementAndNotInPlacementPhase(Move move) {
		return players.isCurrentPlayersPhaseNot(Player.PLACE_PHASE) &&
			   move.isPlacement();
	}

	private boolean isRemovalAndNotInRemovalMode(Move move) {
		return move.isRemoval();
	}

	protected boolean isNoOperationMove(Move move) {
		return move.isMove() &&
			   move.fromSlot().hashCode() == move.toSlot().hashCode();
	}

	private boolean isPlacementAndPlacementPhaseAndEmptyDestination(Move move,
			Slot endSlot) {
		return move.isPlacement() &&
			   players.isCurrentPlayersPhase(Player.PLACE_PHASE) &&
			   endSlot.isEmpty();
	}

	private boolean isMoveToUsedSlotAndNotRemovalMode(Slot endSlot) {
		return  !endSlot.isEmpty();
	}

	private boolean isMoveAndJumpPhaseAndEmptyDestination(Move move, Slot endSlot) {
		return move.isMove() &&
			   players.isCurrentPlayersPhase(Player.JUMP_PHASE) &&
			   endSlot.isEmpty();
	}

	private boolean isMoveOfOpponentsStoneOrEmptySlot(Slot startSlot) {
		return players.isCurrentPlayersPhase(Player.MOVE_PHASE) && 
			   startSlot.getStatus() != players.getCurrentPlayersSlotStatus();
	}

	private boolean isMoveAndMovePhaseAndDestinationEmptyNeighbour(Move move,
			Slot startSlot, Slot endSlot) {
		if (players.isCurrentPlayersPhaseNot(Player.MOVE_PHASE))
			return false;
		
		for (Slot neighbour : gameboard.getNeighbours(startSlot)) {
			if (neighbour != null &&
				neighbour.hashCode() == endSlot.hashCode() &&
				neighbour.isEmpty()) {
				return true;
			}
		}
		
		
		return false;
	}
	
	private boolean hasTopAndBottomStatusOf(Slot[] closeSlotNeighbours, Slot.Status status) {
		return closeSlotNeighbours[0].getStatus() == status &&
			   closeSlotNeighbours[2].getStatus() == status;
	}

	private boolean hasLeftAndRightStatusOf(Slot[] closeSlotNeighbours, Slot.Status status) {
		return closeSlotNeighbours[1].getStatus() == status &&
			   closeSlotNeighbours[3].getStatus() == status;
	}
	
}
