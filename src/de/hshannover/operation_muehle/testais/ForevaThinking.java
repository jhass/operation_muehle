package de.hshannover.operation_muehle.testais;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;
import de.hshannover.inform.muehle.strategy.Strategy;

public class ForevaThinking implements Strategy {

	@Override
	public Move doMove(Move arg0, Slot arg1, int arg2) {
		while (Integer.valueOf('0') != 1) {}
		return null;
	}

	@Override
	public int getGroup() {
		return 2;
	}

	@Override
	public String getStrategyName() {
		return "We're sinking!";
	}

	@Override
	public Slot placeStone(Slot arg0, Slot arg1, int arg2) {
		while (Integer.valueOf('0') != 1) {}
		return null;
	}

	@Override
	public Slot removeStone(int arg0) {
		while (Integer.valueOf('0') != 1) {}
		return null;
	}

}
