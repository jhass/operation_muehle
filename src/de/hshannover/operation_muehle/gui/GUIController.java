package de.hshannover.operation_muehle.gui;

import java.util.HashMap;

import de.hshannover.inform.muehle.strategy.Slot;
import de.hshannover.operation_muehle.Facade;
import de.hshannover.operation_muehle.logic.Player;
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
	private final LogWindow logWindow;
	
	public GUIController() {
		this.mainWindow = new MainWindow();
		this.logWindow = new LogWindow();
		
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
	}

	@Override
	public void updateObservable() {
		// TODO Auto-generated method stub
		
	}
	
	
	/** Initialize a new game
	 * 
	 */
	public void newGame() {
		HashMap<String,PlayerOptions> gameOptions;
		gameOptions = NewGameDialog.getGameOptions(this.mainWindow);
		
		if (gameOptions != null) {
			//Facade.getInstance().newGame(gameOptions); // TODO: find out why this blocks us
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
		this.logWindow.toggleVisibility();
	}
	
	/** Obtain a slot from the user
	 * 
	 * @param player
	 * @return
	 */
	public Slot selectSlot(Player player) {
		return null;
	}
	
//	/** ??
//	 * 
//	 * @param gamestate
//	 */
//	public void evaluteGameState(Gamestate gamestate) {
//		// whatever happens here
//	}
	
	public void close() {
		System.exit(0); // -> Facade?
	}
}
