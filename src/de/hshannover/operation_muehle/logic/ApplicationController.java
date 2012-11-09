package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;
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
	private HashMap<SlotStatus, Player> players;
	private Logger logger;
	private Gameboard gameboard;
	private SlotStatus currentPlayer;
	private SlotStatus winner;
	private boolean gameStopped;
	
	/**
	 * Simple, basic Constructor.
	 */
	public ApplicationController() {
		players = new HashMap<SlotStatus, Player>();
		logger = new Logger();
		gameboard = new Gameboard();
	}
	
	/**
	 * Initialize a new Game with the given Options.
	 * @param gameOption The Options for the new Game.
	 * @see SaveState
	 */
	public void initializeNew(HashMap<String,PlayerOptions> gameOptions) {
		PlayerOptions pWhite = gameOptions.get("white");
		PlayerOptions pBlack = gameOptions.get("black");
		this.players.put(SlotStatus.WHITE, new Player(pWhite, SlotStatus.WHITE));
		this.players.put(SlotStatus.BLACK, new Player(pBlack, SlotStatus.BLACK));
		currentPlayer = SlotStatus.WHITE;
		winner = SlotStatus.EMPTY;
		playGame();
	}
	
	/**
	 * Initialize a Game with given Options from the SaveState
	 * @param s SaveState from an older game.
	 * @see SaveState
	 */
	public void initializeSaved(SaveState s) {
		players = s.getPlayers();
		gameboard = s.currentGB;
		currentPlayer = s.currentPlayer;
		playGame();
	}
	
	/**
	 * Plays the Game, until it is finished or interrupted.
	 */
	public void playGame() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				//Move lastMove = null;
				Move lastMove = new Move(new Slot(1,1), new Slot(1,4));
				Slot lastSlot = null;
				gameStopped = false;
				Player cPlayer;
				
				while(!gameStopped) {
					cPlayer = players.get(currentPlayer);
					/*
					 * Schleife zum Simulieren der Spielzuege
					 */
					if(cPlayer.isAI()) {
						lastMove = cPlayer.doMove(lastMove, lastSlot);
						if (isValidMove(lastMove)) {
							winner = currentPlayer.getOtherPlayer();
							break;
						}
					} else {
						//Playermove from GUI
					}
					/*
					 * Pruefung, ob eine Muehle geschlossen wurde. Wenn ja
					 * entsprechender Aufruf an AI/ GUI zum entfernen eines 
					 * Spielsteins.
					 */
					if (isInMill(lastMove.toSlot())) {
						if (cPlayer.isAI()) { 
							lastSlot = (Slot) cPlayer.removeStone();
						} else {
							do {
							//TODO: Bei Mutti in der GUI nachfragen und zu
							//	entfernenden Stein erfragen.
							} while (!canRemove(lastSlot));
						}
					} else {
						lastSlot = null;
					}
					
					executeMove(lastMove);
					winner = checkWinner();
					gameStopped = true;
				}
			}
			
		}).start();
	}
	
	/**
	 * Gives a Move to the Controller. Used for GUI-Interaction
	 * @param m The Move of the Player.
	 */
	public void givePlayerMove(Move m) throws InvalidMoveException {
		if(!isValidMove(m)) throw new InvalidMoveException();
		//aufwachen, static move setzen
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
		Slot startSlot = m.fromSlot();
		Slot endSlot = m.toSlot();
		
		/*
		 * Move-Evaluation fuer Spielzuege in Phase 2: Ist das Endfeld
		 * Nachbarfeld des Startfeldes und ist das Feld nicht belegt, dann
		 * ist der Zug gueltig.
		 */
		if (this.players.get(this.currentPlayer).getPhase() == 2) {
			Slot[] startNeighbour = gameboard.getNeighbours(startSlot);
			
			for (int i = 0; i <= 3; i++) {
				if (startNeighbour[i].hashCode() == endSlot.hashCode()
				 &&  startNeighbour[i].getStatus() == SlotStatus.EMPTY) return true; 
			}
		/*
		 * Move-Evaluation fuer das Spielfeld in Phase 1: Ist das Endfeld leer, dann
		 * darf der Stein in das Feld gesetzt werden. Das Startfeld ist in diesem
		 * Fall leer.
		 * Move-Evaluation fuer das Spielfeld in Phase 3: Ist das Endfeld leer, dann
		 * darf der Stein dorthin gesetzt werden. Das Startfeld ist in diesem Fall nicht
		 * relevant, da man an ein beliebiges Feld springen darf.
		 */
		} else if (this.players.get(this.currentPlayer).getPhase() == 1 ||
				    this.players.get(this.currentPlayer).getPhase() == 3) {
			if (endSlot.getStatus() == SlotStatus.EMPTY) return true;
		}
		
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
			/* Wenn der untere Nachbar leer ist, befindet sich der uebergebene Slot
			 * am unteren Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn oberhalb bestimmt werden.
			 */
			return checkForMill(slot, closeSlotNeighbours[0],0);
		} else if (checkbottom) {
			/* Wenn der obere Nachbar leer ist, befindet sich der uebergebene Slot
			 * am oberen Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn unterhalb bestimmt werden.
			 */
			return checkForMill(slot, closeSlotNeighbours[2],2);
		}
		
		if (checkleft && checkright) {
			/* Wenn die Felder links und rechts des zu pruefenden Feldes existieren,
			 * pruefe, ob eine Muehle geschlossen ist
			 */
			
			SlotStatus status = slot.getStatus(); //Status des zu preufenden Feldes
			if (closeSlotNeighbours[1].getStatus() == status &&
				closeSlotNeighbours[3].getStatus() == status) return true;
		} else if (checkleft) {
			/* Wenn der rechte Nachbar leer ist, befindet sich der uebergebene Slot
			 * am rechten Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn in linker Richtung bestimmt werden.
			 */
			return checkForMill(slot, closeSlotNeighbours[1],1);
		} else if (checkright) {
			/* Wenn der linke Nachbar leer ist, befindet sich der uebergebene Slot
			 * am linken Ende einer Dreierreihe. Es muessen zum Vergleich die beiden
			 * Nachbarn in rechter Richtung bestimmt werden.
			 */
			return checkForMill(slot, closeSlotNeighbours[3],3);
		}
		return false;
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
		ArrayList<Slot> pSlot = gameboard.getStonesFromColor(
				                               currentPlayer.getOtherPlayer());
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
			if (removeableSlots.contains(slot)) return true;
		}
		
		return false;
	}
	
	/**
	 * Die Methode prueft, ob eine der Siegbedingungen eingetreten ist
	 * @return boolean
	 */
	private SlotStatus checkWinner() {
		/*
		 * Gewinnbedingung fuer die Anzahl der Steine (n < 3)
		 */
		for (SlotStatus cStatus: players.keySet()) {
			Player cPlayer = players.get(cStatus);
			if (cPlayer.getStones() < 3) return cPlayer.getColor().getOtherPlayer();
		}
		
		/*
		 * Gewinnbedingung, wenn kein Zug mehr moeglich ist
		 */
		SlotStatus nPlayer = currentPlayer.getOtherPlayer();
		ArrayList<Slot> slotList = gameboard.getStonesFromColor(nPlayer);
		
		for (Slot iteSlot: slotList) {
			Slot[] neighbours = gameboard.getNeighbours(iteSlot);
			for (int i = 0; i <= 3; i++) {
				if (neighbours[i] == null) return SlotStatus.EMPTY;
			}
		}
		
		return currentPlayer.getOtherPlayer();
	}
}