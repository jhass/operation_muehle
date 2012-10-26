package de.hshannover.operation_muehle.logic;

import java.util.ArrayList;
/**
 * A simple Class for Logging a Game in a String-ArrayList
 * @Author Richard Pump
 */
public class Logger {
	private ArrayList<String> log;
	
	/**
	 * Constructor, creates an empty log.
	 */
	public Logger() {
		log = new ArrayList<String>();
	}
	
	/**
	 * Adds the given Object to the Log
	 * @param o The Object to Log, can be Slot, Move
	 * @see Slot
	 * @see Move
	 */
	public void addEntry(Object o) {
		//find out, what we got, save it!
	}
	
	/**
	 * This method returns the last saved entry from the Log as a String.
	 * Replaces getLastMove!
	 * @return String-Representation of the last Entry
	 */
	public String getLastEntry() {
		return "A Move I am"; //example!
	}
	
	/**
	 * Gives the complete ArrayList the Logger is currently working with.
	 * Replaces getFullLog!
	 * @return The ArrayList log
	 */
	public ArrayList<String>getLog() {
		return log;
	}
	
	/**
	 * Imports a Log from a save.
	 * @param log The Log to import, ArrayList.
	 * @see SaveState
	 */
	public void importLog(ArrayList<String> log) {
		this.log = log; //well, duh...
	}
	
	/**
	 * Simple toString
	 * @returns A String-Representation of the whole log itself.
	 * @see ArrayList
	 */
	public String toString() {
		return log.toString(); //Am I lazy?
	}
}
