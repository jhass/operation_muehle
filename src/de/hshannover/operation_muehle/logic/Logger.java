package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * A simple Class for Logging a Game in a String-ArrayList
 * @Author Richard Pump
 */
public class Logger {
	private HashMap<Level,ArrayList<String>> log;
	
	/**
	 * Constructor, creates an empty log.
	 */
	public Logger() {
		log = new HashMap<Level,ArrayList<String>>();
		for (Level level: Level.values())
		log.put(level, new ArrayList<String>());
	}
	
	private void log(String entry, Level level) {
		log.get(level).add(entry);
	}
	
	public void logFatal(String entry) {
		log(entry, Level.FATAL);
	}
	
	public void logError(String entry) {
		log(entry, Level.ERROR);
	}
	
	public void logWarning(String entry) {
		log(entry, Level.WARNING);
	}
	
	public void logInfo(String entry) {
		log(entry, Level.INFO);
	}
	
	public void logDebug(String entry) {
		log(entry, Level.DEBUG);
	}
	
	public String getMessagesForLevel(Level level) {
		StringBuilder fullLog = new StringBuilder();
		for (String message: log.get(level)) {
			fullLog.append(message+"\n");
		}
		return fullLog.toString();
	}
	
	public enum Level {
		FATAL,
		ERROR,
		WARNING,
		INFO,
		DEBUG
	}
}
