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
	public PlayerManager players;
	public Player winner;
	public ArrayList<String> log;
	
	/**
	 * Konstruktor
	 * @param gameboard Das aktuelle Spielfeld
	 * @param players Der aktuelle PlayerManager
	 * @param winner Der Spieler, der das Spiel gewonnen hat
	 * @param log Die Notizen (Log) der Spielzuege
	 */
	public GameState(Gameboard gameboard, PlayerManager players,
				     Player winner, ArrayList<String> log) {
		this.currentGB = gameboard;
		this.players = players;
		this.winner = winner;
		this.log = log;
	}

}