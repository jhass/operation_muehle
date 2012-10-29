package examples.reflection;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;
import de.hshannover.operation_muehle.utils.loader.ClassFileLoader;
import de.hshannover.inform.muehle.strategy.Strategy;

public class TestAlex implements Strategy {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ClassFileLoader cfl = new ClassFileLoader(
				"/home/zero/workspace/operation_muehle/src/examples/reflection");
		ArrayList a = cfl.getClassesOfType(String.class);
		for (Iterator i = a.iterator(); i.hasNext();) {
			Object o = i.next();
			System.out.println(o.toString());
			Strategy test = (Strategy)((Class)o).newInstance();
			test.doMove(null, null, 0);
			test.getStrategyName();
			test.placeStone(null, null, 0);
		}
	}

	@Override
	public Move doMove(Move arg0, Slot arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroup() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStrategyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Slot placeStone(Slot arg0, Slot arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Slot removeStone(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
