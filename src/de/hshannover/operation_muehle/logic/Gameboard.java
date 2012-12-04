package de.hshannover.operation_muehle.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Diese Klasse repraesentiert das Spielfeld. Beim Aufruf des Konstruktors werden 24 
 * Spielfelder in einer HashMap erzeugt, die alle gueltigen Felder des Muehlefeldes
 * abdecken. Diese Klasse ist primaer Datentragend, liefert auf Anfrage die Nachbarn
 * eines Feldes zurueck und erlaubt die Aenderung des Feldzustandes, sollte ein Stein
 * gesetzt, gezogen oder geschlagen werden.
 * @author Benjamin Held
 *
 */
public class Gameboard implements Serializable, Iterable<Slot> {
	private static final long serialVersionUID = 1L;
	private HashMap<Integer,Slot> board;
	
	/**
	 * Konstruktor
	 */
	public Gameboard() {
		this.board = new HashMap<Integer,Slot>();
		initializeSlots();
	}
	
	public Gameboard(Gameboard gameboard) {
		this.board = new HashMap<Integer,Slot>();
		for (Slot slot : gameboard) {
			this.board.put(
				slot.hashCode(),
				new Slot(
					slot.getColumn(),
					slot.getRow(),
					slot.getStatus()
				)
			);
		}
	}
	
	/**
	 * Anlegen der gueltigen Spielfelder
	 */
	private void initializeSlots() {
		for (int i = 1; i<4; i++) {
			generateNewPair(1,1+(i-1)*3);
			generateNewPair(2,2*i);
			generateNewPair(3,i+2);
			generateNewPair(7,1+(i-1)*3);
			generateNewPair(6,2*i);
			generateNewPair(5,i+2);
			generateNewPair(4,i);
			generateNewPair(4,4+i);
		}

	}
	
	/**
	 * Erstellt ein Schluessel-Werte-Paar zur gegebenem Zeilen- und Spaltenindex
	 * @param column Wert der Spalte
	 * @param row Wert der Zeile
	 */
	public void generateNewPair(int column, int row) {
		Slot slot = new Slot(row, column, Slot.Status.EMPTY);
		int hashValue = slot.hashCode(); 
		this.board.put(hashValue, slot);
	}
	
	/**
	 * Sucht zu einem gegebenen Slot alle möglichen Nachbarslots und gibt diese dann
	 * zurueck. Findet sich in einer Richtung kein Nachbar wird null zurueckgegeben.
	 * Anordnung: 
	 * Slot[0]: oberhalb
	 * Slot[1]: rechts
	 * Slot[2]: unterhalb
	 * Slot[3]: links
	 * @param slot Slot, fuer den die Nachbarslots gesucht werden sollen
	 * @return Slot[]
	 */
	public Slot[] getNeighbours(Slot slot) {
		Coordinate slotCoordinate = new Coordinate(slot.getRow(),
				slot.getColumn());
		Slot[] neighbours = new Slot[4];
		neighbours[0] = getNeighbourSlot(slotCoordinate,1,0);
		neighbours[1] = getNeighbourSlot(slotCoordinate,0,1);
		neighbours[2] = getNeighbourSlot(slotCoordinate,-1,0);
		neighbours[3] = getNeighbourSlot(slotCoordinate,0,-1);
		return neighbours;
	}
	
	/**
	 * Diese Methode sucht in die vorgegebene Richtung (gegeben durch (rowIndex,
	 * columnIndex)) und gibt einen Slot zurueck, wenn in der Richtung ein 
	 * Nachbar existiert, sonst null, wenn in der Richtung kein Nachbar ist
	 * @param coord Koordinate, von der gesucht wird
	 * @param rowIndex Index, ob in Zeilenrichtung gesucht werden muss
	 * @param columnIndex Index, ob in Spaltenrichtung gesucht werden muss
	 * @return Slot
	 */
	public Slot getNeighbourSlot(Coordinate coord, int rowIndex, int columnIndex) {
		int row = coord.row + rowIndex;
		int column = (int) (coord.column - 64) + columnIndex;
		if (row == 4 && column == 4) return null; /* Einige Felder auf dem mittleren
		                                       Ring wuerden sonst 4 Nachbarn finden,
		                                       obwohl sie nur 3 haben*/
		while ( (row > 0 && row < 8) && (column > 0 && column < 8)) {
			Coordinate help = new Coordinate(row, (char) (column+64));
			if (this.board.containsKey(help.hashCode()))
				 return this.board.get(help.hashCode());
			else {
				row += rowIndex;
				column += columnIndex;
			}
		}
		return null;
	}
	
	/**
	 * Die Methode aktualisiert das Spielfeld basierend auf den Informationen
	 * des Slots
	 * @param slot
	 */
	public synchronized void applySlot(Slot slot, Slot.Status status) {
		if (this.board.containsKey(slot.hashCode())) {
			Slot appSlot = this.board.get(slot.hashCode());
			appSlot.setStatus(status);
		} else {
			throw new IllegalArgumentException("Slot nicht vorhanden! " +
					"Gameboard.ApplySlot hat nichts hinzugefuegt");
		}
	}
	
	/**
	 * Die Methode aktualisiert das Spielfeld basierend auf den Informationen
	 * des Move-Objektes.
	 * @param move Das Move-Objekt, aus dem die Aenderungen uebernommen werde sollen
	 */
	public synchronized void applyMove(Move move) {
		Slot start = move.fromSlot();
		Slot end = move.toSlot();
		applySlot(end,returnSlot(start).getStatus());
		applySlot(start, Slot.Status.EMPTY);
	}
	
	/**
	 * Diese Methode erhält einen SlotStatus und zählt, wie viele
	 * Steine dieses Status auf dem Spielfeld vorhanden sind und gibt die 
	 * gefundene Anzahl zurueck
	 * @param status Die Farbe der Steine, die gesucht werden soll
	 * @return int
	 */
	public int getNumberStones(Slot.Status status) {
		int count = 0;
		for (Slot slot: this.board.values()) {
			if (slot.getStatus() == status) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Entfernen eines Steines aus einem Spielfeld, also aendern des Feldstatus fuer das
	 * uebergebene Feld
	 * @param toRemove Spielfeld, aus dem der Stein entfernt wird
	 */
	public synchronized void removeStone(Slot toRemove) { 
		returnSlot(toRemove).setStatus(Slot.Status.EMPTY);
	}
	
	/**
	 * Suchen des zugehoerigen Slots auf dem Spielfeld aus der HashMap
	 * @param s gesuchtes Spielfeld
	 * @return  Slot
	 */
	public Slot returnSlot(Slot slot) {
		return this.board.get(slot.hashCode());
	}
	
	/**
	 * Methode zur Rueckgabe des Spielfeldes
	 * @return Gameboard
	 */
	public HashMap<Integer, Slot> getGameboard() {
		return this.board;
	}
	/**
	 * JavaDoc plx!
	 */
	@Override
	public Iterator<Slot> iterator() {
		return board.values().iterator();
	}
	
	/**
	 * Die Methode sucht alle Slots mit der gegebenen Farbe heraus und 
	 * gibt diese zurueck.
	 * @param status Die Farbe, fuer den die Steine gesucht werden sollen
	 * @return ArrayList<Slot>
	 */
	public ArrayList<Slot> getStonesOfStatus(Slot.Status status) {
		ArrayList<Slot> slotList = new ArrayList<Slot>();
		
		for (Slot slot: this.board.values()) {
			if (slot.getStatus() == status)
				slotList.add(slot);
		}
		
		return slotList;
	}
	
	/**
	 * Einfache toString-Methode zum Debuggen.
	 */
	@Override
	public String toString() {
		String s= "";
		for (Slot slot: this) {
			s += slot.toString()+"\n";
		}
		return s;
	}
	
	/**
	 * Hilfsklasse. Dient als Schluesselobjekt ( z.b. (A,1)) der Hashmap 
	 * @author Benjamin Held
	 * 
	 */
	private class Coordinate {
		private char column;
		private int row;
		
		public Coordinate(int r, char c) {
			this.column = c;
			this.row = r;
		}
		
		@Override
		public int hashCode() {
			return (int) (this.column-64) *10+this.row;
		}
	}
}