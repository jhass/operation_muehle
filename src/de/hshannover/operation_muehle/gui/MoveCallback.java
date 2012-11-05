package de.hshannover.operation_muehle.gui;

import de.hshannover.operation_muehle.gui.board.Spot;
import de.hshannover.operation_muehle.gui.board.Stone;

/** Callback representing a possible Move
 * 
 * @author Jonne Haß
 *
 */
public interface MoveCallback {
	/** Implementers should handle three cases:
	 * 	1) src and dst are set: move Stone from src to dst
	 *  2) src is null, dst is set: Stone should be created
	 *  3) src is set, dst is null: Stone should be removed
	 * 
	 * Returning false will halt the callback chain.
	 * 
	 * @param src
	 * @param dst
	 * @param color
	 * @return Whether the move is a valid one
	 */
	public boolean process(Spot src, Spot dst, Stone.Color color);
}
