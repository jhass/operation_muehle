package de.hshannover.operation_muehle.gui;

import java.util.ArrayList;
import java.util.HashMap;

import de.hshannover.operation_muehle.Facade;
import de.hshannover.operation_muehle.gui.board.Spot;
import de.hshannover.operation_muehle.gui.board.Stone.Color;
import de.hshannover.operation_muehle.logic.GameState;
import de.hshannover.operation_muehle.logic.InvalidMoveException;
import de.hshannover.operation_muehle.logic.PlayerOptions;
import de.hshannover.operation_muehle.utils.PerformAsync;
import de.hshannover.operation_muehle.utils.observer.IObserver;

/** Flow controlling class and main entry point for the GUI layer
 * 
 * @author Jonne Haß
 *
 */
public class GUIController implements IObserver {
	private MainWindow mainWindow;
	private static LogWindow logWindow;
	
	public GUIController() {
		this.mainWindow = new MainWindow();
		logWindow = new LogWindow();
		
		this.mainWindow.addToggleLogCallback(new Runnable() {
			@Override
			public void run() {
				toggleLog();
				
			}
		});
		
		this.mainWindow.addNewGameCallback(new PerformAsync() {
			@Override
			public void task() {
				newGame();
			}
		});
		
		this.mainWindow.addLoadGameCallback(new PerformAsync() {
			@Override
			public void task() {
				loadGame();
			}
		});
		
		this.mainWindow.addSaveGameCallback(new PerformAsync() {
			@Override
			public void task() {
				saveGame();
			}
		});
		
		
		this.mainWindow.addCloseWindowCallback(new Runnable() {
			@Override
			public void run() {
				close();
			}
		});
		
		this.mainWindow.addNewMoveCallback(new MoveCallback() {
			@Override
			public boolean process(Spot src, Spot dst, Color color) {
				return newMove(src, dst, color);
			}
		});
		
		Facade.getInstance().addApplicationControllerObserver(this);
	}

	@Override
	public void updateObservable() {
		final GameState state = Facade.getInstance().getGameState();
		mainWindow.drawBoard(state.currentGB);
	}
	
	
	/** Initialize a new game
	 * 
	 */
	public void newGame() {
		HashMap<String,PlayerOptions> gameOptions;
		gameOptions = NewGameDialog.getGameOptions(this.mainWindow);
		
		if (gameOptions != null) {
			Facade.getInstance().newGame(gameOptions);
			mainWindow.gameMode();
		}
	}
	
	
	/** Load a game
	 * 
	 */
	public void loadGame() {
		String path = LoadDialog.getPath(this.mainWindow);
		if (path != null) {
			System.out.println("load to: "+path); //TODO: call facade
		}
	}
	
	
	/** Save the current game
	 * 
	 */
	public void saveGame() {
		String path = SaveDialog.getPath(this.mainWindow);
		if (path != null) {
			System.out.println("save to: "+path); // TODO: call facade
		}
	}
	
	/** Display/hide log depending on the current state.
	 * 
	 */
	public void toggleLog() {
		logWindow.toggleVisibility();
	}
		
	private boolean newMove(Spot src, Spot dst, Color color) {
		try {
			Facade.getInstance().giveMove(src, dst);
			return true;
		} catch (InvalidMoveException e) {
			return false;
		}
	}
	
	public void close() {
		Facade.getInstance().abortGame();
		System.exit(0); // TODO: -> Facade?
	}
	
	/**
	 * gives the current log to logWindow
	 */
	public static void doLog(ArrayList<String> logList) {
		logWindow.setLog(logList);
	}
}
