package de.hshannover.operation_muehle.utils.loader;

import java.io.IOException;
import java.util.ArrayList;

import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.logic.Logger;


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
	
	public static StrategyLoader getDefaultInstanceOrMock() {
		try {
			return new StrategyLoader();
		} catch (IOException e) {
		} catch (IllegalArgumentException e) {}
		
		System.err.println("WARNING: Can't find or read default search path directory (./lib) to look for foreign AIs!");
		
		try {
			return new StategyLoaderMock();
		} catch (IOException wontHappen) {}
		
		return null;
	}
	
	/** If possible returns an instance of the given Strategy using the default
	 *  constructor.
	 * 
	 * @throws ClassNotFoundException if the given class isn't available
	 * @throws InstantionException if the given class can't be instantiated
	 * @throws IllegalAccessException if the given class can't be accessed
	 * @throws ClassCastException if the given class is not a strategy
	 */
	public Strategy getInstance(String klassName) {
		try {
			Class<?> klass = getClass(klassName);
			for (Class<?> iface : klass.getInterfaces()) {
				if (iface.getCanonicalName().equals("de.hshannover.inform.muehle.strategy.Strategy")) {
					Logger.logDebugf("Found class implementing the Strategy interface: %s", klassName);
					return (Strategy) super.getInstance(klassName); 
				} else {
					Logger.logDebugf("%s implements %s.", klassName, iface.getCanonicalName());
				}
			}
		// Meh, I want Java 7
		} catch (ClassNotFoundException e) {
			Logger.logDebugf("Couldn't load %s (%s).", klassName, e);
		} catch (InstantiationException e) {
			Logger.logDebugf("Couldn't load %s (%s).", klassName, e);
		} catch (IllegalAccessException e) {
			Logger.logDebugf("Couldn't load %s (%s).", klassName, e);
		} catch (ClassCastException e) {
			Logger.logDebugf("Couldn't load %s (%s).", klassName, e);
		} catch (NoClassDefFoundError e) {
			Logger.logDebugf("Couldn't load %s (%s).", klassName, e);
		}
		
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

class StategyLoaderMock extends StrategyLoader {

	public StategyLoaderMock() throws IOException {
		super(".");
	}
	
	@Override
	public Strategy getInstance(String klass) {
		return null;
	}
}
