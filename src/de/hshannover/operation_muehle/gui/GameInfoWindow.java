package de.hshannover.operation_muehle.gui;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.BorderLayout;

import javax.swing.JScrollPane;

/**GameInfoWindow shows the rules of the game
 * 
 * @author Julian Haack
 */
public class GameInfoWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTextPane textpane;
	private JScrollPane scrollPane;

	/**
	 * Create the frame.
	 */
	public GameInfoWindow() {
		setTitle("Game Info");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		
		textpane = new JTextPane();
		textpane.setText("Mill Rules\r\n\r\nYou win the board game if your opponent has only two stones left, or if he cannot move. There are three phases in the game (one set-up phase and two move phases). In every phase, you can capture if you make three in a row (horizontal or vertical), this is called a 'mill'. If you form a mill, you must capture an opposing stone of your choice (except when this stone is part of a mill and your opponent has other stones which are not part of a mill).\r\n\r\nMill is one of the oldest board games and known in Europe by different names: Nine Men Morris, Mühle, Molenspel and Merrelles.\r\n\r\nPhase 1: set-up\r\nIn the first part of the game, the players each place a stone in turns on an intersection of a horizontal and a vertical line (denoted by a dot). This phase ends when every player has placed his 9 stones.\r\n\r\nPhase 2: moving\r\nWhen both players placed all their stones on the bord, the second phase starts. When it is your turn, you must move one of your stones to an adjacent free place (along a line). If you form a mill, you must remove an opposing stone from the board.\r\n\r\nPhase 3: moving (when one player has only 3 stones left)\r\nThe third phase starts when a player has only 3 stones left. That player is allowed to move his stones to any place on the board instead of just an adjacent place. When both players have 3 stones left, both of them may do so.\r\n\r\nEnd of the board game\r\nYou win the game by either capturing 7 opposing stones (so the opponent has just 2 stones left) or if your opponent has no legal move.\r\n\r\nStrategy\r\nThere are four places on the board with 4 neighbouring places (in the middle ring), so those are important and you should get at least two of them. On move 4 or 5, White can always make a mill, but Black places the last stone, which should compensate for that. Tactics are more important in the middlegame. When you form a mill, move one of the millstones away and make the mill again the next turn, will give you one capture each 2 turns. Even better is a construction where you move a stone out of the mill directly into a new mill. This stone will capture every turn.");
		textpane.setEditable(false);
		scrollPane.setViewportView(textpane);
		getContentPane().add(scrollPane);
		setVisible(false);
		
	}
	
	/**
	 * Toggle visibility of the window
	 */
	public void toggleVisibility() {
		setVisible(!isVisible());
	}

}
