package examples.reflection;

import java.io.IOException;

import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.utils.loader.StrategyLoader;

public class Main {
	public static void main(String[] args) {
		try {
			StrategyLoader loader = new StrategyLoader("./ki/");
			System.out.println("Available strategies:");
			for (Strategy strategy : loader.getAllStrategies()) {
				System.out.println(strategy.getStrategyName());
			}
		} catch (IOException e) {
			System.out.println("malicious content in ki folder");
		}
	}
}
