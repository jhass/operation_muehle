package de.hshannover.operation_muehle.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import de.hshannover.operation_muehle.logic.Slot.Status;

/** Manages players participating in a game
 * 
 * @author Jonne Haß
 *
 */
public class PlayerManager implements Serializable, Iterable<Player> {
	private static final long serialVersionUID = 1L;
	
	private Player currentPlayer;
	private Player opponent;
	private ArrayList<Player> players;
	
	public PlayerManager(PlayerOptions white, PlayerOptions black) {
		currentPlayer = new Player(white, Player.Color.WHITE);
		opponent = new Player(black, Player.Color.BLACK);
		players = new ArrayList<Player>();
		players.add(currentPlayer);
		players.add(opponent);
	}
	
	public Player getCurrent() {
		return currentPlayer;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
	public void opponentsTurn() {
		Player tmp = currentPlayer;
		currentPlayer = opponent;
		opponent = tmp;
	}

	public boolean isCurrentPlayerAI() {
		return currentPlayer.isAI();
	}
	
	public boolean isOpponentAI() {
		return opponent.isAI();
	}

	public int getCurrentPlayersPhase() {
		return currentPlayer.getPhase();
	}
	
	public boolean isCurrentPlayersPhase(int phase) {
		return currentPlayer.getPhase() == phase;
	}
	
	public boolean isCurrentPlayersPhaseNot(int phase) {
		return !isCurrentPlayersPhase(phase);
	}

	public void increaseCurrentPlayersStones() {
		currentPlayer.increaseStones();
	}

	public Status getCurrentPlayersSlotStatus() {
		return currentPlayer.getSlotStatus();
	}
	
	public Status getOpponentsSlotStatus() {
		return opponent.getSlotStatus();
	}

	public void decreaseOpponentsNumberOfStones() {
		opponent.decreaseNumberOfStones();
	}

	public Player getOpponentOf(Player player) {
		if (isCurrentPlayer(player)) {
			return opponent;
		} else if (isOpponent(player)) {
			return currentPlayer;
		} else {
			return null;
		}
	}
	
	public boolean isCurrentPlayer(Player player) {
		return player == currentPlayer;
	}
	
	public boolean isOpponent(Player player) {
		return player == opponent;
	}
	
	public Player getWhitePlayer() {
		if (currentPlayer.getColor() == Player.Color.WHITE) {
			return currentPlayer;
		} else {
			return opponent;
		}
	}
	
	public Player getBlackPlayer() {
		return getOpponentOf(getWhitePlayer());
	}
	
	@Override
	public Iterator<Player> iterator() {
		return players.iterator();
	}

}
