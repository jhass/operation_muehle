package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;

/**
 * Diese Klasse erbt von der Klasse GameState und dient zum Speichern eines Spielstandes
 * in eine Datei, wenn das Spiel beendet werden soll.
 * @author Benjamin Held
 *
 */
public class SaveState extends GameState {
	private Player[] players;

	/**
	 * Konstruktor
	 * @param g Das aktuelle Spielfeld
	 * @param cp Der Spieler, der aktuell am Zug ist
	 * @param w Der Spieler, der das Spiel gewonnen hat
	 * @param log Die Notizen (Log) der Spielzuege
	 * @param players Informationen über die beiden beteiligten Spieler
	 */
	public SaveState(Gameboard g, int cp, int w, ArrayList<String> log,
			          Player[] players) {
		super(g, cp, w, log);
		this.players = players;
	}

}