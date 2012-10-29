package de.hshannover.operation_muehle.logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Die Klasse GameState hält regelmäßig den aktuellen Zustand des Spiels fest.
 * @author Benjamin Held
 *
 */
public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	public Gameboard currentGB;
	public int currentPlayer;
	public int winner;
	public ArrayList<String> log;
	
	/**
	 * Konstruktor
	 * @param g Das aktuelle Spielfeld
	 * @param cp Der Spieler, der aktuell am Zug ist
	 * @param w Der Spieler, der das Spiel gewonnen hat
	 * @param log Die Notizen (Log) der Spielzuege
	 */
	public GameState(Gameboard g, int cp, int w, ArrayList<String> log) {
		if (cp != 1 && cp !=2) 
			throw new IllegalArgumentException("GameState.CurrentPlayer ungueltig!");
		if (winner != 1 && winner !=2) 
			throw new IllegalArgumentException("GameState.Winner ungueltig!");
		this.currentGB = g;
		this.currentPlayer = cp;
		this.winner = w;
		this.log = log;
	}

}