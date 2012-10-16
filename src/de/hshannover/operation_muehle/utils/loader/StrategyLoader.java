package de.hshannover.operation_muehle.utils.loader;

import java.io.IOException;
import java.util.ArrayList;

import de.hshannover.inform.muehle.strategy.Strategy;

public class StrategyLoader extends ClassLoader {

	public StrategyLoader(String searchPath)
		throws IOException {
		super(searchPath);
	}
	
	public Strategy getInstance(String klass)
		throws ClassNotFoundException,
		       InstantiationException,
		       IllegalAccessException {
		return (Strategy) super.getInstance(klass);
	}
	
	public ArrayList<Strategy> getAllStrategies() {
		ArrayList<Strategy> strategies = new ArrayList<Strategy>();
		
		for (Object strategy : super.getAllInstances()) {
			strategies.add((Strategy) strategy);
		}
		
		return strategies;
	}
}
