package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JSpinner;

public class CompManagerWindowAddCompetition extends JFrame {

	private JPanel contentPane;
	private JTextField textCompName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerWindowAddCompetition frame = new CompManagerWindowAddCompetition();
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
	public CompManagerWindowAddCompetition() {
		setTitle("Dodaj nową konkurencję");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[250px:n:250px][][40px:n][][][][][][]", "[][40px:n:40px,bottom][][]"));
		
		JLabel lblWybierzTym = new JLabel("Wybierz tym konkurencji:\n");
		contentPane.add(lblWybierzTym, "cell 0 0,alignx trailing");
		
		JComboBox comboBox = new JComboBox();
		contentPane.add(comboBox, "cell 1 0 8 1,growx");
		
		JLabel lblWpiszNazwKonkurencji = new JLabel("<html><body style='heigh=200px; text-align= center'><p align=center>Wpisz nazwę konkurencji. Jeżeli pozostawisz pole puste jako nazwa będzie służył typ konkurencji</p></body></html>");
		lblWpiszNazwKonkurencji.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblWpiszNazwKonkurencji, "cell 0 1 9 1");
		
		textCompName = new JTextField();
		textCompName.setMinimumSize(new Dimension(4, 30));
		textCompName.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPane.add(textCompName, "cell 0 2 9 1,growx");
		textCompName.setColumns(10);
		
		JLabel lblIloWszystkichlizgw = new JLabel("Ilość wszystkich ślizgów / zjazdów:");
		contentPane.add(lblIloWszystkichlizgw, "cell 0 3");
		
		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Dialog", Font.BOLD, 18));
		contentPane.add(spinner, "flowx,cell 2 3");
		
		JLabel lblWTymTreningowych = new JLabel("W tym treningowych:");
		contentPane.add(lblWTymTreningowych, "cell 3 3");
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setFont(new Font("Dialog", Font.BOLD, 18));
		contentPane.add(spinner_1, "cell 4 3");
	}

}
