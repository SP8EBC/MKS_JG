package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.exceptions.FailedOpenSerialPortEx;
import pl.jeleniagora.mks.rte.RTE_CHRONO;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.serial.CommThread;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import java.awt.Font;
import javax.swing.SpinnerNumberModel;

@Component
public class CompManagerWindowSettingsChrono extends JFrame {

	private JPanel contentPane;
	
	JFrame window;
	
	JComboBox<String> comboBox;
	
	@Autowired
	@Qualifier("comBean")
	RTE_COM rte_com;
	
	@Autowired
	RTE_CHRONO rte_chrono;

	JRadioButton rdbtnKadePrzecicieFotoceli;
	JRadioButton rdbtnTylkoPierwszePrzecicie;
	
	JSpinner startChannel;
	JSpinner goalChannel;
	
	void updateToCurrentSettings() {
		startChannel.setValue(rte_chrono.startSignalId);
		goalChannel.setValue(rte_chrono.endSignalId);
		
		if (rte_chrono.restartOnEachStartSignal) {
			rdbtnKadePrzecicieFotoceli.setSelected(true);
			rdbtnTylkoPierwszePrzecicie.setSelected(false);
		}
		else {
			rdbtnKadePrzecicieFotoceli.setSelected(false);
			rdbtnTylkoPierwszePrzecicie.setSelected(true);			
		}
	}
	
	void saveSettings() {
		rte_chrono.startSignalId = (Integer)startChannel.getValue();
		rte_chrono.endSignalId = (Integer)goalChannel.getValue();
		
		if (rdbtnKadePrzecicieFotoceli.isSelected() && !rdbtnTylkoPierwszePrzecicie.isSelected()) {
			rte_chrono.restartOnEachStartSignal = true;
		}
		else if (!rdbtnKadePrzecicieFotoceli.isSelected() && rdbtnTylkoPierwszePrzecicie.isSelected()) {
			rte_chrono.restartOnEachStartSignal = false;
		}
		else;
	}
	
	/**
	 * Create the frame.
	 */
	public CompManagerWindowSettingsChrono() {
		this.window = this;
		
		setTitle("Ustawienia Chronometru");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 483, 353);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][110px:n:110px][][grow]", "[][][grow][][]"));
		
		JLabel lblPortSzeregowy = new JLabel("Port Szeregowy:");
		contentPane.add(lblPortSzeregowy, "cell 0 0,alignx trailing");
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("/dev/ttyS0");
		comboBox.addItem("/dev/ttyUSB0");
		comboBox.addItem("/dev/ttyUSB1");
		comboBox.addItem("/dev/ttyUSB485");
		contentPane.add(comboBox, "cell 1 0 3 1,growx");
		
		JButton btnOtwrzPort = new JButton("Otwórz port");
		btnOtwrzPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String port = (String)comboBox.getSelectedItem();
				
				try {
					rte_com.thread = new CommThread(port, rte_com, false);
					rte_com.thread.startThreads();
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
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz zamknąć port komunikacji z chronometrem?", "Pozor!", JOptionPane.YES_NO_OPTION);
				
				if (a == JOptionPane.NO_OPTION) {
					return;
				}
				else if (a == JOptionPane.YES_OPTION) {
					rte_com.terminateRx = true;
					rte_com.terminateTx = true;
					
					// czekanie na zamknięcie wątków obsługujących zapis i odczyt. Wątki działają cyklicznie
					// ze sleep 1msec
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					rte_com.thread.closePort();
				}
			}
		});
		contentPane.add(btnZamknijPort, "cell 2 1 2 1,growx");
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		contentPane.add(panel, "cell 0 2 4 1,grow");
		panel.setLayout(new MigLayout("", "[][0px:n:0px][][][]", "[][][][]"));
		
		JLabel lblDetekcjaStartu = new JLabel("Detekcja startu:");
		panel.add(lblDetekcjaStartu, "cell 0 0");
		
		ButtonGroup group = new ButtonGroup();
		
		rdbtnKadePrzecicieFotoceli = new JRadioButton("Każde przecięcie fotoceli startu restartuje pomiar od zera");
		panel.add(rdbtnKadePrzecicieFotoceli, "cell 0 1 2 1");
		
		rdbtnTylkoPierwszePrzecicie = new JRadioButton("<html><body style=\"width: 300px;\">Tylko pierwsze przecięcie startu restartuje pomiar, każde kolejne będzie ignorowane aż do zakończenia ślizgu</body></html>");
		panel.add(rdbtnTylkoPierwszePrzecicie, "cell 0 2 2 1");
		
		group.add(rdbtnKadePrzecicieFotoceli);
		group.add(rdbtnTylkoPierwszePrzecicie);
		
		JLabel lblKanaFotoceliStartu = new JLabel("Nr kanału wejściowego startu:");
		panel.add(lblKanaFotoceliStartu, "cell 0 3");
		
		startChannel = new JSpinner();
		startChannel.setModel(new SpinnerNumberModel(1, 1, 9, 1));
		startChannel.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(startChannel, "cell 2 3");
		
		JLabel lblNrKanauMety = new JLabel("Nr kanału mety:");
		panel.add(lblNrKanauMety, "cell 3 3");
		
		goalChannel = new JSpinner();
		goalChannel.setModel(new SpinnerNumberModel(3, 1, 9, 1));
		goalChannel.setFont(new Font("Dialog", Font.BOLD, 14));
		panel.add(goalChannel, "cell 4 3");
		
		JButton btnZapiszUstawieniaI = new JButton("Zapisz ustawienia i wyjdź");
		btnZapiszUstawieniaI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSettings();
				window.dispose();
			}
		});
		contentPane.add(btnZapiszUstawieniaI, "cell 0 3 4 1,growx");
		
		JButton btnZamknijBezZapisywania = new JButton("Zamknij bez zapisywania");
		btnZamknijBezZapisywania.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				window.dispose();
			}
		});
		contentPane.add(btnZamknijBezZapisywania, "cell 0 4 4 1,growx");
	}

}
