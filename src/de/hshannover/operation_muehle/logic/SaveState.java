package de.hshannover.operation_muehle.logic;

/**
 * Diese Klasse erbt von der Klasse GameState und dient zum Speichern eines Spielstandes
 * in eine Datei, wenn das Spiel beendet werden soll.
 * @author Benjamin Held
 *
 */
public class SaveState extends GameState {
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 * @param gameboard Das aktuelle Spielfeld
	 * @param players Der aktuelle PlayerManager
	 * @param log Die Notizen (Log) der Spielzuege
	 */
	public SaveState(Gameboard gameboard, PlayerManager players, Logger log) {
		super(new Gameboard(gameboard), players, log.copy());
	}
}