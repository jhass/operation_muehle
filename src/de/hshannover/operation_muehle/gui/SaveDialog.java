package de.hshannover.operation_muehle.gui;

import java.awt.Component;

/**a dialog to save a game
 * 
 * @author Julian Haack
 * @author Jonne Haß
 *
 */

public class SaveDialog extends GetPathDialog {
	private static final long serialVersionUID = 1L;
	
	private SaveDialog(Component parent) {
		super(parent);
		returnValue = showSaveDialog(parent);
	}
	
	/** @see GetPathDialog
	 */
	public static String getPath(Component parent) {
		GetPathDialog dialog = new SaveDialog(parent);
		String path = doGetPath(dialog);
		if (!path.endsWith(".msave")) {
			path += ".msave";
		}
		
		return path;
	}
}

