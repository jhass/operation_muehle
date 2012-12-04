package de.hshannover.operation_muehle.gui.board;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import de.hshannover.operation_muehle.gui.MoveCallback;
import de.hshannover.operation_muehle.gui.Theme;
import de.hshannover.operation_muehle.gui.board.TextureUtils;
import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Logger;
import de.hshannover.operation_muehle.logic.Player;
import de.hshannover.operation_muehle.logic.PlayerManager;
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
	private PlayerManager players;
	private String infoText;
	private String messageText;
	
	public Board() {
		super();
		
		boardTexture = TextureUtils.load(Theme.BOARD_TEXTURE);
		
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
				Logger.logDebugf("Clicked %s", spot);
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
		Logger.logDebugf("Clicked nothing at %s", clicked);
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
				if (!spot.hasStone() && spot.isCovering(destination)) {
					Logger.logDebugf("User moved %s from %s to %s",
									 draggedStone,draggedStoneSrc, spot);
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
	
	/** Updates the current Gameboard
	 * 
	 * @param gameboard
	 */
	public void setGameboard(Gameboard gameboard) {
		this.currentGameboard = gameboard;
		repopulateStones();
	}
	
	/** Updates the current PlayerManager
	 * 
	 * @param players
	 */
	public void setPlayerInfo(PlayerManager players) {
		this.players = players;
	}
	
	/** Sets an info text that should be presented to the user
	 * 
	 */
	public synchronized void setInfoText(String text) {
		this.infoText = text;
	}
	
	public synchronized void setMessageText(String text) {
		this.messageText = text;
	}

	/** Callback that's run when any kind of new move is generated, no matter if
	 *   it's valid currently or not. 
	 * 
	 * @param moveCallback
	 */
	public void addNewMoveCallback(MoveCallback moveCallback) {
		this.newMoveCallbacks .add(moveCallback);
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
			verticalSpacing = innerSpacing;
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
		
		drawWidgets(pen);
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
		pen.setColor(Theme.BACKGROUND_COLOR);
		
		pen.fillRect(0, 0, width, height);
	}
	
	private void drawBoard(Graphics2D pen) {
		pen.setColor(Theme.BOARD_COLOR);
		
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
		
		pen.setColor(Theme.SPOT_HINT);
		pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (Spot spot : spots) {
			pen.fill(new Ellipse2D.Float(spot.getPosition().x-6,
										 spot.getPosition().y-6, 12, 12));
		}
		pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
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
		pen.setColor(Theme.SHADE_COLOR);
		pen.fillRect(0, 0, width, height);
	}
	
	private void drawWidgets(Graphics2D pen) {
		drawPlayerCards(pen);
		drawMessageBox(pen);
		drawInfoTextBox(pen);
	}
	
	private void drawPlayerCards(Graphics2D pen) {
		if (players == null || !isEnabled()) {
			return;
		}
		Player white = players.getWhitePlayer();
		Player black = players.getBlackPlayer();
		Color whiteBG, blackBG;
		
		if (players.isCurrentPlayer(white)) {
			whiteBG = Theme.PLAYER_INFO_WHITE_ACTIVE_BG_COLOR;
			blackBG = Theme.PLAYER_INFO_BLACK_INACTIVE_BG_COLOR;;
		} else {
			whiteBG = Theme.PLAYER_INFO_WHITE_INACTIVE_BG_COLOR;
			blackBG = Theme.PLAYER_INFO_BLACK_ACTIVE_BG_COLOR;
		}
		
		drawPlayerInfo(
			pen,
			whiteBG,
			Theme.PLAYER_INFO_WHITE_TEXT_COLOR,
			140,
			white,
			players.isCurrentPlayer(white)
		); //FIXME: special value
		
		drawPlayerInfo(
			pen,
			blackBG,
			Theme.PLAYER_INFO_BLACK_TEXT_COLOR,
			width,
			black,
			players.isCurrentPlayer(black)
		);
	}
	
	private void drawPlayerInfo(Graphics2D pen, Color background, Color foreground,
								int leftCornerBase, Player player, boolean active) {
		pen.setColor(background);
		pen.fillRect(leftCornerBase-130, height-130, 120, 100);
		pen.setColor(foreground);
		
		pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font base = pen.getFont();
		Font header = Theme.getPlayerInfoHeader(base, active);
		
		int offset = 110;
		pen.setFont(header);
		pen.drawString(player.getDisplayName(), leftCornerBase-120, height-offset);
		offset -= 20;
		pen.setFont(base);
		if (player.getAvailableStones() > 0) {
			pen.drawString("To place: "+player.getAvailableStones(), leftCornerBase-120, height-offset);
			offset -= 20;
		}
		pen.drawString("On board: "+player.getStones(), leftCornerBase-120, height-offset);
		offset -= 20;
		pen.drawString("Moves: "+player.getNumberOfMoves(), leftCornerBase-120, height-offset);
		offset -= 20;
		pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	private synchronized void drawMessageBox(Graphics2D pen) {
		if (!isEnabled() || messageText == null || messageText.isEmpty()) {
			return;
		}
		
		
		Font base = pen.getFont();
		pen.setFont(Theme.getMessageBoxFont(base));
		drawStringWithBox(
			pen,
			messageText,
			width/2,
			verticalSpacing/2,
			Theme.MESSAGE_BOX_BACKGROUND_COLOR,
			Theme.MESSAGE_BOX_TEXT_COLOR
		);
		
		pen.setFont(base);
	}
	
	private synchronized void drawInfoTextBox(Graphics2D pen) {
		if (infoText == null || infoText.isEmpty()) {
			return;
		}
		
		Font base = pen.getFont();
		pen.setFont(Theme.getInfoBoxFont(base));
		drawStringWithBox(
			pen,
			infoText,
			width/2,
			height/2,
			Theme.INFO_BOX_BACKGROUND_COLOR,
			Theme.INFO_BOX_TEXT_COLOR
		);
		pen.setFont(base);
	}

	private void drawStringWithBox(Graphics2D pen, String string, int centerx,
								   int centery, Color background, Color text) {
		
		Rectangle2D dimensions = pen.getFontMetrics().getStringBounds(string, pen);
		int halfStringWidth = (int) (dimensions.getWidth()/2);
		int halfStringHeight = (int) (dimensions.getHeight()/2);
		
		pen.setColor(background);
		pen.fillRect(
			centerx-halfStringWidth-10,
			centery-halfStringHeight-30,
			halfStringWidth*2+20,
			halfStringHeight*2+30
		);
		
		pen.setColor(text);
		pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		pen.drawString(
				string,
				(int) (centerx-halfStringWidth),
				(int) (centery-halfStringHeight)
				);
		pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
	}
}
