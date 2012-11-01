package de.hshannover.operation_muehle.gui;

import java.awt.Component;

import javax.swing.JFileChooser; 
import javax.swing.filechooser.FileNameExtensionFilter;

/** a dialog where you select a file and pass off the path as string
 * 
 * @author Julian Haack
 * @author Jonne Haß
 *
 */

public class GetPathDialog extends JFileChooser { 
	
	private static final long serialVersionUID = 1L;
	protected FileNameExtensionFilter filter;
	protected int returnValue;
	protected Component parent;
	
	protected GetPathDialog(Component parent) {
		super();
		filter = new FileNameExtensionFilter("Save game", "msave");
		setFileFilter(filter);
		this.parent = parent;
	}
	
	/** Create a new dialog and return the chosen file.
	 * 
	 * @param parent the window which the dialog should be modal for. Can be null
	 * @return String path
	 */
	public static String getPath(Component parent) {
		GetPathDialog dialog = new GetPathDialog(parent);
		return doGetPath(dialog);
	}
	
	protected static String doGetPath(GetPathDialog dialog) {
		if (dialog.returnValue == APPROVE_OPTION) {
			return dialog.getSelectedFile().getPath();
		}
		return null;
	}
} 
