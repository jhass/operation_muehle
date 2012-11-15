package de.hshannover.operation_muehle.logic;

import java.io.Serializable;

import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.Facade;

/**
 * Diese Klasse realisiert ein Spieler-Objekt. Es handelt sich hierbei um
 * eine reine datentragende Klasse, die alle notwendigen Attribute eines
 * Spielers verwaltet und zurückgeben kann.
 * @author Benjamin Held
 *
 */
public class Player implements Serializable {
	public enum Color {
		WHITE {
			public Color getOpponent() {
				return BLACK;
			}
			
			public Slot.Status getSlotStatus() {
				return Slot.Status.WHITE;
			}
		},
		BLACK {
			public Color getOpponent() {
				return WHITE;
			}
			
			public Slot.Status getSlotStatus() {
				return Slot.Status.BLACK;
			}

			
		};
		
		abstract public Color getOpponent();
		abstract public Slot.Status getSlotStatus();
		public boolean hasStoneOn(Slot slot) {
			return slot.getStatus() == getSlotStatus();
		}
	}
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Color color;
	private int stones;
	private boolean isAI;
	private int thinkTime;
	private int phase;
	private Strategy aiStrategy;
	private int availableStones= 9;
	
	/**
	 * Konstruktor fuer Spieler bei einem neuen Spiel. Geandert, um der 
	 * Initialisierung durch PlayerOptions genuege zu tun.
	 * @param name Name des Spielers
	 * @param color Farbe des Spielers (Constraint 1 oder 2)
	 * @param isAI logischer Wert zur Unterscheidung von menschlichen
	 * Spieler und kuenstlicher Intelligenz
	 * @param thinkTime Denkzeit der kuenstlichen Intelligenz 
	 * (Constraint > 0) 
	 */
	public Player(PlayerOptions oPlayer, Color color) {
		if (thinkTime < 0)
			throw new IllegalArgumentException("Player.Thinktime ungueltig!");
		this.name = oPlayer.getName();
		this.color = color;
		this.isAI = oPlayer.isAI();
		this.thinkTime = oPlayer.getThinkTime();
		this.stones = 0;
		this.phase = 1;
		if (this.isAI)	this.aiStrategy = Facade.getInstance().getStrategyLoader().getInstance(this.name);
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
	public Player(String name, Color color, boolean isAI, int thinkTime, 
			       Slot[] stones, int phase) {
		this.name = name;
		this.color = color;
		this.isAI = isAI;
		this.thinkTime = thinkTime;
		this.stones = stones.length;
		this.phase = phase;
	}
	
	public void increaseStones() {
		this.stones++;
		this.availableStones--;
		if (this.availableStones == 0) {
			this.phase++;
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("Player "+this.color+" enters Phase "+this.phase);
			if (this.stones == 3) this.phase++; //??
		}
	}
	
	/**
	 * Methode zum reduzieren der Spielsteine um 1 (wenn ein Stein vom Feld entfernt wurde)
	 */
	public void decreaseNumberOfStones() {
		this.stones--;
		if (this.stones == 3 && this.phase == 2) {
			this.phase++;
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("Entering Phase " + this.phase);
		}
	}
	
	/**
	 * Calls removeStone() on the AI with the thinktime specified in the Object.
	 * @see Strategy
	 */
	public de.hshannover.inform.muehle.strategy.Slot removeStone() {
		return aiStrategy.removeStone(thinkTime);
	}
	
	/**
	 * Methode zur Aenderung der Spielphase um 1
	 */
	public void nextPhase() {
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
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Getter für die Anzahl der Spielsteine
	 * @return int
	 */
	public int getStones() {
		return this.stones;
	}
	
	public int getAvailableStones() {
		return this.availableStones;
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
	
	/**
	 * Calls doMove on AI
	 * @param last Last Move.
	 * @param removed Last removed Stone. Null if nothing has been removed.
	 * @return
	 */
	public Move doMove(Move last, Slot removed ) {
		return (Move) this.aiStrategy.doMove(last, (de.hshannover.inform.muehle.strategy.Slot)removed , this.thinkTime);
	}
	
	/**
	 * Calls placeStone on the AI.
	 * @param last Last placed Stone.
	 * @param removed Last removed Stone. Null if nothing has been removed.
	 * @return
	 */
	public de.hshannover.inform.muehle.strategy.Slot placeStone(Slot last, Slot removed) {
		return (de.hshannover.inform.muehle.strategy.Slot)this.aiStrategy.placeStone(
					(de.hshannover.inform.muehle.strategy.Slot)last, 
					(de.hshannover.inform.muehle.strategy.Slot)removed, this.thinkTime);
	}
}
