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
				new Thread(new Runnable() {
					@Override
					public void run() {
						newGame();
					}
				}).start();
			}
		});
		
		this.mainWindow.addLoadGameCallback(new Runnable() {
			@Override
			public void run() {
				new Thread(new Runnable() {
					@Override
					public void run() {
						loadGame();
					}
				}).start();
			}
		});
		
		this.mainWindow.addSaveGameCallback(new Runnable() {
			@Override
			public void run() {
				new Thread(new Runnable() {
					@Override
					public void run() {
						saveGame();
					}
				}).start();
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
		HashMap<String,HashMap<String,String>> gameOptions = NewGameDialog.getGameOptions();
		
		if (gameOptions != null) {
			Facade.getInstance().newGame(gameOptions);
		}
	}
	
	
	/** Load a game
	 * 
	 */
	public void loadGame() {
		System.out.println("load to: "+LoadDialog.getPath());
	}
	
	
	/** Save the current game
	 * 
	 */
	public void saveGame() {
		System.out.println("save to: "+SaveDialog.getPath());
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
