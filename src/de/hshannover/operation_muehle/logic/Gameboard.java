package de.hshannover.operation_muehle.logic;

import java.util.HashMap;

/**
 * Diese Klasse repraesentiert das Spielfeld. Beim Aufruf des Konstruktors werden 24 
 * Spielfelder in einer HashMap erzeugt, die alle gueltigen Felder des Muehlefeldes
 * abdecken. Diese Klasse ist primaer Datentragend, liefert auf Anfrage die Nachbarn
 * eines Feldes zurueck und erlaubt die Aenderung des Feldzustandes, sollte ein Stein
 * gesetzt, gezogen oder geschlagen werden.
 * !!! Vorlaeufige Version !!! Es bleibt zu klären, ob HashMap oder andere Collectionart nicht
 * effizienter ist.
 * @author Benjamin Held
 *
 */
public class Gameboard {
	private HashMap<Coordinate,Slot> board;
	
	/**
	 * Konstruktor
	 */
	public Gameboard() {
		this.board= new HashMap<Coordinate,Slot>();
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
		}
		for (int i = 1; i<4; i++) {
			generateNewPair(4,i);
			generateNewPair(4,4+i);
		}
	}
	
	public void generateNewPair(int c, int r) {
		Coordinate cTemp= new Coordinate(r,(char) (c+64));
		Slot s= new Slot(r,c,0);
		this.board.put(cTemp, s);
	}
	
	/**
	 * Comming soon
	 * @param s
	 * @return
	 */
	public Slot[] getNeighbours(Slot s) {
		return null;
	}
	
	/**
	 * Noch bearbeitungsbeduerftig, da die "Setzzuege" aus Phase 1 zu einer Exception fuehren.
	 * @param m
	 */
	public void applyMove(Move m) {
		Slot start = m.fromSlot();
		Slot end = m.toSlot();
		end.setStatus(start.getStatus());
		this.removeStone(start);
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
			this.column= c;
			this.row= r;
		}
	}
}
