package de.hshannover.operation_muehle.advancedAI;

import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Slot;

public class AIState {
	public static final int PLACE_PHASE = 1;
	public static final int MOVE_PHASE  = 2;
	public static final int JUMP_PHASE  = 3;
	
	public Gameboard board;
	public int phase;
	public Color color;
	public boolean removalRequested;
	public int availableStones;
	public int stones;
	
	public AIState() {
		phase = AIState.PLACE_PHASE;
		board = new Gameboard();
		availableStones = 9;
		stones = 0;
	}
	
	
	public synchronized void increaseStones() {
		stones++;
		availableStones--;
		if (availableStones == 0) {
			phase = MOVE_PHASE;
		}
	}
	
	/**
	 * Methode zum reduzieren der Spielsteine um 1 (wenn ein Stein vom Feld entfernt wurde)
	 */
	public synchronized void decreaseNumberOfStones() {
		stones--;
		// If white makes a mill with its first 3 stones,
		// black would enter jump phase from place phase,
		// so do not allow moving from place to jump phase
		if (stones <= 3 && phase != PLACE_PHASE) {
			phase = JUMP_PHASE;
		}
	}

	public enum Color {
		WHITE {
			public Color getOpponent() {
				return BLACK;
			}
			
			public Slot.Status getSlotStatus() {
				return Slot.Status.WHITE;
			}
		},
		BLACK {
			public Color getOpponent() {
				return WHITE;
			}
			
			public Slot.Status getSlotStatus() {
				return Slot.Status.BLACK;
			}

			
		};
		
		abstract public Color getOpponent();
		abstract public Slot.Status getSlotStatus();
		public Slot.Status getOpponentSlotStatus() {
			return getOpponent().getSlotStatus();
		}
		public boolean hasStoneOn(Slot slot) {
			return slot.getStatus() == getSlotStatus();
		}
	}
}
