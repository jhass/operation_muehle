package de.hshannover.operation_muehle.logic;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Diese Klasse repraesentiert das Spielfeld. Beim Aufruf des Konstruktors werden 24 
 * Spielfelder in einer HashMap erzeugt, die alle gueltigen Felder des Muehlefeldes
 * abdecken. Diese Klasse ist primaer Datentragend, liefert auf Anfrage die Nachbarn
 * eines Feldes zurueck und erlaubt die Aenderung des Feldzustandes, sollte ein Stein
 * gesetzt, gezogen oder geschlagen werden.
 * @author Benjamin Held
 *
 */
public class Gameboard implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<Integer,Slot> board;
	
	/**
	 * Konstruktor
	 */
	public Gameboard() {
		this.board = new HashMap<Integer,Slot>();
		initializeSlots();
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
	 * @param c Wert der Spalte
	 * @param r Wert der Zeile
	 */
	public void generateNewPair(int c, int r) {
		Slot s = new Slot(r,c,0);
		int hashValue = s.hashCode(); 
		this.board.put(hashValue, s);
	}
	
	/**
	 * Sucht zu einem gegebenen Slot alle möglichen Nachbarslots und gibt diese dann
	 * zurueck. Findet sich in einer Richtung kein Nachbar wird null zurueckgegeben.
	 * @param s Slot, fuer den die Nachbarslots gesucht werden sollen
	 * @return Slot[]
	 */
	public Slot[] getNeighbours(Slot s) {
		Coordinate cTemp = new Coordinate(s.getRow(),s.getColumn());
		Slot[] neighbours = new Slot[4];
		neighbours[0] = getNeighbourSlot(cTemp,1,0);
		neighbours[1] = getNeighbourSlot(cTemp,0,1);
		neighbours[2] = getNeighbourSlot(cTemp,-1,0);
		neighbours[3] = getNeighbourSlot(cTemp,0,-1);
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
		int r = coord.row + rowIndex;
		int c = (int) (coord.column - 64) + columnIndex;
		if (r == 4 && c == 4) return null; /* Einige Felder auf dem mittleren
		                                       Ring wuerden sonst 4 Nachbarn finden,
		                                       obwohl sie nur 3 haben*/
		while ( (r > 0 && r < 8) && (c > 0 && c < 8)) {
			Coordinate help = new Coordinate(r, (char) (c+64));
			if (this.board.containsKey(help.hashCode()))
				 return this.board.get(help.hashCode());
			else {
				r += rowIndex;
				c += columnIndex;
			}
		}
		return null;
	}
	
	/**
	 * Die Methode aktualisiert das Spielfeld basierend auf den Informationen
	 * des Slots
	 * @param s
	 */
	public void applySlot(Slot s, int status) {
		Slot appSlot = null;
		if (this.board.containsKey(s.hashCode()))
			 appSlot = this.board.get(s.hashCode());
		appSlot.setStatus(status);
	}
	
	/**
	 * Die Methode aktualisiert das Spielfeld basierend auf den Informationen
	 * des Move-Objektes.
	 * @param m Das Move-Objekt, aus dem die Aenderungen uebernommen werde sollen
	 */
	public void applyMove(Move m) {
		Slot start = m.fromSlot();
		Slot end = m.toSlot();
		applySlot(end, start.getStatus());
	}
	
	/**
	 * Entfernen eines Steines aus einem Spielfeld, also aendern des Feldstatus fuer das
	 * uebergebene Feld
	 * @param s Spielfeld, aus dem der Stein entfernt wird
	 */
	public void removeStone(Slot s) {
		Slot toRemove = returnSlot(s); 
		toRemove.setStatus(0);
	}
	
	/**
	 * Suchen des zugehoerigen Slots auf dem Spielfeld aus der HashMap
	 * @param s gesuchtes Spielfeld
	 * @return  Slot
	 */
	public Slot returnSlot(Slot s) {
		Coordinate temp = new Coordinate(s.getRow(),s.getColumn());
		return this.board.get(temp);
	}
	
	/**
	 * Methode zur Rueckgabe des Spielfeldes
	 * @return Gameboard
	 */
	public Gameboard getGameboard() {
		return this;
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