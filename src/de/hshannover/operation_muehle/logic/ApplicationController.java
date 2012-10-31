package de.hshannover.operation_muehle.logic;

import java.util.HashMap;

import de.hshannover.operation_muehle.utils.observer.AObservable;

/**
 * The ApplicationController, doing all of the games logic.
 * Extends the Observable abstract class, so it can be observed.
 * @author Richard Pump
 * @author Benjamin Held
 *
 */
public class ApplicationController extends AObservable{
	private Player[] players;
	private Logger logger;
	private Gameboard gameboard;
	private int currentPlayer;
	private int winner;
	private boolean gameStopped;
	
	/**
	 * Simple, basic Constructor.
	 */
	public ApplicationController() {
		players = new Player[2];
		logger = new Logger();
		gameboard = new Gameboard();
	}
	
	/**
	 * Initialize a new Game with the given Options.
	 * @param gameOption The Options for the new Game.
	 * @see SaveState
	 */
	public void initializeNew(HashMap<String,PlayerOptions> gameOptions) {
		//create Players?
		currentPlayer = 1;
		winner = -1;
		playGame();
	}
	
	/**
	 * Initialize a Game with given Options from the SaveState
	 * @param s SaveState from an older game.
	 * @see SaveState
	 */
	public void initializeSaved(SaveState s) {
		players = s.players;
		gameboard = s.currentGB;
		currentPlayer = s.currentPlayer;
		playGame();
	}
	
	/**
	 * Plays the Game, until it is finished or interrupted.
	 */
	public void playGame() {
		gameStopped = false;
		while(!gameStopped) {
			//Do things and such
		}
	}
	
	/**
	 * Gives a Move to the Controller. Used for GUI-Interaction
	 * @param m The Move of the Player.
	 */
	public void givePlayerMove(Move m) {
		//use the move m
	}
	
	/**
	 * Returns a new GameState, representing the game in its most recent state.
	 * @return A "fresh" GameState.
	 * @see GameState
	 */
	public GameState getGameState() {
		return new GameState(gameboard, currentPlayer, 
									winner, logger.getLog());
	}
	
	/**
	 * Returns a new SaveState, containing all information 
	 * about the game in its most recent state.
	 * @return A "fresh" SaveState
	 * @see SaveState
	 */
	public SaveState getSaveState() {
		return new SaveState(gameboard, currentPlayer, 
									winner, logger.getLog(), players);
	}
	
	/**
	 * Ends the current Game. Needs Love badly.
	 */
	public void endGame() {
		gameStopped = true;
	}
	
	/**
	 * Checks if a Move is a valid one or not.
	 * @param m The Move to be checked.
	 * @return If the Move is true or not.
	 */
	private boolean isValidMove(Move m) {
		//Magic.
		return false;
	}
	
	/**
	 * Executes a Move on the Gameboard. 
	 * @param m A VALID(!) Move.
	 */
	private void executeMove(Move m) {
		gameboard.applyMove(m);
	}
	
	/**
	 * Checks if the Stone in the given Slot is closing a 
	 * row of three (mill).
	 * @param slot The Slot that contains the Stone to be checked.
	 * @return If the Stone is part of a Mill.
	 */
	private boolean isInMill(Slot slot) {
		boolean isInMill = false;
		Slot[] closeSlotNeighbours = this.gameboard.getNeighbours(slot);
		boolean checktop = (closeSlotNeighbours[0] != null);
		boolean checkright = (closeSlotNeighbours[1] != null);
		boolean checkbottom = (closeSlotNeighbours[2] != null);
		boolean checkleft = (closeSlotNeighbours[3] != null);
				 
		if (checktop && checkbottom) {
			/* Wenn die Felder oberhalb und unterhalb des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			SlotStatus status = slot.getStatus(); //Status des zu preufenden Feldes
			if (closeSlotNeighbours[0].getStatus() == status &&
				closeSlotNeighbours[2].getStatus() == status) return true;
		} else if (checktop) {
			/* Wenn Feld am unteren Ende eine moeglichen Muehle steht, benoetigte Felder
			 * holen und auf Muehle pruefen
			 */
			
		} else if (checkbottom) {
			/* Wenn Feld am oberen Ende eine moeglichen Muehle steht, benoetigte Felder
			 * holen und auf Muehle pruefen
			 */
			
		}
		
		if (checkleft && checkright) {
			/* Wenn die Felder links und rechts des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			SlotStatus status = slot.getStatus(); //Status des zu preufenden Feldes
			if (closeSlotNeighbours[1].getStatus() == status &&
				closeSlotNeighbours[3].getStatus() == status) return true;
		} else if (checkleft) {
			/* Wenn Feld am rechten Ende eine moeglichen Muehle steht, benoetigte Felder
			 * holen und auf Muehle pruefen
			 */
		} else if (checkright) {
			/* Wenn Feld am linken Ende eine moeglichen Muehle steht, benoetigte Felder
			 * holen und auf Muehle pruefen
			 */
		}
		return isInMill;
	}
	/**
	 * Checks if a Stone in a Slot is removable or not.
	 * @param slot The Slot that contains the Stone in question.
	 * @return If the Stone can be removed.
	 */
	private boolean canRemove(Slot slot) {
		boolean removable = true;
		if(isInMill(slot)) { removable = true; } //replace with something smart
		return removable;
	}


}
