package de.hshannover.operation_muehle.utils.loader;

import java.io.IOException;
import java.util.ArrayList;

import de.hshannover.inform.muehle.strategy.Strategy;


/** Loads any found strategies
 *
 * @author Jonne Haß
 *
 */
public class StrategyLoader extends ClassesLoader {
	
	/** Try searching in ./lib/
	 * 
	 * @see ClassesLoader
	 * @throws IOException
	 */
	public StrategyLoader() throws IOException {
		this("./lib/");
	}
	
	
	/**
	 * @see ClassesLoader
	 */
	public StrategyLoader(String searchPath) throws IOException {
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
	public Strategy getInstance(String klass) {
		try {
			return (Strategy) super.getInstance(klass);
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {}
		
		return null;
	}
	
	/** Get a list of instances for all available strategies
	 * 
	 * @return a list of strategies
	 */
	public ArrayList<Strategy> getAllStrategies() {
		ArrayList<Strategy> strategies = new ArrayList<Strategy>();
		
		
		for (Object strategy : super.getAllInstances()) {
			if (strategy != null) {
				strategies.add((Strategy) strategy);
			}
		}
		
		return strategies;
	}
}
