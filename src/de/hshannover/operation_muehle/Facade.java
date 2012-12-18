package de.hshannover.operation_muehle;

import java.io.IOException;
import java.util.HashMap;

import de.hshannover.operation_muehle.gui.board.Spot;
import de.hshannover.operation_muehle.logic.State;
import de.hshannover.operation_muehle.logic.InvalidMoveException;
import de.hshannover.operation_muehle.logic.Logger;
import de.hshannover.operation_muehle.logic.PlayerOptions;
import de.hshannover.operation_muehle.logic.Slot;
import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.operation_muehle.logic.ApplicationController;
import de.hshannover.operation_muehle.logic.IOOperation;
import de.hshannover.operation_muehle.utils.loader.StrategyLoader;
import de.hshannover.operation_muehle.utils.observer.IObserver;

/**
 * Facade for GUI-Interaction
 * @author Richard Pump
 */
public class Facade {
	private static Facade instance;
	private ApplicationController appController;
	private StrategyLoader strategyLoader;
	
	/**
	 * Simple Constructor.
	 */
	private Facade() {
		appController = new ApplicationController();
		strategyLoader = StrategyLoader.getDefaultInstanceOrMock();
	}
	
	/**
	 * Creates a Facade if not existent, otherwise returning
	 * the existing one.
	 * @return The Facade(Singleton)
	 */
	public static Facade getInstance() {
		if (instance == null) {instance = new Facade(); }
		return instance;
	}
	
	/**
	 * Starts a new Game with the given Options.
	 * @param gameOptions The Options.
	 * @see GUIController
	 */
	public void newGame(final HashMap<String,PlayerOptions> gameOptions) {
		appController.initializeNew(gameOptions);
	}
	
	public void addApplicationControllerObserver(IObserver observer) {
		appController.addObserver(observer);
	}
	
	/**
	 * Saves the Game at the given Path
	 * @param path The path, where the game is to be saved.
	 * @throws IOException 
	 * @see IOOperation
	 */
	public void saveGame(String path) throws IOException {
		State save = appController.getSaveState();
		IOOperation.saveGameInfo(path, save);
	}
	/**
	 * Loads the Game from the given Path
	 * @param path the path of the saved game.
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public void loadGame(String path) throws IOException, ClassNotFoundException {
		State save = IOOperation.loadGameInfo(path);
		appController.initializeSaved(save);
	}
	
	/**
	 * Aborts the current Game.
	 */
	public void abortGame() {
		//Threads for the Thread-God, Saves for the Savethrone!
		appController.endGame();
	}
	
	/**
	 * Gives the StrategyLoader
	 * @return The StrategyLoader
	 * @see StrategyLoader
	 */
	public StrategyLoader getStrategyLoader() {
		return strategyLoader;
	}
	
	/** Gives a Move to the ApplicationController
	 * 
	 * 	Either dst or src must be set, both cannot be null
	 * 
	 * @param src The spot to move from, if null a stone should be created at dst 
	 * @param dst The spot to move to, if null a stone should be removed from src
	 * @throws InvalidMoveException 
	 */
	public void giveMove(Spot src, Spot dst) throws InvalidMoveException {
		Slot srcSlot = null, dstSlot = null;
		
		if (src != null) {
			srcSlot = new Slot(src.getIntegerColumn(), src.getRow());
		}
		
		if (dst != null) {
			dstSlot = new Slot(dst.getIntegerColumn(), dst.getRow());
		}
		
		Move move = new Move(srcSlot, dstSlot);
		Logger.logDebugf("Generated move from user interaction: %s", move);
		appController.givePlayerMove(move);
	}

	/** Get the current GameState
	 * 
	 * @return
	 */
	public State getGameState() {
		return appController.getGameState();
	}
}
