package de.hshannover.operation_muehle.gui;

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
	private boolean isFinished;
	protected FileNameExtensionFilter filter;
	protected int returnValue;
	
	public GetPathDialog() {
		super();
		isFinished = false;
		filter = new FileNameExtensionFilter("Save game", "msave");
		setFileFilter(filter);
	}
	
	
	public static String getPath() {
		GetPathDialog dialog = new GetPathDialog();
		return doGetPath(dialog);
	}
	
	protected static String doGetPath(GetPathDialog dialog) {
		if (dialog.returnValue == APPROVE_OPTION) {
			return dialog.getSelectedFile().getPath();
		}
		return null;
	}
	
	private boolean isFinished() {
		return isFinished;
	}
} 
