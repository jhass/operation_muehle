package de.hshannover.operation_muehle.gui.board;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import de.hshannover.operation_muehle.gui.MoveCallback;
import de.hshannover.operation_muehle.gui.board.TextureUtils;
import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Slot;


/** Provides the canvas to display the current Gameboard
 * 
 * @author Jonne Haß
 *
 */
public class Board extends Canvas {
	private static final long serialVersionUID = 1L;
	private int innerSpacing;

	private Spot spots[];
	private Gameboard currentGameboard;
	private int width;
	private int height;
	private BufferedImage boardTexture;
	private BufferedImage board;
	private Stone draggedStone;
	private Spot draggedStoneSrc;
	private ArrayList<MoveCallback> newMoveCallbacks = new ArrayList<MoveCallback>();
	private int horizontalSpacing;
	private int verticalSpacing;
	
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
			public void mouseClicked(MouseEvent event) {
				Point clicked = new Point(event.getX(), event.getY());
				pointClicked(clicked);
			}

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
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent event) {
				updatedDraggedStone(event);
			}
		});
	}

	private void pointClicked(Point clicked) {
		for (Spot spot : spots) {
			if (spot.isCovering(clicked)) {
				System.out.printf("Clicked %s\n", spot);
				for (MoveCallback callback : newMoveCallbacks) {
					if (spot.hasStone()) {
						if (!callback.process(spot, null, null)) {
							break;
						}
					} else {
						if (!callback.process(null, spot, null)) {
							break;
						}
					}
				}
				return;
			}
		}
		System.out.println("Nothing at "+clicked);
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
			spotLoop: for (Spot spot : spots) {
				// TODO: don't check if the spot has a stone, that's app logic
				if (!spot.hasStone() && spot.isCovering(destination)) {
					System.out.println("Moved "+draggedStone+" from "+draggedStoneSrc+" to "+spot);
					for (MoveCallback callback : newMoveCallbacks) {
						if (!callback.process(draggedStoneSrc, spot,
											  draggedStone.getColor())) {
							break spotLoop;
						}
					}
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
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		repaint();
	}
	
	/** Updates the current Gameboard and triggers a redraw
	 * 
	 * @param gameboard
	 */
	public void setGameboard(Gameboard gameboard) {
		this.currentGameboard = gameboard;
		repopulateStones();
		repaint();
	}

	private void repopulateStones() {
		if (currentGameboard == null) {
			return;
		}
		
		for (Slot slot : currentGameboard) {
			for (Spot spot : spots) {
				if (spot.getRow() == slot.getRow() &&
					spot.getColumn() == slot.getColumn()) {
					if (slot.getStatus() == Slot.Status.EMPTY) {
						spot.setStone(null);
					} else {
						spot.setStone(new Stone(
							spot.getPosition(),
							Stone.Color.fromSlotStatus(slot.getStatus())
						));
					}
				}
			}
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
		Graphics2D enhancedPen = (Graphics2D) buffer.getDrawGraphics();
		
		draw(enhancedPen);
		
		// Display the offline rendering buffer and ensure a redraw
		enhancedPen.dispose();
		buffer.show();
		Toolkit.getDefaultToolkit().sync();
	}

	private void draw(Graphics2D pen) {
		if (dimensionChanged()) {
			width = getWidth();
			height = getHeight();
			innerSpacing = (width+height)/25;
			verticalSpacing = innerSpacing/3;
			horizontalSpacing = 150; //FIXME: special value
		
			recreateSpots();
			recreateBoard();
		}
		
		redrawBoard(pen);
		drawSpots(pen);
		drawDraggedStone(pen);
		if (!isEnabled()) {
			drawShade(pen);
		}
	}

	private boolean dimensionChanged() {
		return width != getWidth() || height != getHeight();
	}

	private void recreateSpots() {
		spots = new Spot[24];
		int index = 0;
		
		for (int row = 1; row <= 3; row++) {
			spots[index++] = new Spot( // A1, B2, C3
				new Point(
					horizontalSpacing+row*innerSpacing,
					height-verticalSpacing-row*innerSpacing
				),
				(char) (64+row),
				row
			);
			spots[index++] = new Spot( // A4, B4, C4 
				new Point(
					horizontalSpacing+row*innerSpacing,
					height/2
				),
				(char) (64+row),
				4
			);
			spots[index++] = new Spot( // A7, B6, C5
				new Point(
					horizontalSpacing+row*innerSpacing,
					verticalSpacing+row*innerSpacing
				),
				(char) (64+row),
				8-row
			);
			
			spots[index++] = new Spot( // D1, D2, D3
				new Point(
					width/2,
					height-verticalSpacing-row*innerSpacing
				),
				'D',
				row
			);
			spots[index++] = new Spot( // D7, D6, D5
				new Point(
					width/2,
					verticalSpacing+row*innerSpacing
				),
				'D',
				8-row
			);
			
			spots[index++] = new Spot( // G1, F2, E3
				new Point(
					width-horizontalSpacing-row*innerSpacing,
					height-verticalSpacing-row*innerSpacing
				),
				(char) (72-row),
				row
			);
			spots[index++] = new Spot( // E4, F5, G4
				new Point(
					width-horizontalSpacing-row*innerSpacing,
					height/2
				),
				(char) (72-row),
				4
			);
			spots[index++] = new Spot( // E5, F6, G7
				new Point(
					width-horizontalSpacing-row*innerSpacing,
					verticalSpacing+row*innerSpacing
				),
				(char) (72-row),
				8-row
			);
		}
		
		repopulateStones();
	}

	private void recreateBoard() {
		Graphics2D pen;
		try {
			board = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			pen = board.createGraphics();
			drawBackground(pen);
			pen.drawImage(
				TextureUtils.makeRoundedCorner(
					TextureUtils.getScaledImage(
						boardTexture,
						width-horizontalSpacing*2,
						height-verticalSpacing*2
					),
					30
				),
				horizontalSpacing,
				verticalSpacing,
				null
			);
			drawBoard(pen);
		} catch (IOException e) {}
	}

	private void drawBackground(Graphics pen) {
		pen.setColor(new Color(11, 106, 11));
		
		pen.fillRect(0, 0, width, height);
	}
	
	private void drawBoard(Graphics2D pen) {
		pen.setColor(Color.BLACK);
		
		pen.drawRect(horizontalSpacing+innerSpacing, verticalSpacing+innerSpacing,
					 width-2*horizontalSpacing-2*innerSpacing, 
					 height-2*verticalSpacing-2*innerSpacing);
		
		pen.drawRect(horizontalSpacing+2*innerSpacing, verticalSpacing+2*innerSpacing,
				     width-2*horizontalSpacing-4*innerSpacing,
				     height-2*verticalSpacing-4*innerSpacing);
		
		
		pen.drawRect(horizontalSpacing+3*innerSpacing, verticalSpacing+3*innerSpacing,
				     width-2*horizontalSpacing-6*innerSpacing,
				     height-2*verticalSpacing-6*innerSpacing);
		
		pen.drawLine(horizontalSpacing+innerSpacing, height/2,
					 horizontalSpacing+3*innerSpacing, height/2);
		pen.drawLine(width-horizontalSpacing-3*innerSpacing, height/2,
					 width-horizontalSpacing-innerSpacing, height/2);
		pen.drawLine(width/2, verticalSpacing+innerSpacing,
					 width/2, verticalSpacing+3*innerSpacing);
		pen.drawLine(width/2, height-verticalSpacing-3*innerSpacing,
					 width/2, height-verticalSpacing-innerSpacing);
		
		pen.setColor(new Color(0xEE222222, true));
		pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Spot spot : spots) {
			pen.fill(new Ellipse2D.Float(spot.getPosition().x-6,
										 spot.getPosition().y-6, 12, 12));
		}
	}

	private void redrawBoard(Graphics pen) {
		pen.drawImage(board, 0, 0, null);
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

	private void drawShade(Graphics pen) {
		pen.setColor(new Color(0xDD222222, true));
		pen.fillRect(0, 0, width, height);
	}

	/** Callback that's run when any kind of new move is generated, no matter if
	 *   it's valid currently or not. 
	 * 
	 * @param moveCallback
	 */
	public void addNewMoveCallback(MoveCallback moveCallback) {
		this.newMoveCallbacks .add(moveCallback);
	}
}