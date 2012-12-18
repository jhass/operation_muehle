package de.hshannover.operation_muehle.logic;

import java.io.Serializable;

/**
 * Die Klasse GameState hält regelmäßig den aktuellen Zustand des Spiels fest.
 * @author Benjamin Held
 *
 */
public class State implements Serializable {
	private static final long serialVersionUID = 1L;
	public Gameboard currentGB;
	public PlayerManager players;
	public Player winner;
	public Logger logger;
	public boolean inRemovalPhase;
	
	/**
	 * Konstruktor
	 * @param gameboard Das aktuelle Spielfeld
	 * @param players Der aktuelle PlayerManager
	 * @param log Die Notizen (Log) der Spielzuege
	 */
	public State(Gameboard gameboard, PlayerManager players, Logger logger) {
		this.currentGB = gameboard;
		this.players = players;
		this.logger = logger;
		this.inRemovalPhase = false;
		this.winner = null;
	}

}