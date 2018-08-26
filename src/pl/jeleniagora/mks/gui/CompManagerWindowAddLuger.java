package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.factories.ClubsFactory;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Luger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;

/**
 * Klasa do obsługi okna Saneczkarze -> Dodaj nowego do bazy programu
 * @author mateusz
 *
 */
@Component
public class CompManagerWindowAddLuger extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -748324294202197217L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	@Autowired
	RTE_ST rte_st;
	
	JLabel lblAktualnieWProgramie;
	
	CompManagerWindowAddLuger window;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerWindowAddLuger frame = new CompManagerWindowAddLuger();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void updateCurrentNumberOfLugers() {
		int num = rte_st.competitions.listOfAllLugersInThisCompetitions.size();
		lblAktualnieWProgramie.setText("Aktualnie w programie zdefiniowano " + num + " saneczkarzy");
	}

	/**
	 * Create the frame.
	 */
	public CompManagerWindowAddLuger() {
		this.window = this;
		
		setTitle("Dodaj nowego saneczkarza do programu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 706, 362);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][300px:n:900px,grow][220px:n:220px]", "[][][][][][][][]"));
		
		JLabel lblWprowadDaneZawodnika = new JLabel("<html><body style='width=600px'>'<p align=center>Wprowadź kolejno dane każdego zawodnika, którego chcesz dodać do programu. Miej na uwadzę, że nie zostaną oni automatycznie dodani do jakiejkolwiek konkurencji. Po dodaniu do programu należy przypisać ich do właściwych konkurencji, lub np. sparować w dwójkę sankową i dodać do konkurencji dwójkowej </p></body></html>");
		contentPane.add(lblWprowadDaneZawodnika, "cell 0 0 3 1");
		
		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, "cell 2 1 1 7,grow");
		
		
		JLabel lblImi = new JLabel("Imię:");
		contentPane.add(lblImi, "cell 0 1,alignx trailing");
		
		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPane.add(textField, "cell 1 1,growx");
		textField.setColumns(10);
		
		JLabel lblNazwisko = new JLabel("Nazwisko:");
		contentPane.add(lblNazwisko, "cell 0 2,alignx trailing");
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPane.add(textField_1, "cell 1 2,growx");
		textField_1.setColumns(10);
		
		JLabel lblDataUrodzenia = new JLabel("Data Urodzenia:");
		contentPane.add(lblDataUrodzenia, "cell 0 3");
		
		JDateChooser dateChooser = new JDateChooser();
		contentPane.add(dateChooser, "cell 1 3,grow");
		
		JLabel lblAdresEmail = new JLabel("Adres email:");
		contentPane.add(lblAdresEmail, "cell 0 4,alignx trailing");
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPane.add(textField_2, "cell 1 4,growx");
		textField_2.setColumns(10);
		
		JLabel lblKlub = new JLabel("Klub:");
		contentPane.add(lblKlub, "cell 0 5,alignx trailing");
		
		textField_3 = new JTextField();
		textField_3.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPane.add(textField_3, "cell 1 5,growx");
		textField_3.setColumns(10);
		
		lblAktualnieWProgramie = new JLabel("Aktualnie w programie zdefiniowano %d saneczkarzy");
		lblAktualnieWProgramie.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblAktualnieWProgramie, "cell 0 6 2 1");
		
		JLabel lblOstatnioDodany = new JLabel("<html><body style='width=300px'><p align = center>Obowiązkowo należy podać wszystkie dane za wyjątkiem adresu e-mail, ktory w zamierzeniu ma służyć do automatycznego wysyłania raportów wyników do zawodnków.</p></body></html>");
		lblOstatnioDodany.setForeground(Color.RED);
		lblOstatnioDodany.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblOstatnioDodany, "cell 0 7 2 1");
		
		JButton btnNewButton = new JButton("Zapisz i wyjdź");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String _name = textField.getText();
				String _surname = textField_1.getText();

				@SuppressWarnings("unused")
				String _email = textField_2.getText();
				String _club = textField_3.getText();
				
				Date _date = dateChooser.getDate();
				
				if (_name == null || _surname == null || _date == null || _club == null) {
					JOptionPane.showMessageDialog(null, "Brak wpisu w jednym bądź więcej pól obowiązkowych!");
					return;
				}

				LocalDate _local_date = _date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				Luger l = new Luger();
				l.birthDate = _local_date;
				l.name = _name;
				l.surname = _surname;
				l.email = _email;
				l.club = ClubsFactory.createNewFromName(_club);

				int sizeBeforeAdd = rte_st.competitions.listOfAllLugersInThisCompetitions.size();
				
				/* Dodowanie nowego saneczkarza do listy (tylko do listy, nie do konkurencji */
				rte_st.competitions.listOfAllLugersInThisCompetitions.add(l);
				
				int sizeAfterAdd = rte_st.competitions.listOfAllLugersInThisCompetitions.size();
				
				/* Wyciąganie dla weryfikacji ostatnio dodanego */
				Luger added = rte_st.competitions.listOfAllLugersInThisCompetitions.get(sizeAfterAdd - 1);
				
				/* Aktualzowanie labelek*/
				lblOstatnioDodany.setText("Ostatnio dodany: " + added.toString());
				lblAktualnieWProgramie.setText("Aktualnie w programie zdefiniowano " + sizeAfterAdd + " saneczkarzy");
				
				window.dispose();
			}
		});
		
		JButton btnZapiszIDodaj = new JButton("Zapisz i dodaj kolejnego");
		btnZapiszIDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String _name = textField.getText();
				String _surname = textField_1.getText();

				@SuppressWarnings("unused")
				String _email = textField_2.getText();
				String _club = textField_3.getText();
				
				Date _date = dateChooser.getDate();
				
				if (_name == null || _surname == null || _date == null || _club == null) {
					JOptionPane.showMessageDialog(null, "Brak wpisu w jednym bądź więcej pól obowiązkowych!");
					return;
				}

				LocalDate _local_date = _date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				Luger l = new Luger();
				l.birthDate = _local_date;
				l.name = _name;
				l.surname = _surname;
				l.email = _email;
				l.club = ClubsFactory.createNewFromName(_club);
				l.hasBeenAdded = false;

				int sizeBeforeAdd = rte_st.competitions.listOfAllLugersInThisCompetitions.size();
				
				/* Dodowanie nowego saneczkarza do listy (tylko do listy, nie do konkurencji */
				rte_st.competitions.listOfAllLugersInThisCompetitions.add(l);
				
				int sizeAfterAdd = rte_st.competitions.listOfAllLugersInThisCompetitions.size();
				
				/* Wyciąganie dla weryfikacji ostatnio dodanego */
				Luger added = rte_st.competitions.listOfAllLugersInThisCompetitions.get(sizeAfterAdd - 1);
				
				/* Aktualzowanie labelek*/
				lblOstatnioDodany.setText("Ostatnio dodany: " + added.toString());
				lblAktualnieWProgramie.setText("Aktualnie w programie zdefiniowano " + sizeAfterAdd + " saneczkarzy");
				
				/* Czyszczenie pól po dodaniu */
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				
			}
		});
		
		JButton btnWyjdBezZapisywania = new JButton("Wyjdź bez zapisywania");
		btnWyjdBezZapisywania.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.dispose();
			
			}
		});
		GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
		gl_buttonPanel.setHorizontalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnWyjdBezZapisywania, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnZapiszIDodaj, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(422))
		);
		gl_buttonPanel.setVerticalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addComponent(btnZapiszIDodaj, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnWyjdBezZapisywania, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		buttonPanel.setLayout(gl_buttonPanel);
	
	}
}
