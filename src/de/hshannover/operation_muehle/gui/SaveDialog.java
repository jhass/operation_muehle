package de.hshannover.operation_muehle.gui;

/**a dialog to save a game
 * 
 * @author Julian Haack
 * @author Jonne Haß
 *
 */

public class SaveDialog extends GetPathDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the SaveDialog frame.
	 */
	
	public SaveDialog() {
		super();
		returnValue = showSaveDialog(null);
	}
	
	public static String getPath() {
		GetPathDialog dialog = new SaveDialog();
		return doGetPath(dialog);
	}
}

