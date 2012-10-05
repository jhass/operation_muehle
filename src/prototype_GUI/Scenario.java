package prototype_GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import javax.swing.JSlider;

public class Scenario extends JFrame {

	private JPanel contentPane;
	private final Action actSpielen = new SwingAction();
	private final Action actAbbrechen = new SwingAction_1();
	private JLabel jlblWeiﬂDenkzeit;
	private JLabel jlblSchwarzDenkzeit;

	/**
	 * Create the frame.
	 */
	public Scenario() {	
		setTitle("Scenario");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 325, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel jlblWeiﬂ = new JLabel("Spieler Wei\u00DF");
		
		JRadioButton jrdbtnWeiﬂSpieler = new JRadioButton("Spieler");
		jrdbtnWeiﬂSpieler.setSelected(true);
		
		JRadioButton jrdbtnWeiﬂKi = new JRadioButton("KI");
		
		JLabel jlblSchwarz = new JLabel("Spieler Schwarz");
		
		JRadioButton jrdbtnSchwarzSpieler = new JRadioButton("Spieler");
		jrdbtnSchwarzSpieler.setSelected(true);
		
		JRadioButton jrdbtnSchwarzKi = new JRadioButton("KI");
		
		JButton jbtnSpielen = new JButton("Spielen");
		jbtnSpielen.setAction(actSpielen);
		
		JButton jbtnLaden = new JButton("Laden");
		jbtnLaden.setEnabled(false);
		
		JButton jbtnAbbrechen = new JButton("Abbrechen");
		jbtnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		jbtnAbbrechen.setAction(actAbbrechen);
		
		JSlider jsliderWeiﬂ = new JSlider();
		jsliderWeiﬂ.setMinimum(1);
		jsliderWeiﬂ.setMaximum(5);
		
		JSlider jsliderSchwarz = new JSlider();
		jsliderSchwarz.setMinimum(1);
		jsliderSchwarz.setMaximum(5);
		
		jlblWeiﬂDenkzeit = new JLabel("KI Denkzeit:");
		
		jlblSchwarzDenkzeit = new JLabel("KI Denkzeit:");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(jlblWeiﬂ)
								.addComponent(jrdbtnWeiﬂSpieler)
								.addComponent(jrdbtnWeiﬂKi)
								.addComponent(jsliderWeiﬂ, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
							.addGap(43)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(jrdbtnSchwarzKi)
								.addComponent(jrdbtnSchwarzSpieler)
								.addComponent(jlblSchwarz)
								.addComponent(jsliderSchwarz, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
								.addComponent(jlblSchwarzDenkzeit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(jbtnSpielen)
							.addGap(18)
							.addComponent(jbtnLaden)
							.addPreferredGap(ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
							.addComponent(jbtnAbbrechen))
						.addComponent(jlblWeiﬂDenkzeit, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(jlblWeiﬂ)
						.addComponent(jlblSchwarz))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(jrdbtnWeiﬂSpieler)
						.addComponent(jrdbtnSchwarzSpieler))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(jrdbtnWeiﬂKi)
						.addComponent(jrdbtnSchwarzKi))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(jlblWeiﬂDenkzeit)
						.addComponent(jlblSchwarzDenkzeit))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(jsliderWeiﬂ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jsliderSchwarz, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(jbtnSpielen)
						.addComponent(jbtnLaden)
						.addComponent(jbtnAbbrechen))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Spielen");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			Scenario.this.setVisible(false);
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Abbrechen");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			Scenario.this.setVisible(false);
		}
	}
}
