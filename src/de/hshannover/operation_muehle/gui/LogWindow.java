package de.hshannover.operation_muehle.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**LogWindow shows the game log
 * 
 * @author Julian Haack
 */

public class LogWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextPane textPane;
	private JScrollBar verticalScrollbar;

	/**
	 * LogWindow Frame
	 */
	
	public LogWindow() {
		setTitle("Log");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		verticalScrollbar = scrollPane.getVerticalScrollBar();
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		getContentPane().add(scrollPane);
		setVisible(false);
				
	}
	
	/**
	 * Toggle visibility of the window
	 */
	
	public void toggleVisibility() {
		setVisible(!isVisible());
	}
	
	/**
	 * Add String to textarea
	 */
	
	public void setLog(String log) {
		textPane.setText(log);
		verticalScrollbar.setValue(verticalScrollbar.getMaximum());
	}
}
