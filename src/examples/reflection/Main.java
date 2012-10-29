package examples.reflection;

import java.io.IOException;

import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.utils.loader.StrategyLoader;

public class Main {
	public static void main(String[] args) throws IOException {
		StrategyLoader loader = new StrategyLoader("lib/");
		System.out.println("Available strategies:");
		for (Strategy strategy : loader.getAllStrategies()) {
			System.out.println(strategy.getStrategyName());
			System.out.println(strategy.getClass().getName());
		}
	}
}
