package de.hshannover.operation_muehle.logic;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * A simple Class for Logging a Game in a String-ArrayList
 * @Author Richard Pump
 */
public class Logger implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger instance;
	
	private ArrayList<LogEntry> log;
	
	
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		
		return instance;
	}
	
	public static void setInstance(Logger logger) {
		instance = logger;
	}
	
	public static void clear() {
		instance = null;
	}
	
	/**
	 * Constructor, creates an empty log.
	 */
	private Logger() {
		log = new ArrayList<LogEntry>();
	}
	
	@SuppressWarnings("unchecked")
	private Logger(Logger logger) {
		log = (ArrayList<LogEntry>) logger.log.clone();
	}
	
	private void log(String entry, Level level) {
		log.add(new LogEntry(level,entry));
	}
	
	public Logger copy() {
		return new Logger(this);
	}
	
	public static void logFatal(String entry) {
		getInstance().log(entry, Level.FATAL);
	}
	
	public static void logError(String entry) {
		getInstance().log(entry, Level.ERROR);
	}
	
	public static void logWarning(String entry) {
		getInstance().log(entry, Level.WARNING);
	}
	
	public static void logInfo(String entry) {
		getInstance().log(entry, Level.INFO);
	}
	
	public static void logDebug(String entry) {
		getInstance().log(entry, Level.DEBUG);
	}
	
	public String getMessagesForLevel(Level level) {
		StringBuilder fullLog = new StringBuilder();
		
		for (LogEntry entry: log) {
			if (entry.level.getPriority() >= level.getPriority())
			fullLog.append(entry.message+"\n");
		}
		
		return fullLog.toString();
	}
	
	public enum Level {
		FATAL {
			public int getPriority() { return 5; }
		},
		ERROR{
			public int getPriority() { return 4; }
		},
		WARNING{
			public int getPriority() { return 3; }
		},
		INFO{
			public int getPriority() { return 2; }
		},
		DEBUG{
			public int getPriority() { return 1; }
		};
		
		abstract int getPriority();
	}
	
	private class LogEntry implements Serializable {
		private static final long serialVersionUID = 1L;

		protected Level level;
		protected String message;
		
		public LogEntry(Level level, String entry) {
			this.level = level;
			this.message= entry;
		}
	}
}
