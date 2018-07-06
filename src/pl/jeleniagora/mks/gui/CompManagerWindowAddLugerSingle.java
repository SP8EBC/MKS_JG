package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

@Component
public class CompManagerWindowAddLugerSingle extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -679230823534376426L;
	private JPanel contentPane;
	private JTextField searchField;
	private JTable searchTable;
	private JTable table;

	@Autowired
	CompManagerWindowAddLugerSingleLTableModel leftModel;
	
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
		setResizable(false);
		setTitle("Dopisz lub usuń zawodnika z konkurencji jedynek");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1017, 570);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel searchPanel = new JPanel();
		
		JPanel compSelPanel = new JPanel();
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		
		JButton btnDodajZaznaczonegoDo = new JButton("Dodaj zaznaczonego w lewym panelu do konkurencji");
		
		JPanel panel = new JPanel();
		
		JButton btnUsuZKonkurencji = new JButton("Usuń zaznaczonego w prawym panelu z konkurencji");
		
		JButton btnZakocz = new JButton("Zakończ");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(searchPanel, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnZakocz, GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
						.addComponent(btnUsuZKonkurencji, GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
							.addComponent(btnDodajZaznaczonegoDo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(infoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(compSelPanel, GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
						.addComponent(searchPanel, GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(compSelPanel, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnDodajZaznaczonegoDo, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnUsuZKonkurencji, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnZakocz, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		
		table = new JTable();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(table, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(table, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);
		infoPanel.setLayout(new MigLayout("", "[114px][8px][280px:280px]", "[15px][15px][][][][][][][][]"));
		
		JLabel lblSzczegoweInformacjeO = new JLabel("Szczegółowe informacje o wybranym saneczkarzu");
		infoPanel.add(lblSzczegoweInformacjeO, "cell 0 0 3 1,alignx center,aligny top");
		
		JLabel lblImiINazwisko = new JLabel("Imię i Nazwisko:");
		infoPanel.add(lblImiINazwisko, "cell 0 2,alignx left,aligny top");
		
		JLabel lblImienazwisko = new JLabel("imie_nazwisko");
		infoPanel.add(lblImienazwisko, "cell 2 2,alignx left,aligny top");
		
		JLabel lblKlub = new JLabel("Klub:");
		infoPanel.add(lblKlub, "cell 0 3");
		
		JLabel lblKlub_1 = new JLabel("klub");
		infoPanel.add(lblKlub_1, "cell 2 3");
		
		JLabel lblDataUrodzenia = new JLabel("Data Urodzenia:");
		infoPanel.add(lblDataUrodzenia, "cell 0 4");
		
		JLabel lblDataurodzenia = new JLabel("dataurodzenia");
		infoPanel.add(lblDataurodzenia, "cell 2 4");
		
		JLabel lblEmail = new JLabel("e-mail:");
		infoPanel.add(lblEmail, "cell 0 5");
		
		JLabel lblEmail_1 = new JLabel("email");
		infoPanel.add(lblEmail_1, "cell 2 5");
		
		JLabel lblNrTelefonu = new JLabel("Nr telefonu:");
		infoPanel.add(lblNrTelefonu, "cell 0 6");
		
		JLabel lblNrtelefonu = new JLabel("nrtelefonu");
		infoPanel.add(lblNrtelefonu, "cell 2 6");
		
		JLabel lbllistaPoLewej = new JLabel("<html><body style='width: 330px;'><p align=center>Lista po lewej stronie pokazuje wszystkich dodanych do systemu saneczkarzy. Pole tekstowe nad nią służy do szybkiego przeszukiwania listy po konkretnym imieniu lub nazwisku. Lista po prawej stronie wyświetla zawodników dodanych aktualnie do wybranej z pola rozwijanego konkurencji. Jest ona aktualizowana za każdym użyciem przycisku \"Dodaj\" albo \"Usuń\"</p></body></html>");
		lbllistaPoLewej.setForeground(Color.RED);
		infoPanel.add(lbllistaPoLewej, "cell 0 9 3 1");
		
		JLabel lblWybierzKonkurencjeDo = new JLabel("Wybierz konkurencje do której chcesz dodać zawodników");
		lblWybierzKonkurencjeDo.setHorizontalAlignment(SwingConstants.CENTER);
		
		JComboBox comboBox = new JComboBox();
		GroupLayout gl_compSelPanel = new GroupLayout(compSelPanel);
		gl_compSelPanel.setHorizontalGroup(
			gl_compSelPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(lblWybierzKonkurencjeDo, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
				.addGroup(gl_compSelPanel.createSequentialGroup()
					.addGap(12)
					.addComponent(comboBox, 0, 402, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_compSelPanel.setVerticalGroup(
			gl_compSelPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_compSelPanel.createSequentialGroup()
					.addComponent(lblWybierzKonkurencjeDo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(16, Short.MAX_VALUE))
		);
		compSelPanel.setLayout(gl_compSelPanel);
		
		searchField = new JTextField();
		searchField.setColumns(10);
		
		searchTable = new JTable();
		GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(searchField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
				.addComponent(searchTable, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
		);
		gl_searchPanel.setVerticalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addGap(5)
					.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(searchTable, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
		);
		searchPanel.setLayout(gl_searchPanel);
		contentPane.setLayout(gl_contentPane);
	}
}
