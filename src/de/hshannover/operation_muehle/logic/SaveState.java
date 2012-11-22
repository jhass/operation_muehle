package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;

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
	 * @param winner Der Spieler, der das Spiel gewonnen hat
	 * @param log Die Notizen (Log) der Spielzuege
	 */
	public SaveState(Gameboard gameboard, PlayerManager players, Player winner, ArrayList<String> log) {
		super(gameboard, players, winner, log);
	}
}