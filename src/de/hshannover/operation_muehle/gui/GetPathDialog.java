package de.hshannover.operation_muehle.gui;

import java.beans.PropertyChangeEvent; 
import java.beans.PropertyChangeListener; 
import java.io.File; 

import javax.swing.JFileChooser; 

/**a dialog where you select a file and pass off the path as string
 * 
 * @author Julian Haack
 *
 */

public class GetPathDialog { 
	
	/**
	 * opens a JFileChooser
	 * ToDo: add return value
	 */

    public void open() { 
        final JFileChooser chooser = new JFileChooser("Verzeichnis wählen"); 
        chooser.setDialogType(JFileChooser.OPEN_DIALOG); 
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        final File file = new File("/home"); 

        chooser.setCurrentDirectory(file); 

        chooser.addPropertyChangeListener(new PropertyChangeListener() { 
            public void propertyChange(PropertyChangeEvent e) { 
                if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) 
                        || e.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) { 
                    final File f = (File) e.getNewValue(); 
                } 
            } 
        }); 

        chooser.setVisible(true); 
        final int result = chooser.showOpenDialog(null); 
        
        /**
         * output: path with selected file's name as string
         */

        if (result == JFileChooser.APPROVE_OPTION) { 
            File pathFile = chooser.getSelectedFile(); 
            String path = pathFile.getPath();        
        } 
        chooser.setVisible(false); 
    } 
} 
