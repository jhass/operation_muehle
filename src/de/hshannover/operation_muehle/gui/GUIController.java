package de.hshannover.operation_muehle.gui;

import java.util.HashMap;

import de.hshannover.inform.muehle.strategy.Slot;
import de.hshannover.operation_muehle.Facade;
import de.hshannover.operation_muehle.logic.Player;
import de.hshannover.operation_muehle.utils.observer.IObserver;

/** Flow controlling class and main entry point for the GUI layer
 * 
 * @author mrzyx
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
		
		this.mainWindow.addNewGameCallback(new Runnable() {
			@Override
			public void run() {
				newGame();
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
		HashMap<String,String> gameOptions = new HashMap<String,String>();
		Facade.getInstance().newGame(gameOptions);
	}
	
	
	/** Load a game
	 * 
	 */
	public void loadGame() {
		Facade.getInstance().loadGame("foobar");
	}
	
	
	/** Save the current game
	 * 
	 */
	public void saveGame() {
		Facade.getInstance().saveGame("foobar");
	}
	
	/** Display/hide log depending on the current state.
	 * 
	 */
	public void toggleLog() {
		
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
