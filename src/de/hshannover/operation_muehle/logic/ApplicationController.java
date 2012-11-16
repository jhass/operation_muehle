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
	private HashMap<Player.Color, Player> players;
	private Logger logger;
	private Gameboard gameboard;
	private Player currentPlayer;
	private Player winner;
	private boolean gameStopped;
	private Move playerMove;
	private Move lastMove;
	private Slot reMove;
	private boolean moveAvailable;
	private boolean closedMill = false;
	boolean removeableStone = false;
	private Thread gameThread;
	
	/**
	 * Simple, basic Constructor.
	 */
	public ApplicationController() {
		players = new HashMap<Player.Color, Player>();
		logger = new Logger();
	}
	
	/**
	 * Initialize a new Game with the given Options.
	 * @param gameOption The Options for the new Game.
	 * @see SaveState
	 */
	public void initializeNew(HashMap<String,PlayerOptions> gameOptions) {
		if(gameThread != null) {
			endGame();
			gameThread.interrupt();
		}
		PlayerOptions pWhite = gameOptions.get("white");
		PlayerOptions pBlack = gameOptions.get("black");
		this.players.put(Player.Color.WHITE, new Player(pWhite, Player.Color.WHITE));
		this.players.put(Player.Color.BLACK, new Player(pBlack, Player.Color.BLACK));
		currentPlayer = players.get(Player.Color.WHITE);
		winner = null;
		gameboard = new Gameboard();
		this.moveAvailable = false;
		setObservableChanged(true);
		notifyObserver();
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
		setObservableChanged(true);
		notifyObserver();
		playGame();
	}
	
	/**
	 * Plays the Game, until it is finished or interrupted.
	 */
	public void playGame() {
		gameThread = new Thread() {
			@Override
			public void run() {
				
				lastMove = null;
				reMove = null;
				playerMove = new Move(null,null);
				Slot lastSlot = null;
				gameStopped = false;
				Player cPlayer;
				
				while(!gameStopped) {
					cPlayer = currentPlayer;
					/*
					 * Schleife zum Simulieren der Spielzuege
					 */
					if(cPlayer.isAI()) {
						if (cPlayer.getPhase() == 1) {
							if(lastMove != null) {
								lastSlot = lastMove.toSlot();
							}
							de.hshannover.inform.muehle.strategy.Slot slota = cPlayer.placeStone(lastSlot, reMove);
							Slot slotb = new Slot(slota.getColumn(),slota.getRow());
							playerMove = new Move(null,(Slot) slotb);
						}
						else {
							 de.hshannover.inform.muehle.strategy.Move aMove = cPlayer.doMove(lastMove, reMove);
							 de.hshannover.inform.muehle.strategy.Slot fromSlot = new Slot(aMove.fromSlot().getColumn(),aMove.fromSlot().getRow());
							 de.hshannover.inform.muehle.strategy.Slot toSlot = new Slot(aMove.toSlot().getColumn(),aMove.toSlot().getRow());

							 
							 playerMove = new Move((Slot)fromSlot,(Slot)toSlot); // I put on my Robe and my Wizard Hat <:|
						}
						moveAvailable = true;
						if (!isValidMove(playerMove)) {
							winner = players.get(currentPlayer.getColor().getOpponent());
							System.out.println("Invalid Move from AI: "+ playerMove.toString());
							break;
						}
					} else {
						do {
							try {
								sleep(100);
							} catch (InterruptedException e) {}
						} while (!moveAvailable);
					}
					reMove = null;
					lastMove = playerMove;

						if (lastMove.toSlot() != null &&
								currentPlayer.getPhase() == 1) {
							cPlayer.increaseStones();
							gameboard.applySlot(lastMove.toSlot(), currentPlayer.getColor().getSlotStatus());
							logger.addEntry(lastMove.toSlot());
						} else if (currentPlayer.getPhase() > 1) {
							executeMove(lastMove);
						}
						setObservableChanged(true);
						notifyObserver();	
					
						/*
						 * Pruefung, ob eine Muehle geschlossen wurde. Wenn ja
						 * entsprechender Aufruf an AI/ GUI zum entfernen eines 
						 * Spielsteins.
						 */
						
						if (lastMove.toSlot() != null &&
							isInMill(gameboard.returnSlot(lastMove.toSlot()))) {
							closedMill = true;
							System.out.println("Muehle: "+closedMill);
							if (cPlayer.isAI()) {
								de.hshannover.inform.muehle.strategy.Slot slota =cPlayer.removeStone();
								playerMove = new Move (new Slot(slota.getColumn(),slota.getRow()),null);
								if(!isValidMove(playerMove)) {
									winner = players.get(currentPlayer.getColor().getOpponent());
									System.out.println("Invalid Move from AI: "+ playerMove.toString());
									break;
								}
							} else {
								
								setObservableChanged(true);
								notifyObserver();
								do {
									try {
										sleep(100);
									} catch (InterruptedException e) {}
								} while (!removeableStone);
							}
							reMove = playerMove.fromSlot();
							closedMill = false;
							removeableStone = false;
						} else {
							reMove = null;
						}
						
						if (reMove != null) {
							players.get(currentPlayer.getColor().getOpponent()).decreaseNumberOfStones();
							gameboard.removeStone(reMove);
							System.out.println("Stein entfernt: " + reMove.toString());
						}
						setObservableChanged(true);
						notifyObserver();
						winner = checkWinner();
						if (winner != null) { 
							gameStopped = true;
							System.out.println("Gewinner: "+winner);
						}
						moveAvailable = false;
						currentPlayer = players.get(currentPlayer.getColor().getOpponent());
						System.out.println(currentPlayer.getColor());
					
				}System.out.println("Winner " + winner.getColor().toString());
			}
			
		};
		
		gameThread.start();
	}
	
	/**
	 * Gives a Move to the Controller. Used for GUI-Interaction
	 * @param m The Move of the Player.
	 */
	public void givePlayerMove(Move move) throws InvalidMoveException {
		if(!isValidMove(move)) throw new InvalidMoveException();
		playerMove = move;
		this.moveAvailable = true;
		gameThread.interrupt();
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
	 * @param move The Move to be checked.
	 * @return If the Move is true or not.
	 */
	private boolean isValidMove(Move move) {
		Slot startSlot = move.fromSlot();
		Slot endSlot = move.toSlot();
		
		if (startSlot != null && endSlot != null &&
			startSlot.hashCode() == endSlot.hashCode()) return false;
		
		if (closedMill && startSlot != null && endSlot == null) {
			if (canRemove(startSlot)) {
				removeableStone = true;
				return true;
			}
			return false;
		}
		
		/*
		 * Move-Evaluation fuer Spielzuege in Phase 2: Ist das Endfeld
		 * Nachbarfeld des Startfeldes und ist das Endfeld nicht belegt, dann
		 * ist der Zug gueltig.
		 * Abfangen von Einzelklicks auf dem Feld, wenn nur Zug entgegen genommen
		 * werden soll (sonst NullpointerException).
		 * Abfangen, wenn ein Spielstein der anderen Farbe verwendet wurde.
		 */
		if (currentPlayer.getPhase() == 2) {
			if (move.fromSlot() == null ||
				move.toSlot() == null ||
				gameboard.returnSlot(startSlot).getStatus() != currentPlayer.getColor().getSlotStatus())
				return false;
			Slot[] slotNeighbour = gameboard.getNeighbours(startSlot);
			
			for (int i = 0; i <= 3; i++) {
				if (slotNeighbour[i] != null &&
					slotNeighbour[i].hashCode() == endSlot.hashCode() &&
					slotNeighbour[i].getStatus() == Slot.Status.EMPTY) return true; 
			}
		/*
		 * Move-Evaluation fuer das Spielfeld in Phase 1: Ist das Endfeld leer, dann
		 * darf der Stein in das Feld gesetzt werden. Das Startfeld ist in diesem
		 * Fall leer.
		 */
		} else if (currentPlayer.getPhase() == 1) {
			if (startSlot != null) return false;
			if (endSlot.getStatus() == Slot.Status.EMPTY) return true;
		/*
		 * Move-Evaluation fuer das Spielfeld in Phase 3: Ist das Endfeld leer, dann
		 * darf der Stein dorthin gesetzt werden. Das Startfeld ist in diesem Fall nicht
		 * relevant, da man an ein beliebiges Feld springen darf.
		 */
		} else if (currentPlayer.getPhase() == 3 &&
				   startSlot != null && endSlot != null) {
		if (this.gameboard.returnSlot(startSlot).getStatus() == currentPlayer.getColor().getSlotStatus() &&
			endSlot.getStatus() == Slot.Status.EMPTY) return true;
		}
		
		return false;
	}
	
	/**
	 * Executes a Move on the Gameboard. 
	 * @param move A VALID(!) Move.
	 */
	private void executeMove(Move move) {
//		System.out.println("Spielfeld vor Zugausfuehrung.\n");
//		System.out.println(gameboard.toString());
		gameboard.applyMove(move);
		logger.addEntry(move);
//		System.out.println("Spielfeld nach Zugausfuehrung.\n");
//		System.out.println(gameboard.toString());
		System.out.println("Letzter Zug: \n");
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
		if (slot.getStatus() == Slot.Status.EMPTY) return false;
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
		ArrayList<Slot> pSlot = gameboard.getStonesFromColor(
				                               currentPlayer.getColor().getOpponent().getSlotStatus());
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
	private Player checkWinner() {
		Player winner = null;
		
		for (Player.Color cStatus: players.keySet()) {
			Player cPlayer = players.get(cStatus);
			
			/*
			 * Gewinnbedingung fuer die Anzahl der Steine (n < 3)
			 */
			if (cPlayer.getStones() < 3 && cPlayer.getPhase() > 1) {
				return players.get(cPlayer.getColor().getOpponent());
			} else {
				winner = null;
			}
			
			if (cPlayer.getPhase() == 2) {
				/*
				 * Gewinnbedingung, wenn kein Zug mehr moeglich ist
				 */
				winner = players.get(cPlayer.getColor().getOpponent());
				ArrayList<Slot> slotList = gameboard.getStonesFromColor(cStatus.getSlotStatus());
				
				for (Slot iteSlot: slotList) {
					Slot[] neighbours = gameboard.getNeighbours(iteSlot);
					for (int i = 0; i <= 3; i++) {
						if (neighbours[i] != null && 
							neighbours[i].getStatus() == Slot.Status.EMPTY) 
							winner = null;
					}
				}
			}
			if (winner != null) return winner;
		}
		
		return winner;
	}
	
	/** Getter for the Logger object
	 * 
	 * @return
	 */
	public Logger getLogger() {
		return logger;
	}
}
