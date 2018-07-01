package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

public class CompManagerWindowJudges extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerWindowJudges frame = new CompManagerWindowJudges();
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
	public CompManagerWindowJudges() {
		setTitle("Podaj personalia sędziów");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]", "[10px:n:10px][][][]"));
		
		JLabel lblSdziaPierwszy = new JLabel("Sędzia pierwszy:");
		contentPane.add(lblSdziaPierwszy, "cell 0 1");
		
		JLabel lblSdziaDrugi = new JLabel("Sędzia Drugi:");
		contentPane.add(lblSdziaDrugi, "cell 0 2");
		
		JLabel lblSdziaTrzeci = new JLabel("Sędzia trzeci:");
		contentPane.add(lblSdziaTrzeci, "cell 0 3");
	}

}
