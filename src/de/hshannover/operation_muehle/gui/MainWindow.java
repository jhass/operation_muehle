package de.hshannover.operation_muehle.gui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.hshannover.operation_muehle.gui.board.Board;
import de.hshannover.operation_muehle.logic.State;

import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/** Main window displaying the game and providing access to all other functions.
 * 
 * @author Jonne Haß
 *
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Runnable> toggleLogCallbacks = new ArrayList<Runnable>();
	private ArrayList<Runnable> newGameCallbacks = new ArrayList<Runnable>();
	private ArrayList<Runnable> closeWindowCallbacks = new ArrayList<Runnable>();
	private ArrayList<Runnable> loadGameCallbacks = new ArrayList<Runnable>();
	private ArrayList<Runnable> saveGameCallbacks = new ArrayList<Runnable>();
	private ArrayList<Runnable> toggleGameInfoCallbacks = new ArrayList<Runnable>();
	
	private final JButton btnNewGame = new JButton("New Game");
	private final JButton btnToggleLog = new JButton("Toggle Log");
	private final JButton btnLoadGame = new JButton("Load Game");
	private final JButton btnSaveGame = new JButton("Save Game");
	private final JButton btnToggleGameInfo = new JButton("Info");

	private Board board;
	private boolean saveableGame;
	
	public MainWindow() {
		setVisible(true);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		board = new Board();
		
		getContentPane().add(board);
		
		JPanel buttonContainer = new JPanel();
		getContentPane().add(buttonContainer, BorderLayout.SOUTH);
		buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		buttonContainer.add(btnToggleLog);
		buttonContainer.add(btnNewGame);
		buttonContainer.add(btnLoadGame);
		buttonContainer.add(btnSaveGame);
		buttonContainer.add(btnToggleGameInfo);
		
		setupListener();
		setTitle("Mühle");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		
		noGameMode();
	}
	
	/** Whether a game should be saveable
	 * 
	 * @param saveable
	 */
	public void setGameSaveable(boolean saveable) {
		saveableGame = saveable;
	}
	
	/** Set the window into no game mode
	 * 
	 */
	public void noGameMode() {
		board.setEnabled(false);
		board.setInfoText("Start a new game!");
		saveableGame = false;
		btnSaveGame.setEnabled(saveableGame);
	}
	
	/** Set the window into game mode
	 * 
	 */
	public void gameMode() {
		board.setEnabled(true);
		board.setInfoText(null);
		btnSaveGame.setEnabled(saveableGame);
	}
	
	private void setupListener() {
		setupButtonCallbacks(btnNewGame, newGameCallbacks);
		setupButtonCallbacks(btnToggleLog, toggleLogCallbacks);
		setupButtonCallbacks(btnLoadGame, loadGameCallbacks);
		setupButtonCallbacks(btnSaveGame, saveGameCallbacks);
		setupButtonCallbacks(btnToggleGameInfo, toggleGameInfoCallbacks);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				for (Runnable callback : closeWindowCallbacks) {
					callback.run();
				}
			}
		});
	}
	
	private void setupButtonCallbacks(JButton button,
		final ArrayList<Runnable> callbacks) {
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (Runnable callback : callbacks) {
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

	/** Add an item to the callback chain for when the gameInfowWindow should be toggled
	 * 
	 * @param callback
	 */
	public void addToggleGameInfoCallback(Runnable callback) {
		this.toggleGameInfoCallbacks.add(callback);
	}
	
	/** Add an item to the callback chain for when a new game is requested
	 * 
	 * @param callback
	 */
	public void addNewGameCallback(Runnable callback) {
		this.newGameCallbacks.add(callback);
	}
	
	/** Add an item to the callback chain for when the closing of the window is requested
	 * 
	 * @param callback
	 */
	public void addCloseWindowCallback(Runnable callback) {
		this.closeWindowCallbacks.add(callback);
	}
	
	/** Add an item to the callback chain for when the loading of a game is requested
	 * 
	 * @param callback
	 */
	public void addLoadGameCallback(Runnable callback) {
		this.loadGameCallbacks.add(callback);
	}
	
	
	/** Add an item to the callback chain for when saveing of the game is requested 
	 * 
	 * @param callback
	 */
	public void addSaveGameCallback(Runnable callback) {
		this.saveGameCallbacks.add(callback);
	}

	/** @see Board.addNewMoveCallback
	 * 
	 * @param moveCallback
	 */
	public void addNewMoveCallback(MoveCallback moveCallback) {
		board.addNewMoveCallback(moveCallback);
	}
	
	@Override
	public void repaint() {
		super.repaint();
		board.repaint();
	}

	/** Draws a Gameboard
	 * 
	 * @param gameboard
	 */
	public void update(State state) {
		board.setState(state);
	}

	/** Sets the info text
	 * 
	 * @param string
	 */
	public void setInfoText(String text) {
		board.setInfoText(text);
	}

	/** Sets the message text
	 * 
	 * @param determineCurrentMessage
	 */
	public void setMessageText(String text) {
		board.setMessageText(text);
	}
}
