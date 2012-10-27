package de.hshannover.operation_muehle.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.Facade;
import de.hshannover.operation_muehle.utils.loader.StrategyLoader;

/** Panel providing the settings for a single player
 * 
 * @author mrzyx
 *
 */
public class PlayerSettingsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField humanName;
	private final ButtonGroup typeSelect = new ButtonGroup();
	private JSlider aiStrength;
	private JLabel lblStrength;
	private JLabel lblName;
	private JPanel nameSelector;
	private JComboBox<String> aiSelect;
	private JRadioButton rdbtnAi;
	
	
	//TODO: refactor into smaller private methods
	public PlayerSettingsPanel(String panelLabel) {
		GridBagLayout gbl_player = new GridBagLayout();
		gbl_player.columnWidths = new int[]{0, 0, 0};
		gbl_player.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_player.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_player.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_player);
		
		JLabel lblWhitePlayer = new JLabel(panelLabel);
		GridBagConstraints gbc_lblplayer = new GridBagConstraints();
		gbc_lblplayer.gridwidth = 2;
		gbc_lblplayer.insets = new Insets(0, 0, 5, 0);
		gbc_lblplayer.gridx = 0;
		gbc_lblplayer.gridy = 0;
		add(lblWhitePlayer, gbc_lblplayer);
		
		JRadioButton rdbtnHuman = new JRadioButton("Human");
		rdbtnHuman.setSelected(true);
		rdbtnHuman.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aiStrength.setVisible(false);
				lblStrength.setVisible(false);
				lblName.setText("Name");
				aiSelect.setVisible(false);
				humanName.setVisible(true);
			}
		});
		typeSelect.add(rdbtnHuman);
		GridBagConstraints gbc_rdbtnHuman = new GridBagConstraints();
		gbc_rdbtnHuman.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnHuman.gridx = 0;
		gbc_rdbtnHuman.gridy = 1;
		add(rdbtnHuman, gbc_rdbtnHuman);
		
		rdbtnAi = new JRadioButton("AI");
		rdbtnAi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aiStrength.setVisible(true);
				lblStrength.setVisible(true);
				lblName.setText("AI");
				humanName.setVisible(false);
				aiSelect.setVisible(true);
			}
		});
		typeSelect.add(rdbtnAi);
		GridBagConstraints gbc_rdbtnAi = new GridBagConstraints();
		gbc_rdbtnAi.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnAi.gridx = 1;
		gbc_rdbtnAi.gridy = 1;
		add(rdbtnAi, gbc_rdbtnAi);
		
		lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 2;
		add(lblName, gbc_lblName);
		
		nameSelector = new JPanel();
		GridBagConstraints gbc_nameSelector = new GridBagConstraints();
		gbc_nameSelector.fill = GridBagConstraints.BOTH;
		gbc_nameSelector.insets = new Insets(0, 0, 5, 0);
		gbc_nameSelector.gridx = 1;
		gbc_nameSelector.gridy = 2;
		add(nameSelector, gbc_nameSelector);
		GridBagLayout gbl_nameSelector = new GridBagLayout();
		gbl_nameSelector.columnWidths = new int[]{0, 0};
		gbl_nameSelector.rowHeights = new int[]{0, 0, 0};
		gbl_nameSelector.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_nameSelector.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		nameSelector.setLayout(gbl_nameSelector);
		
		humanName = new JTextField();
		GridBagConstraints gbc_humanName = new GridBagConstraints();
		gbc_humanName.insets = new Insets(0, 0, 5, 0);
		gbc_humanName.fill = GridBagConstraints.HORIZONTAL;
		gbc_humanName.gridx = 0;
		gbc_humanName.gridy = 0;
		nameSelector.add(humanName, gbc_humanName);
		humanName.setColumns(10);
		
		aiSelect = new JComboBox<String>();
		aiSelect.setVisible(false);
		GridBagConstraints gbc_aiSelect = new GridBagConstraints();
		gbc_aiSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_aiSelect.gridx = 0;
		gbc_aiSelect.gridy = 1;
		nameSelector.add(aiSelect, gbc_aiSelect);
		
		StrategyLoader loader = Facade.getInstance().getStrategyLoader();
		for (Strategy strategy : loader.getAllStrategies()) {
			aiSelect.addItem(strategy.getStrategyName());
		}
		
		
		lblStrength = new JLabel("AI strength");
		lblStrength.setVisible(false);
		GridBagConstraints gbc_lblStrength = new GridBagConstraints();
		gbc_lblStrength.insets = new Insets(0, 0, 0, 5);
		gbc_lblStrength.gridx = 0;
		gbc_lblStrength.gridy = 3;
		add(lblStrength, gbc_lblStrength);
		
		aiStrength = new JSlider();
		aiStrength.setVisible(false);
		aiStrength.setValue(3);
		aiStrength.setSnapToTicks(true);
		aiStrength.setPaintTicks(true);
		aiStrength.setPaintLabels(true);
		aiStrength.setMinorTickSpacing(1);
		aiStrength.setMaximum(10);
		GridBagConstraints gbc_strength = new GridBagConstraints();
		gbc_strength.fill = GridBagConstraints.HORIZONTAL;
		gbc_strength.gridx = 1;
		gbc_strength.gridy = 3;
		add(aiStrength, gbc_strength);
	}
	
	/** First draft, TODO: await or create a dedicated object
	 * 
	 */
	public HashMap<String,String> collectGameOptions() {
		HashMap<String,String> gameOptions = new HashMap<String,String>();
		boolean isAi = rdbtnAi.isSelected();
		gameOptions.put("AI", Boolean.valueOf(isAi).toString()); // BIG FAT UGLY SMELLING TODO
		if (isAi) {
			gameOptions.put("name", (String) aiSelect.getSelectedItem());
			gameOptions.put("strength", Integer.valueOf(aiStrength.getValue()).toString()); // Can't have enough TODO markers in this one, actually lets add a FIXME just because I can
		} else {
			gameOptions.put("name", humanName.getText());
		}
		
		
		return gameOptions;
	}

}
