package de.hshannover.operation_muehle.gui.board;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import de.hshannover.operation_muehle.gui.board.TextureUtils;

/** GUI helper class representing the position and color of a Stone
 * 
 * @author Jonne Haß
 *
 */
class Stone extends Point {
	/** The color of a stone
	 * 
	 * @author Jonne Haß
	 *
	 */
	enum Color {
		WHITE,
		BLACK
	}
	
	/**
	 * The radius of a stone
	 */
	public static final int RADIUS = 20;
	private static final long serialVersionUID = 1L;
	
	private static BufferedImage blackStoneTexture;
	private static BufferedImage whiteStoneTexture;
	
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
	 */
	public void draw(Graphics pen) {
		pen.drawImage(
				getTexture(),
				x-Stone.RADIUS,
				y-Stone.RADIUS,
				null);
	}

	private BufferedImage getTexture() {
		switch (color) {
		case WHITE:
			return getWhiteStoneTexture();
		case BLACK:
			return getBlackStoneTexture();
		default:
			return null;
		}
	}
	
	private BufferedImage getWhiteStoneTexture() {
		if (whiteStoneTexture == null) {
			whiteStoneTexture = TextureUtils.makeOval(
				TextureUtils.load("white-marble.jpg"),
					RADIUS*2, RADIUS*2
			);
		}
		
		return whiteStoneTexture;
	}
	
	private BufferedImage getBlackStoneTexture() {
		if (blackStoneTexture == null) {
			blackStoneTexture = TextureUtils.makeOval(
				TextureUtils.load("dark-wood.jpg"),
				RADIUS*2, RADIUS*2
			);
		}
		
		return blackStoneTexture;
	}
	
	@Override
	public String toString() {
		return color.toString();
	}
}