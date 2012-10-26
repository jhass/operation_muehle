package de.hshannover.operation_muehle.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;


/** Provides the canvas to display the current Gameboard
 * 
 * @author mrzyx
 *
 */
public class Board extends Canvas {
	private static final long serialVersionUID = 1L;

	@Override
	public void paint(Graphics pen) {		
		int width = getWidth();
		int height = getHeight();
		int horizontalSpacing = 50;
		int verticalSpacing = 50;
		
		pen.setColor(Color.BLACK);
		
		pen.drawRect(horizontalSpacing, verticalSpacing,
					 width-2*horizontalSpacing, 
					 height-2*verticalSpacing);
		
		pen.drawRect(2*horizontalSpacing, 2*verticalSpacing,
				     width-4*horizontalSpacing,
				     height-4*verticalSpacing);
		
		
		pen.drawRect(4*horizontalSpacing, 4*verticalSpacing,
				     width-8*horizontalSpacing,
				     height-8*verticalSpacing);
	}
}
