package de.hshannover.operation_muehle.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/** Provides the canvas to display the current Gameboard
 * 
 * @author Jonne Haß
 *
 */
public class Board extends Canvas {
	private static final long serialVersionUID = 1L;
	private int spacing;

	private Spot spots[];
	private int width;
	private int height;
	
	public Board() {
		super();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Point clicked = new Point(event.getX(), event.getY());
				for (Spot spot : spots) {
					if (clicked.x > spot.position.x-15 &&
						clicked.x < spot.position.x+15 &&
						clicked.y > spot.position.y-15 &&
						clicked.y < spot.position.y+15) {
						System.out.printf("Clicked %s%d\n", spot.column, spot.row);
						return;
					}
				}
				System.out.println("Nothing at "+clicked);
			}
		});
	}

	@Override
	public void paint(Graphics pen) {
		width = getWidth();
		height = getHeight();
		spacing = (width+height)/20; // TODO: better algorithm, maybe separate border and inner spacing
		
		recreateSpots();
		
		drawBackground(pen);
		drawBoard(pen);
		drawStones(pen);
	}

	private void recreateSpots() {
		spots = new Spot[24];
		int index = 0;
		
		for (int row = 1; row <= 3; row++) {
			spots[index++] = new Spot( // A1, B2, C3
				new Point(row*spacing, height-row*spacing),
				(char) (64+row), row);
			spots[index++] = new Spot( // A4, B4, C4 
				new Point(row*spacing, height/2), (char) (64+row), 4);
			spots[index++] = new Spot( // A7, B6, C5
				new Point(row*spacing, row*spacing),
				(char) (64+row), 8-row);
			
			spots[index++] = new Spot( // D1, D2, D3
				new Point(width/2, height-row*spacing), 'D', row);
			spots[index++] = new Spot( // D7, D6, D5
				new Point(width/2, row*spacing), 'D', 8-row);
			
			spots[index++] = new Spot( // G1, F2, E3
				new Point(width-row*spacing, height-row*spacing),
				(char) (72-row), row);
			spots[index++] = new Spot( // E4, F5, G4
				new Point(width-row*spacing, height/2), (char) (72-row), 4);
			spots[index++] = new Spot( // E5, F6, G7
				new Point(width-row*spacing, row*spacing),
				(char) (72-row), 8-row);
		}
	}

	private void drawBackground(Graphics pen) {
		pen.setColor(Color.DARK_GRAY);
		
		pen.fillRect(0, 0, width, height);
	}

	private void drawBoard(Graphics pen) {
		pen.setColor(Color.BLACK);
		
		pen.drawRect(spacing, spacing,
					 width-2*spacing, 
					 height-2*spacing);
		
		pen.drawRect(2*spacing, 2*spacing,
				     width-4*spacing,
				     height-4*spacing);
		
		
		pen.drawRect(3*spacing, 3*spacing,
				     width-6*spacing,
				     height-6*spacing);
		
		pen.drawLine(spacing, height/2, 3*spacing, height/2);
		pen.drawLine(width-3*spacing, height/2, width-spacing, height/2);
		pen.drawLine(width/2, spacing, width/2, 3*spacing);
		pen.drawLine(width/2, height-3*spacing, width/2, height-spacing);
	}

	private void drawStones(Graphics pen) {
		pen.setColor(Color.WHITE);
		
		
		for (Spot spot : spots) {
			pen.fillOval(spot.position.x-15, spot.position.y-15, 30, 30);
		}
	}
}

class Spot {
	Point position;
	char column;
	int row;
	
	public Spot(Point position, char column, int row) {
		this.position = position;
		this.column = column;
		this.row = row;
	}
}
