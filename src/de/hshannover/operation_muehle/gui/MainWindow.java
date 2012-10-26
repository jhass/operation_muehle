package de.hshannover.operation_muehle.gui;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/** Main window displaying the game and providing access to all other functions.
 * 
 * @author Jonne Haß
 *
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Runnable> toggleLogCallbacks = new ArrayList<Runnable>();
	private ArrayList<Runnable> newGameCallbacks = new ArrayList<Runnable>();
	
	private final JButton btnNewGame = new JButton("New Game");
	private final JButton btnToggleLog = new JButton("Toggle Log");
	
	public MainWindow() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		setVisible(true);
		Board board = new Board();
		getContentPane().add(board);
		
		JPanel buttonContainer = new JPanel();
		getContentPane().add(buttonContainer);
		buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
		
		buttonContainer.add(btnToggleLog);
		buttonContainer.add(btnNewGame);
		
		setupButtonCallbacks();
		
		setTitle("Mühle");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);
	}
	
	private void setupButtonCallbacks() {
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Runnable callback : newGameCallbacks) {
					callback.run();
				}
			}
		});
		
		btnToggleLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (Runnable callback : toggleLogCallbacks) {
					callback.run();
				}
			}
		});
	}
	
	
	/** Add an item to the callback chain for when the log should be toggled
	 * 
	 * @param callback
	 */
	public void addToggleLogCallback(Runnable callback) {
		this.toggleLogCallbacks.add(callback);
	}
	
	
	/** Add an item to the callback chain for when a new game is requested
	 * 
	 * @param callback
	 */
	public void addNewGameCallback(Runnable callback) {
		this.newGameCallbacks.add(callback);
	}
}
