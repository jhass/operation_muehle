package de.hshannover.operation_muehle.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

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
	private boolean autoscroll;
	private JScrollPane scrollPane;

	/**
	 * LogWindow Frame
	 */
	
	public LogWindow() {
		setTitle("Log");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent ev) {
				autoscroll = ev.getAdjustable().getMaximum()-ev.getValue() < 250;
			}
		});
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		getContentPane().add(scrollPane);
		setVisible(false);
		
		autoscroll = true;
	}
	
	/**
	 * Toggle visibility of the window
	 */
	
	public void toggleVisibility() {
		setVisible(!isVisible());
		scrollToTheEnd();
	}
	
	/**
	 * Add String to textarea
	 */
	
	public void setLog(String log) {
		textPane.setText(log);
		scrollToTheEnd();
	}

	private void scrollToTheEnd() {
		if (autoscroll) {
			scrollPane.revalidate();
			final JScrollBar verticalScrollbar = scrollPane.getVerticalScrollBar();
			EventQueue.invokeLater(new Runnable() { // Give the revalidate a chance to populate
				@Override
				public void run() {
					verticalScrollbar.setValue(verticalScrollbar.getMaximum());
				}
			});
		}
	}
}
