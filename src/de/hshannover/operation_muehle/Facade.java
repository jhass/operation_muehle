package de.hshannover.operation_muehle;

import java.io.IOException;
import java.util.HashMap;

import de.hshannover.operation_muehle.gui.board.Spot;
import de.hshannover.operation_muehle.logic.PlayerOptions;
import de.hshannover.operation_muehle.logic.SaveState;
import de.hshannover.operation_muehle.logic.Slot;
import de.hshannover.operation_muehle.logic.Move;
import de.hshannover.operation_muehle.logic.Player;
import de.hshannover.operation_muehle.logic.ApplicationController;
import de.hshannover.operation_muehle.logic.IOOperation;
import de.hshannover.operation_muehle.utils.loader.StrategyLoader;

/**
 * Facade for GUI-Interaction
 * @author Richard Pump
 */
public class Facade {
	private static Facade instance;
	private ApplicationController appController;
	private StrategyLoader stratload;
	
	/**
	 * Simple Constructor.
	 */
	private Facade() {
		//FIXME: Make the app controller a Thread so that the constructor can return
		new Thread(new Runnable() {
			@Override
			public void run() {
				appController = new ApplicationController();
			}
		});
		
		try {
			stratload = new StrategyLoader();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		//FIXME: Make the app controller a Thread so that the constructor can return
		new Thread(new Runnable() {
			@Override
			public void run() {
				appController.initializeNew(gameOptions);
			}
		});
	}
	
	/**
	 * Saves the Game at the given Path
	 * @param path The path, where the game is to be saved.
	 * @see IOOperation
	 */
	public void saveGame(String path) {
		SaveState save = appController.getSaveState();
		IOOperation.saveGameInfo(path, save);
	}
	/**
	 * Loads the Game from the given Path
	 * @param path Ye Pathe of thy saveth Game.
	 */
	public void loadGame(String path) {
		SaveState save = IOOperation.loadGameInfo(path);
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
		return stratload;
	}
	
	/**
	 * What was this for again?
	 * replace with giveSlot (like giveMove)?
	 * @param player
	 * @return
	 */
	public Slot getSlot(Player player) {
		return null;
	}
	
	/**
	 * Gives a Move to the ApplicationController
	 * @param src The spot to move from
	 * @param dst The spot to move to
	 */
	public void giveMove(Spot src, Spot dst) {
		Slot srcSlot = null, dstSlot = null;
		
		if (src != null) {
			srcSlot = new Slot(src.getIntegerColumn(), src.getRow());
		}
		
		if (dst != null) {
			dstSlot = new Slot(dst.getIntegerColumn(), dst.getRow());
		}
		
		Move move = new Move(srcSlot, dstSlot);
		System.out.println(move);
		//appController.givePlayerMove(move); // FIXME: appController is null since the constructor doesn't return
	}
	


}
