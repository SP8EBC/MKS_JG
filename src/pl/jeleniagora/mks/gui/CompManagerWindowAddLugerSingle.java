package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;

public class CompManagerWindowAddLugerSingle extends JFrame {

	private JPanel contentPane;
	private JTextField searchField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerWindowAddLugerSingle frame = new CompManagerWindowAddLugerSingle();
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
	public CompManagerWindowAddLugerSingle() {
		setTitle("Dopisz zawodnika do konkurencji jedynek");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 672, 445);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel listPanel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(listPanel, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(388, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(listPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		searchField = new JTextField();
		searchField.setColumns(10);
		GroupLayout gl_listPanel = new GroupLayout(listPanel);
		gl_listPanel.setHorizontalGroup(
			gl_listPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(searchField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
		);
		gl_listPanel.setVerticalGroup(
			gl_listPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_listPanel.createSequentialGroup()
					.addGap(5)
					.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(367, Short.MAX_VALUE))
		);
		listPanel.setLayout(gl_listPanel);
		contentPane.setLayout(gl_contentPane);
	}
}
