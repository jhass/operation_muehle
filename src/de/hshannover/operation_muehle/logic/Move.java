package de.hshannover.operation_muehle.logic;

/**
 * Diese Klasse realisiert einen Spielzug, bestehend aus zwei Spielfeld-
 * Objekten.
 * @author Benjamin Held
 *
 */
public class Move implements de.hshannover.inform.muehle.strategy.Move {
	private Slot startSlot;
	private Slot endSlot;
	
	/**
	 * Konstruktor fuer die Zugphase
	 * @param start Spielfeld, von dem gezogen wird
	 * @param end Spielfeld, in das gezogen wird
	 */
	public Move(Slot start, Slot end) {
		this.startSlot = start;
		this.endSlot = end;
	}
	
	/**
	 * Kontruktor fuer die Setzphase
	 * @param end Spielfeld, in das ein Stein gesetzt wird
	 */
	public Move(Slot end) {
		this.startSlot = null;
		this.endSlot= end;
	}
	
	/**
	 * Gibt das Startfeld zurück.
	 * @return Slot
	 */
	@Override
	public Slot fromSlot() {
		return startSlot;
	}
	
	/**
	 * Gibt das Endfeld zurück.
	 * @return Slot
	 */
	@Override
	public Slot toSlot() {
		return endSlot;
	}
}