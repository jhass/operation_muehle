package prototype_GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;

public class Log extends JFrame {

	private JPanel contentPane;
	private final Action actSchlieﬂen = new SwingAction();


	/**
	 * Create the frame.
	 */
	public Log() {
		setTitle("Log");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(600, 100, 300, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JList jlistLog = new JList();
		
		JButton jbtnSchlieﬂen = new JButton("Schlie\u00DFen");
		jbtnSchlieﬂen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		jbtnSchlieﬂen.setAction(actSchlieﬂen);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(jlistLog, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
						.addComponent(jbtnSchlieﬂen))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(jlistLog, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(jbtnSchlieﬂen)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Schlie\u00DFen");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			Log.this.setVisible(false);
		}
	}
}
