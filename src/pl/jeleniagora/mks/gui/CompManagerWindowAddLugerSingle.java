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
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.exceptions.MissingCompetitionEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionTypes;
import pl.jeleniagora.mks.types.Luger;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerSingle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Component
@Lazy
public class CompManagerWindowAddLugerSingle extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -679230823534376426L;
	private JPanel contentPane;
	
	/**
	 * Pole tekstowe do przeszukiwania listy zawodników do dodania
	 */
	private JTextField searchField;
	
	/**
	 * Tabela po lewej do przeszukiwania po zawodnikach do dodania do konkurencji
	 */
	private JTable searchTable;
	
	/**
	 * Tablea po lewej do pokazania aktualnie dodanych do konkurencji zawodników
	 */
	private JTable competitionTable;
	
	/**
	 * ComboBox do wybierania konkurencji do dodania/usunięcia zawodników
	 */
	JComboBox<Competition> comboBox;
	
	/**
	 * Konkurencja wybrana przez ComboBoxa
	 */
	Competition selected;
	
	JLabel lblImienazwisko;
	JLabel lblKlub_1;
	JLabel lblDataurodzenia;
	JLabel lblEmail_1;
	JLabel lblNrtelefonu;
	
	/**
	 * Zawodnik wybrany w lewej tabeli
	 */
	Luger selectedInSearchTable;
	
	/**
	 * Zawodik wybrany w prawej tabeli
	 */
	LugerSingle selectedInCompetitionTable;

	CompManagerWindowAddLugerSingle window;
	
	@Autowired
	CompManagerWindowAddLugerSingleLTableModel leftModel;

	CompManagerWindowAddLugerSingleRTableModel rightModel;


	@Autowired
	RTE_ST rte_st;
	
	@Autowired
	RTE_GUI rte_gui;
	
	/**
	 * Pole ustawiane przez checkbox poniżej listy zawodników. Po zaznaczeniu lewy panel ma pokazywać
	 * tylko tych sankarzy którzy nie są jeszcze przypisani do żadnej konkurencji
	 */
	boolean showOnlyNotAddedLugers = true;
	
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
	
	public void updateContent() throws MissingCompetitionEx {
		searchTable.setModel(leftModel); 	// W momencie wywołania konstruktora obiekt leftModel jest jeszcze przed
											// autowiringiem przez co JTable jest tworzona z nullowym modelem.
		leftModel.updateContent(true);	// aktualizowanie modelu danych do tabeli po lewej stronie
		comboBox.removeAllItems(); 	// wyciepywanie wszystkich aktualnie istniejących elementów checkboxa
		
		if (rte_st.competitions.competitions == null) {
			throw new MissingCompetitionEx();
		}
		
		for (Competition c : rte_st.competitions.competitions) {
			if (c.competitionType != CompetitionTypes.MEN_SINGLE &&
					c.competitionType != CompetitionTypes.WOMAN_SINGLE)
				continue;
			
			// aktualizowane comboboxa w oparciu o wszystkie aktualnie zdefiniowane konkurencje
			comboBox.addItem(c);
		}		
		selected = rte_st.competitions.competitions.get(0);
		comboBox.setSelectedItem(selected);
		
		rightModel = new CompManagerWindowAddLugerSingleRTableModel(selected);
		competitionTable.setModel(rightModel);
		rightModel.fireTableDataChanged();
	}

	/**
	 * Create the frame.
	 */
	public CompManagerWindowAddLugerSingle() {
		this.window = this;
		setResizable(false);
		setTitle("Dopisz lub usuń zawodnika z konkurencji jedynek");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1017, 570);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel searchPanel = new JPanel();
		
		JPanel compSelPanel = new JPanel();
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		
		JButton btnDodajZaznaczonegoDo = new JButton("Dodaj zaznaczonego w lewym panelu do konkurencji");
		btnDodajZaznaczonegoDo.addActionListener(new ActionListener() {
			// dodawanie zaznaczonego w panelu po lewej saneczkarza do konkurencji
			
			// dodawanie do aktualnie rozgrywanej konkurencji jest możliwe ale zawodnik zostanie dodany bez nru startowego (0)
			// co spowoduje że nie będzie uwzględniany na liście startowej. Jego wciągnięcie na listę będzie wymagało
			// ponownego wygenerowanie nrów startowych
			public void actionPerformed(ActionEvent arg0) {
				boolean gender = false;
				if(selected.competitionType.equals(CompetitionTypes.WOMAN_SINGLE))
					gender = true;
				else;
				
				LugerSingle toAdd = new LugerSingle(gender);
				toAdd.single = selectedInSearchTable;
				toAdd.single.hasBeenAdded = true;
				selected.addToCompetition(toAdd);
				rte_st.competitions.listOfAllCompetingLugersInThisComps.add(toAdd);
				
				rightModel = new CompManagerWindowAddLugerSingleRTableModel(selected);
				competitionTable.setModel(rightModel);
				rightModel.fireTableDataChanged();
				
				leftModel.updateContent(showOnlyNotAddedLugers);
				leftModel.fireTableStructureChanged();
				leftModel.fireTableDataChanged();
			}
		});
		
		JPanel panel = new JPanel();
		
		JButton btnUsuZKonkurencji = new JButton("Usuń zaznaczonego w prawym panelu z konkurencji");
		btnUsuZKonkurencji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				LugerSingle toBeDeleted = selectedInCompetitionTable;
				
				// sprawdzanie czy użytkownik nie próbuje usunąć zawodnika z aktualnie rozgrywanej konkurencji.
				// działanie takie jest niedozwolone gdyż z definicji wymagało by ponownego wylosowania nrów startowych
				// co kompletnie rozwaliło by logikę aplikacji, a przede wszystkim poważnie zaburzyło kolejność startową
				if (rte_st.currentCompetition != null && rte_st.currentCompetition.equals(selected)) {
					JOptionPane.showMessageDialog(window, "Nie możesz usunąć zawodika z aktualnie rozgrywanej konkurencji!!");
					return;
				}
				
				// sprawdzanie czy użytkownik przed użyciem przycisku usuń wybrał jakiegokolwiek zawodnika
				if (toBeDeleted != null) {
					
					boolean result = selected.removeFromCompetition(toBeDeleted);
					
					if (!result) {
						// result może być równy false jeżeli z jakichś powodów próbowano usunąć zawodnika który np. nie znajdował się
						// w ogóle w tej konkurencji. Raczej coś takiego nie wystąpi, bo jeżeli nie był w tej konkurencji to nie powinien
						// się również wyświetlać na liście
						JOptionPane.showMessageDialog(window, "Wystąpił problem podczas usuwania zawodnika z konkurencji");
						return;
					}
					
					rte_st.competitions.listOfAllCompetingLugersInThisComps.remove(selectedInCompetitionTable);
					toBeDeleted.single.hasBeenAdded = false;
					
					// regenrowanie modelu danych do prawej tabeli aby uwzględnić zmiany w konkrencji
					rightModel = new CompManagerWindowAddLugerSingleRTableModel(selected);
					competitionTable.setModel(rightModel);
					rightModel.fireTableDataChanged();
					
					competitionTable.changeSelection(-1, -1, false, false);
				}
				else {
					;
				}
			}
		});
		
		JButton btnZakocz = new JButton("Zamknij okno");
		btnZakocz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// sprawdzanie czy w głównym oknie nie jest wyświetlana konkurencja z której usuwa bądź dodaje się zawodników
				if (rte_gui.competitionBeingShown.equals(selected)) {
					// jeżeli tak to należy odświeżyć jej widok
					try {
						rte_gui.compManagerScoreModel.updateTableData(selected, false);
						rte_gui.compManagerScoreModel.fireTableDataChanged();		// wywoływanie triggera po aktualizacji zawartości tabeli
					} catch (UninitializedCompEx e) {
						e.printStackTrace();	// ten wyjątek raczej się tutaj nie rzuci
					}
				}
				else {
					;	// jeżeli wyświetlana jest inna to nic nie rób
				}
				
				window.dispose();
			}
		});
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
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
		);
		
		competitionTable = new JTable();
		scrollPane.setViewportView(competitionTable);
		competitionTable.setFillsViewportHeight(true);
		competitionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				@SuppressWarnings("unused")
				int selectedRowFromModel = -1;	// ? czy to jest potrzebe?
				
				// numer zaznaczonego z widoku
				int selectedRowFromView = competitionTable.getSelectedRow();
				
				try {
					// konwersja na pozycję w modelu 
					selectedRowFromModel = competitionTable.convertColumnIndexToModel(selectedRowFromView);
				}
				catch(Exception e) {
					;
				}
				
				if (selectedRowFromView < 0) {
					selectedInCompetitionTable = null;
					return;
				}
				
				selectedInCompetitionTable = (LugerSingle) rightModel.displayVct.get(selectedRowFromView);
				
			}
			
		});
		/////

		panel.setLayout(gl_panel);
		infoPanel.setLayout(new MigLayout("", "[114px][8px][280px:280px]", "[15px][15px][][][][][][][][]"));
		
		JLabel lblSzczegoweInformacjeO = new JLabel("Szczegółowe informacje o wybranym saneczkarzu");
		infoPanel.add(lblSzczegoweInformacjeO, "cell 0 0 3 1,alignx center,aligny top");
		
		JLabel lblImiINazwisko = new JLabel("Imię i Nazwisko:");
		infoPanel.add(lblImiINazwisko, "cell 0 2,alignx left,aligny top");
		
		lblImienazwisko = new JLabel("imie_nazwisko");
		infoPanel.add(lblImienazwisko, "cell 2 2,alignx left,aligny top");
		
		JLabel lblKlub = new JLabel("Klub:");
		infoPanel.add(lblKlub, "cell 0 3");
		
		lblKlub_1 = new JLabel("klub");
		infoPanel.add(lblKlub_1, "cell 2 3");
		
		JLabel lblDataUrodzenia = new JLabel("Data Urodzenia:");
		infoPanel.add(lblDataUrodzenia, "cell 0 4");
		
		lblDataurodzenia = new JLabel("dataurodzenia");
		infoPanel.add(lblDataurodzenia, "cell 2 4");
		
		JLabel lblEmail = new JLabel("e-mail:");
		infoPanel.add(lblEmail, "cell 0 5");
		
		lblEmail_1 = new JLabel("email");
		infoPanel.add(lblEmail_1, "cell 2 5");
		
		JLabel lblNrTelefonu = new JLabel("Nr telefonu:");
		infoPanel.add(lblNrTelefonu, "cell 0 6");
		
		lblNrtelefonu = new JLabel("nrtelefonu");
		infoPanel.add(lblNrtelefonu, "cell 2 6");
		
		JLabel lbllistaPoLewej = new JLabel("<html><body style='width: 330px;'><p align=center>Lista po lewej stronie pokazuje wszystkich dodanych do systemu saneczkarzy. Pole tekstowe nad nią służy do szybkiego przeszukiwania listy po konkretnym imieniu lub nazwisku. Lista po prawej stronie wyświetla zawodników dodanych aktualnie do wybranej z pola rozwijanego konkurencji. Jest ona aktualizowana za każdym użyciem przycisku \"Dodaj\" albo \"Usuń\"</p></body></html>");
		lbllistaPoLewej.setForeground(Color.RED);
		infoPanel.add(lbllistaPoLewej, "cell 0 9 3 1");
		
		JLabel lblWybierzKonkurencjeDo = new JLabel("Wybierz konkurencje do której chcesz dodać zawodników");
		lblWybierzKonkurencjeDo.setHorizontalAlignment(SwingConstants.CENTER);
		
		comboBox = new JComboBox<Competition>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object object = arg0.getSource();
				
				// wyciąganie zaznaczonej w polu rozwijanym konkurencji
				if (object instanceof JComboBox<?>) {
					
					@SuppressWarnings("unchecked")
					JComboBox<Competition> selector = (JComboBox<Competition>)object;
					
					selected = (Competition) selector.getSelectedItem();
				}
				else return;
				
				// wypełnianie prawego panela zawodnikami dopisanymi już do wybranej konkurencji
				rightModel = new CompManagerWindowAddLugerSingleRTableModel(selected);
				competitionTable.setModel(rightModel);
				
				searchField.setText("");
				
			}
		});
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
		searchField.addKeyListener(new KeyAdapter() {
			// listener od naciśnięcia przycisku w oknie
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				return;
			}
			@Override
			public void keyReleased(KeyEvent e) {
				String text = searchField.getText();
				leftModel.updateContent(showOnlyNotAddedLugers, text);
			}
		});
		searchField.setColumns(10);
		//searchTable.setModel();
		
		JCheckBox chckbxPokazujWycznieNiedodanych = new JCheckBox("Pokazuj wyłącznie niedodanych");
		chckbxPokazujWycznieNiedodanych.setSelected(true);
		chckbxPokazujWycznieNiedodanych.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object source = arg0.getSource();
				JCheckBox c = (JCheckBox)source;
				if (c.isSelected())
					showOnlyNotAddedLugers = true;
				else 
					showOnlyNotAddedLugers = false;
				
				leftModel.updateContent(showOnlyNotAddedLugers);
				leftModel.fireTableStructureChanged();
				leftModel.fireTableDataChanged();
				
				searchField.setText("");
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(searchField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addComponent(chckbxPokazujWycznieNiedodanych)
					.addContainerGap())
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_searchPanel.setVerticalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addGap(5)
					.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxPokazujWycznieNiedodanych)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		searchTable = new JTable(leftModel);
		searchTable.setAutoCreateRowSorter(true);
		searchTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			// listener od kliknięcia na wiersz
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int selectedRowFromModel = -1;
				
				// numer klikniętego wiersza w widoku
				int selectedRow = searchTable.getSelectedRow();
				
				try {
					// konwersja wiersza w widoku na wiersz w modelu danych
					selectedRowFromModel = searchTable.convertRowIndexToModel(selectedRow);
				}
				catch (Exception e) {
					;
				}
				
				if (selectedRowFromModel < 0)
					return;		// jeżeli tabela jest pusta
				
				// ustawianie klikniętego w lewej tabeli
				selectedInSearchTable = leftModel.listOfLugersToShow.get(selectedRowFromModel);
				
				// aktualizowanie labelek
				updateMoreInfo(selectedInSearchTable);
			}
			
		});
		
		scrollPane_1.setViewportView(searchTable);
		searchPanel.setLayout(gl_searchPanel);
		contentPane.setLayout(gl_contentPane);
	}
	
	void updateMoreInfo(Luger in) {
		lblImienazwisko.setText(in.name + " " + in.surname);
		lblKlub_1.setText(in.club.name);
		if (in.email != null && in.email.contains("@"))
			lblEmail_1.setText(in.email);
		else 
			lblEmail_1.setText("// Nie zdefiniowano");
		lblDataurodzenia.setText(in.birthDate.toString());
		if (in.telephoneNumber > 100)
			lblNrtelefonu.setText(new Long(in.telephoneNumber).toString());
		else
			lblNrtelefonu.setText("// Nie zdefiniowano");
	}
}
