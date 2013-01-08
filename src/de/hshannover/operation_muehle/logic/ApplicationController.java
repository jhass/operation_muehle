package de.hshannover.operation_muehle.logic;

import java.util.HashMap;

import de.hshannover.operation_muehle.utils.TimeObserver;
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
	private Gameboard gameboard;
	private Move currentMove;
	private Thread gameThread;
	private boolean gameRunning;
	private MoveValidator moveValidator;
	private MoveValidator removeMoveValidator;
	private MoveValidator currentMoveValidator;
	private State snapshot;
	private State state;
	
	/**
	 * Simple, basic Constructor.
	 */
	public ApplicationController() {
		resetController();
	}

	private synchronized void resetController() {
		players = null;
		gameboard = new Gameboard();
		currentMove = null;
		gameRunning = false;
		if (gameThread != null) {
			gameThread.interrupt();
			gameThread = null;
		}
		moveValidator = new MoveValidator(gameboard, players);
		removeMoveValidator = new RemoveMoveValidator(gameboard, players);
		currentMoveValidator = moveValidator;
		Logger.clear();
		snapshot = null;
		state = null;
		
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
		
		startGame();
	}
	
	/**
	 * Initialize a Game with given Options from the SaveState
	 * @param state SaveState from an older game.
	 * @see SaveState
	 */
	public void initializeSaved(State state) {
		resetController();
		
		players = state.players;
		gameboard = state.currentGB;
		moveValidator.setPlayers(players);
		removeMoveValidator.setPlayers(players);
		Logger.setInstance(state.logger);
		
		startGame();
	}
	
	private void startGame() {
		state = null; // ensure a clean gamestate
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
							
							
							if (getGameState().winner == null) {
								updateWinner();
							}
							
							if (getGameState().winner != null) {
								gameRunning = false;
								break;
							}
							
							players.opponentsTurn();
							
							lastMove = currentMove;
							currentMove = null;
							snapshot();
							setObservableChanged(true);
							
							notifyObserver();
						} else {
							waitForHumanPlayerMove();
						}
					}
				} catch (InvalidMoveException e) {
					if (e.move == null) {
						Logger.logErrorf("AI %s has not made a valid Move in %sms",
								players.getCurrentPlayersDisplayName(),
								players.getCurrent().getThinkTime());
					} else {
						Logger.logErrorf("AI %s made invalid move: %s! ",
										 players.getCurrentPlayersDisplayName(),
										 e.move);
					}
					getGameState().winner = players.getOpponent();
					gameRunning = false;
				} catch (Exception e) {
					Logger.logErrorf("An Exception occured from AI: %s", 
									players.getCurrentPlayersDisplayName());
					Logger.logErrorf("Exception type: %s", e);
					Logger.logDebugf("Stacktrace: %s", Logger.traceToString(e));
					getGameState().winner = players.getOpponent();
					gameRunning = false;
				}
				
				if (getGameState().winner != null) { 
					Logger.logInfof("Winner in %d moves: %s",
									getGameState().winner.getNumberOfMoves(),
									getGameState().winner.getDisplayName());
					setObservableChanged(true);
					notifyObserver();
				}
			}

			private void setCurrentMoveToAIMove(final Move lastMove)
				throws InvalidMoveException {
				try {
					if (players.isCurrentPlayersPhase(Player.PLACE_PHASE)) {
						TimeObserver observer = new TimeObserver(players.getCurrent()
																 .getThinkTime()+100) {
							@Override
							public void run() {
								currentMove = new Move(
										null,
										new Slot(
											players.getCurrent().placeStone(
													lastMove != null ? lastMove.toSlot() : null,
													lastRemovedStone
											)
										)
									);
								
							}
						};
						
						if (!observer.hasFinishedInTime()) throw new InvalidMoveException(null);
						
					} else {
						TimeObserver observer = new TimeObserver(players.getCurrent()
								 .getThinkTime()+100) {
							@Override
							public void run() {
								
								// lastMove is already correctly set in the transition
								// special case since we internally handle remove that way.
								currentMove = players.getCurrent().doMove(lastMove,
										lastRemovedStone);
							
							}
						};
						
						if (!observer.hasFinishedInTime()) throw new InvalidMoveException(null);
					}
				} catch (Exception e) {
					Logger.logDebugf("AI (%s) missbehaved: %s",
							players.getCurrentPlayersDisplayName(),
							Logger.traceToString(e));//e.printStackTrace();
					throw new InvalidMoveException(currentMove);
				}
				
				if (isInvalidMove(currentMove)) {
					throw new InvalidMoveException(currentMove);
				}
			}

			private void queryRemovalIfNecessary(Move move)
				throws InvalidMoveException {
				if (hasClosedMill(move)) {
					currentMoveValidator = removeMoveValidator;
					Logger.logDebugf("%s made a mill, querying removal.",
									 players.getCurrentPlayersDisplayName());
					
					// Reset query mechanism
					Move currentMoveCache = currentMove;
					currentMove = null;
					
					getGameState().inRemovalPhase = true;
					setObservableChanged(true);
					// TODO: catch Exceptions
					if (players.isCurrentPlayerAI()) {
						TimeObserver observer = new TimeObserver(players.getCurrent()
								 .getThinkTime()) {
							
							@Override
							public void run() {
								currentMove = new Move( 
										new Slot(players.getCurrent().removeStone()),
										null
									);
							}
						};

						if (!observer.hasFinishedInTime()) throw new InvalidMoveException(null);
						
						if (isInvalidMove(currentMove)) {
							currentMoveValidator = moveValidator;
							throw new InvalidMoveException(currentMove);
						}
					} else {
						notifyObserver();
						
						// Wait for move
						while (noMoveAvailable()) {
							waitForHumanPlayerMove();
						}
					}
					
					executeMove(currentMove);
					
					lastRemovedStone = currentMove.fromSlot();
					
					getGameState().inRemovalPhase = false;
					setObservableChanged(true);
					
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
	 * @see State
	 */
	public synchronized State getGameState() {
		if (state == null) {
			state = new State(gameboard, players, Logger.getInstance());
		}
		
		return state;
	}
	
	/**
	 * Returns a new SaveState, containing all information 
	 * about the game in its most recent state.
	 * @return A "fresh" SaveState
	 * @see SaveState
	 */
	public State getSaveState() {
		if (snapshot == null) {
			snapshot();
		}
		return snapshot;
	}
	
	/**
	 * Ends the current Game. Needs Love badly.
	 */
	public synchronized void endGame() {
		gameRunning = false;
	}
	
	private void snapshot()  {
		snapshot = new State(gameboard, players, Logger.getInstance());
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
		// log first since increasing/decreasing a players stones can
		// cause a phase change log that looks weird if it comes
		// before this one :P
		Logger.logInfo(move.toStringWithPlayer(players.getCurrentPlayersDisplayName()));		
		
		if (move.isRemoval()) {
			players.decreaseOpponentsNumberOfStones();
			gameboard.removeStone(currentMove.fromSlot());
		} else if (move.isPlacement()) {
			players.increaseCurrentPlayersStones();
			gameboard.applySlot(currentMove.toSlot(), players.getCurrentPlayersSlotStatus());
		} else if (players.getCurrentPlayersPhase() >= Player.MOVE_PHASE) {
			gameboard.applyMove(move);
		}
		
		players.getCurrent().incrementMoveCounter();
		setObservableChanged(true);
		
		// give the GUI a chance to redraw
		if (players.isCurrentPlayerAI()) {
			notifyObserver();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * Die Methode prueft, ob eine der Siegbedingungen eingetreten ist
	 * @return boolean
	 */
	private synchronized void updateWinner() {
		state.winner = null;
		
		for (Player player: players) {			
			/*
			 * Gewinnbedingung fuer die Anzahl der Steine (n < 3)
			 */
			if (player.getStones() < 3 && player.getPhase() > Player.PLACE_PHASE) {
				state.winner = players.getOpponentOf(player);
				break;
			}
			
			if (player.getPhase() == Player.MOVE_PHASE) {
				/*
				 * Gewinnbedingung, wenn kein Zug mehr moeglich ist
				 */
				state.winner = players.getOpponentOf(player);
				
				slotLoop:
				for (Slot slot : gameboard.getStonesOfStatus(player.getSlotStatus())) {
					for (Slot neighbour : gameboard.getNeighbours(slot)) {
						if (neighbour != null && 
							neighbour.isEmpty()) {
							state.winner = null;
							break slotLoop;
						}
					}
				}
				
				if (state.winner != null) {
					break;
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
