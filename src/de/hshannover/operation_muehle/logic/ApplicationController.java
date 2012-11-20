package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;
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
	private boolean closedMill; //TODO: feels like a hack
	private Thread gameThread;
	private boolean gameRunning;
	
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
		closedMill = false;
		
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
					closedMill = true;
					System.out.println("Muehle: "+closedMill); //TODO: debug, remove me
					
					// Reset query mechanism
					Move currentMoveCache = currentMove;
					currentMove = null;
					
					if (players.isCurrentPlayerAI()) {
						currentMove = new Move( //TODO: enforce think time
							new Slot(players.getCurrent().removeStone()),
							null
						);
						
						if (isInvalidMove(currentMove)) {
							closedMill = false;
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
					closedMill = false;
					
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
		return !isValidMove(move);
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
			   isInMill(gameboard.returnSlot(move.toSlot()));
	}

	/**
	 * Checks if the Stone in the given Slot is closing a 
	 * row of three (mill).
	 * @param slot The Slot that contains the Stone to be checked.
	 * @return If the Stone is part of a Mill.
	 */
	private boolean isInMill(Slot slot) {
		if (slot.isEmpty())
			return false;
		
		Slot[] closeSlotNeighbours = gameboard.getNeighbours(slot);
		boolean isTopNull = (closeSlotNeighbours[0] == null);
		boolean isRightNull = (closeSlotNeighbours[1] == null);
		boolean isBottomNull = (closeSlotNeighbours[2] == null);
		boolean isLeftNull = (closeSlotNeighbours[3] == null);
		boolean isMill = false;
				 
		if (!(isTopNull || isBottomNull)) {
			/* Wenn die Felder oberhalb und unterhalb des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			isMill = hasTopAndBottomStatusOf(closeSlotNeighbours, slot.getStatus());
		} else if (isBottomNull) {
			/* Wenn der untere Nachbar leer ist, befindet sich der uebergebene Slot
			 * am unteren Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn oberhalb bestimmt werden.
			 */
			isMill = checkForMill(slot, closeSlotNeighbours[0],0);
		} else if (isTopNull) {
			/* Wenn der obere Nachbar leer ist, befindet sich der uebergebene Slot
			 * am oberen Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn unterhalb bestimmt werden.
			 */
			isMill = checkForMill(slot, closeSlotNeighbours[2],2);
		}
		if (isMill)
			return true;
		
		if (!(isLeftNull || isRightNull)) {
			/* Wenn die Felder links und rechts des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			isMill = hasLeftAndRightStatusOf(closeSlotNeighbours, slot.getStatus());
		} else if (isLeftNull) {
			/* Wenn der rechte Nachbar leer ist, befindet sich der uebergebene Slot
			 * am rechten Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn in linker Richtung bestimmt werden.
			 */
			isMill = checkForMill(slot, closeSlotNeighbours[1],1);
		} else if (isRightNull) {
			/* Wenn der linke Nachbar leer ist, befindet sich der uebergebene Slot
			 * am linken Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn in rechter Richtung bestimmt werden.
			 */
			isMill = checkForMill(slot, closeSlotNeighbours[3],3);
		}
		
		return isMill;
	}

	private boolean hasTopAndBottomStatusOf(Slot[] closeSlotNeighbours, Slot.Status status) {
		return closeSlotNeighbours[0].getStatus() == status &&
			   closeSlotNeighbours[2].getStatus() == status;
	}

	/**
	 * Die Methode nimmt zwei Slots entgegen (die in einer Reihe liegen), bestimmt den
	 * verbleibenden dritten Slot und prueft die drei Slots auf Statusgleichheit.
	 * Gibt true zurueck, wenn es eine Muehle ist, gibt falsche zurueck, wenn es keine
	 * Muehle ist
	 * @param firstSlot Urspruenglicher Slot
	 * @param secondSlot Erster Nachbar in Reihe
	 * @param index Index, an dem der dritte Reihenstein zu finden ist
	 * @return boolean
	 */
	private boolean checkForMill(Slot firstSlot, Slot secondSlot, int index) {
		return firstSlot.getStatus() == secondSlot.getStatus() &&
			   firstSlot.getStatus() == gameboard.getNeighbours(secondSlot)[index].getStatus();
	}

	private boolean hasLeftAndRightStatusOf(Slot[] closeSlotNeighbours, Slot.Status status) {
		return closeSlotNeighbours[1].getStatus() == status &&
			   closeSlotNeighbours[3].getStatus() == status;
	}

	private boolean noMoveAvailable() {
		return currentMove == null;
	}

	/** Checks if a Move is a valid one or not.
	 * 
	 * @param move The Move to be checked.
	 * @return If the Move is true or not.
	 */
	private boolean isValidMove(Move move) {
		Slot startSlot, endSlot;;
		
		if (isPlacementAndNotInPlacementPhase(move))
			return false;
		if (isRemovalAndNotInRemovalMode(move))
			return false;
		if (isNoOperationMove(move))
			return false;
		
		
		if (isRemovalAndInRemovalMode(move)) {
			// We don't do this earlier because this function is performance critical
			startSlot = gameboard.returnSlot(move.fromSlot());
			return canRemove(startSlot);
		}
		
		// We don't do this earlier because this function is performance critical
		endSlot = gameboard.returnSlot(move.toSlot());
		
		if (isPlacementAndPlacementPhaseAndEmptyDestination(move, endSlot))
			return true;
		if (isMoveToUsedSlotAndNotRemovalMode(endSlot))
			return false;
		if (isMoveAndJumpPhaseAndEmptyDestination(move, endSlot))
			return true;
		
		// Yeah, I know, not DRY
		startSlot = gameboard.returnSlot(move.fromSlot());
		
		
		if (isMoveOfOpponentsStoneOrEmptySlot(startSlot))
			return false;
		// most expensive op, last one
		if (isMoveAndMovePhaseAndDestinationEmptyNeighbour(move, startSlot, endSlot))
			return true;
		
		return false; // In doubt refuse
	}

	private boolean isPlacementAndNotInPlacementPhase(Move move) {
		return (closedMill || players.isCurrentPlayersPhaseNot(Player.PLACE_PHASE)) &&
			   move.isPlacement();
	}

	private boolean isRemovalAndNotInRemovalMode(Move move) {
		return move.isRemoval() && !closedMill;
	}

	private boolean isNoOperationMove(Move move) {
		return move.isMove() &&
			   move.fromSlot().hashCode() == move.toSlot().hashCode();
	}

	private boolean isRemovalAndInRemovalMode(Move move) {
		return move.isRemoval() && closedMill;
	}

	/**
	 * Checks if a Stone in a Slot is removable or not.
	 * @param slot The Slot that contains the Stone in question.
	 * @return If the Stone can be removed.
	 */
	private boolean canRemove(Slot slot) {
		ArrayList<Slot> removeableSlots = new ArrayList<Slot>();
		
		/*
		 * Pruefen, ob alle gegnerischen Steine innerhalb einer Muehle sind
		 * Wenn ein gegnerischer Stein nicht in einer Muehle ist, dann wird der
		 * Slot in die Liste hinzugefuegt. Wenn der gesuchte Slot nicht in einer
		 * Mühle ist kann er entfernt werden.
		 */
		for (Slot opponentSlot: gameboard.getStonesOfStatus(players.getOpponentsSlotStatus())) {
			if (!isInMill(opponentSlot)) {
				if (opponentSlot.hashCode() == slot.hashCode())
					return true;
				removeableSlots.add(opponentSlot);
			}
		}
		
		/*
		 * Gibt true zurueck, wenn der die Liste leer ist, da nun alle Steine
		 * innherhalb einer Muehle liegen und der Stein erfernt werden darf.
		 * Oder wenn in der Liste Elemente sind, gibt es Steine außerhalb
		 * der Muehle, dann darf der Stein nur entfernt werden, wenn er in der
		 * Liste enthalten ist.
		 */
		if (removeableSlots.size() == 0 || removeableSlots.contains(slot))
			return true;
		
		return false;
	}

	private boolean isPlacementAndPlacementPhaseAndEmptyDestination(Move move,
			Slot endSlot) {
		return move.isPlacement() &&
			   players.isCurrentPlayersPhase(Player.PLACE_PHASE) &&
			   endSlot.isEmpty();
	}

	private boolean isMoveToUsedSlotAndNotRemovalMode(Slot endSlot) {
		return !closedMill && !endSlot.isEmpty();
	}

	private boolean isMoveAndJumpPhaseAndEmptyDestination(Move move, Slot endSlot) {
		return move.isMove() &&
			   players.isCurrentPlayersPhase(Player.JUMP_PHASE) &&
			   endSlot.isEmpty();
	}

	private boolean isMoveOfOpponentsStoneOrEmptySlot(Slot startSlot) {
		return players.isCurrentPlayersPhase(Player.MOVE_PHASE) && 
			   startSlot.getStatus() != players.getCurrentPlayersSlotStatus();
	}

	private boolean isMoveAndMovePhaseAndDestinationEmptyNeighbour(Move move,
			Slot startSlot, Slot endSlot) {
		if (players.isCurrentPlayersPhaseNot(Player.MOVE_PHASE))
			return false;
		
		for (Slot neighbour : gameboard.getNeighbours(startSlot)) {
			if (neighbour != null &&
				neighbour.hashCode() == endSlot.hashCode() &&
				neighbour.isEmpty()) {
				return true;
			}
		}
		
		
		return false;
	}
}
