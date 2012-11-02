package de.hshannover.operation_muehle.gui;

import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;

import de.hshannover.operation_muehle.logic.PlayerOptions;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import java.awt.Dimension;


/** Provides a Dialog and interface to collect the needed data to initialize a new game
 * 
 * @author Jonne Haß
 *
 */
public class NewGameDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private boolean isFinished;
	private boolean isAborted;
	private PlayerSettingsPanel whitePlayer;
	private PlayerSettingsPanel blackPlayer;
	
	private NewGameDialog(Frame parent) {
		super(parent);
		
		this.isFinished = false;
		this.isAborted = false;
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel playerSettings = new JPanel();
		playerSettings.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(playerSettings, BorderLayout.CENTER);
		playerSettings.setLayout(new BoxLayout(playerSettings, BoxLayout.X_AXIS));
		
		whitePlayer = new PlayerSettingsPanel("White Player");
		playerSettings.add(whitePlayer);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		playerSettings.add(rigidArea);
		
		
		blackPlayer = new PlayerSettingsPanel("Black Player");
		playerSettings.add(blackPlayer);
		
		JPanel buttons = new JPanel();
		getContentPane().add(buttons, BorderLayout.SOUTH);
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});
		
		JButton btnNewGame = new JButton("New Game");
		buttons.add(btnNewGame);
		
		JButton btnCancel = new JButton("Cancel");
		buttons.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				cancel();
			}
		});
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				isFinished = true;
				setVisible(false);
			}
		});
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("New Game");
		setSize(700,250);
	}
	
	private void cancel() {
		isFinished = true;
		isAborted = true;
		setVisible(false);
	}
	
	/** Create and show a new dialog, block and return the collected data.
	 * 
	 * @return The return value has the following content:
	 *         {"white": options, "black": options} with options being a
	 *         PlayerOptions object.
	 */
	public static HashMap<String,PlayerOptions> getGameOptions(Frame parent) {
		NewGameDialog dialog = new NewGameDialog(parent);
		dialog.setVisible(true);
		
		while (!dialog.isFinished()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
		if (dialog.isAborted()) {
			return null;
		}
		
		return dialog.collectGameOptions();
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public boolean isAborted() {
		return isAborted;
	}

	private HashMap<String,PlayerOptions> collectGameOptions() {
		HashMap<String,PlayerOptions> gameOptions =
			new HashMap<String,PlayerOptions>();
		gameOptions.put("white", whitePlayer.collectGameOptions());
		gameOptions.put("black", blackPlayer.collectGameOptions());
		return gameOptions; 
	}

}