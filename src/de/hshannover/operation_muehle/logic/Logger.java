package de.hshannover.operation_muehle.logic;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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
	
	private synchronized void log(String entry, Level level) {
		log.add(new LogEntry(level,entry));
	}
	
	private void logf(Level level, String entry, Object... args) {
		log(String.format(entry, args), level);
	}
	
	public Logger copy() {
		return new Logger(this);
	}
	
	public static void logFatal(String entry) {
		getInstance().log(entry, Level.FATAL);
	}
	
	public static void logFatalf(String entry, Object... args) {
		getInstance().logf(Level.FATAL, entry, args);
	}
	
	public static void logError(String entry) {
		getInstance().log(entry, Level.ERROR);
	}
	
	
	public static void logErrorf(String entry, Object... args) {
		getInstance().logf(Level.ERROR, entry, args);
	}
	
	public static void logWarning(String entry) {
		getInstance().log(entry, Level.WARNING);
	}
	
	public static void logWarningf(String entry, Object... args) {
		getInstance().logf(Level.WARNING, entry, args);
	}
	
	public static void logInfo(String entry) {
		getInstance().log(entry, Level.INFO);
	}
	
	
	public static void logInfof(String entry, Object... args) {
		getInstance().logf(Level.INFO, entry, args);
	}
	
	public static void logDebug(String entry) {
		getInstance().log(entry, Level.DEBUG);
	}
	
	
	public static void logDebugf(String entry, Object... args) {
		getInstance().logf(Level.DEBUG, entry, args);
	}
	
	public synchronized ArrayList<LogEntry> getEntriesForLevel(Level level) {
		ArrayList<LogEntry> entries = new ArrayList<LogEntry>();
		
		for (LogEntry entry : log) {
			if (entry.level.getPriority() >= level.getPriority()) {
				entries.add(entry);
			}
		}
		
		return entries;
	}
	
	public String getMessagesForLevel(Level level) {
		StringBuilder fullLog = new StringBuilder();
		
		for (LogEntry entry: getEntriesForLevel(level)) {
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
	
	public class LogEntry implements Serializable {
		private static final long serialVersionUID = 1L;

		protected Level level;
		protected String message;
		protected long timestamp;
		
		public LogEntry(Level level, String entry) {
			this.level = level;
			this.message= entry;
			this.timestamp = System.currentTimeMillis();
		}
		
		@Override
		public String toString() {
			return String.format("%tc [%s]: %s", timestamp, level, message);
		}
	}

	public static String traceToString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
