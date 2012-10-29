package examples.reflection;

import java.util.ArrayList;
import java.util.Iterator;

import de.hshannover.operation_muehle.utils.loader.StrategyLoader;
import de.hshannover.inform.muehle.strategy.Strategy;

public class TestAlex{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		StrategyLoader cfl = new StrategyLoader(
				"/home/zero/workspace/operation_muehle/src/examples/reflection");
		ArrayList<Class<?>> a = cfl.getClasses(String.class);
		for (Iterator<Class<?>> i = a.iterator(); i.hasNext();) {
			Class<?> o = i.next();
			Strategy s = (Strategy)o.newInstance();
			System.out.println("=========================================");
			System.out.println("Class: "+o);
			System.out.println("Strategy: "+s.getStrategyName());
			System.out.println("Group: "+s.getGroup());
			System.out.println("doMove: "+s.doMove(null, null, 0));
			System.out.println("placeStone: "+s.placeStone(null, null, 0));
			System.out.println("removeStone: "+s.removeStone(0));
		}
	}

}
