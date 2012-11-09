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
	public SlotStatus currentPlayer;
	public SlotStatus winner;
	public ArrayList<String> log;
	
	/**
	 * Konstruktor
	 * @param g Das aktuelle Spielfeld
	 * @param cp Der Spieler, der aktuell am Zug ist
	 * @param w Der Spieler, der das Spiel gewonnen hat
	 * @param log Die Notizen (Log) der Spielzuege
	 */
	public GameState(Gameboard g, SlotStatus cp, SlotStatus w, ArrayList<String> log) {
		this.currentGB = g;
		this.currentPlayer = cp;
		this.winner = w;
		this.log = log;
	}

}