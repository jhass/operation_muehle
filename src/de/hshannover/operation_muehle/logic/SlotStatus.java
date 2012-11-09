package de.hshannover.operation_muehle.logic;

/** Enum indicating the status of a Slot
 * 
 * @author Jonne Haß
 *
 */
public enum SlotStatus {
	/** Slot is used with a white stone
	 */
	WHITE {
		public SlotStatus getOtherPlayer() {
			return BLACK;
		}
	},
	
	/** Slot is used with a black stone
	 */
	BLACK {
		public SlotStatus getOtherPlayer() {
			return WHITE;
		}
	},
	 
	/** Slot can take a stone
	 */
	EMPTY {
		public SlotStatus getOtherPlayer() {
			return null;
		}
	};
	
	abstract SlotStatus getOtherPlayer();
}
