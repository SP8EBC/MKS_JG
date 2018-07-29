package pl.jeleniagora.mks.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

import pl.jeleniagora.mks.start.order.StartOrderInterface;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.Run;

import javax.swing.JComboBox;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;

public class CompManagerWindowEditCompetition extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7099964404892224928L;
	private JPanel contentPane;
	private JTextField textFieldName;
	
	StartOrderInterface choosenStartOrder;
	
	JComboBox<Competition> comboBoxCompetition;
	
	/**
	 * Ilość ślizgów treningowych dla konkurencji wybranej z listy rozwijanej
	 */
	int trainingRunsForChoosen;
	
	int indexOfLastTrainingRun;
	Run lastTrainingRun;
	
	/**
	 * Ilość ślizgów punktwanych dla konkurencji wybraej z listy rozwijanej
	 */
	int scoredRunsForChoosen;
	
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
		
		trainingRunsForChoosen = competition.numberOfTrainingRuns;
		scoredRunsForChoosen = competition.numberOfAllRuns - trainingRunsForChoosen;
		
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
		
		spinnerTrainingRuns.setValue(trainingRunsForChoosen);
		spinnerScoredRuns.setValue(scoredRunsForChoosen);
	}

	/**
	 * Create the frame.
	 */
	public CompManagerWindowEditCompetition() {
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
				
				Competition choosen = (Competition)castedSource.getSelectedItem();
				
				updateContent(choosen);
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
		
		JRadioButton rdbtnUproszczonaWg = new JRadioButton("Uproszczona - wg numerów startowych rosnąco");
		competitionParametersPanel.add(rdbtnUproszczonaWg, "cell 1 1 8 1");
		
		JRadioButton rdbtnZgodnaZRegulamnem = new JRadioButton("Zgodna z regulamnem sportowym FIL");
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
		spinnerTrainingRuns.setModel(new SpinnerNumberModel(1, 1, 9, 1));
		competitionParametersPanel.add(spinnerTrainingRuns, "cell 4 4");
		
		JLabel lblPunktowane = new JLabel("Punktowane");
		competitionParametersPanel.add(lblPunktowane, "cell 6 4");
		
		spinnerScoredRuns = new JSpinner();
		spinnerScoredRuns.setModel(new SpinnerNumberModel(0, 0, 9, 1));
		competitionParametersPanel.add(spinnerScoredRuns, "cell 7 4");
		
		JLabel lbluwagaZmniejszenieLiczby = new JLabel("<html><p align='center'>Uwaga! Zmniejszenie liczby ślizgów i zatwierdzenie zmian spowoduje bezpowrotne usunięcie części czasów.</p></html>");
		lbluwagaZmniejszenieLiczby.setForeground(Color.RED);
		competitionParametersPanel.add(lbluwagaZmniejszenieLiczby, "cell 0 5 9 1");
		
		JButton btnZapiszIZamknij = new JButton("Zapisz i zamknij");
		competitionParametersPanel.add(btnZapiszIZamknij, "cell 0 6 3 1,growx");
		
		JButton btnWyjdBezZapisu = new JButton("Wyjdź bez zapisu");
		competitionParametersPanel.add(btnWyjdBezZapisu, "cell 4 6 5 1,growx");
		contentPane.setLayout(gl_contentPane);
	}
}
