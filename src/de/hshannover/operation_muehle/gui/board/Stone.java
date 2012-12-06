package de.hshannover.operation_muehle.gui.board;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import de.hshannover.operation_muehle.gui.Theme;
import de.hshannover.operation_muehle.gui.board.TextureUtils;
import de.hshannover.operation_muehle.logic.Player;
import de.hshannover.operation_muehle.logic.Slot;

/** GUI helper class representing the position and color of a Stone
 * 
 * @author Jonne Haß
 *
 */
public class Stone extends Point {
	/** The color of a stone
	 * 
	 * @author Jonne Haß
	 *
	 */
	public enum Color {
		WHITE,
		BLACK;

		public static Color fromSlotStatus(Slot.Status status) {
			switch (status) {
			case WHITE: return Color.WHITE;
			case BLACK: return Color.BLACK;
			default: return null;
			}
		}

		public static Color fromPlayerColor(Player.Color color) {
			switch(color) {
			case WHITE: return Color.WHITE;
			case BLACK: return Color.BLACK;
			default: return null;
			}
		}
	}
	
	/**
	 * The radius of a stone
	 */
	public static final int RADIUS = 20;
	private static final long serialVersionUID = 1L;
	
	private static BufferedImage blackStoneTexture;
	private static BufferedImage highlightedBlackStoneTexture;
	private static BufferedImage whiteStoneTexture;
	private static BufferedImage highlightedWhiteStoneTexture;
	
	private Stone.Color color;
	
	/** Copy constructor
	 * 
	 * @param stone
	 */
	public Stone(Stone stone) {
		super(stone.x, stone.y);
		this.color = stone.color;
	}
	
	/** Create a new stone at point with the color color.
	 * 
	 * @param point
	 * @param color
	 */
	public Stone(Point point, Color color) {
		super(point);
		this.color = color;
	}
	
	/** Create a new stone at point(x,y) with the color color.
	 * 
	 * @param x
	 * @param y
	 * @param color
	 */
	public Stone(int x, int y, Color color) {
		super(x, y);
		this.color = color;
	}
	
	/** Draw the stone on the given Graphics object
	 * 
	 * @param pen
	 * @param highlighted 
	 */
	public void draw(Graphics2D pen, boolean highlighted) {
		pen.drawImage(
				getTexture(highlighted),
				x-Stone.RADIUS,
				y-Stone.RADIUS,
				null);
	}

	private BufferedImage getTexture(boolean highlighted) {
		switch (color) {
		case WHITE:
			return getWhiteStoneTexture(highlighted);
		case BLACK:
			return getBlackStoneTexture(highlighted);
		default:
			return null;
		}
	}
	
	private BufferedImage getWhiteStoneTexture(boolean highlighted) {
		if (highlighted) {
			if (highlightedWhiteStoneTexture == null) {
				highlightedWhiteStoneTexture = generateTexture(Theme.HIGHLIGHTED_WHITE_STONE_TEXTURE);
			}
			return highlightedWhiteStoneTexture;
		} else {
			if (whiteStoneTexture == null) {
				whiteStoneTexture = generateTexture(Theme.WHITE_STONE_TEXTURE);
			}
			return whiteStoneTexture;
		}
	}
	
	private BufferedImage getBlackStoneTexture(boolean highlighted) {
		if (highlighted) {
			if (highlightedBlackStoneTexture == null) {
				highlightedBlackStoneTexture = generateTexture(Theme.HIGHLIGHTED_BLACK_STONE_TEXTURE );
			}
			return highlightedBlackStoneTexture;
		} else {
			if (blackStoneTexture == null) {
				blackStoneTexture = generateTexture(Theme.BLACK_STONE_TEXTURE);
			}
			return blackStoneTexture;
		}
	}

	private BufferedImage generateTexture(String texture) {
		return TextureUtils.makeOval(
			TextureUtils.load(texture),
			RADIUS*2, RADIUS*2
		);
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public String toString() {
		return color.toString();
	}
}