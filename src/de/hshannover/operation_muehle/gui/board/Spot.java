package de.hshannover.operation_muehle.gui.board;

import java.awt.Graphics2D;
import java.awt.Point;

import de.hshannover.operation_muehle.logic.State;
import de.hshannover.operation_muehle.logic.Player;


/** GUI helper class representing a Spot aka Slot, that is a location that has a
 *  coordinate on the Gameboard and can hold a stone.
 * 
 * @author Jonne Haß
 *
 */
public class Spot {
	private Stone stone;
	private Point position;
	private char column;
	private int row;
	private boolean highlighted;
	
	/** Create a new spot at the graphical position position and with the
	 *  coordinates (column,row). 
	 * 
	 * @param position
	 * @param column
	 * @param row
	 */
	public Spot(Point position, char column, int row) {
		this.position = position;
		this.column = column;
		this.row = row;
	}
	
	/** Return the Stone this Spot is currently holding or null if there's none
	 * 
	 * @return
	 */
	public Stone getStone() {
		return stone;
	}
	
	/** Get the spots column
	 * 
	 * @return
	 */
	public char getColumn() {
		return this.column;
	}
	
	/** Get the spots column as integer
	 * 
	 * @return
	 */
	public int getIntegerColumn() {
		return this.column-64;
	}
	
	/** Get the spots row
	 * 
	 * @return
	 */
	public int getRow() {
		return this.row;
	}

	/** Set the stone this Spot should hold. 
	 * 
	 * If the Spot currently holds no stone and stone is not null set it.
	 * If the Spot currently holds no stone and stone is null no action is taken.
	 * If the Spot currently holds a stone and stone is not null no action is tkanen.
	 * If the Spot currently holds a stone and stone is null the stone is removed.
	 * 
	 * @param stone
	 */
	public void setStone(Stone stone) {
		if (stone == null || !hasStone()) {
			this.stone = stone;
		}
		
		if (this.stone != null) {
			this.stone.setLocation(position);
		}
	}
	
	/** Whether this Spot holds a stone currently.
	 * 
	 * @return
	 */
	public boolean hasStone() {
		return getStone() != null;
	}

	public boolean hasStoneOfColor(Stone.Color color) {
		return hasStone() && stone.getColor() == color;
	}

	/** Get the graphical position of this Spot.
	 * 
	 * @return
	 */
	public Point getPosition() {
		return position;
	}

	
	/** Whether the given graphical point is inside this Spot.
	 * 
	 * The radius if a spot is determined by the radius of a stone. //TODO: maybe make larger
	 * 
	 * @param point
	 * @return
	 */
	public boolean isCovering(Point point) {
		return point.x > position.x-Stone.RADIUS &&
				point.x < position.x+Stone.RADIUS &&
				point.y > position.y-Stone.RADIUS &&
				point.y < position.y+Stone.RADIUS;
	}

	public void determineHighlighted(State currentState, Point point) {
		Player.Color targetColor;
		if (currentState.inRemovalPhase) {
			targetColor = currentState.players.getOpponent().getColor();
		} else {
			targetColor = currentState.players.getCurrent().getColor();
		}
		this.highlighted =
			(currentState.players.getCurrent().getPhase() == Player.MOVE_PHASE ||
			 currentState.inRemovalPhase) &&
			hasStoneOfColor(
				Stone.Color.fromPlayerColor(targetColor)
			) &&
			isCovering(point); //TODO: exclude mills
	}

	/** Draw the Spot on the given Graphics object.
	 * 
	 * If the Spot is holding no stone no action is taken.
	 * 
	 * @param pen
	 */
	public void draw(Graphics2D pen) {
		if (getStone() == null) {
			return;
		}
		
		getStone().draw(pen, highlighted);
	}
	
	@Override
	public String toString() {
		String repr = column+""+row;
		
		if (stone == null) {
			repr += " (empty)";
		} else {
			repr += " "+stone;
		}
		
		return repr;
	}
}