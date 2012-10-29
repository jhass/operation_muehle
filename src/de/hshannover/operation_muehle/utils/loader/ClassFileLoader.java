package de.hshannover.operation_muehle.utils.loader;

import java.io.IOException;
import java.util.ArrayList;

import de.hshannover.inform.muehle.strategy.Strategy;


/** Loads any found strategies
 *
 */
public class StrategyLoader extends ClassLoader {

	/**
	 * @see ClassLoader
	 */
	public StrategyLoader()
		throws IOException {
		super("KI_test");
	}
	
	/**
	 * @see ClassLoader
	 */
	public StrategyLoader(String searchPath)
		throws IOException {
		super(searchPath);
	}
	
	/** If possible returns an instance of the given Strategy using the default
	 *  constructor.
	 * 
	 * @throws ClassNotFoundException if the given class isn't available
	 * @throws InstantionException if the given class can't be instantiated
	 * @throws IllegalAccessException if the given class can't be accessed
	 * @throws ClassCastException if the given class is not a strategy
	 */
	public Strategy getInstance(String klass)
		throws ClassNotFoundException,
		       InstantiationException,
		       IllegalAccessException,
		       ClassCastException {
		return (Strategy) super.getInstance(klass);
	}
	
	/** Get a list of instances for all available strategies
	 * 
	 * @return a list of strategies
	 */
	public ArrayList<Strategy> getAllStrategies() {
		ArrayList<Strategy> strategies = new ArrayList<Strategy>();
		
		for (Object strategy : super.getAllInstances()) {
			try {
				strategies.add((Strategy) strategy);
			} catch (ClassCastException e) {
			}
		}
		
		return strategies;
	}
}
