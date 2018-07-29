package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionTypes;
import pl.jeleniagora.mks.types.Luger;
import pl.jeleniagora.mks.types.LugerDouble;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JCheckBox;

@Component
public class CompManagerWindowAddLugerDouble extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5534020364822749669L;
	
	private JPanel contentPane;
	private JTextField searchField;
	private JTable searchTable;
	private JTable competitionTable;
	
	JFrame window;

	JLabel lblImie;
	JLabel lblNazisko;
	JLabel lblDataur;
	JLabel lblTel;
	JLabel lblEm;
	
	/**
	 * Konkurencja wybrana przez ComboBoxa
	 */
	Competition selected;
	
	JComboBox<Competition> comboBox;
	
	/**
	 * Zawodnik wybrany w lewej tabeli
	 */
	Luger selectedInSearchTable;
	
	/**
	 * Zawodik wybrany w prawej tabeli
	 */
	LugerDouble selectedInCompetitionTable;
	
	/**
	 * Pole ustawiane przez checkbox poniżej listy zawodników. Po zaznaczeniu lewy panel ma pokazywać
	 * tylko tych sankarzy którzy nie są jeszcze przypisani do żadnej konkurencji
	 */
	boolean showOnlyNotAddedLugers;
	
	@Autowired
	CompManagerWindowAddLugerDoubleLTableModel leftModel;
	
	CompManagerWindowAddLugerDoubleRTableModel rightModel;
	
	@Autowired
	RTE_ST rte_st;
	
	@Autowired
	RTE_GUI rte_gui;
	
	LugerDouble toAdd;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerWindowAddLugerDouble frame = new CompManagerWindowAddLugerDouble();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void updateContent() {
		searchTable.setModel(leftModel); 	// W momencie wywołania konstruktora obiekt leftModel jest jeszcze przed
											// autowiringiem przez co JTable jest tworzona z nullowym modelem.
		leftModel.updateContent(true);	// aktualizowanie modelu danych do tabeli po lewej stronie
		comboBox.removeAllItems(); 	// wyciepywanie wszystkich aktualnie istniejących elementów checkboxa
		for (Competition c : rte_st.competitions.competitions) {
			
			// sprawdzanie czy aktualnie obrabiana konkurencja to konkurencja dwójkowa
			if (c.competitionType != CompetitionTypes.DOUBLE &&
					c.competitionType != CompetitionTypes.DOUBLE_MEN_ONLY &&
					c.competitionType != CompetitionTypes.DOUBLE_MIXED &&
					c.competitionType != CompetitionTypes.DOUBLE_WOMAN_ONLY &&
					c.competitionType != CompetitionTypes.MARRIED_COUPLE)
				continue;
			
			// aktualizowane comboboxa w oparciu o wszystkie aktualnie zdefiniowane konkurencje
			comboBox.addItem(c);
		}
		
		if (comboBox.getItemCount() == 0) {
			JOptionPane.showMessageDialog(this, "W konfiguracji programu nie została zdefiniowana żadna konkurencja dwójkowa!");
			selected = null;
		}
		else {
			selected = comboBox.getItemAt(0);
			comboBox.setSelectedItem(selected);
			
			rightModel = new CompManagerWindowAddLugerDoubleRTableModel(selected);
			competitionTable.setModel(rightModel);
			rightModel.fireTableDataChanged();
		}
	
	}

	/**
	 * Create the frame.
	 */
	public CompManagerWindowAddLugerDouble() {
		this.window = this;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1017, 570);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel searchPanel = new JPanel();
		
		JPanel compSelPanel = new JPanel();
		
		JPanel competitionPanel = new JPanel();
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		
		JButton btnZamknijOkno = new JButton("Zamknij okno");
		btnZamknijOkno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.dispose();
			}
		});
		btnZamknijOkno.setMinimumSize(new Dimension(433, 25));
		btnZamknijOkno.setMaximumSize(new Dimension(433, 25));
		btnZamknijOkno.setPreferredSize(new Dimension(433, 25));
		
		JButton btnUsuWybranW = new JButton("Usuń wybraną w panelu po prawej dwójkę z konkurencji");
		btnUsuWybranW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// sprawdzanie czy użytkownik nie próbuje usunąć zawodnika z aktualnie rozgrywanej konkurencji.
				// działanie takie jest niedozwolone gdyż z definicji wymagało by ponownego wylosowania nrów startowych
				// co kompletnie rozwaliło by logikę aplikacji, a przede wszystkim poważnie zaburzyło kolejność startową
				if (rte_st.currentCompetition.equals(selected)) {
					JOptionPane.showMessageDialog(window, "Nie możesz usunąć zawodnika z aktualnie rozgrywanej konkurencji!!");
					return;
				}
				
				// sprawdzanie czy użytkownik przed użyciem przycisku usuń wybrał jakiegokolwiek zawodnika
				if (selectedInCompetitionTable != null) {
					
					boolean result = selected.removeFromCompetition(selectedInCompetitionTable);
					
					if (!result) {
						// result może być równy false jeżeli z jakichś powodów próbowano usunąć zawodnika który np. nie znajdował się
						// w ogóle w tej konkurencji. Raczej coś takiego nie wystąpi, bo jeżeli nie był w tej konkurencji to nie powinien
						// się również wyświetlać na liście
						JOptionPane.showMessageDialog(window, "Wystąpił problem podczas usuwania zawodnika z konkurencji");
						return;
					}
					
					rte_st.competitions.listOfAllCompetingLugersInThisComps.remove(selectedInCompetitionTable);
					
					selectedInCompetitionTable.lower.hasBeenAdded = false;
					selectedInCompetitionTable.upper.hasBeenAdded = false;
										
					// regenrowanie modelu danych do prawej tabeli aby uwzględnić zmiany w konkrencji
					rightModel = new CompManagerWindowAddLugerDoubleRTableModel(selected);
					competitionTable.setModel(rightModel);
					rightModel.fireTableDataChanged();
					
					competitionTable.changeSelection(-1, -1, false, false);
				}
				else {
					;
				}
			}
		});
		
		JButton btnDodajPrzygotowanDwjke = new JButton("Dodaj przygotowaną dwójke do konkurencji");
		btnDodajPrzygotowanDwjke.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				////
				if (toAdd == null || toAdd.upper == null || toAdd.lower == null) {
					JOptionPane.showMessageDialog(null, "Nie ustawiłeś odpowiednio saneczkarzy do tej dwójki!");
					return;
				}
				else {
					toAdd.upper.hasBeenAdded = true;
					toAdd.lower.hasBeenAdded = true;
					
					selected.addToCompetition(toAdd);
					rte_st.competitions.listOfAllCompetingLugersInThisComps.add(toAdd);

					rightModel = new CompManagerWindowAddLugerDoubleRTableModel(selected);
					competitionTable.setModel(rightModel);
					rightModel.fireTableDataChanged();
				}
			}
		});
		btnDodajPrzygotowanDwjke.setMinimumSize(new Dimension(433, 25));
		btnDodajPrzygotowanDwjke.setMaximumSize(new Dimension(433, 25));
		btnDodajPrzygotowanDwjke.setPreferredSize(new Dimension(433, 25));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(searchPanel, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnDodajPrzygotowanDwjke, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
						.addComponent(btnUsuWybranW, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(btnZamknijOkno, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
							.addComponent(infoPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(compSelPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)))
					.addGap(18)
					.addComponent(competitionPanel, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(searchPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(compSelPanel, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnDodajPrzygotowanDwjke, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnUsuWybranW, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnZamknijOkno, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
				.addComponent(competitionPanel, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
		);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_competitionPanel = new GroupLayout(competitionPanel);
		gl_competitionPanel.setHorizontalGroup(
			gl_competitionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_competitionPanel.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_competitionPanel.setVerticalGroup(
			gl_competitionPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
		);
		
		competitionTable = new JTable();
		scrollPane_1.setColumnHeaderView(competitionTable);
		scrollPane_1.setViewportView(competitionTable);
		competitionPanel.setLayout(gl_competitionPanel);
		competitionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				@SuppressWarnings("unused")
				int selectedRowFromModel = -1;
				
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
				
				selectedInCompetitionTable = (LugerDouble) rightModel.displayVct.get(selectedRowFromView);
				
			}
			
		});
		
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
		
		JScrollPane scrollPane = new JScrollPane();
		
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
		
		GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(searchField, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addComponent(chckbxPokazujWycznieNiedodanych)
					.addContainerGap())
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_searchPanel.setVerticalGroup(
			gl_searchPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchPanel.createSequentialGroup()
					.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 480, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(chckbxPokazujWycznieNiedodanych))
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
		scrollPane.setColumnHeaderView(searchTable);
		scrollPane.setViewportView(searchTable);
		searchPanel.setLayout(gl_searchPanel);
		infoPanel.setLayout(new MigLayout("", "[][][140px:140px:140px][150px:150px:150px,fill]", "[][][][][][][][][][]"));
		
		JLabel lblInformacjeOSankarzu = new JLabel("Informacje o sankarzu wybranym w lewym panelu");
		lblInformacjeOSankarzu.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblInformacjeOSankarzu, "cell 0 0 4 1,growx");
		
		JLabel lblImi = new JLabel("Imię i Nazwisko:");
		infoPanel.add(lblImi, "cell 1 1");
		
		lblImie = new JLabel("imie");
		lblImie.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblImie, "cell 2 1 2 1,growx");
		
		JLabel lblKlub = new JLabel("Klub:");
		infoPanel.add(lblKlub, "cell 1 2");
		
		lblNazisko = new JLabel("nazisko");
		lblNazisko.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblNazisko, "cell 2 2 2 1,growx");
		
		JLabel lblDataUrodzenia = new JLabel("Data Urodzenia:");
		infoPanel.add(lblDataUrodzenia, "cell 1 3");
		
		lblDataur = new JLabel("dataur");
		lblDataur.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblDataur, "cell 2 3 2 1,growx");
		
		JLabel lblEmail = new JLabel("e-mail:");
		infoPanel.add(lblEmail, "cell 1 4");
		
		lblEm = new JLabel("em");
		lblEm.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblEm, "cell 2 4 2 1,growx");
		
		JLabel lblTelefon = new JLabel("Telefon:");
		infoPanel.add(lblTelefon, "cell 1 5");
		
		lblTel = new JLabel("tel");
		lblTel.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblTel, "cell 2 5 2 1,growx");
		
		JLabel lblUstawWybranego = new JLabel("<html><p align=center>Poniższa sekcja służy do ustawiania wybranego sankarza w dwójce jako \"tego na dole\", bądź \"tego na górze\". Po przygotowaniu dwójki nalezy dodać ją do konkurencji przy użycie stosownego przycisku w dole okna.</p></html>");
		lblUstawWybranego.setForeground(Color.RED);
		infoPanel.add(lblUstawWybranego, "cell 1 7 3 1");
		
		JLabel lblGora = new JLabel("// nie wybrano");
		infoPanel.add(lblGora, "cell 1 8");
		
		JButton btnUstawNaGrze = new JButton("Ustaw na górze");
		btnUstawNaGrze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (toAdd == null)
					toAdd = new LugerDouble();
				
				toAdd.upper = selectedInSearchTable;
				
				lblGora.setText( toAdd.upper.toString());
			}
		});
		infoPanel.add(btnUstawNaGrze, "cell 3 8");
		
		JLabel lblDol = new JLabel("// nie wybrano");
		infoPanel.add(lblDol, "cell 1 9");
		
		JButton btnUstawNaDole = new JButton("Ustaw na dole");
		btnUstawNaDole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (toAdd == null)
					toAdd = new LugerDouble();
				
				toAdd.lower = selectedInSearchTable;
				
				lblDol.setText( toAdd.lower.toString());
			}
		});
		infoPanel.add(btnUstawNaDole, "cell 3 9");
		
		comboBox = new JComboBox<Competition>();
		
		JLabel lblWybierzKonkurencjeDo = new JLabel("Wybierz konkurencje dwójkową do edycji");
		lblWybierzKonkurencjeDo.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_compSelPanel = new GroupLayout(compSelPanel);
		gl_compSelPanel.setHorizontalGroup(
			gl_compSelPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(comboBox, 0, 440, Short.MAX_VALUE)
				.addComponent(lblWybierzKonkurencjeDo, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
		);
		gl_compSelPanel.setVerticalGroup(
			gl_compSelPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_compSelPanel.createSequentialGroup()
					.addComponent(lblWybierzKonkurencjeDo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		compSelPanel.setLayout(gl_compSelPanel);
		contentPane.setLayout(gl_contentPane);
	}
	
	void updateMoreInfo(Luger in) {
		lblImie.setText(in.name + " " + in.surname);
		lblNazisko.setText(in.club.name);
		if (in.email != null && in.email.contains("@"))
			lblEm.setText(in.email);
		else 
			lblEm.setText("// Nie zdefiniowano");
		lblDataur.setText(in.birthDate.toString());
		if (in.telephoneNumber > 100)
			lblTel.setText(new Long(in.telephoneNumber).toString());
		else
			lblTel.setText("// Nie zdefiniowano");
	}
	
}
