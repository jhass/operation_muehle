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
	private PlayerManager players;

	/**
	 * Konstruktor
	 * @param g Das aktuelle Spielfeld
	 * @param cp Der Spieler, der aktuell am Zug ist
	 * @param w Der Spieler, der das Spiel gewonnen hat
	 * @param log Die Notizen (Log) der Spielzuege
	 * @param players Informationen über die beiden beteiligten Spieler
	 */
	public SaveState(Gameboard g, Player cp, Player w, ArrayList<String> log,
			          PlayerManager players) {
		super(g, cp, w, log);
		this.setPlayers(players);
	}

	public PlayerManager getPlayers() {
		return this.players;
	}

	public void setPlayers(PlayerManager players) {
		this.players = players;
	}

}