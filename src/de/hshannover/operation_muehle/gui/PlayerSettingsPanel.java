package de.hshannover.operation_muehle.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

import de.hshannover.operation_muehle.logic.PlayerOptions;
import de.hshannover.inform.muehle.strategy.Strategy;
import de.hshannover.operation_muehle.Facade;
import de.hshannover.operation_muehle.utils.loader.StrategyLoader;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.SwingConstants;


/** Panel providing the settings for a single player
 * 
 * @author Jonne Haß
 *
 */
public class PlayerSettingsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField humanName;
	private JSlider aiStrength;
	private JLabel lblStrength;
	private JLabel lblName;
	private JComboBox aiSelect;
	private JRadioButton rdbtnAi;
	private Component rigidArea;
	private Component rigidArea_1;
	
	
	public PlayerSettingsPanel(String panelLabel) {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setRootLayout();
		addLabel(panelLabel);
		addTypeSelect();
		JPanel nameSelectPanel = addNameSelectPanel();
		fillNameSelectPanel(nameSelectPanel);
		addAIStrenghSelect();
		
		StrategyLoader loader = Facade.getInstance().getStrategyLoader();
		for (Strategy strategy : loader.getAllStrategies()) {
			aiSelect.addItem(new StrategyWrapper(strategy));
		}
		
		
	}

	private void setRootLayout() {
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] {0, 0, 0, 0, 0};
		gbl.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl);
	}

	private void addLabel(String panelLabel) {
		
		rigidArea = Box.createRigidArea(new Dimension(10, 10));
		GridBagConstraints gbc_rigidArea = new GridBagConstraints();
		gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
		gbc_rigidArea.gridx = 0;
		gbc_rigidArea.gridy = 0;
		add(rigidArea, gbc_rigidArea);
		
		rigidArea_1 = Box.createRigidArea(new Dimension(10, 10));
		GridBagConstraints gbc_rigidArea_1 = new GridBagConstraints();
		gbc_rigidArea_1.insets = new Insets(0, 0, 5, 0);
		gbc_rigidArea_1.gridx = 3;
		gbc_rigidArea_1.gridy = 0;
		add(rigidArea_1, gbc_rigidArea_1);
		JLabel lblPlayer = new JLabel(panelLabel);
		GridBagConstraints gbc_lblplayer = new GridBagConstraints();
		gbc_lblplayer.gridwidth = 2;
		gbc_lblplayer.insets = new Insets(0, 0, 5, 5);
		gbc_lblplayer.gridx = 1;
		gbc_lblplayer.gridy = 1;
		add(lblPlayer, gbc_lblplayer);
	}

	private void addTypeSelect() {
		ButtonGroup typeSelect = new ButtonGroup();
		
		JRadioButton rdbtnHuman = new JRadioButton("Human");
		rdbtnHuman.setSelected(true);
		GridBagConstraints gbc_rdbtnHuman = new GridBagConstraints();
		gbc_rdbtnHuman.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnHuman.gridx = 1;
		gbc_rdbtnHuman.gridy = 2;
		
		typeSelect.add(rdbtnHuman);
		add(rdbtnHuman, gbc_rdbtnHuman);
		
		rdbtnAi = new JRadioButton("AI");
		rdbtnAi.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_rdbtnAi = new GridBagConstraints();
		gbc_rdbtnAi.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAi.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnAi.gridx = 2;
		gbc_rdbtnAi.gridy = 2;
		
		typeSelect.add(rdbtnAi);
		add(rdbtnAi, gbc_rdbtnAi);
		
		rdbtnHuman.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aiStrength.setVisible(false);
				lblStrength.setVisible(false);
				lblName.setText("Name");
				aiSelect.setVisible(false);
				humanName.setVisible(true);
			}
		});
		
		rdbtnAi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aiStrength.setVisible(true);
				lblStrength.setVisible(true);
				lblName.setText("AI");
				humanName.setVisible(false);
				aiSelect.setVisible(true);
			}
		});
	}

	private JPanel addNameSelectPanel() {
		JPanel nameSelectPanel = new JPanel();
		GridBagConstraints gbc_nameSelector = new GridBagConstraints();
		gbc_nameSelector.fill = GridBagConstraints.VERTICAL;
		gbc_nameSelector.insets = new Insets(0, 0, 5, 5);
		gbc_nameSelector.gridx = 2;
		gbc_nameSelector.gridy = 3;
		add(nameSelectPanel, gbc_nameSelector);
		
		GridBagLayout gbl_nameSelector = new GridBagLayout();
		gbl_nameSelector.columnWidths = new int[]{0, 0};
		gbl_nameSelector.rowHeights = new int[]{0, 0, 0};
		gbl_nameSelector.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_nameSelector.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		nameSelectPanel.setLayout(gbl_nameSelector);
		
		return nameSelectPanel;
	}

	private void fillNameSelectPanel(JPanel nameSelectPanel) {
		lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 3;
		add(lblName, gbc_lblName);
		
		humanName = new JTextField();
		GridBagConstraints gbc_humanName = new GridBagConstraints();
		gbc_humanName.insets = new Insets(0, 0, 5, 0);
		gbc_humanName.fill = GridBagConstraints.HORIZONTAL;
		gbc_humanName.gridx = 0;
		gbc_humanName.gridy = 0;
		nameSelectPanel.add(humanName, gbc_humanName);
		humanName.setColumns(10);
		
		aiSelect = new JComboBox();
		aiSelect.setVisible(false);
		GridBagConstraints gbc_aiSelect = new GridBagConstraints();
		gbc_aiSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_aiSelect.gridx = 0;
		gbc_aiSelect.gridy = 1;
		nameSelectPanel.add(aiSelect, gbc_aiSelect);
	}

	private void addAIStrenghSelect() {
		lblStrength = new JLabel("AI strength");
		lblStrength.setVisible(false);
		GridBagConstraints gbc_lblStrength = new GridBagConstraints();
		gbc_lblStrength.anchor = GridBagConstraints.EAST;
		gbc_lblStrength.insets = new Insets(0, 0, 0, 5);
		gbc_lblStrength.gridx = 1;
		gbc_lblStrength.gridy = 4;
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
		gbc_strength.insets = new Insets(0, 0, 0, 5);
		gbc_strength.gridx = 2;
		gbc_strength.gridy = 4;
		add(aiStrength, gbc_strength);
	}

	/** Create a new PlayerOptions object from the current state
	 * 
	 * @return @see PlayerOptions
	 */
	public PlayerOptions collectGameOptions() {
		boolean isAi = rdbtnAi.isSelected();
		if (isAi) {
			return new PlayerOptions(((StrategyWrapper) aiSelect.getSelectedItem())
			                            .strategy.getClass().getName(), aiStrength.getValue());
		} else {
			return new PlayerOptions(humanName.getText());
		}
	}

}

// TODO: remove me
class StrategyWrapper {
	public Strategy strategy;
	
	public StrategyWrapper(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public String toString() {
		return strategy.getStrategyName(); 
	}
}
