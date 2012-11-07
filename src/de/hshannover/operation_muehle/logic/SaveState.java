package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Diese Klasse erbt von der Klasse GameState und dient zum Speichern eines Spielstandes
 * in eine Datei, wenn das Spiel beendet werden soll.
 * @author Benjamin Held
 *
 */
public class SaveState extends GameState {
	private static final long serialVersionUID = 1L;
	private HashMap<SlotStatus, Player> players;

	/**
	 * Konstruktor
	 * @param g Das aktuelle Spielfeld
	 * @param cp Der Spieler, der aktuell am Zug ist
	 * @param w Der Spieler, der das Spiel gewonnen hat
	 * @param log Die Notizen (Log) der Spielzuege
	 * @param players Informationen über die beiden beteiligten Spieler
	 */
	public SaveState(Gameboard g, SlotStatus cp, SlotStatus w, ArrayList<String> log,
			          HashMap<SlotStatus, Player> players) {
		super(g, cp, w, log);
		this.setPlayers(players);
	}

	public HashMap<SlotStatus, Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(HashMap<SlotStatus, Player> players) {
		this.players = players;
	}

}