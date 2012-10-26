package de.hshannover.operation_muehle.logic;

/**
 * Diese Klasse realisiert ein Spieler-Objekt. Es handelt sich hierbei um
 * eine reine datentragende Klasse, die alle notwendigen Attribute eines
 * Spielers verwaltet und zurückgeben kann.
 * @author Benjamin Held
 *
 */
public class Player {
	private String name;
	private int color;
	private int stones;
	private boolean isAI;
	private int thinkTime;
	private int phase;
	
	/**
	 * Konstruktor fuer Spieler bei einem neuen Spiel.
	 * @param name Name des Spielers
	 * @param color Farbe des Spielers (Constraint 1 oder 2)
	 * @param isAI logischer Wert zur Unterscheidung von menschlichen
	 * Spieler und kuenstlicher Intelligenz
	 * @param thinkTime Denkzeit der kuenstlichen Intelligenz 
	 * (Constraint > 0) 
	 */
	public Player(String name, int color, boolean isAI, int thinkTime) {
		if (color != 1 && color !=2) 
			throw new IllegalArgumentException("Player.Color ungueltig!");
		if (thinkTime < 0)
			throw new IllegalArgumentException("Player.Thinktime ungueltig!");
		this.name = name;
		this.color = color;
		this.isAI = isAI;
		this.thinkTime = thinkTime;
		this.stones = 9;
		this.phase = 1;
	}
	/**
	 * Konstruktor fuer Spieler bei einem geladenen Spielstand.
	 * @param name Name des Spielers
	 * @param color Farbe des Spielers
	 * @param isAI logischer Wert zur Unterscheidung von menschlichen
	 * Spieler und kuenstlicher Intelligenz
	 * @param thinkTime Denkzeit der kuenstlichen Intelligenz
	 * @param stones Array mit den Feldern, in denen der Spieler Spielsteine
	 * hat
	 * @param phase Spielphase, in der sich der Spieler befindet 
	 */
	public Player(String name, int color, boolean isAI, int thinkTime, 
			       Slot[] stones, int phase) {
		this.name = name;
		this.color = color;
		this.isAI = isAI;
		this.thinkTime = thinkTime;
		this.stones = stones.length;
		this.phase = phase;
	}
	
	/**
	 * Methode zum reduzieren der Speilsteine um 1 (wenn ein Stein vom Feld entfernt wurde)
	 */
	public void removeStone() {
		this.stones--;
	}
	
	/**
	 * Methode zur Aenderung der Spielphase um 1
	 */
	public void changePhase() {
		this.phase++;
	}
	
	/**
	 * Getter fuer den Namen
	 * @return String
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter für die Farbe
	 * @return int
	 */
	public int getColor() {
		return this.color;
	}
	
	/**
	 * Getter für die Anzahl der Spielsteine
	 * @return int
	 */
	public int getStones() {
		return this.stones;
	}
	
	/**
	 * Getter für die Spielphase
	 * @return int
	 */
	public int getPhase() {
		return this.phase;
	}
	
	/**
	 * Getter für die Denkzeit
	 * @return int
	 */
	public int getThinkTime() {
		return this.thinkTime;
	}
	
	/**
	 * Getter für die Information, um was fuer einen Spieler es sich handelt
	 * @return boolean
	 */
	public boolean isAI() {
		return this.isAI;
	}
}