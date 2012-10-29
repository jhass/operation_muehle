package examples.reflection;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;

public class TestKI implements de.hshannover.inform.muehle.strategy.Strategy {

	@Override
	public Move doMove(Move arg0, Slot arg1, int arg2) {
		System.out.println("do Move!!");
		// TODO Auto-generated method stub
		return new de.hshannover.operation_muehle.logic.Move(
				new de.hshannover.operation_muehle.logic.Slot(7,7,0));
	}

	@Override
	public int getGroup() {
		System.out.println("get Group!");
		// TODO Auto-generated method stub
		return 999999;
	}

	@Override
	public String getStrategyName() {
		System.out.println("test KKKIII!! BANSAI!!!!!!!");
		// TODO Auto-generated method stub
		return "testKI";
	}

	@Override
	public Slot placeStone(Slot arg0, Slot arg1, int arg2) {
		// TODO Auto-generated method stub
		System.out.println("place Stone!!");
		return new de.hshannover.operation_muehle.logic.Slot(7,7,0);
	}

	@Override
	public Slot removeStone(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("remove Stone!!!");
		return new de.hshannover.operation_muehle.logic.Slot(7,7,0);
	}

}
