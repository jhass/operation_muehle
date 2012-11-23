package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;
/**
 * A simple Class for Logging a Game in a String-ArrayList
 * @Author Richard Pump
 */
public class Logger {
	private ArrayList<LogEntry> log;
	
	/**
	 * Constructor, creates an empty log.
	 */
	public Logger() {
		log = new ArrayList<LogEntry>();
	}
	
	private void log(String entry, Level level) {
		log.add(new LogEntry(level,entry));
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
	
	private class LogEntry {
		protected Level level;
		protected String message;
		
		public LogEntry(Level level, String entry) {
			this.level = level;
			this.message= entry;
		}
	}
}
