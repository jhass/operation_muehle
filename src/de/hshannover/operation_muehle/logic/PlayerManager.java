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
	
	public synchronized Player getCurrent() {
		return currentPlayer;
	}
	
	public synchronized Player getOpponent() {
		return opponent;
	}
	
	public synchronized void opponentsTurn() {
		Player tmp = currentPlayer;
		currentPlayer = opponent;
		opponent = tmp;
		Logger.logDebugf("Switched current player to %s", currentPlayer);
	}

	public synchronized boolean isCurrentPlayerAI() {
		return currentPlayer.isAI();
	}
	
	public synchronized boolean isOpponentAI() {
		return opponent.isAI();
	}

	public synchronized int getCurrentPlayersPhase() {
		return currentPlayer.getPhase();
	}
	
	public synchronized String getCurrentPlayersDisplayName() {
		return currentPlayer.getDisplayName();
	}
	
	public synchronized String getOpponentsDisplayName() {
		return opponent.getDisplayName();
	}
	
	public synchronized boolean isCurrentPlayersPhase(int phase) {
		return currentPlayer.getPhase() == phase;
	}
	
	public synchronized boolean isCurrentPlayersPhaseNot(int phase) {
		return !isCurrentPlayersPhase(phase);
	}

	public synchronized void increaseCurrentPlayersStones() {
		currentPlayer.increaseStones();
	}

	public synchronized Status getCurrentPlayersSlotStatus() {
		return currentPlayer.getSlotStatus();
	}
	
	public synchronized Status getOpponentsSlotStatus() {
		return opponent.getSlotStatus();
	}

	public synchronized void decreaseOpponentsNumberOfStones() {
		opponent.decreaseNumberOfStones();
	}

	public synchronized Player getOpponentOf(Player player) {
		if (isCurrentPlayer(player)) {
			return opponent;
		} else if (isOpponent(player)) {
			return currentPlayer;
		} else {
			return null;
		}
		
	}
	
	public synchronized boolean isCurrentPlayer(Player player) {
		return player == currentPlayer;
	}
	
	public synchronized boolean isOpponent(Player player) {
		return player == opponent;
	}
	
	public synchronized Player getWhitePlayer() {
		if (currentPlayer.getColor() == Player.Color.WHITE) {
			return currentPlayer;
		} else {
			return opponent;
		}
	}
	
	public synchronized Player getBlackPlayer() {
		return getOpponentOf(getWhitePlayer());
	}
	
	@Override
	public Iterator<Player> iterator() {
		return players.iterator();
	}

}
