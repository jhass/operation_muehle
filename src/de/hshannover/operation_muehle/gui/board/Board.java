package de.hshannover.operation_muehle.gui.board;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import de.hshannover.operation_muehle.utils.TextureUtils;


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
	private BufferedImage boardTexture;
	private BufferedImage board;
	private Stone draggedStone;
	private Spot draggedStoneSrc;
	
	public Board() {
		super();
		
		boardTexture = TextureUtils.load("light-wood.jpg");
		
		addOfflineRenderBuffersInitializer();
		addInteractionListeners();
	}

	private void addOfflineRenderBuffersInitializer() {
		addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(HierarchyEvent event) {
				if (event.getChangeFlags() == HierarchyEvent.PARENT_CHANGED) {
					createBufferStrategy(2);
				}
			}
		});
	}

	private void addInteractionListeners() {
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent event) {
				Point clicked = new Point(event.getX(), event.getY());
				startDragFromPoint(clicked);
			}

			@Override
			public void mouseReleased(MouseEvent event) {
				Point released = new Point(event.getX(), event.getY());
				endDragToPoint(released);
			}

			@Override
			public void mouseClicked(MouseEvent event) {  // TODO: debug output, replace with placement request
				Point clicked = new Point(event.getX(), event.getY());
				for (Spot spot : spots) {
					if (spot.isCovering(clicked)) {
						System.out.printf("Clicked %s\n", spot);
						return;
					}
				}
				System.out.println("Nothing at "+clicked);
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent event) {
				updatedDraggedStone(event);
			}
		});
	}

	private void startDragFromPoint(Point source) {
		for (Spot spot : spots) {
			if (spot.hasStone() && spot.isCovering(source)) {
				draggedStone = spot.getStone();
				spot.setStone(null);
				draggedStoneSrc = spot;
				return;
			}
		}
	}

	private void endDragToPoint(Point destination) {
		if (draggedStone != null) {
			for (Spot spot : spots) {
				if (!spot.hasStone() && spot.isCovering(destination)) {
					spot.setStone(draggedStone);
					draggedStone = null;
					break;
				}
			}
			
			if (draggedStone != null) {
				draggedStoneSrc.setStone(draggedStone);
				draggedStone = null;
			}
			
			draggedStoneSrc = null;
			repaint();
		}
	}

	private void updatedDraggedStone(MouseEvent event) {
		if (draggedStone != null) {
			draggedStone.setLocation(event.getX(), event.getY());
			repaint();
		}
	}

	// We redraw everything anyway so no need to clear the whole canvas
	@Override
	public void update(Graphics pen) {
		paint(pen);
	}
	
	@Override
	public void paint(Graphics pen) {
		// Create offline rendering buffer
		BufferStrategy buffer = getBufferStrategy();
		pen = buffer.getDrawGraphics();
		
		draw(pen);
		
		// Display the offline rendering buffer and ensure a redraw
		pen.dispose();
		buffer.show();
		Toolkit.getDefaultToolkit().sync();
	}

	private void draw(Graphics pen) {
		if (dimensionChanged()) {
			width = getWidth();
			height = getHeight();
			spacing = (width+height)/20; // TODO: better algorithm, maybe separate border and inner spacing
		
			recreateSpots();
			resizeBoard();
		}
		
		drawBackground(pen);
		drawBoard(pen);
		drawSpots(pen);
		drawDraggedStone(pen);
	}

	private boolean dimensionChanged() {
		return width != getWidth() || height != getHeight();
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
		
		//TODO: remove me, draw Gameboard state
		boolean white = true;
		boolean skip = true;
		for (Spot spot : spots) {
			skip = !skip;
			if (skip) continue;
			spot.setStone(new Stone(spot.getPosition(), white ? Stone.Color.WHITE : Stone.Color.BLACK));
			white = !white;
		}
	}

	private void resizeBoard() {
		try {
			board = TextureUtils.makeRoundedCorner(
					TextureUtils.getScaledImage(boardTexture, width-spacing, height-spacing),
					30);
		} catch (IOException e) {}
	}

	private void drawBackground(Graphics pen) {
		pen.setColor(new Color(11, 106, 11));
		
		pen.fillRect(0, 0, width, height);
	}
	
	private void drawBoard(Graphics pen) {
		pen.drawImage(board, spacing/2, spacing/2, null);
		
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

	private void drawSpots(Graphics pen) {
		for (Spot spot : spots) {
			spot.draw(pen);
		}
	}

	private void drawDraggedStone(Graphics pen) {
		if (draggedStone != null) {
			draggedStone.draw(pen);
		}
	}
}