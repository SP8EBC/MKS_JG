package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.chrono.Chrono;
import pl.jeleniagora.mks.chrono.SimulateChrono;
import pl.jeleniagora.mks.display.Clear;
import pl.jeleniagora.mks.display.DisplayRuntimeAndStartNum;
import pl.jeleniagora.mks.display.DisplayStartScreen;
import pl.jeleniagora.mks.display.SectroBigRasterDisplay;
import pl.jeleniagora.mks.events.AfterStartListGeneration;
import pl.jeleniagora.mks.events.ChangeCompetition;
import pl.jeleniagora.mks.events.DidNotFinished;
import pl.jeleniagora.mks.events.DidNotStart;
import pl.jeleniagora.mks.events.Disqualification;
import pl.jeleniagora.mks.events.EndOfRun;
import pl.jeleniagora.mks.events.LandedStateReached;
import pl.jeleniagora.mks.events.SaveRuntime;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.exceptions.FailedOpenSerialPortEx;
import pl.jeleniagora.mks.exceptions.MissingCompetitionEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.factories.StartListFactory;
import pl.jeleniagora.mks.files.xml.XmlLoader;
import pl.jeleniagora.mks.files.xml.XmlSaver;
import pl.jeleniagora.mks.online.WebServicePostConsumer;
import pl.jeleniagora.mks.online.WebServicePutConsumer;
import pl.jeleniagora.mks.reports.CompetitionReportGenerator;
import pl.jeleniagora.mks.rte.RTE;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.rte.RTE_COM_DISP;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.serial.CommThread;
import pl.jeleniagora.mks.serial.CommThreadTermHook;
import pl.jeleniagora.mks.serial.RxCommType;
import pl.jeleniagora.mks.settings.DisplayS;
import pl.jeleniagora.mks.settings.GeneralS;
import pl.jeleniagora.mks.settings.SerialCommS;
import pl.jeleniagora.mks.settings.SpringS;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionEncapsulationForSelector;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.Reserve;

import javax.swing.JMenuBar;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.JComboBox;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Color;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBoxMenuItem;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JRadioButton;

public class CompManager extends JFrame {
	
	static AnnotationConfigApplicationContext ctx;
	static CompManager frame;
	
	private JPanel contentPane;
	private JTable table;
	
	private static CommThread com, comDisplay;
	
	private static RTE_ST st;
	
	/**
	 * Tablica referencji do Stringów przechowywająca nazwy kolumn głównej tabeli. 
	 */
	private String[] columnNamesForTable;
	
	/**
	 * Referencja używana do przechowywania aktualnie wybranej przez użytkownika konkurencji 
	 */
	private Competition competition;
	
	/**
	 * Referencja do wektora przechowywującego referencję do wszystkich konkurencji w ramach zawodów / sesji treningowej
	 */
	private Vector<Competition> allCompetitions;
	
	/**
	 * Ustawiane na true jeżeli aktualnie wybrana konkurencja to dwójki. 
	 * Zmienia listę kolumn dodając miejsce na imię i naziwsko drugiego saneczkarza w dwojce
	 */
	private boolean isDoubleRun;
	private JTextField textField_m;
	private JTextField textField_s;
	private JTextField textField_msec;
	JLabel lblredniaPrdko;
	JLabel label;	// czas mety z chronometru
	JButton btnWybierzZaznaczonegoW;
	
	private final Action action = new SwingAction();
	
	private static SimulateChrono simul;
	
	public static void utMain(AnnotationConfigApplicationContext context) {
		ctx = context;
		CompManager.main(null);
	}
	
	/**
	 * Publiczna metoda umożliwiająca automatyczne posortowanie tabeli po numerach startowych w kolejności
	 * rosnącej
	 */
	public void sortByStartNumberAscending() {
		/*
		 * Tutaj jest użyty pewien knif umożliwiający zawsze wywołanie sortowania rosnącego dla pierwszej kolumny.
		 * Dostępna jest tylko metoda toggleSortOrger, która przełącza raz rosnąco, raz malejąco.
		 * 
		 * 
		 */
		table.getRowSorter().toggleSortOrder(1);		
		table.getRowSorter().toggleSortOrder(0);
	}
	
	/**
	 * Metoda ustawia kursor w tabeli wyników na konkretnego zawodnika i konkretny ślizg
	 * @param startNumber
	 * @param runIndex
	 */
	public void markConreteRun(int startNumber, int runIndex) {
		/*
		 * numery startowe saneczkarzy idą zawsze od jedynki w górę. Dlatego pierwszy indeks tabeli modelu to zawsze 
		 * pierwszy saneczkarz itp. Tutaj ma miejsce konwersja saneczkarza o wskazanym numerze startowym na jego 
		 * id w widoku
		 */
		int rowToSelect = ((CompManagerScoreTableModel)frame.getScoreTableModel()).getModelIndexFromStartNumber(startNumber);
		
		if (rowToSelect >= 0)
			rowToSelect = table.convertRowIndexToView(rowToSelect);
		else return;
		
		/*
		 * Pięć pierwszych kolumn zawiera dane o zawodniku takie jak imie nazwisko, etc. dopiero szósta kolumna
		 * to pierwsza kolumna z czasami
		 */
		int columnToSelect = runIndex + DisplayS.columnOffset;
		
		System.out.println("markConcreteRun - rts " + rowToSelect + " - cts " + columnToSelect + " - startnum" + startNumber);
		
		table.changeSelection(rowToSelect, columnToSelect, false, false);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
				
		if (ctx == null) {
			ctx = new AnnotationConfigApplicationContext(SpringS.class);
		}
		
		RTE_GUI rte_gui = ctx.getBean(RTE_GUI.class);
		RTE_ST rte_st = ctx.getBean(RTE_ST.class);
		RTE_COM rte_com = (RTE_COM)ctx.getBean("comBean");
		RTE_COM_DISP rte_com_disp = (RTE_COM_DISP)ctx.getBean("comDispBean");
		
		st = rte_st;
		
		rte_gui.syncCompManagerRdy = new Object();
		
		CompManagerUpdateAndRefreshScoreTable updater = ctx.getBean(CompManagerUpdateAndRefreshScoreTable.class);
		rte_gui.updater = updater;
		
		try {
			com = new CommThread("/dev/ttyUSB232i", rte_com, true);		// konwerter z izolacją meratronik
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (FailedOpenSerialPortEx e1) {
			System.out.println("Failed opening port /dev/ttyUSB232i");
		//	e1.printStackTrace();
		}
		
		try {
			comDisplay = new CommThread("/dev/ttyUSB485", rte_com_disp, false); // konwerter do wyświetlacza od razu na 485
		} catch (IOException | FailedOpenSerialPortEx e1) {
			System.out.println("Failed opening port /dev/ttyUSB485");

		}		
		
		System.out.println("done");
		
		new Thread(new Chrono(ctx)).start();

		if (com != null) {
			com.startThreads();
		}
		if (comDisplay != null) {
			comDisplay.startThreads();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new CommThreadTermHook(ctx)));
		
		SerialCommS.setMaxRxTimeoutMsec(2000);
		rte_com.rxCommType = RxCommType.NUM_OF_BYTES;
		rte_com.numberOfBytesToRx = 14;
		rte_com.activateRx = true;
				
		simul = ctx.getBean(SimulateChrono.class);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				CompManagerCSelectorUpdater selectorUpdater = (CompManagerCSelectorUpdater)ctx.getBean(CompManagerCSelectorUpdater.class);
				AfterStartListGeneration.setAppCtx(ctx);
				ChangeCompetition.setAppCtx(ctx);
				UpdateCurrentAndNextLuger.setAppCtx(ctx);
				SaveRuntime.setAppCtx(ctx);
				DidNotFinished.setAppCtx(ctx);
				DidNotStart.setAppCtx(ctx);
				Disqualification.setAppCtx(ctx);
				EndOfRun.setAppCtx(ctx);
				LandedStateReached.setAppCtx(ctx);
				
				try {
					DisplayS.setShowAllTimeDigits(true);
					
					/* 
					 * Obiekt klasy Competitions musi być stworzony przed wczytaniem XMLa bo adaptery typów zapisują
					 * tam dane pośrednie które są potrzebne do odczytywania 
					 * */
					Competitions competitions = new Competitions();
					rte_st.competitions = competitions;
					
					frame = new CompManager();
					frame.setVisible(true);
					
					CompManagerScoreTableModel mdl = (CompManagerScoreTableModel)frame.getScoreTableModel();
					
					rte_gui.model = mdl;
					rte_gui.compManager = frame;
					rte_gui.compManagerScoreModel = mdl;
					
					XmlLoader loader = (XmlLoader)ctx.getBean(XmlLoader.class);
					loader.setFilename("out_test.xml");
					competitions = loader.loadFromXml(false);
					
					selectorUpdater.updateSelectorContent(competitions.competitions);
					
					///////
					XmlSaver saver = (XmlSaver)ctx.getBean(XmlSaver.class);	
					saver.setFilename("out_test2.xml");
					saver.saveToXml(competitions);
					
					//////
					
					///////
					/// display test
					//SectroBigRasterDisplay disp = (SectroBigRasterDisplay)ctx.getBean(SectroBigRasterDisplay.class);
					SectroBigRasterDisplay disp = RTE.getRte_disp_interface();
					disp.clearDisplay();
					disp.setScrolling(false);
					disp.setBrightness(16);
					
					disp.sendText("MKS_JG\r\nwer 1.00\r\nAutor: Mateusz Lubecki = Bielsko-Biała 2018   ", 0, 0);
					///////
					
					synchronized(rte_gui.syncCompManagerRdy) {
						rte_gui.syncCompManagerRdy.notifyAll();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				catch (Reserve r) {
//					r.printStackTrace();
//				}
				catch (UninitializedCompEx e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Reserve e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public CompManager() {
				
//		RTE_GUI rte_gui = ctx.getBean(RTE_GUI.class);
		RTE_GUI rte_gui = RTE.getGUI();
		
		// inicjalizacja tablicy pod nazwy kolumn
		this.columnNamesForTable = new String[9];
		
		// początek automatycznie generowanego kodu
		setTitle("MKS_JG - Obsługa treningów i zawodów");
		setAlwaysOnTop(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1259, 658);
		setFocusable(true);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.isShiftDown()) {
					rte_gui.shiftPressed = true;
					btnWybierzZaznaczonegoW.setText("<html><p align=\"center\">Ustaw zaznaczonego " + "<br>" + " w tabeli saneczkarza jako " + "<br>" + " aktualnie na torze</p></html>");
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (!e.isShiftDown()) {

				rte_gui.shiftPressed = false;
				btnWybierzZaznaczonegoW.setText("<html><p align=\"center\">Ustaw zaznaczonego " + "<br>" + " w tabeli saneczkarza jako " + "<br>" + " następnego</p></html>");
				}
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Plik");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNowyPlikZawodw = new JMenuItem("Nowy Plik Zawodów");
		mntmNowyPlikZawodw.addActionListener((CompManagerNewFileListener)ctx.getBean(CompManagerNewFileListener.class));
		mnNewMenu.add(mntmNowyPlikZawodw);
		
		JMenuItem mntmOtwrzIstniejcyPlik = new JMenuItem("Otwórz Istniejący Plik Zawodów");
		mntmOtwrzIstniejcyPlik.addActionListener((CompManagerOpenFileListener)ctx.getBean(CompManagerOpenFileListener.class));
		mnNewMenu.add(mntmOtwrzIstniejcyPlik);
		
		JMenuItem mntmZapiszPlikZawodw = new JMenuItem("Zapisz Plik Zawodów Jako");
		mntmZapiszPlikZawodw.addActionListener((CompManagerSaveFileAsListener)ctx.getBean(CompManagerSaveFileAsListener.class));
		mnNewMenu.add(mntmZapiszPlikZawodw);
		
		mnNewMenu.add(new JSeparator());
		
		JMenuItem mntmWczytajZawodyZ = new JMenuItem("Wczytaj Zawody z Bazy Danych");
		mnNewMenu.add(mntmWczytajZawodyZ);
		
		mnNewMenu.add(new JSeparator());
		
		JMenuItem mntmOtwrzPlikZ = new JMenuItem("Otwórz Plik z Zawodnikami");
		mnNewMenu.add(mntmOtwrzPlikZ);
		
		mnNewMenu.add(new JSeparator());
		
		JMenuItem mntmZamknij = new JMenuItem("Zamknij");
		mnNewMenu.add(mntmZamknij);
		
		JMenu mnZawody = new JMenu("Zawody");
		menuBar.add(mnZawody);
		
		JMenuItem mntmNazwaIData = new JMenuItem("Nazwa i data");
		mntmNazwaIData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// akcja od naciśnięcia przycisku myszy na pozycję w menu
				CompManagerWindowNameAndDate window = new CompManagerWindowNameAndDate(ctx);
				window.frmUstawNazwI.setVisible(true);
			}
		});
		mnZawody.add(mntmNazwaIData);
		
		JMenuItem mntmWybierzTor = new JMenuItem("Wybierz tor");
		mntmWybierzTor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				CompManagerWindowChooseTrack window = (CompManagerWindowChooseTrack)ctx.getBean(CompManagerWindowChooseTrack.class);
				window.setVisible(true);
			}
			
		});
		mnZawody.add(mntmWybierzTor);
				
		JCheckBoxMenuItem chckbxmntmOsobnaKonkurencjaDruynowa = new JCheckBoxMenuItem("Osobna konkurencja drużynowa");
		mnZawody.add(chckbxmntmOsobnaKonkurencjaDruynowa);
		
		JMenuItem mntmSdziowie = new JMenuItem("Sędziowie");
		mntmSdziowie.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CompManagerWindowJudges judges = (CompManagerWindowJudges)ctx.getBean(CompManagerWindowJudges.class);
				judges.frame.setVisible(true);
			}
			
		});
		mnZawody.add(mntmSdziowie);
		
		JSeparator separator_3 = new JSeparator();
		mnZawody.add(separator_3);
		
		ButtonGroup compTrainingGroup = new ButtonGroup();
		
		JSeparator separator_4 = new JSeparator();
		mnZawody.add(separator_4);
		
		JMenuItem mntmPrzeliczPunktacje = new JMenuItem("Przelicz punktacje");
		mnZawody.add(mntmPrzeliczPunktacje);
		
		JMenu mnSposbObliczaniaLokaty = new JMenu("Sposób obliczania lokaty w aktualnym ślizgu");
		mnZawody.add(mnSposbObliczaniaLokaty);
		
		//
		ButtonGroup partialRanksButtonGroup = new ButtonGroup();
		
		JRadioButton rdbtnNaPodstawiePoprzednich = new JRadioButton("Na podstawie poprzednich i aktualnego ślizgu");
		mnSposbObliczaniaLokaty.add(rdbtnNaPodstawiePoprzednich);
		partialRanksButtonGroup.add(rdbtnNaPodstawiePoprzednich);
		rdbtnNaPodstawiePoprzednich.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object actionSrc = arg0.getSource();
				JRadioButton actionRb = (JRadioButton)actionSrc;
				
				if (actionRb.isSelected()) {
					GeneralS.setPartialRanksRunOnly(false);
					System.out.println("ParialRanksRunOnly disabled");
				}
				else {
					GeneralS.setPartialRanksRunOnly(true);		
					System.out.println("ParialRanksRunOnly enable");
				}
			}
			
		});
		
		JRadioButton rdbtnTylkoNaPodstawie = new JRadioButton("Tylko na podstawie aktualnego ślizgu");
		mnSposbObliczaniaLokaty.add(rdbtnTylkoNaPodstawie);
		partialRanksButtonGroup.add(rdbtnTylkoNaPodstawie);
		rdbtnTylkoNaPodstawie.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object actionSrc = arg0.getSource();
				JRadioButton actionRb = (JRadioButton)actionSrc;
				
				if (actionRb.isSelected()) {
					GeneralS.setPartialRanksRunOnly(true);
					System.out.println("ParialRanksRunOnly enable");
				}
				else {
					GeneralS.setPartialRanksRunOnly(false);		
					System.out.println("ParialRanksRunOnly disable");
				}
			}
			
		});
		
		JMenuItem mntmWyjanienie = new JMenuItem("Wyjaśnienie");
		mnSposbObliczaniaLokaty.add(mntmWyjanienie);
				
		JMenu mnKonkurencje = new JMenu("Konkurencje");
		menuBar.add(mnKonkurencje);
		
		JMenuItem mntmDodajKonkurencj = new JMenuItem("Dodaj konkurencję");
		mntmDodajKonkurencj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CompManagerWindowAddCompetition addCmp = (CompManagerWindowAddCompetition)ctx.getBean(CompManagerWindowAddCompetition.class);
				addCmp.setVisible(true);
			}
		});
		mnKonkurencje.add(mntmDodajKonkurencj);
		
		JMenuItem mntmUsuKonkurencj = new JMenuItem("Usuń konkurencję");
		mntmUsuKonkurencj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CompManagerWindowDeleteCompetition delCmp = (CompManagerWindowDeleteCompetition)ctx.getBean(CompManagerWindowDeleteCompetition.class);
				delCmp.initializeComboBox();
				delCmp.setVisible(true);
			}
			
		});
		mnKonkurencje.add(mntmUsuKonkurencj);
		
		JMenuItem mntmEdytujKonkurencj = new JMenuItem("Edytuj konkurencję");
		mntmEdytujKonkurencj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CompManagerWindowEditCompetition editCmp = (CompManagerWindowEditCompetition)ctx.getBean(CompManagerWindowEditCompetition.class);
				editCmp.updateContent(st.competitions);
				editCmp.setVisible(true);
				
			}
		});
		mnKonkurencje.add(mntmEdytujKonkurencj);
		
		JSeparator separator = new JSeparator();
		mnKonkurencje.add(separator);
		
		JMenuItem mntmPrzypiszBramkiStartowe = new JMenuItem("Przypisz bramki startowe");
		mnKonkurencje.add(mntmPrzypiszBramkiStartowe);
		
		JMenu mnSaneczkarze = new JMenu("Saneczkarze");
		menuBar.add(mnSaneczkarze);
		
		JMenuItem mntmDodajDoKonkurencji = new JMenuItem("Dodaj nowego do bazy programu");
		mntmDodajDoKonkurencji.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CompManagerWindowAddLuger window = (CompManagerWindowAddLuger)ctx.getBean(CompManagerWindowAddLuger.class);
				window.updateCurrentNumberOfLugers();
				window.setVisible(true);
			}
		});
		mnSaneczkarze.add(mntmDodajDoKonkurencji);
		
		JSeparator separator_2 = new JSeparator();
		mnSaneczkarze.add(separator_2);
		
		JMenuItem mntmDopiszIstniejcegoDo = new JMenuItem("Dopisz z bazy do konkurencji jedynek");
		mntmDopiszIstniejcegoDo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				///
				CompManagerWindowAddLugerSingle window = (CompManagerWindowAddLugerSingle)ctx.getBean(CompManagerWindowAddLugerSingle.class);
				try {
					window.updateContent();
				} catch (MissingCompetitionEx e) {
					JOptionPane.showMessageDialog(frame, "Nie zdefiniowano żadanej konkurencji!");
					window.dispose();
					return;
				}
				window.setVisible(true);
			}
		});
		mnSaneczkarze.add(mntmDopiszIstniejcegoDo);
		
		JMenuItem mntmSparujWDwjki = new JMenuItem("Sparuj w dwójki i dopisz do konkurencji");
		mntmSparujWDwjki.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				///
				CompManagerWindowAddLugerDouble window = (CompManagerWindowAddLugerDouble)ctx.getBean(CompManagerWindowAddLugerDouble.class);
				window.updateContent();
				window.setVisible(true);
			}
		});
		mnSaneczkarze.add(mntmSparujWDwjki);
		
		JMenuItem mntmUtwrzDruyn = new JMenuItem("Utwórz drużynę i dopisz do konkurencji");
		mnSaneczkarze.add(mntmUtwrzDruyn);
		
		JMenuItem mntmUtwrzSztafet = new JMenuItem("Utwórz sztafetę i dopisz do konkurencji");
		mnSaneczkarze.add(mntmUtwrzSztafet);
		
		JSeparator separator_1 = new JSeparator();
		mnSaneczkarze.add(separator_1);
		
		JMenuItem mntmLosujNumeryStartowe = new JMenuItem("Losuj numery startowe");
		mntmLosujNumeryStartowe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int answer = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wylosować numery startowe dla konkurencji: " + rte_gui.competitionBeingShown.toString(), "Pozor", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.NO_OPTION) {
					System.out.println("no");;
					return;
				}
				else if (answer == JOptionPane.YES_OPTION) {
					System.out.println("yes");;
					StartListFactory.generateStartList(rte_gui.competitionBeingShown);
					
					try {
						rte_gui.model.updateTableData(rte_gui.competitionBeingShown, false);
					} catch (UninitializedCompEx e1) {
						e1.printStackTrace();
					}
					
					rte_gui.model.fireTableDataChanged();
					
					if (rte_gui.competitionBeingShown.equals(st.currentCompetition)) {
						AfterStartListGeneration.process(st.currentCompetition);
					}
				}
				
			}
		});
		mnSaneczkarze.add(mntmLosujNumeryStartowe);
		
		JMenuItem mntmEdytujRcznieNumer = new JMenuItem("Edytuj ręcznie numer wskazanego zawodnika");
		mntmEdytujRcznieNumer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CompManagerWindowEditStartNum editor = new CompManagerWindowEditStartNum(rte_gui.competitorClickedInTable,
																						rte_gui.competitionBeingShown,
																						rte_gui.model);
				editor.setVisible(true);
			}
		});
		mnSaneczkarze.add(mntmEdytujRcznieNumer);
		
		JMenu mnRaporty = new JMenu("Raporty");
		menuBar.add(mnRaporty);
		
		JMenu mnWybierzSzablon = new JMenu("Wybierz szablon");
		mnRaporty.add(mnWybierzSzablon);
		
		ButtonGroup templateGroup = new ButtonGroup();
		
		JRadioButtonMenuItem rdbtnmntmOglnopolskaOlimpiadaModziey = new JRadioButtonMenuItem("Ogólnopolska Olimpiada Młodzieży");
		mnWybierzSzablon.add(rdbtnmntmOglnopolskaOlimpiadaModziey);
		templateGroup.add(rdbtnmntmOglnopolskaOlimpiadaModziey);
		
		JRadioButtonMenuItem rdbtnmntmSiguldaswissTiming = new JRadioButtonMenuItem("Sigulda (Swiss Timing)");
		rdbtnmntmSiguldaswissTiming.setSelected(true);
		mnWybierzSzablon.add(rdbtnmntmSiguldaswissTiming);
		templateGroup.add(rdbtnmntmSiguldaswissTiming);
		
		JMenu mnJzykRaportu = new JMenu("Język raportu");
		mnRaporty.add(mnJzykRaportu);
		
		ButtonGroup reportLang = new ButtonGroup();
		
		JRadioButtonMenuItem rdbtnmntmPolski = new JRadioButtonMenuItem("Polski");
		rdbtnmntmPolski.setSelected(true);
		mnJzykRaportu.add(rdbtnmntmPolski);
		reportLang.add(rdbtnmntmPolski);
		
		JRadioButtonMenuItem rdbtnmntmAngielski = new JRadioButtonMenuItem("Angielski");
		mnJzykRaportu.add(rdbtnmntmAngielski);
		reportLang.add(rdbtnmntmAngielski);
		
		JMenu mnDodatkoweUstawienia = new JMenu("Dodatkowe ustawienia");
		mnRaporty.add(mnDodatkoweUstawienia);
		
		JCheckBoxMenuItem chckbxmntmWstawLogoFil = new JCheckBoxMenuItem("Wstaw logo FIL po lewej stronie");
		mnDodatkoweUstawienia.add(chckbxmntmWstawLogoFil);
		
		JCheckBoxMenuItem chckbxmntmWstawLogoMks = new JCheckBoxMenuItem("Wstaw logo MKS po lewej stronie");
		chckbxmntmWstawLogoMks.setSelected(true);
		mnDodatkoweUstawienia.add(chckbxmntmWstawLogoMks);
		
		JMenu mnLogoOom = new JMenu("Logo OOM");
		mnDodatkoweUstawienia.add(mnLogoOom);
		
		ButtonGroup oomLogoGrp = new ButtonGroup();
		
		JRadioButtonMenuItem rdbtnmntmBrak = new JRadioButtonMenuItem("Brak");
		rdbtnmntmBrak.setSelected(true);
		mnLogoOom.add(rdbtnmntmBrak);
		oomLogoGrp.add(rdbtnmntmBrak);
		
		JRadioButtonMenuItem rdbtnmntmPoLewej = new JRadioButtonMenuItem("Po lewej");
		mnLogoOom.add(rdbtnmntmPoLewej);
		oomLogoGrp.add(rdbtnmntmPoLewej);
		
		JRadioButtonMenuItem rdbtnmntmPoPrawej = new JRadioButtonMenuItem("Po prawej");
		mnLogoOom.add(rdbtnmntmPoPrawej);
		oomLogoGrp.add(rdbtnmntmPoPrawej);
		
		JMenu mnHerbWojewdztwapo = new JMenu("Herb województwalub miasta (po prawej)");
		mnDodatkoweUstawienia.add(mnHerbWojewdztwapo);
		
		ButtonGroup countyLogoGrp = new ButtonGroup();
		
		JRadioButtonMenuItem rdbtnmntmBrak_1 = new JRadioButtonMenuItem("Brak");
		mnHerbWojewdztwapo.add(rdbtnmntmBrak_1);
		countyLogoGrp.add(rdbtnmntmBrak_1);
		
		JRadioButtonMenuItem rdbtnmntmKarpacz = new JRadioButtonMenuItem("Karpacz");
		mnHerbWojewdztwapo.add(rdbtnmntmKarpacz);
		countyLogoGrp.add(rdbtnmntmKarpacz);
		
		JRadioButtonMenuItem rdbtnmntmJeleniaGra = new JRadioButtonMenuItem("Jelenia Góra");
		mnHerbWojewdztwapo.add(rdbtnmntmJeleniaGra);
		countyLogoGrp.add(rdbtnmntmJeleniaGra);
		
		JRadioButtonMenuItem rdbtnmntmKrynicaZdrj = new JRadioButtonMenuItem("Krynica Zdrój");
		mnHerbWojewdztwapo.add(rdbtnmntmKrynicaZdrj);
		countyLogoGrp.add(rdbtnmntmKrynicaZdrj);
		
		JRadioButtonMenuItem rdbtnmntmBielskoBiaa = new JRadioButtonMenuItem("Bielsko - Biała");
		mnHerbWojewdztwapo.add(rdbtnmntmBielskoBiaa);
		countyLogoGrp.add(rdbtnmntmBielskoBiaa);
		
		JRadioButtonMenuItem rdbtnmntmGrnylsk = new JRadioButtonMenuItem("Górny Śląsk");
		mnHerbWojewdztwapo.add(rdbtnmntmGrnylsk);
		countyLogoGrp.add(rdbtnmntmGrnylsk);
		
		JRadioButtonMenuItem rdbtnmntmDolnylsk = new JRadioButtonMenuItem("Dolny Śląsk");
		rdbtnmntmDolnylsk.setSelected(true);
		mnHerbWojewdztwapo.add(rdbtnmntmDolnylsk);
		countyLogoGrp.add(rdbtnmntmDolnylsk);
		
		JRadioButtonMenuItem rdbtnmntmMaopolska = new JRadioButtonMenuItem("Małopolska");
		mnHerbWojewdztwapo.add(rdbtnmntmMaopolska);
		countyLogoGrp.add(rdbtnmntmMaopolska);
		
		JMenu mnGenerujRaport = new JMenu("Generuj raport");
		mnRaporty.add(mnGenerujRaport);
		
		JMenuItem mntmZAktualnieRozgrywanej = new JMenuItem("Z aktualnie rozgrywanej konkurencji");
		mntmZAktualnieRozgrywanej.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CompetitionReportGenerator gen = new CompetitionReportGenerator(st.currentCompetition, st.competitions, "./");
				try {
					gen.generate();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mnGenerujRaport.add(mntmZAktualnieRozgrywanej);
		
		JMenuItem mntmZAktualnieWywietlanej = new JMenuItem("Z aktualnie wyświetlanej konkurencji");
		mntmZAktualnieWywietlanej.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CompetitionReportGenerator gen = new CompetitionReportGenerator(rte_gui.competitionBeingShown, st.competitions, "./");
				try {
					gen.generate();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnGenerujRaport.add(mntmZAktualnieWywietlanej);
		
		JMenu mnChronometr = new JMenu("Chronometr");
		menuBar.add(mnChronometr);
		
		JMenu mnWywietlacz = new JMenu("Wyświetlacz");
		menuBar.add(mnWywietlacz);
		
		JMenuItem mntmWyczyZawarto = new JMenuItem("Wyczyść Zawartość");
		mntmWyczyZawarto.addActionListener(new Clear(RTE.getRte_disp_interface()));
		mnWywietlacz.add(mntmWyczyZawarto);
		
		JMenuItem mntmTest = new JMenuItem("Test 1");
		mntmTest.addActionListener(new DisplayRuntimeAndStartNum(RTE.getRte_disp_interface()));
		
		JMenu mnWywietl = new JMenu("Wyświetl");
		mnWywietlacz.add(mnWywietl);
		
		JMenuItem mntmAktualnieNaTorze = new JMenuItem("Aktualnie na torze");
		mntmAktualnieNaTorze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		mnWywietl.add(mntmAktualnieNaTorze);
		
		JMenuItem mntmEkranStartowy = new JMenuItem("Ekran Startowy");
		mnWywietl.add(mntmEkranStartowy);
		mntmEkranStartowy.addActionListener(new DisplayStartScreen(RTE.getRte_disp_interface()));
		
		JMenu mnUstawienia = new JMenu("Ustawienia");
		mnWywietlacz.add(mnUstawienia);
		
		JMenu mnJasno = new JMenu("Jasność");
		mnUstawienia.add(mnJasno);
		
		JRadioButtonMenuItem rdbtnmntmMinimalna = new JRadioButtonMenuItem("Minimalna");
		mnJasno.add(rdbtnmntmMinimalna);
		
		JRadioButtonMenuItem rdbtnmntmrednia = new JRadioButtonMenuItem("Średnia");
		mnJasno.add(rdbtnmntmrednia);
		
		JRadioButtonMenuItem rdbtnmntmJasna = new JRadioButtonMenuItem("Duża");
		mnJasno.add(rdbtnmntmJasna);
		
		JRadioButtonMenuItem rdbtnmntmMaksymalna = new JRadioButtonMenuItem("Maksymalna");
		mnJasno.add(rdbtnmntmMaksymalna);
		mnWywietlacz.add(mntmTest);
		
		JMenuItem mntmTest_1 = new JMenuItem("Symulacja Chrono 1");
		mntmTest_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//SimulateChrono simul = (SimulateChrono)ctx.getBean("SimulateChrono");
				simul.simulate(1);
			}
		});
		mnWywietlacz.add(mntmTest_1);
		
		JMenuItem mntmSymulacjaChrono = new JMenuItem("Symulacja Chrono 2");
		mnWywietlacz.add(mntmSymulacjaChrono);
		
		JMenu mnLugepl = new JMenu("Luge.pl");
		menuBar.add(mnLugepl);
		
		JCheckBox chckbxWczWynikiOnline = new JCheckBox("Włącz wyniki online");
		chckbxWczWynikiOnline.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					st.enableOnlineScoring = true;
				}
				else if (arg0.getStateChange() == ItemEvent.DESELECTED) {
					st.enableOnlineScoring = false;

				}
				else;
			}
		});
		mnLugepl.add(chckbxWczWynikiOnline);
		
		JSeparator separator_6 = new JSeparator();
		mnLugepl.add(separator_6);
		
		JMenuItem mntmSprawdKomunikacjzSerwerem = new JMenuItem("Sprawdź komunikację z serwerem");
		mnLugepl.add(mntmSprawdKomunikacjzSerwerem);
		
		JMenuItem mntmDodajBierzceZawody = new JMenuItem("Dodaj/aktualizuj informację o zawodach do systemu");
		mntmDodajBierzceZawody.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WebServicePutConsumer consumer = new WebServicePutConsumer(null);
				consumer.competitionsUpdater(st.competitions);
			}
		});
		mnLugepl.add(mntmDodajBierzceZawody);
		
		JMenuItem mntmDodajaktualizujWywietlanKonkurencje = new JMenuItem("Aktualizuj czasy wyświetlanej konkurencji");
		mntmDodajaktualizujWywietlanKonkurencje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rte_gui.competitionBeingShown == null) {
					JOptionPane.showMessageDialog(frame, "Nie zdefinowano żadnej konkurencji!");
					return;
				}
				
				if (rte_gui.competitionBeingShown.invertedStartList.size() <= 1) {
					JOptionPane.showMessageDialog(frame, "Nie możesz dodać konkurencji bez wygenerowanych numerów startowych");
					return;					
				}
				WebServicePutConsumer consumer = new WebServicePutConsumer(null);
				consumer.competitionDataUpdater(rte_gui.competitionBeingShown);
			}
		});
		
		JMenuItem mntmUsuBieceZawody = new JMenuItem("Dodaj wszystkie konkurencję z zawodów");
		mntmUsuBieceZawody.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WebServicePutConsumer consumer = new WebServicePutConsumer(null);
				consumer.mappingsUpdater(st.competitions);
			}
		});
		mnLugepl.add(mntmUsuBieceZawody);
		mnLugepl.add(mntmDodajaktualizujWywietlanKonkurencje);
		
		JMenuItem mntmDodajaktualizujRozgrywanKonkurencje = new JMenuItem("Aktualizuj czasy rozgrywanej konkurencji");
		mntmDodajaktualizujRozgrywanKonkurencje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (st.currentCompetition == null) {
					JOptionPane.showMessageDialog(frame, "Nie zdefinowano żadnej konkurencji!");
					return;
				}
				
				if (st.currentCompetition.invertedStartList.size() <= 1) {
					JOptionPane.showMessageDialog(frame, "Nie możesz dodać konkurencji bez wygenerowanych numerów startowych");
					return;					
				}
				
				WebServicePutConsumer consumer = new WebServicePutConsumer(null);
				consumer.competitionDataUpdater(st.currentCompetition);
			}
		});
		mnLugepl.add(mntmDodajaktualizujRozgrywanKonkurencje);
		
		JMenu mnPomoc = new JMenu("Pomoc");
		menuBar.add(mnPomoc);
		
		JMenuItem mntmInstrukcjaSzybkiegoStartu = new JMenuItem("Instrukcja szybkiego startu");
		mnPomoc.add(mntmInstrukcjaSzybkiegoStartu);
		
		JMenuItem mntmInformacjeKontaktowe = new JMenuItem("Informacje kontaktowe");
		mnPomoc.add(mntmInformacjeKontaktowe);
		
		JMenuItem mntmOProgramie = new JMenuItem("O programie");
		mnPomoc.add(mntmOProgramie);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 34, 1019, 510);
		
		/* Tworzenie JTable w oparciu o przygotowany model		 */
		table = new JTable(new CompManagerScoreTableModel());
		table.setFocusable(true);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.isShiftDown()) {
					rte_gui.shiftPressed = true;
					btnWybierzZaznaczonegoW.setText("<html><p align=\"center\">Ustaw zaznaczonego " + "<br>" + " w tabeli saneczkarza jako " + "<br>" + " aktualnie na torze</p></html>");
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (!e.isShiftDown()) {

				rte_gui.shiftPressed = false;
				btnWybierzZaznaczonegoW.setText("<html><p align=\"center\">Ustaw zaznaczonego " + "<br>" + " w tabeli saneczkarza jako " + "<br>" + " następnego</p></html>");
				}
			}
		});
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);
		table.getSelectionModel().addListSelectionListener(new CompManagerScoreTableSelectListener(table, ctx));
		table.getColumnModel().getSelectionModel().addListSelectionListener(new CompManagerScoreTableSelectListener(table, ctx));
//		table.getSelectionModel().addSelectionInterval(1, 1);
		
		/* 
		 * Ustawianie domyślnego renderera dla typu Integer na klasę która przerobi tego Intigera
		 * zawierającego czas ślizgu wyrażany jako wielokrotność setek mikrosekund na czas
		 * */
		table.setDefaultRenderer(Integer.class, new CompManagerScoreTableTimeRenderer(ctx));
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);
		
		JLabel lblKonkurencja = new JLabel("Podgląd:");
		lblKonkurencja.setHorizontalAlignment(SwingConstants.RIGHT);
		lblKonkurencja.setBounds(5, 11, 105, 15);
		contentPane.add(lblKonkurencja);
		
		textField_m = new JTextField();
		textField_m.setFont(new Font("Dialog", Font.PLAIN, 30));
		textField_m.setBounds(1036, 53, 55, 44);
		contentPane.add(textField_m);
		textField_m.setColumns(10);
		
		textField_s = new JTextField();
		textField_s.setFont(new Font("Dialog", Font.PLAIN, 30));
		textField_s.setColumns(10);
		textField_s.setBounds(1103, 53, 55, 44);
		contentPane.add(textField_s);
		
		textField_msec = new JTextField();
		textField_msec.setFont(new Font("Dialog", Font.PLAIN, 30));
		textField_msec.setColumns(10);
		textField_msec.setBounds(1170, 53, 71, 44);
		contentPane.add(textField_msec);
		
		System.out.println("CompManager Constructor");
		
		JLabel lblNewLabel = new JLabel("Czas dla wybranego ślizgu");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(1036, 35, 205, 15);
		contentPane.add(lblNewLabel);
		
		JButton btnSaveTime = new JButton("Zapisz Czas Zawodnika");
		btnSaveTime.addActionListener(new CompManagerStoreRuntimeBtnActionListener(ctx));
		btnSaveTime.setBounds(1036, 130, 205, 44);
		contentPane.add(btnSaveTime);
		
		JLabel lblMin = new JLabel("Min");
		lblMin.setHorizontalAlignment(SwingConstants.CENTER);
		lblMin.setBounds(1036, 103, 55, 15);
		contentPane.add(lblMin);
		
		JLabel lblSec = new JLabel("Sec");
		lblSec.setHorizontalAlignment(SwingConstants.CENTER);
		lblSec.setBounds(1103, 103, 55, 15);
		contentPane.add(lblSec);
		
		JLabel lblMiliseconds = new JLabel("Milisec");
		lblMiliseconds.setHorizontalAlignment(SwingConstants.CENTER);
		lblMiliseconds.setBounds(1170, 103, 71, 15);
		contentPane.add(lblMiliseconds);
		
		JButton btnDNF = new JButton("Nie ukończył (DNF)");
		btnDNF.addActionListener(new CompManagerDnfBtnActionListener(ctx));
		btnDNF.setBounds(1036, 186, 205, 44);
		contentPane.add(btnDNF);
		
		JButton btnDSQ = new JButton("Dyskwalifikacja (DSQ)");
		btnDSQ.addActionListener(new CompManagerDsqBtnActionListener(ctx));
		btnDSQ.setBounds(1036, 242, 205, 44);
		contentPane.add(btnDSQ);
		
		JButton btnDNS = new JButton("Nie wystartował (DNS)");
		btnDNS.addActionListener(new CompManagerDnsBtnActionListener(ctx));
		btnDNS.setBounds(1036, 298, 205, 44);
		contentPane.add(btnDNS);
		
		JButton btnPrzedslizgacz1 = new JButton("Przedślizgacz 1");
		btnPrzedslizgacz1.setBounds(1036, 388, 205, 44);
		contentPane.add(btnPrzedslizgacz1);
		
		JButton btnPrzedlizgacz2 = new JButton("Przedślizgacz 2");
		btnPrzedlizgacz2.setBounds(1036, 444, 205, 44);
		contentPane.add(btnPrzedlizgacz2);
		
		JLabel lbllizg = new JLabel("Aktualnie rozgrywana:");
		lbllizg.setBounds(626, 11, 172, 15);
		contentPane.add(lbllizg);
		
		JButton btnNextSlider = new JButton("<html><p align=\"center\">Omiń aktualnego i przejdź do nast. z listy</p></html>");
		btnNextSlider.setBounds(1036, 500, 205, 44);
		btnNextSlider.addActionListener(new CompManagerNextLugerBtnActionListener(ctx));
		contentPane.add(btnNextSlider);
		
		JLabel lblAktualnieNaTorze = new JLabel("AKTUALNIE NA TORZE:");
		lblAktualnieNaTorze.setForeground(Color.RED);
		lblAktualnieNaTorze.setFont(new Font("Dialog", Font.BOLD, 18));
		lblAktualnieNaTorze.setBounds(15, 550, 260, 15);
		contentPane.add(lblAktualnieNaTorze);
		
		JLabel lblNastpnySaneczkarz = new JLabel("NASTĘPNIE:");
		lblNastpnySaneczkarz.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNastpnySaneczkarz.setForeground(Color.RED);
		lblNastpnySaneczkarz.setFont(new Font("Dialog", Font.BOLD, 18));
		lblNastpnySaneczkarz.setBounds(25, 577, 219, 15);
		contentPane.add(lblNastpnySaneczkarz);
		
		JLabel lblActuallyOnTrack = new JLabel("TESTOWY TEST");
		lblActuallyOnTrack.setFont(new Font("Dialog", Font.BOLD, 18));
		lblActuallyOnTrack.setBounds(256, 551, 253, 15);
		contentPane.add(lblActuallyOnTrack);
		
		JLabel lblNextOnTrack = new JLabel("KOWALSKI AAAAAAAA");
		lblNextOnTrack.setFont(new Font("Dialog", Font.BOLD, 18));
		lblNextOnTrack.setBounds(256, 577, 253, 15);
		contentPane.add(lblNextOnTrack);
		
		btnWybierzZaznaczonegoW = new JButton("Wybierz zaznaczonego w tabeli saneczkarza jako następnego");
		btnWybierzZaznaczonegoW.setBounds(523, 546, 225, 59);
		btnWybierzZaznaczonegoW.addActionListener(new CompManagerSetMrkAsNextBtnActionListener(ctx));
		btnWybierzZaznaczonegoW.setText("<html><p align=\"center\">Ustaw zaznaczonego " + "<br>" + " w tabeli saneczkarza jako " + "<br>" + " następnego</p></html>");
		contentPane.add(btnWybierzZaznaczonegoW);
		rte_gui.btnWybierzZaznaczonegoW = btnWybierzZaznaczonegoW;
		
		JButton btnPowrDoKolejnoci = new JButton("Powróć do kolejności saneczkarzy wg listy startowej");
		btnPowrDoKolejnoci.setMargin(new Insets(2, 7, 2, 7));
		btnPowrDoKolejnoci.setBounds(760, 546, 264, 59);
		btnPowrDoKolejnoci.addActionListener(new CompManagerRetNormalBtnActionListener());
		btnPowrDoKolejnoci.setText("<html><p align=\"center\">Powróć do kolejności wg numerów<br>startowych oraz podświetl w<br> tabeli aktualnego\n saneczkarza</p></html>");
		contentPane.add(btnPowrDoKolejnoci);
		
		JLabel lblCzaslizguOtrzymany = new JLabel("Czas mety z chronometru");
		lblCzaslizguOtrzymany.setHorizontalAlignment(SwingConstants.CENTER);
		lblCzaslizguOtrzymany.setBounds(1036, 551, 205, 15);
		contentPane.add(lblCzaslizguOtrzymany);
		
		label = new JLabel("0:00.000");
		label.setFont(new Font("Dialog", Font.BOLD, 22));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(1036, 573, 205, 27);
		contentPane.add(label);
		rte_gui.lblTimeFromChrono = label; 
		
		lblredniaPrdko = new JLabel("");
		lblredniaPrdko.setForeground(Color.RED);
		lblredniaPrdko.setHorizontalAlignment(SwingConstants.CENTER);
		lblredniaPrdko.setBounds(1036, 11, 205, 15);
		contentPane.add(lblredniaPrdko);
		rte_gui.lblredniaPrdko = lblredniaPrdko;
		
		JCheckBox chckbxAutozapisCzasulizgu = new JCheckBox("Autozapis czasu ślizgu");
		chckbxAutozapisCzasulizgu.setSelected(false);
		chckbxAutozapisCzasulizgu.addItemListener(new CompManagerAsaveChbxItemListener());
		chckbxAutozapisCzasulizgu.setBounds(1036, 354, 205, 23);
		contentPane.add(chckbxAutozapisCzasulizgu);
		
		JComboBox<CompetitionEncapsulationForSelector> competitionSelector = new JComboBox<CompetitionEncapsulationForSelector>();
		competitionSelector.addActionListener(new CompManagerCSelectorActionListener((CompManagerScoreTableModel)this.getScoreTableModel(), ctx));
		competitionSelector.setBounds(111, 6, 300, 24);
		contentPane.add(competitionSelector);
		
		rte_gui.min = textField_m;
		rte_gui.sec = textField_s;
		rte_gui.msec = textField_msec;
		
		rte_gui.compManagerCSelector = competitionSelector;	// dodawanaie referencji do RTE
		rte_gui.actuallyOnTrack = lblActuallyOnTrack;
		rte_gui.nextOnTrack = lblNextOnTrack;
		
		JButton btnZmieKonkurencje = new JButton("Zmień konkurencje");
		btnZmieKonkurencje.setBounds(423, 6, 185, 25);
		btnZmieKonkurencje.addActionListener(new CompManagerChgCompBtnActionListener(ctx));
		contentPane.add(btnZmieKonkurencje);
		
		JLabel lblCurrentComp = new JLabel("---");
		lblCurrentComp.setFont(new Font("Dialog", Font.BOLD, 14));
		lblCurrentComp.setForeground(Color.BLACK);
		lblCurrentComp.setBounds(801, 11, 223, 15);
		contentPane.add(lblCurrentComp);
		
		rte_gui.currentCompetition = lblCurrentComp;

	}
	
	public TableModel getScoreTableModel() {
		return table.getModel();
	}
	
	public JTable getScoreTable() {
		return table;
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
