package de.hshannover.operation_muehle.gui;

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
	
	public LoadDialog() {
		super();
		returnValue = showOpenDialog(null);
	}
	
	public static String getPath() {
		GetPathDialog dialog = new LoadDialog();
		return doGetPath(dialog);
	}
}
