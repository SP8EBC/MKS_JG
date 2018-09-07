package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.display.SectroBigRasterDisplay;
import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;
import pl.jeleniagora.mks.exceptions.FailedOpenSerialPortEx;
import pl.jeleniagora.mks.rte.RTE_COM_DISP;
import pl.jeleniagora.mks.rte.RTE_DISP;
import pl.jeleniagora.mks.serial.CommThread;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

@Component
public class CompManagerWindowSettingsDisplay extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8585369990723358551L;
	private JPanel contentPane;
	
	JFrame window;
	JComboBox<String> comboBox;

	JSlider sliderBrightness;
	JCheckBox chckbxAutomatycznieWywietlajKolejnego;
	JCheckBox chckbxAutomatycznieWywietlajCzas;
	SectroBigRasterDisplay display;
	
	@Autowired
	RTE_DISP rte_disp;
	
	@Autowired
	@Qualifier("comDispBean")
	RTE_COM_DISP rte_com_disp;
	
	void updateToCurrentSettings(SectroBigRasterDisplay disp) {
		display = disp;
		
		sliderBrightness.setValue(rte_disp.brightness);
		
		if (rte_disp.autoShowNextLuger) {
			chckbxAutomatycznieWywietlajKolejnego.setSelected(true);
		}
		else {
			chckbxAutomatycznieWywietlajKolejnego.setSelected(false);
		}
		
		if (rte_disp.autoShowRuntimeAfterLanding) {
			chckbxAutomatycznieWywietlajCzas.setSelected(true);
		}
		else {
			chckbxAutomatycznieWywietlajCzas.setSelected(false);			
		}

	}
	
	void saveSettings() {
		int setBrightness = sliderBrightness.getValue();
		
		if (setBrightness <= 0)
			rte_disp.brightness = 16;
		else if (setBrightness > 255)
			rte_disp.brightness = 255;
		else
			rte_disp.brightness = (short) setBrightness;
		
		if (chckbxAutomatycznieWywietlajKolejnego.isSelected()) {
			rte_disp.autoShowNextLuger = true;
		}
		else {
			rte_disp.autoShowNextLuger = false;
		}
		
		if (chckbxAutomatycznieWywietlajCzas.isSelected()) {
			rte_disp.autoShowRuntimeAfterLanding = true;
		}
		else {
			rte_disp.autoShowRuntimeAfterLanding = false;
		}
		
	}
	
	/**
	 * Create the frame.
	 */
	public CompManagerWindowSettingsDisplay() {
		this.window = this;
		
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
		
		comboBox = new JComboBox<String>();
		contentPane.add(comboBox, "cell 1 0 3 1,growx");
		comboBox.addItem("/dev/ttyS0");
		comboBox.addItem("/dev/ttyUSB0");
		comboBox.addItem("/dev/ttyUSB1");
		comboBox.addItem("/dev/ttyUSB485");
		
		JButton btnOtwrzPort = new JButton("Otwórz port");
		btnOtwrzPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String port = (String)comboBox.getSelectedItem();
				
				try {
					rte_com_disp.thread = new CommThread(port, rte_com_disp, false);
					rte_com_disp.thread.startThreads();
				} catch (IOException | FailedOpenSerialPortEx e1) {
					JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas otwierania portu " + port);
					return;
				}
				JOptionPane.showMessageDialog(null, "Poprawnie otwarto port " + port);
			}
		});
		contentPane.add(btnOtwrzPort, "cell 0 1 2 1,growx");
		
		JButton btnZamknijPort = new JButton("Zamknij port");
		btnZamknijPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int a = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz zamknąć port komunikacji z wyświetlaczem?", "Pozor!", JOptionPane.YES_NO_OPTION);
			
				if (a == JOptionPane.NO_OPTION) {
					return;
				}
				else if (a == JOptionPane.YES_OPTION) {
					rte_com_disp.terminate = true;
				}
			}
		});
		contentPane.add(btnZamknijPort, "cell 2 1 2 1,growx");
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		contentPane.add(panel, "cell 0 2 4 1,grow");
		panel.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		
		JLabel lblJasno = new JLabel("Jasność:");
		panel.add(lblJasno, "cell 0 0");
		
		sliderBrightness = new JSlider();
		sliderBrightness.setPaintLabels(true);
		sliderBrightness.setValue(64);
		sliderBrightness.setMajorTickSpacing(32);
		sliderBrightness.setMinorTickSpacing(32);
		sliderBrightness.setSnapToTicks(true);
		sliderBrightness.setMaximum(255);
		panel.add(sliderBrightness, "cell 1 0,growx");
		
		chckbxAutomatycznieWywietlajKolejnego = new JCheckBox("<html>Automatycznie wyświetlaj kolejnego zawodnika<br>po zakończonym ślizgu</html>");
		chckbxAutomatycznieWywietlajKolejnego.setSelected(true);
		panel.add(chckbxAutomatycznieWywietlajKolejnego, "cell 0 1 2 1");
		
		chckbxAutomatycznieWywietlajCzas = new JCheckBox("Automatycznie wyświetlaj czas z chronometru");
		panel.add(chckbxAutomatycznieWywietlajCzas, "cell 0 2 2 1");
		
		JButton btnZapiszIWyjd = new JButton("Zapisz ustawienia i wyjdź");
		btnZapiszIWyjd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSettings();
				
				display.waitForDisplay();
				
				try {
					display.setBrightness(rte_disp.brightness);
				} catch (DisplayFunctionNotSupportedEx e1) {
					e1.printStackTrace();
				}
				
				display.releaseDisplay();
				
				window.dispose();
				
			}
		});
		contentPane.add(btnZapiszIWyjd, "cell 0 3 4 1,growx");
		
		JButton btnWyjdBezZapisywania = new JButton("Wyjdź bez zapisywania");
		btnWyjdBezZapisywania.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				window.dispose();
			}
		});
		contentPane.add(btnWyjdBezZapisywania, "cell 0 4 4 1,growx");
	}

}
