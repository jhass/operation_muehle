package de.hshannover.operation_muehle.ki;

import de.hshannover.operation_muehle.logic.Gameboard;
import de.hshannover.operation_muehle.logic.Move;

public class MoveGenerator {
	
	MoveEvaluator eval;

	public MoveGenerator(){
		this.eval = new MoveEvaluator();
	}
	
	public Move generateMove(Gameboard board){
		Move result = null;
		
		return result;
	}
	
	public boolean validateMove(Move move){
		this.eval.evaluateMove(move);
		
		return false;
	}
	
	

}
