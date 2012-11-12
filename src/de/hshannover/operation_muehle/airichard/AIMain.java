package de.hshannover.operation_muehle.airichard;

import java.util.ArrayList;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;
import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.logic.Player;
/**
 * A working AI doing random Moves.
 * @author Stingray
 *
 */
public class AIMain implements Strategy {
	AIGameBoard gameboard;
	Player.Color me = Player.Color.WHITE;

	@Override
	public int getGroup() {
		return 2;
	}

	@Override
	public String getStrategyName() {
		return "QDaI";
	}

	@Override
	public Slot placeStone(Slot last, Slot removed, int thinkTime) {
		if(gameboard == null) {
			gameboard = new AIGameBoard();
		}
		
		if(last != null) {
			gameboard.applySlot((de.hshannover.operation_muehle.logic.Slot) last, 
						me.getOpponent().getSlotStatus());
		}
		
		if(removed != null) {
			gameboard.removeStone((de.hshannover.operation_muehle.logic.Slot) removed);
		}
		ArrayList<de.hshannover.operation_muehle.logic.Slot> freeSlots = gameboard.getFreeSlots();
		Slot mySlot = freeSlots.get(freeSlots.size()/2);
		gameboard.applySlot((de.hshannover.operation_muehle.logic.Slot) mySlot, 
				me.getSlotStatus());
		return mySlot;
	}
	
	@Override
	public Move doMove(Move last, Slot removed, int thinkTime) {
		if(last != null) {
			gameboard.applyMove((de.hshannover.operation_muehle.logic.Move) last);
		}
		
		if(removed != null) {
			gameboard.removeStone((de.hshannover.operation_muehle.logic.Slot) removed);
		}
		
		ArrayList<de.hshannover.operation_muehle.logic.Slot> myStones = gameboard.getMoveableStones();
		
		de.hshannover.operation_muehle.logic.Slot startSlot = null;
		de.hshannover.operation_muehle.logic.Slot endSlot = null;
		for(int i = myStones.size(); i > 0; i--){
			Slot[] neighbours = gameboard.getNeighbours(myStones.get(i)); 
			for(int j = 0; j < neighbours.length; j++){
				if(gameboard.returnSlot((de.hshannover.operation_muehle.logic.Slot) neighbours[j]).getStatus()
						== de.hshannover.operation_muehle.logic.Slot.Status.EMPTY) {
					startSlot = myStones.get(i);
					endSlot = (de.hshannover.operation_muehle.logic.Slot) neighbours[j];
				}
			}
		}
		Move myMove = new de.hshannover.operation_muehle.logic.Move(startSlot,endSlot);
		gameboard.applyMove((de.hshannover.operation_muehle.logic.Move) myMove);
		
		return myMove; 
	}

	@Override
	public Slot removeStone(int thinkTime) {
		ArrayList<de.hshannover.operation_muehle.logic.Slot> oppositeStones 
			= gameboard.getOppositeStones();
		return oppositeStones.get(oppositeStones.size()/2);
	}

}
