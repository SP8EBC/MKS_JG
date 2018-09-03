package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JCheckBox;

public class CompManagerWindowSettingsDisplay extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8585369990723358551L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public CompManagerWindowSettingsDisplay() {
		setTitle("Ustawienia Wyświetlacza");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 345);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[120px:120px:120px,grow][90px:90px:90px][100px:n:100px][grow]", "[][][grow][][]"));
		
		JLabel lblPortSzeregowy = new JLabel("Port szeregowy:");
		contentPane.add(lblPortSzeregowy, "cell 0 0,alignx trailing");
		
		JComboBox comboBox = new JComboBox();
		contentPane.add(comboBox, "cell 1 0 3 1,growx");
		
		JButton btnOtwrzPort = new JButton("Otwórz port");
		contentPane.add(btnOtwrzPort, "cell 0 1 2 1,growx");
		
		JButton btnZamknijPort = new JButton("Zamknij port");
		contentPane.add(btnZamknijPort, "cell 2 1 2 1,growx");
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		contentPane.add(panel, "cell 0 2 4 1,grow");
		panel.setLayout(new MigLayout("", "[][grow]", "[][]"));
		
		JLabel lblJasno = new JLabel("Jasność:");
		panel.add(lblJasno, "cell 0 0");
		
		JSlider sliderBrightness = new JSlider();
		sliderBrightness.setPaintLabels(true);
		sliderBrightness.setValue(64);
		sliderBrightness.setMajorTickSpacing(32);
		sliderBrightness.setMinorTickSpacing(32);
		sliderBrightness.setSnapToTicks(true);
		sliderBrightness.setMaximum(255);
		panel.add(sliderBrightness, "cell 1 0,growx");
		
		JCheckBox chckbxAutomatycznieWywietlajKolejnego = new JCheckBox("<html>Automatycznie wyświetlaj kolejnego zawodnika<br>po zakończonym ślizgu</html>");
		chckbxAutomatycznieWywietlajKolejnego.setSelected(true);
		panel.add(chckbxAutomatycznieWywietlajKolejnego, "cell 0 1 2 1");
		
		JButton btnZapiszIWyjd = new JButton("Zapisz ustawienia i wyjdź");
		contentPane.add(btnZapiszIWyjd, "cell 0 3 4 1,growx");
		
		JButton btnWyjdBezZapisywania = new JButton("Wyjdź bez zapisywania");
		contentPane.add(btnWyjdBezZapisywania, "cell 0 4 4 1,growx");
	}

}
