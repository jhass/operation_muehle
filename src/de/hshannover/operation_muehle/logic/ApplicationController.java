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
// REFACTOR TODO
//TODO: refactor isInMill
//TODO: refactor canRemove
//TODO: refactor checkForMill
//TODO: fix updateWinner/winner detection
//TODO: test PvAI
//TODO: test AIvAI
//TODO: reorganize method order
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
				//TODO: restore from SaveState or serialize whole thread into saveState
				Move lastMove = null;
				lastRemovedStone = null;
				gameRunning = true;
				
				while(isGameRunning()) {
					
					if(players.isCurrentPlayerAI()) {
						setCurrentMoveToAIMove(lastMove);
						
						if (isInvalidMove(currentMove)) {
							winner = players.getOpponent(); //TODO log, inform GUI
							gameRunning = false;
							break; //FIXME: do we need any kind of cleanup?
						}
					}
					
					if (moveAvailable()) {
						executeMove(currentMove);
						queryRemovalIfNecessary(currentMove);
						
						if (winner != null)
							updateWinner();
						
						if (winner != null) { 
							System.out.println("Gewinner: "+winner); //TODO: inform GUI, log, remove
							gameRunning = false;
							break; //FIXME: do we need any kind of cleanup?
						}
						
						players.opponentsTurn();
						lastMove = currentMove;
						currentMove = null;
					} else {
						waitForHumanPlayerMove();
					}
				}
			}

			private void waitForHumanPlayerMove() {
				try {
					sleep(100);
				} catch (InterruptedException e) {}
			}

			private void queryRemovalIfNecessary(Move move) {
				if (hasClosedMill(move)) {
					closedMill = true;
					System.out.println("Muehle: "+closedMill); //TODO: debug, remove me
					
					if (players.isCurrentPlayerAI()) {
						lastRemovedStone = new Slot(players.getCurrent().removeStone());
						
					} else {
						setObservableChanged(true);
						notifyObserver(); //TODO: inform GUI that the user should remove a stone
						
						// Reset query mechanism
						Move currentMoveCache = currentMove;
						currentMove = null;
						
						// Wait for move
						while (noMoveAvailable()) {
							waitForHumanPlayerMove();
						}
						
						executeMove(currentMove);
						lastRemovedStone = currentMove.fromSlot();
						
						//Restore current move
						currentMove = currentMoveCache;
					}
					
					closedMill = false;
				} else {
					lastRemovedStone = null;
				}
			}

			private boolean hasClosedMill(Move move) {
				return move.isNoRemoval() &&
					   isInMill(gameboard.returnSlot(move.toSlot()));
			}

			private void setCurrentMoveToAIMove(Move lastMove) {
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
					//FIXME: interface seems incorrectly used, if it's the first move of white in MOVE_PHASE, lastMove should be (null, lastPlacedStoneOfBlack)
					//Yes, the interface is a complete mindfuck.
					currentMove = players.getCurrent().doMove(lastMove,
															  lastRemovedStone);
				}
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
			throw new InvalidMoveException();
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
									winner, logger.getLog()); //FIXME: this prevents localizing winner, do we really need to serialize it?
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

	private boolean moveAvailable() {
		return !noMoveAvailable();
	}

	private boolean isInvalidMove(Move move) {
		return !isValidMove(move);
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

	private boolean isRemovalAndNotInRemovalMode(Move move) {
		return move.isRemoval() && !closedMill;
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

	private boolean isMoveAndJumpPhaseAndEmptyDestination(Move move, Slot endSlot) {
		return move.isMove() &&
			   players.isCurrentPlayersPhase(Player.JUMP_PHASE) &&
			   endSlot.isEmpty();
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

	private boolean isMoveOfOpponentsStoneOrEmptySlot(Slot startSlot) {
		return players.isCurrentPlayersPhase(Player.MOVE_PHASE) && 
			   startSlot.getStatus() != players.getCurrentPlayersSlotStatus();
	}

	private boolean isRemovalAndInRemovalMode(Move move) {
		return move.isRemoval() && closedMill;
	}

	private boolean isPlacementAndNotInPlacementPhase(Move move) {
		return players.isCurrentPlayersPhaseNot(Player.PLACE_PHASE) &&
			   move.isPlacement();
	}

	private boolean isNoOperationMove(Move move) {
		return move.isMove() &&
			   move.fromSlot().hashCode() == move.toSlot().hashCode();
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
	 * Checks if the Stone in the given Slot is closing a 
	 * row of three (mill).
	 * @param slot The Slot that contains the Stone to be checked.
	 * @return If the Stone is part of a Mill.
	 */
	private boolean isInMill(Slot slot) {
		boolean isMill = false;
		if (slot.isEmpty()) return false;
		Slot[] closeSlotNeighbours = this.gameboard.getNeighbours(slot);
		boolean isTopNull = (closeSlotNeighbours[0] == null);
		boolean isRightNull = (closeSlotNeighbours[1] == null);
		boolean isBottomNull = (closeSlotNeighbours[2] == null);
		boolean isLeftNull = (closeSlotNeighbours[3] == null);
				 
		if (!(isTopNull || isBottomNull)) {
			/* Wenn die Felder oberhalb und unterhalb des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			Slot.Status status = slot.getStatus(); //Status des zu preufenden Feldes
			if (closeSlotNeighbours[0].getStatus() == status &&
				closeSlotNeighbours[2].getStatus() == status) isMill = true;
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
		if (isMill) return true;
		
		if (!(isLeftNull || isRightNull)) {
			/* Wenn die Felder links und rechts des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			Slot.Status status = slot.getStatus(); //Status des zu preufenden Feldes
			if (closeSlotNeighbours[1].getStatus() == status &&
				closeSlotNeighbours[3].getStatus() == status) isMill = true;
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
		Slot[] distantSlotNeighbours= 
				this.gameboard.getNeighbours(secondSlot);
		if (firstSlot.getStatus() == secondSlot.getStatus() &&
			firstSlot.getStatus() == distantSlotNeighbours[index].getStatus())
			return true;
		return false;
	}
	
	/**
	 * Checks if a Stone in a Slot is removable or not.
	 * @param slot The Slot that contains the Stone in question.
	 * @return If the Stone can be removed.
	 */
	private boolean canRemove(Slot slot) {
		ArrayList<Slot> pSlot = gameboard.getStonesOfStatus(players.getOpponentsSlotStatus());
		ArrayList<Slot> removeableSlots = new ArrayList<Slot>();
		
		/*
		 * Pruefen, ob alle gegnerischen Steine innerhalb einer Muehle sind
		 * Wenn ein gegnerischer Stein nicht in einer Muehle ist, dann wird der
		 * Slot in die Liste hinzugefuegt.
		 */
		for (Slot hSlot: pSlot) {
			if (!isInMill(hSlot)) removeableSlots.add(hSlot);
		}
		
		/*
		 * Gibt true zurueck, wenn der die Liste leer ist, da nun alle Steine
		 * innherhalb einer Muehle liegen und der Stein erfernt werden darf.
		 */
		if (removeableSlots.size() == 0) return true;
		else {
			/*
			 * Wenn in der Liste Elemente sind, gibt es Steine außerhalb
			 * der Muehle, dann darf der Stein nur entfernt werden, wenn er in der
			 * Liste enthalten ist.
			 */
			if (removeableSlots.contains(gameboard.returnSlot(slot))) return true;
		}
		
		return false;
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
					Slot[] neighbours = gameboard.getNeighbours(slot);
					for (int i = 0; i <= 3; i++) {
						if (neighbours[i] != null && 
							neighbours[i].isEmpty()) {
							winner = null;
							break slotLoop;
						}
					}
				}
			}
		}
	}
}
