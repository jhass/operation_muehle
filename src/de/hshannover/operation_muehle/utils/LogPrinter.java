package de.hshannover.operation_muehle.utils;

import java.io.PrintStream;
import java.util.ArrayList;

import de.hshannover.operation_muehle.Facade;
import de.hshannover.operation_muehle.logic.Logger;

public class LogPrinter extends Thread {
	private PrintStream output;
	private Logger.Level level;
	private ArrayList<Logger.LogEntry> alreadyPrinted;
	
	public LogPrinter(PrintStream output, Logger.Level level) {
		super();
		alreadyPrinted = new ArrayList<Logger.LogEntry>();
		this.output = output;
		this.level = level;
	}
	
	public void run() {
		while (true) {
			try {
				for (Logger.LogEntry entry : logger().getEntriesForLevel(level)) {
					if (!alreadyPrinted.contains(entry)) {
						output.println(entry);
						alreadyPrinted.add(entry);
					}
				}
				sleep(100);
			} catch (InterruptedException e) {}
		}
	}

	private Logger logger() {
		return Facade.getInstance().getGameState().logger;
	}
}
