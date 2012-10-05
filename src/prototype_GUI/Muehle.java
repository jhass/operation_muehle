package prototype_GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import javax.swing.JTable;

public class Muehle extends JFrame {

	private JPanel contentPane;
	private final Action actStart = new Start();
	private final Action actLog = new LogOeffnen();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Muehle frame = new Muehle();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Muehle() {
		setTitle("M\u00FChle");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton jbtnStart = new JButton("Start");
		jbtnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		jbtnStart.setAction(actStart);
		
		JButton jbtnLog = new JButton("Log");
		jbtnLog.setAction(actLog);
		jbtnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton jbtnSpeichern = new JButton("Speichern");
		jbtnSpeichern.setEnabled(false);
		
		JLabel jlblStatus = new JLabel("Klicke auf \"Start\" um ein Spiel zu beginnen");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(jlblStatus, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(jbtnStart)
							.addGap(68)
							.addComponent(jbtnLog)
							.addGap(18)
							.addComponent(jbtnSpeichern)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(jlblStatus)
					.addPreferredGap(ComponentPlacement.RELATED, 504, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(jbtnStart)
						.addComponent(jbtnLog)
						.addComponent(jbtnSpeichern))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	private class Start extends AbstractAction {
		public Start() {
			putValue(NAME, "Start");
			putValue(SHORT_DESCRIPTION, "\u00D6ffnet das Scenario Fenster. Im Scenario Fenster k\u00F6nnen Sie die gew\u00FCnschen Spieleinstellungen f\u00FCr ein neues Spiel vornehmen oder ein Spiel laden.");
		}
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Scenario frame = new Scenario();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});	
		}
	}
	private class LogOeffnen extends AbstractAction {
		public LogOeffnen() {
			putValue(NAME, "Log");
			putValue(SHORT_DESCRIPTION, "Zeigt den Spielverlauf an.");
		}
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Log frame = new Log();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
