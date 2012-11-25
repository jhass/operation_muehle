package de.hshannover.operation_muehle.logic;

import de.hshannover.operation_muehle.gui.GUIController;
import de.hshannover.operation_muehle.utils.LogPrinter;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GUIController();
		new LogPrinter(System.out, Logger.Level.DEBUG).start();
	}

}
