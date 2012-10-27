package de.hshannover.operation_muehle.gui;

import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/** Provides a Dialog and interface to collect the needed data to initialize a new game
 * 
 * @author mrzyx
 *
 */
public class NewGameDialog extends JFrame {
	private static final long serialVersionUID = 1L;
	private boolean isFinished;
	private boolean isAborted;
	private PlayerSettingsPanel whitePlayer;
	private PlayerSettingsPanel blackPlayer;
	
	private NewGameDialog() {
		this.isFinished = false;
		this.isAborted = false;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isFinished = true;
				isAborted = true;
				setVisible(false);
			}
		});
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		whitePlayer = new PlayerSettingsPanel("White Player");
		panel.add(whitePlayer);
		
		
		blackPlayer = new PlayerSettingsPanel("Black Player");
		panel.add(blackPlayer);
		
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				isFinished = true;
				setVisible(false);
			}
		});
		getContentPane().add(btnNewGame);
		
		setSize(400,200);
	}
	
	/** Create and show a new dialog, block and return the collected data.
	 * 
	 * @return The return value has the following layout for now: (TODO: make this a dedicated object)
	 *         {"white": options, "black": options} with options being a HashMap with the following content:
	 *         {"AI": "true/false", "name": "playerName/strategyClass", "strength": "thinkTime in seconds"}
	 *         Returns null if the dialog was aborted.
	 */
	public static HashMap<String,HashMap<String,String>> getGameOptions() {
		NewGameDialog dialog = new NewGameDialog();
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

	private HashMap<String,HashMap<String,String>> collectGameOptions() {
		HashMap<String,HashMap<String,String>> gameOptions =
			new HashMap<String,HashMap<String,String>>();
		gameOptions.put("white", whitePlayer.collectGameOptions());
		gameOptions.put("black", blackPlayer.collectGameOptions());
		return gameOptions; 
	}

}
