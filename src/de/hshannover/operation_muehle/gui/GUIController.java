package de.hshannover.operation_muehle.gui;

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
				//logWindow.toggle();
				
			}
		});
		
		this.mainWindow.addNewGameCallback(new Runnable() {
			@Override
			public void run() {
				newGame();
			}
		});
	}

	@Override
	public void updateObservable() {
		// TODO Auto-generated method stub
		
	}
	
	public void newGame() {
		
	}
	
	public void loadGame() {
		
	}
	
	public void saveGame() {
		
	}
}
