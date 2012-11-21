package de.hshannover.operation_muehle.logic;

import java.util.HashMap;

import de.hshannover.operation_muehle.utils.observer.AObservable;

/**
 * The ApplicationController, doing all of the games logic.
 * Extends the Observable abstract class, so it can be observed.
 * @author Richard Pump
 * @author Benjamin Held
 * @author Jonne Haß
 *
 */

public class ApplicationController extends AObservable{
	private PlayerManager players;
	private Logger logger;
	private Gameboard gameboard;
	private Player winner;
	private Move currentMove;
	private Thread gameThread;
	private boolean gameRunning;
	private MoveValidator moveValidator;
	private MoveValidator removeMoveValidator;
	private MoveValidator currentMoveValidator;
	
	/**
	 * Simple, basic Constructor.
	 */
	public ApplicationController() {
		resetController();
	}

	private void resetController() {
		players = null;
		logger = new Logger();
		gameboard = new Gameboard();
		currentMove = null;
		gameRunning = false;
		winner = null;
		moveValidator = new MoveValidator(gameboard, players);
		removeMoveValidator = new RemoveMoveValidator(gameboard, players);
		currentMoveValidator = moveValidator;
		
		setObservableChanged(true);
	}
	
	/**
	 * Initialize a new Game with the given Options.
	 * @param gameOption The Options for the new Game.
	 * @see SaveState
	 */
	public void initializeNew(HashMap<String,PlayerOptions> gameOptions) {
		resetController();
		
		players = new PlayerManager(gameOptions.get("white"), gameOptions.get("black"));
		moveValidator.setPlayers(players);
		removeMoveValidator.setPlayers(players);
		
		notifyObserver();
		playGame();
	}
	
	/**
	 * Initialize a Game with given Options from the SaveState
	 * @param state SaveState from an older game.
	 * @see SaveState
	 */
	public void initializeSaved(SaveState state) {
		resetController();
		
		players = state.getPlayers();
		gameboard = state.currentGB;
		moveValidator.setPlayers(players);
		removeMoveValidator.setPlayers(players);
		
		notifyObserver();
		playGame();
	}
	
	/**
	 * Plays the Game, until it is finished or interrupted.
	 */
	public void playGame() {
		gameThread = new Thread() {
			private Slot lastRemovedStone;

			@Override
			public void run() {
				Move lastMove = null;
				lastRemovedStone = null;
				gameRunning = true;
				
				try {
					while(isGameRunning()) {
						if(players.isCurrentPlayerAI()) {
							setCurrentMoveToAIMove(lastMove);
						}
						
						if (moveAvailable()) {
							executeMove(currentMove);
							queryRemovalIfNecessary(currentMove);
							
							if (winner == null) {
								updateWinner();
							}
							
							if (winner != null) {
								gameRunning = false;
								break;
							}
							
							players.opponentsTurn();
							lastMove = currentMove;
							currentMove = null;
						} else {
							waitForHumanPlayerMove();
						}
					}
				} catch (InvalidMoveException e) {
					System.out.println("AI made invalid move "+e.move);
					winner = players.getOpponent(); //TODO log, inform GUI
					gameRunning = false;
				}
				
				if (winner != null) { 
					System.out.println("Gewinner: "+winner); //TODO: inform GUI, log, remove debug
				}
			}

			private void setCurrentMoveToAIMove(Move lastMove)
				throws InvalidMoveException {
				if (players.isCurrentPlayersPhase(Player.PLACE_PHASE)) {
					currentMove = new Move(
						null,
						new Slot(
							players.getCurrent().placeStone( // TODO: enforce thinkTime
									lastMove != null ? lastMove.toSlot() : null,
									lastRemovedStone
							)
						)
					);
				} else {
					// lastMove is already correctly set in the transition
					// special case since we internally handle remove that way.
					currentMove = players.getCurrent().doMove(lastMove,
															  lastRemovedStone);
				}
				
				if (isInvalidMove(currentMove)) {
					throw new InvalidMoveException(currentMove);
				}
			}

			private void queryRemovalIfNecessary(Move move)
				throws InvalidMoveException {
				if (hasClosedMill(move)) {
					currentMoveValidator = removeMoveValidator;
					System.out.println("Muehle!"); //TODO: debug, remove me
					
					// Reset query mechanism
					Move currentMoveCache = currentMove;
					currentMove = null;
					
					if (players.isCurrentPlayerAI()) {
						currentMove = new Move( //TODO: enforce think time
							new Slot(players.getCurrent().removeStone()),
							null
						);
						
						if (isInvalidMove(currentMove)) {
							currentMoveValidator = moveValidator;
							throw new InvalidMoveException(currentMove);
						}
					} else {
						setObservableChanged(true);
						notifyObserver(); //TODO: inform GUI that the user should remove a stone
						
						// Wait for move
						while (noMoveAvailable()) {
							waitForHumanPlayerMove();
						}
					}
					
					executeMove(currentMove);
					
					lastRemovedStone = currentMove.fromSlot();
					
					//Restore current move
					currentMove = currentMoveCache;
					currentMoveValidator = moveValidator;
					
				} else {
					lastRemovedStone = null;
				}
			}

			private void waitForHumanPlayerMove() {
				try {
					sleep(100);
				} catch (InterruptedException e) {}
			}
			
		};
		
		gameThread.start();
	}
	
	/**
	 * Gives a Move to the Controller. Used for GUI-Interaction
	 * @param move The Move of the Player.
	 */
	public void givePlayerMove(Move move) throws InvalidMoveException {
		if(players.isCurrentPlayerAI() || isInvalidMove(move)) {
			throw new InvalidMoveException(move);
		}
		
		currentMove = move;
		gameThread.interrupt();
	}
	
	/**
	 * Returns a new GameState, representing the game in its most recent state.
	 * @return A "fresh" GameState.
	 * @see GameState
	 */
	public GameState getGameState() {
		return new GameState(gameboard, players.getCurrent(), 
									winner, logger.getLog());
	}
	
	/** Returns the current logger object
	 * 
	 * @return
	 */
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * Returns a new SaveState, containing all information 
	 * about the game in its most recent state.
	 * @return A "fresh" SaveState
	 * @see SaveState
	 */
	public SaveState getSaveState() {
		return new SaveState(gameboard, players.getCurrent(), 
									winner, logger.getLog(), players);
	}
	
	/**
	 * Ends the current Game. Needs Love badly.
	 */
	public synchronized void endGame() {
		gameRunning = false;
	}
	
	private synchronized boolean isGameRunning() {
		return gameRunning;
	}

	private boolean isInvalidMove(Move move) {
		return !currentMoveValidator.isValidMove(move);
	}

	private boolean moveAvailable() {
		return !noMoveAvailable();
	}

	/**
	 * Executes a Move on the Gameboard. 
	 * @param move A VALID(!) Move.
	 */
	private void executeMove(Move move) {
		if (move.isRemoval()) {
			players.decreaseOpponentsNumberOfStones();
			gameboard.removeStone(currentMove.fromSlot());
		} else if (move.isPlacement()) {
			players.increaseCurrentPlayersStones();
			gameboard.applySlot(currentMove.toSlot(), players.getCurrentPlayersSlotStatus());
			logger.addEntry(move.toSlot());
		} else if (players.getCurrentPlayersPhase() > Player.PLACE_PHASE) {
			gameboard.applyMove(move);
			logger.addEntry(move);
		}
		
		setObservableChanged(true);
		notifyObserver();
		
		System.out.println("Letzter Zug: \n"); //TODO: debug, remove me
		System.out.println(logger.getLastEntry());
	}

	/**
	 * Die Methode prueft, ob eine der Siegbedingungen eingetreten ist
	 * @return boolean
	 */
	private void updateWinner() {
		winner = null;
		
		for (Player player: players) {			
			/*
			 * Gewinnbedingung fuer die Anzahl der Steine (n < 3)
			 */
			if (player.getStones() < 3 && player.getPhase() > Player.PLACE_PHASE) {
				winner = players.getOpponentOf(player);
				break;
			}
			
			if (player.getPhase() == Player.MOVE_PHASE) {
				/*
				 * Gewinnbedingung, wenn kein Zug mehr moeglich ist
				 */
				winner = players.getOpponentOf(player);
				
				slotLoop:
				for (Slot slot : gameboard.getStonesOfStatus(player.getSlotStatus())) {
					for (Slot neighbour : gameboard.getNeighbours(slot)) {
						if (neighbour != null && 
							neighbour.isEmpty()) {
							winner = null;
							break slotLoop;
						}
					}
				}
			}
		}
	}

	private boolean hasClosedMill(Move move) {
		return move.isNoRemoval() &&
			   currentMoveValidator.isInMill(gameboard.returnSlot(move.toSlot()));
	}

	private boolean noMoveAvailable() {
		return currentMove == null;
	}
}
