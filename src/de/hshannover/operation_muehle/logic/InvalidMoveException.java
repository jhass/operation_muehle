package de.hshannover.operation_muehle.logic;

public class InvalidMoveException extends Exception {
	private static final long serialVersionUID = 1L;
	public Move move;

	public InvalidMoveException(Move move) {
		this.move = move;
	}
}
