package de.hshannover.operation_muehle.logic;

import java.io.Serializable;

/**
 * Diese Klasse realisiert ein einzelnes Feld des Spielbrettes. Es hat ein 
 * Koordinatentupel (row,column) und einen Status (white, black, empty).
 * Intern werden sowohl Zeilen- als auch Spaltenindex als Zahl gespeichert.
 * Die Rueckgabe des Spaltenindex ist ein Char, da das Interface diesen
 * Rueckgabewert fordert.
 * @author Benjamin Held
 *
 */
public class Slot implements de.hshannover.inform.muehle.strategy.Slot,
                                Serializable {
	private static final long serialVersionUID = 1L;
	private SlotStatus status;
	private int row;
	private int column;
	
	/**
	 * Konstruktor, der den Status des Slot entsprechend der Eingabe besetzt
	 * @param row Zeilenindex (Constraint >0)
	 * @param column Spaltenindex (Constraint >0)
	 * @param status Attribut zur Beschreibung des Spielfeldinhaltes
	 *         (0= empty, 1= white, 2=black)
	 */
	public Slot(int row, int column, SlotStatus status) {
		if (row<0) 
			throw new IllegalArgumentException("Slot.Row ungueltig!");
		if (column<0) 
			throw new IllegalArgumentException("Slot.Column ungueltig!");
		this.status = status;
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Konstruktor, der den Status automatisch auf unbenutzt
	 * @param row Zeilenindex (Constraint >0)
	 * @param column Spaltenindex (Constraint >0)
	 */
	public Slot(int row, int column) {
		if (row<0) 
			throw new IllegalArgumentException("Slot.Row ungueltig!");
		if (column<0) 
			throw new IllegalArgumentException("Slot.Column ungueltig!");
		this.status = SlotStatus.FREE;
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Setzen bzw. Aendern eines Feldzustandes
	 * @param status Statusindex
	 */
	public void setStatus(SlotStatus status) {
		this.status = status;
	}
	
	/**
	 * Get-Methode fuer den Feldzustand
	 * @return int 
	 */
	public SlotStatus getStatus() {
		return this.status;
	}
	
	/**
	 * Get-Methode fuer den Spaltenindex
	 * Rückgabe als Character zur Einhaltung der Infacemethode
	 * @return char
	 */
	@Override
	public char getColumn() {
		return (char)(this.column+64);
	}
	
	/**
	 * Get-Methode fuer den Zeilenindex
	 * @return int
	 */
	@Override
	public int getRow() {
		return this.row;
	}
	
	@Override
	public int hashCode() {
		return this.column*10+this.row;
	}
}