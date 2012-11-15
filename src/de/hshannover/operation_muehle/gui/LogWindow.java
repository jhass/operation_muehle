package de.hshannover.operation_muehle.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.ArrayList;

/**LogWindow shows the game log
 * 
 * @author Julian Haack
 */
public class LogWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea jtxtareaLog;

	/**
	 * LogWindow Frame
	 */	
	public LogWindow() {
		setTitle("Log");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane.setViewportView(scrollPane_1);
		
		jtxtareaLog = new JTextArea();
		jtxtareaLog.setEditable(false);
		scrollPane_1.setViewportView(jtxtareaLog);
		setVisible(false);
				
	}
	
	/**
	 * Toggle visibility of the window
	 */	
	public void toggleVisibility() {
		setVisible(!isVisible());
	}
	
	/**
	 * Delete log and fill it again with an ArayList
	 */
	public void setLog(ArrayList<String> logList) {
		jtxtareaLog.setText("");
		for(int i = 0; i < logList.size(); i++){
            jtxtareaLog.append((String)logList.get(i));
        }
	}
}
