package pl.jeleniagora.mks.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.start.order.FilOrder;
import pl.jeleniagora.mks.start.order.SimpleOrder;
import pl.jeleniagora.mks.start.order.StartOrderInterface;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Reserve;
import pl.jeleniagora.mks.types.Run;

import javax.swing.JComboBox;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompManagerWindowEditCompetition extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7099964404892224928L;
	private JPanel contentPane;
	private JTextField textFieldName;
	
	@Autowired
	RTE_GUI rte_gui;
	
	@Autowired
	RTE_ST rte_st;
	
	CompManagerWindowEditCompetition window;
	
	StartOrderInterface choosenStartOrder;
	
	JComboBox<Competition> comboBoxCompetition;
	Competition chosenCompetition;
	
	/**
	 * Ilość ślizgów treningowych dla konkurencji wybranej z listy rozwijanej
	 */
	int trainingRunsForChosen;
	
	/**
	 * Ilość ślizgów punktwanych dla konkurencji wybraej z listy rozwijanej
	 */
	int scoredRunsForChosen;
	
	/**
	 * Indek ostatniego ślizgu treningowego w konkurencji
	 */
	int indexOfLastTrainingRun;
	Run lastTrainingRun;
	
	/**
	 * Indeks pierwszego punktowanego ślizgu w wektorze Run
	 */
	int indexOfFirstScored;
	Run firstScored;
	
	/**
	 * Nazwa konkurencji wybranej z listy rozwijanej
	 */
	String nameForChoosen;
	
	/**
	 * Spinner do zmiany ilości ślizgów treningowych
	 */
	JSpinner spinnerTrainingRuns;
	
	/**
	 * Spinner do zmiany ilości ślizgów punktowanych
	 */
	JSpinner spinnerScoredRuns;
	
	JRadioButton rdbtnUproszczonaWg;
	JRadioButton rdbtnZgodnaZRegulamnem;
	
	public void updateContent(Competitions competitions) {
		
		if (competitions.competitions.size() == 0) 
			return;
		
		comboBoxCompetition.removeAllItems();
		
		// dodawanie od nowa wszystkich aktualnie zdefiniowanych 
		for (Competition c : competitions.competitions) {
			comboBoxCompetition.addItem(c);
		}
		
		comboBoxCompetition.setSelectedItem(competitions.competitions.get(0));
	}
	
	private void updateContent(Competition competition) {
		nameForChoosen = competition.name;
		
		trainingRunsForChosen = competition.numberOfTrainingRuns;
		scoredRunsForChosen = competition.numberOfAllRuns - trainingRunsForChosen;
		
		for (Run r : competition.runsTimes) {
			// pętla chodzi po wszystkich ślizgach - zjazdach w tej konkurencji
			if (!r.trainingOrScored) {
				// co do zasady ślizgi są posortowane w kolejności najpierw treingowe
				// potem punktowane. Dlatego ostatni ślizg, który zostanie tutaj 
				indexOfLastTrainingRun = competition.runsTimes.indexOf(r);
				lastTrainingRun = r;
			}
		}
		
		indexOfFirstScored = indexOfLastTrainingRun + 1;
		firstScored = competition.runsTimes.elementAt(indexOfFirstScored);
		
		spinnerTrainingRuns.setValue(trainingRunsForChosen);
		spinnerScoredRuns.setValue(scoredRunsForChosen);
		
		textFieldName.setText(nameForChoosen);
		
		if (competition.startOrder instanceof SimpleOrder) {
			rdbtnUproszczonaWg.setSelected(true);
			rdbtnZgodnaZRegulamnem.setSelected(false);			
		}
		else if (competition.startOrder instanceof FilOrder) {
			rdbtnZgodnaZRegulamnem.setSelected(true);
			rdbtnUproszczonaWg.setSelected(false);

		}
	}

	/**
	 * Create the frame.
	 */
	public CompManagerWindowEditCompetition() {
		this.window = this;
		setResizable(false);
		setTitle("Edytuj parametry konkurencji");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 574, 302);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblWybierzKonkurencjDo = new JLabel("Wybierz konkurencję do edycji:");
		
		comboBoxCompetition = new JComboBox<Competition>();
		comboBoxCompetition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object actionSource = arg0.getSource();
				
				@SuppressWarnings("unchecked")
				JComboBox<Competition> castedSource = (JComboBox<Competition>)actionSource;
				
				chosenCompetition = (Competition)castedSource.getSelectedItem();
				
				updateContent(chosenCompetition);
			}
			
		});
		
		JPanel competitionParametersPanel = new JPanel();
		competitionParametersPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblWybierzKonkurencjDo, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxCompetition, 0, 288, Short.MAX_VALUE))
				.addComponent(competitionParametersPanel, GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWybierzKonkurencjDo)
						.addComponent(comboBoxCompetition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(competitionParametersPanel, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))
		);
		competitionParametersPanel.setLayout(new MigLayout("", "[][grow][][][][][][][3.00][]", "[][][][][][][]"));
		
		JLabel lblNazwaKonkurencji = new JLabel("Nazwa konkurencji:");
		competitionParametersPanel.add(lblNazwaKonkurencji, "cell 0 0,alignx trailing");
		
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("Dialog", Font.PLAIN, 16));
		competitionParametersPanel.add(textFieldName, "cell 1 0 8 1,grow");
		textFieldName.setColumns(10);
		
		JLabel lblKolejnoStartowa = new JLabel("Kolejność startowa:");
		competitionParametersPanel.add(lblKolejnoStartowa, "cell 0 1");
		
		ButtonGroup startOrderGroup = new ButtonGroup();
		
		rdbtnUproszczonaWg = new JRadioButton("Uproszczona - wg numerów startowych rosnąco");
		competitionParametersPanel.add(rdbtnUproszczonaWg, "cell 1 1 8 1");
		
		rdbtnZgodnaZRegulamnem = new JRadioButton("Zgodna z regulamnem sportowym FIL");
		competitionParametersPanel.add(rdbtnZgodnaZRegulamnem, "cell 1 2 8 1");
		
		startOrderGroup.add(rdbtnZgodnaZRegulamnem);
		startOrderGroup.add(rdbtnUproszczonaWg);
		
		JLabel lblUwagaZmianaKolejoci = new JLabel("<html><p align='center'>Uwaga! Zmiana kolejości startowej zostanie wprowadzona dopiero po zakończeniu aktualnego ślizgu!</p></html>");
		lblUwagaZmianaKolejoci.setForeground(Color.RED);
		competitionParametersPanel.add(lblUwagaZmianaKolejoci, "cell 0 3 9 1");
		
		JLabel lblZmieIlolizgw = new JLabel("Ilość ślizgów w konkurencji:");
		competitionParametersPanel.add(lblZmieIlolizgw, "cell 0 4 2 1");
		
		JLabel lblTreningowe = new JLabel("Treningowe");
		competitionParametersPanel.add(lblTreningowe, "cell 3 4");
		
		spinnerTrainingRuns = new JSpinner();
		spinnerTrainingRuns.setModel(new SpinnerNumberModel(0, 0, 9, 1));
		competitionParametersPanel.add(spinnerTrainingRuns, "cell 4 4");
		
		JLabel lblPunktowane = new JLabel("Punktowane");
		competitionParametersPanel.add(lblPunktowane, "cell 6 4");
		
		spinnerScoredRuns = new JSpinner();
		spinnerScoredRuns.setModel(new SpinnerNumberModel(1, 1, 9, 1));
		competitionParametersPanel.add(spinnerScoredRuns, "cell 7 4");
		
		JLabel lbluwagaZmniejszenieLiczby = new JLabel("<html><p align='center'>Uwaga! Zmniejszenie liczby ślizgów i zatwierdzenie zmian spowoduje bezpowrotne usunięcie części czasów.</p></html>");
		lbluwagaZmniejszenieLiczby.setForeground(Color.RED);
		competitionParametersPanel.add(lbluwagaZmniejszenieLiczby, "cell 0 5 9 1");
		
		JButton btnZapiszIZamknij = new JButton("Zapisz i zamknij");
		btnZapiszIZamknij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// sprawdzanie czy użytkownik nie próbuje zmniejszyć ilości ślizgów
				if(	(int)spinnerScoredRuns.getValue() < scoredRunsForChosen ||
					(int)spinnerTrainingRuns.getValue() < trainingRunsForChosen) {
					
					Competition c = chosenCompetition;
					
					int answer = JOptionPane.showConfirmDialog(window, "Czy na pewno chcesz zmniejszyć liczbę ślizgów i usunąć część czasów?", "Pozor!", JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						//zmniejszanie ilości ślizgów przez usunięcie ostatnich ślizgów odpowiednio treningowych a potem punktowanych;
						
						// indeks pierwszego ślizgu treningowego do usunięcia
						int firstTrainingToRemove = trainingRunsForChosen - (trainingRunsForChosen - (int)spinnerTrainingRuns.getValue());
						
						// sprawdzenie czy w ogóle należy usuwać jakiekolwiek treningowe (może chcieć tylko punktowane)
						if (firstTrainingToRemove < trainingRunsForChosen) {
							// usuwanie ślizgów treningowych
							for (int i = firstTrainingToRemove; i <= indexOfLastTrainingRun; i++) {
								c.runsTimes.remove(i);
							}
							
							trainingRunsForChosen = (int)spinnerTrainingRuns.getValue();
							c.numberOfTrainingRuns = trainingRunsForChosen;
						}
						
						// powtórne odszukiwanie indeksów ostatniego treningowego i pierwszego punktowanego
						// gdyż operacja usuwania z wektora powoduje przesuwanie następnych elementów w lewo
						for (Run r : c.runsTimes) {
							// pętla chodzi po wszystkich ślizgach - zjazdach w tej konkurencji
							if (!r.trainingOrScored) {
								// co do zasady ślizgi są posortowane w kolejności najpierw treingowe
								// potem punktowane. Dlatego ostatni ślizg, który zostanie tutaj 
								indexOfLastTrainingRun = chosenCompetition.runsTimes.indexOf(r);
								lastTrainingRun = r;
							}
						}
						
						indexOfFirstScored = indexOfLastTrainingRun + 1;
						firstScored = chosenCompetition.runsTimes.elementAt(indexOfFirstScored);
						
						// wyciąganie aktualnej liczby wszystkich ślizgów/zjazdów 
						int runsCount = c.runsTimes.size();
						
						// znajdywanie pierwszego indeksu ślizgu do usunięcia (punktowane są zawsze po treningach)
						int firstScoredToRemove = runsCount - (scoredRunsForChosen - (int)spinnerScoredRuns.getValue());
						
						if (firstScoredToRemove < runsCount) {
							for (int i = firstScoredToRemove; i < runsCount; i++) {
								c.runsTimes.remove(i);
							}
							
							scoredRunsForChosen = (int)spinnerScoredRuns.getValue();
							chosenCompetition.numberOfAllRuns = trainingRunsForChosen + scoredRunsForChosen;
						}
						
					}
					if (answer == JOptionPane.NO_OPTION) {
						;
					}
				}
				// sprawdzanie czy użytkownik nie chcę dodać nowych ślizgów / zjazdów do konkurencji
				else if ((int)spinnerScoredRuns.getValue() > scoredRunsForChosen ||
					(int)spinnerTrainingRuns.getValue() > trainingRunsForChosen) 
				{
					int scoredToAdd = (int)spinnerScoredRuns.getValue() - scoredRunsForChosen;
					int trainingToAdd = (int)spinnerTrainingRuns.getValue() - trainingRunsForChosen;
					
					// tworzenie wektora z saneczkarzami z tej konkurencji na podstawie listy czasów
					// z innego ślizgu z tej konkurencji
					Set<Entry<LugerCompetitor, LocalTime>> set =  firstScored.totalTimes.entrySet();
					
					// wektor na saneczkarzy do dodania do kolejnego ślizgu
					Vector<LugerCompetitor> cmptr = new Vector<LugerCompetitor>();
					
					for (Entry<LugerCompetitor, LocalTime> elem : set) {
						LugerCompetitor key = elem.getKey();
						
						cmptr.addElement(key);
					}
					
					// sprawdzanie czy trzeba dodać jakiś ślizg punktowany
					if (scoredToAdd > 0) {
						// jeżeli trzeba dodać to trzeba go dodać na samym końcu
						for (int i = 0; i < scoredToAdd; i++) {
							Run toAdd = new Run(cmptr, (byte)1);
							chosenCompetition.runsTimes.add(toAdd);
							chosenCompetition.numberOfAllRuns++;
						}
					}
					if (trainingToAdd > 0) {
						for (int i = 0; i < trainingToAdd; i++) {
							// każdy ślizg treningowy powinien zostać dodany po ostatnim treningowym
							Run toAdd = new Run(cmptr, (byte)0);
							chosenCompetition.runsTimes.add(i + trainingRunsForChosen, toAdd);
							chosenCompetition.numberOfAllRuns++;
							chosenCompetition.numberOfTrainingRuns++;
						}
					}
				}
				else;
				
				if (rte_gui.competitionBeingShown.equals(rte_st.currentCompetition)) {
					try {
						rte_gui.compManagerScoreModel.updateTableData(chosenCompetition, false);
						rte_gui.compManagerScoreModel.updateTableHeading(chosenCompetition, false);
					} catch (UninitializedCompEx | Reserve e1) {
						e1.printStackTrace();
					}
					rte_gui.compManagerScoreModel.fireTableStructureChanged();
					rte_gui.compManagerScoreModel.fireTableDataChanged();
				}
				window.dispose();
			}
			
		});
		competitionParametersPanel.add(btnZapiszIZamknij, "cell 0 6 3 1,growx");
		
		JButton btnWyjdBezZapisu = new JButton("Wyjdź bez zapisu");
		btnWyjdBezZapisu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				window.dispose();
			}
		});
		competitionParametersPanel.add(btnWyjdBezZapisu, "cell 4 6 5 1,growx");
		contentPane.setLayout(gl_contentPane);
	}
}
