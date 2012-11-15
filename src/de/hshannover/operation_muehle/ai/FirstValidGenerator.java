package de.hshannover.operation_muehle.ai;

import java.util.HashMap;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;

public class FirstValidGenerator implements MoveGenerator {

	public FirstValidGenerator() {
	}

	public Move generateMove(AIBoard board) {
		Move result = null;

		HashMap<Integer, Status> myStones = board.getFieldsWith(Status.MYSTONE);

		for (int key : myStones.keySet()) {
			HashMap<Integer, Status> neighbours = board.getNeighbours(board
					.generateSlotByAddress(key));
			for (int neigh : neighbours.keySet()) {
				if (neighbours.get(neigh) == Status.EMPTY) {
					result = new G2Move(board.generateSlotByAddress(key),
							board.generateSlotByAddress(neigh));
					break;
				}
			}
			if (result != null)
				break;
		}

		return result;
	}

	public Move generateJump(AIBoard board) {
		Move result = null;

		HashMap<Integer, Status> myStones = board.getFieldsWith(Status.MYSTONE);

		HashMap<Integer, Status> emptyPlaces = board
				.getFieldsWith(Status.EMPTY);

		for (int key : myStones.keySet()) {
			for (int empt : emptyPlaces.keySet()) {
				result = new G2Move(board.generateSlotByAddress(key),
						board.generateSlotByAddress(empt));
				break;
			}
			if (result != null)
				break;
		}
		return result;
	}

	public Slot generatePlace(AIBoard board) {
		Slot result = null;

		HashMap<Integer, Status> emptyPlaces = board
				.getFieldsWith(Status.EMPTY);

		for (int key : emptyPlaces.keySet()) {
			result = board.generateSlotByAddress(key);
			break;
		}

		return result;
	}

	public Slot generateRemove(AIBoard board) {
		Slot result = null;

		HashMap<Integer, Status> opponentStones = board
				.getFieldsWith(Status.FOREIGNSTONE);

		if (opponentStones == null) {
			throw new IllegalArgumentException(
					"There are no stones from opponent to remove.");
		}

		for (int key : opponentStones.keySet()) {
			result = board.generateSlotByAddress(key);
			break;
		}

		return result;
	}

}
