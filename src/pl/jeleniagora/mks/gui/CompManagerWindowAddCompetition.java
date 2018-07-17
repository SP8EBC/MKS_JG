package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionTypes;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;
import java.awt.event.ActionEvent;

@Component
public class CompManagerWindowAddCompetition extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3526082900980367384L;
	private JPanel contentPane;
	private JTextField textCompName;

	@Autowired
	RTE_ST rte_st;
	
	@Autowired
	CompManagerCSelectorUpdater selectorUpdater;	
	
	public CompManagerWindowAddCompetition window;
	
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
		this.window = this;
		
		setResizable(false);
		setTitle("Dodaj nową konkurencję");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 530, 363);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[250px:n:250px][][40px:n][][][]", "[][40px:n:40px,bottom][][][][][][][]"));
		
		JLabel lblWybierzTym = new JLabel("Wybierz tym konkurencji:\n");
		contentPane.add(lblWybierzTym, "cell 0 0,alignx trailing");
		
		JComboBox<CompetitionTypes> comboBox = new JComboBox<CompetitionTypes>();
		comboBox.addItem(CompetitionTypes.MEN_SINGLE);
		comboBox.addItem(CompetitionTypes.WOMAN_SINGLE);
		comboBox.addItem(CompetitionTypes.DOUBLE);
		comboBox.addItem(CompetitionTypes.DOUBLE_MEN_ONLY);
		comboBox.addItem(CompetitionTypes.DOUBLE_MIXED);
		comboBox.addItem(CompetitionTypes.DOUBLE_WOMAN_ONLY);
		comboBox.addItem(CompetitionTypes.MARRIED_COUPLE);
		contentPane.add(comboBox, "cell 1 0 5 1,growx");
		
		JLabel lblWpiszNazwKonkurencji = new JLabel("<html><body style='heigh=200px; text-align= center'><p align=center>Wpisz nazwę konkurencji. Jeżeli pozostawisz pole puste jako nazwa będzie służyło określenie typu konkurencji</p></body></html>");
		lblWpiszNazwKonkurencji.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblWpiszNazwKonkurencji, "cell 0 1 6 1");
		
		textCompName = new JTextField();
		textCompName.setMinimumSize(new Dimension(4, 30));
		textCompName.setFont(new Font("Dialog", Font.BOLD, 14));
		contentPane.add(textCompName, "cell 0 2 6 1,growx");
		textCompName.setColumns(10);
		
		JLabel lblIloWszystkichlizgw = new JLabel("Ilość wszystkich ślizgów / zjazdów:");
		contentPane.add(lblIloWszystkichlizgw, "cell 0 3");
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(0, 0, 9, 1));
		spinner.setFont(new Font("Dialog", Font.BOLD, 18));
		contentPane.add(spinner, "flowx,cell 2 3");
		
		JLabel lblWTymTreningowych = new JLabel("W tym treningowych:");
		contentPane.add(lblWTymTreningowych, "cell 3 3");
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(0, 0, 9, 1));
		spinner_1.setFont(new Font("Dialog", Font.BOLD, 18));
		contentPane.add(spinner_1, "cell 4 3");
		
		JCheckBox chckbxTraktujWszystkielizgi = new JCheckBox("Traktuj wszystkie ślizgi / zjazdy jako treningowe");
		contentPane.add(chckbxTraktujWszystkielizgi, "cell 0 4");
		
		JLabel lblZaznaczeniePowyszejOpcji = new JLabel("<html><body style='width = 200px;'><p align=center>Zaznaczenie powyższej opcji powoduje że program będzie obliczał lokatę po zakończeniu każdego zjazdu / ślizgu (tak jak dla punktowanych) bez względu na liczbę nastawioną w polu \"w tym treningowe\". Jednoczeście użycie opcji DNF / DNS będzie odnosiło się do bierzącego ślizgu a dyskwalifikacja nie będzie działała w ogóle (tak jak dla treningu)</p></body></html>");
		contentPane.add(lblZaznaczeniePowyszejOpcji, "cell 0 5 5 2");
		
		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Competition c = new Competition();
				
				CompetitionTypes type = (CompetitionTypes)comboBox.getSelectedItem();
				String compName = textCompName.getText();
				if (compName.length() > 0) {
					;
				}
				else {
					compName = type.toString();
				}
				
				c.id = rte_st.competitions.getNextIdForNewCompetition();
				
				c.competitionType = type;
				c.name = compName;
				c.trainingOrContest = !(chckbxTraktujWszystkielizgi.isSelected());
				c.numberOfAllRuns = (Integer)spinner.getValue();
				c.numberOfTrainingRuns = (Integer)spinner_1.getValue();
				
				c.ranks = new HashMap<LugerCompetitor, Short>();
				c.startList = new HashMap<LugerCompetitor, Short>();
				c.invertedStartList = new HashMap<Short, LugerCompetitor>();
				c.scoredRunsDone  = new Vector<Integer>();
				
				c.runsTimes = new Vector<Run>();
				for (int i = 0; i < c.numberOfTrainingRuns; i++) {
					Run run = new Run(c.startList, (byte)0);
					c.runsTimes.add(run);
				}
				for (int i = 0; i < (c.numberOfAllRuns - c.numberOfTrainingRuns); i++) {
					Run run = new Run(c.startList, (byte)1);
					c.runsTimes.add(run);					
				}
				
				rte_st.competitions.competitions.add(c);
				selectorUpdater.updateSelectorContent(rte_st.competitions.competitions);
				
				window.dispose();
				
			}
		});
		contentPane.add(btnDodaj, "cell 0 8,growx");
		
		JButton btnAnuluj = new JButton("Anuluj");
		btnAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.dispose();
			}
		});
		contentPane.add(btnAnuluj, "cell 2 8 3 1,growx");
	}

}
