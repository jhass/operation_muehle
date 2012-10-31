package de.hshannover.operation_muehle.gui;

import java.awt.Component;

/**a dialog to load a saved game
 * 
 * @author Julian Haack
 * @author Jonne Haß
 *
 */

public class LoadDialog extends GetPathDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the LoadDialog frame.
	 */
	
	public LoadDialog(Component parent) {
		super(parent);
		returnValue = showOpenDialog(parent);
	}
	
	public static String getPath(Component parent) {
		GetPathDialog dialog = new LoadDialog(parent);
		return doGetPath(dialog);
	}
}
