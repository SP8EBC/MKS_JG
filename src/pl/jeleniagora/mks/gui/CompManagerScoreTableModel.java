package pl.jeleniagora.mks.gui;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.factories.ClubsFactory;
import pl.jeleniagora.mks.factories.LugersFactory;
import pl.jeleniagora.mks.factories.StartListFactory;
import pl.jeleniagora.mks.settings.DisplayS;
import pl.jeleniagora.mks.start.order.SimpleOrder;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionTypes;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerDouble;
import pl.jeleniagora.mks.types.LugerSingle;
import pl.jeleniagora.mks.types.Reserve;
import pl.jeleniagora.mks.types.Run;

/**
 * Model używany do rysowania tabeli wyników typu JTable
 * @author mateusz
 *
 */

public class CompManagerScoreTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1945596334511452851L;

	/**
	 * Referencja na aktualnie wyświetlaną konkurencję
	 */
	private Competition comp;
	
	private String[] columnNames;
	
	/**
	 * Tablica klas przechowywująca reprezentację typów danych poszczególnych kolumn w JTable
	 */
	@SuppressWarnings("rawtypes")
	private Class[] types;
	
	/**
	 * Tablica obiektów przechowywująca właściwe dane wyświetlane przez JTable w oknie obsługi zawodów/treningów (CompManager)
	 */
	private Object[][] tableData;
	
	/** 
	 * Liczba saneczkarzy w tej konkurencji/treningu. A najciekawsze jest to, że saneczkarz po angielsku nazywa się
	 * jednak tak samo jak dość popularny pistolet :) 
	 */
	private int numberOfLugers;
	
	/**
	 * Domyślny konstruktor który inicjalizuje niektóre pola do domyślnych wartości, żeby JTable w ogóle
	 * się pokazała w jakimkolwiek stanie a nie rzuciła jakimś wyjątkiem typu NullPointerException czy coś takiego..
	 */
	public CompManagerScoreTableModel() {
		this.comp = new Competition();
		columnNames = new String[6];
		columnNames[0] = new String("NrStartowy");
		columnNames[1] = new String("Lokata ogółem");
		columnNames[2] = new String("Lokata w ślizgu");
		columnNames[3] = new String("Imię");
		columnNames[4] = new String("Nazwisko");
		columnNames[5] = new String("Klub");


		
		types = new Class[6];
		types[0] = new Short((short)0).getClass();
		types[1] = new Short((short)0).getClass();
		types[2] = new Short((short)0).getClass();
		types[3] = new String().getClass();
		types[4] = new String().getClass();
		types[5] = new String().getClass();
		
		numberOfLugers = 0;

	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		if (comp == null)
			return 0;
		else
			return (comp.numberOfAllRuns + 5);
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return numberOfLugers;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return tableData[arg0][arg1];
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class getColumnClass(int c) {
		return types[c];
		
	}
	
	@Override
	public String getColumnName(int c) {
		return columnNames[c];
		
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getTypes() {
		return types;
	}

	public Object[][] getTableData() {
		return tableData;
	}

	/**
	 * Tablica wyników nie jest edytowalna bezpośrednio przez dwuklik na komórce
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	/**
	 * MMetoda służy do zerownia/inicjalizacji tabeli z wynikami podczas tworzenia nowego pliku zawodów, czy też podczas uruchamiania
	 * programu
	 */
	public void eraseEverything() {
		columnNames = new String[6];
		columnNames[0] = new String("NrStartowy");
		columnNames[1] = new String("Lokata ogółem");
		columnNames[2] = new String("Lokata w ślizgu");
		columnNames[3] = new String("Imię");
		columnNames[4] = new String("Nazwisko");
		columnNames[5] = new String("Klub");


		
		types = new Class[6];
		types[0] = new Short((short)0).getClass();
		types[1] = new Short((short)0).getClass();
		types[2] = new Short((short)0).getClass();
		types[3] = new String().getClass();
		types[4] = new String().getClass();
		types[5] = new String().getClass();
		
		numberOfLugers = 0;
		comp = null;
	}
	
	/**
	 * Metoda służy do wyszukiwania indeksu w modelu danych odpowidającemu zadanemu numerowi startowemu zawodnika. Należy mieć pozor, że
	 * co do zasdy zawodnicy są najpierw dodawani do tabeli modelu danych bez numerów startowych (inicjalzacja zerami). Z tego powodu indeksy
	 * w modelu danych nie odpowiadają numerom startowym
	 * a potem te numery są dolosowywane
	 * @param startNumber
	 * @return
	 */
	public int getModelIndexFromStartNumber(int startNumber) {
		
		for (int i = 0; i < this.numberOfLugers; i++) {
			short startNumToChk = (short)(this.tableData[i][0]);
			if (startNumToChk == startNumber) {
				return i;
			}
			else;
		}
		
		return -1;
	}
	
	/**
	 * Metoda do aktualizacji głównej tabeli z wynikami. Przepisuje po prostu dane z wejściowej klasy i wszystkich
	 * "podklas" to tabeli Object[][] która jest już bezpośrednim źródłem danych dla JTable
	 * @param competition
	 * @param intermediateTiming
	 * @throws UninitializedCompEx 
	 */
	public void updateTableData(Competition competition, boolean intermediateTiming) throws UninitializedCompEx {
		// TODO:!! Ważne !! aktualnie metoda obsługuje tylko konkurencje jedynkowe
		
		if (competition == null)
			return;
		
		/*
		 * Numer aktualnie przetwarzanego saneczkarza
		 */
		int lugerProcessCounter = 0;
		
		if (!intermediateTiming) {
			int allRuns = competition.numberOfAllRuns;
//			int trainingRuns = competition.numberOfTrainingRuns;
			
			int lugersCnt = competition.getLugersCnt();
			
			this.tableData = new Object[lugersCnt][];
			this.numberOfLugers = lugersCnt;
			
			/*
			 * Entry set wyciągnięty z listy startowej aby można było na nim zastosować iterator
			 */
			Set<Entry<LugerCompetitor, Short>> lugersEntrySet = competition.startList.entrySet();

			/*
			 * Iterator do poruszania się po elementach przechowywanych w mapie
			 */
			Iterator<Entry<LugerCompetitor, Short>> lugersIt = lugersEntrySet.iterator();

			/**
			 * Regenerowanie tabeli obiektów zawierjącej wyświetlane dane
			 */
			this.tableData = new Object[this.numberOfLugers][];
			
			while (lugersIt.hasNext()) {
				int j = 0;
				/*
				 * Przeciepywanie danych z klas do tablicy Object[][]
				 */
				Object[] l = new Object[DisplayS.columnOffset + allRuns];

				Entry<LugerCompetitor, Short> currLuger = lugersIt.next();

				LugerCompetitor luger = currLuger.getKey();
				CompetitionTypes lugerType = luger.getCompetitorType();
				
				switch (lugerType) {
					case DOUBLE:
					case DOUBLE_MEN_ONLY:
					case DOUBLE_MIXED:
					case DOUBLE_WOMAN_ONLY:
					case MARRIED_COUPLE:
						LugerDouble d = (LugerDouble) luger;
						int startNumm = currLuger.getValue();	// numer startowy
						String upper = d.upper.toString();
						String lower = d.lower.toString();
						String lugeCl = d.upper.club.name;
						
						l[0] = (short)startNumm;	// numer startowy
						l[1] = (short)competition.ranks.get(d); // miejsce ogółem
						l[2] = upper;
						l[3] = lower;
						l[4] = lugeCl;
						
						j = DisplayS.columnOffset;
						
						
						break;
					case TEAM_RELAY:
						break;
					case TRAINING:
						break;
					case UNINITIALIZED_COMP:
						throw new UninitializedCompEx();
					case MEN_SINGLE:
					case WOMAN_SINGLE:
						LugerSingle s = (LugerSingle) luger;
						String name = s.single.name;
						String surname = s.single.surname;
						String lugeClub = s.single.club.name;
						int startNum = currLuger.getValue();	// numer startowy
						
						l[0] = (short)startNum;	// numer startowy
						l[1] = (short)competition.ranks.get(s); // miejsce
						if (competition.partialRanks != null && 
							competition.partialRanks.containsKey(s))
							l[2] = (short)competition.partialRanks.get(s); // miejsce w ślizgu
						else
							l[2] = (short)0;
						l[3] = name;
						l[4] = surname;
						l[5] = lugeClub;
						
						j = DisplayS.columnOffset;
					default:
						break;
				}
				
				/*
				 * Czasy ślizgów
				 */
//				Vector<Short> times = new Vector<Short>();
				for (int i = 0; i < allRuns; i++) {
					Integer t = competition.runsTimes.get(i).getRunTimeForCompetitor(luger);
					l[j + i] = t;
				}
				
				/*
				 * Przepisywanaie danych z pośredniej tablicy l to docelowej tablicy
				 */
				
				int runsTimesSize = competition.runsTimes.size();

				this.tableData[lugerProcessCounter] = new Object[j + runsTimesSize];
				
				for (int i = 0; i < j + runsTimesSize; i++) {
					this.tableData[lugerProcessCounter][i] = l[i];
				}
				
				lugerProcessCounter++;
			}
		}
		else return;
	}
	
	/**
	 *  Publiczna metoda służaca do aktualizacji nazw kolumn w głównej tabeli z czasami. Ponieważ dla CompManagera
	 *  wszystkie akcje są obsługiwane przez zewnętrzne klasy jest to wykonywane 
	 * @throws Reserve 
	 */
	public void updateTableHeading(Competition competition, boolean intermediateTiming) throws Reserve {
		boolean isDouble = false;
		
		int allRuns = competition.numberOfAllRuns;
		int trainingRuns = competition.numberOfTrainingRuns;
		
		this.columnNames = new String[allRuns + DisplayS.columnOffset];
		this.types = new Class[allRuns + DisplayS.columnOffset];
		
		this.comp = competition;
		
		/* Sprawdzenie czy tablica Stringów podana jako wyjście ma zaalokowane odpowiednio dużo miejsca na elementy */
		if (columnNames.length < allRuns + 2 || types.length < allRuns + 2) 
			throw new Reserve(Reserve.Type.UNSUFFICIENT_SIZE_OF_TABLE);
		
		int i = 0, t = 0;
		
		/* Sprawdzanie czy ta konkurencja to konkurencja dwójkowa*/
		if (competition.competitionType.equals(CompetitionTypes.DOUBLE) ||
				competition.competitionType.equals(CompetitionTypes.DOUBLE_MEN_ONLY) ||
				competition.competitionType.equals(CompetitionTypes.DOUBLE_MIXED) ||
				competition.competitionType.equals(CompetitionTypes.DOUBLE_WOMAN_ONLY) ||
				competition.competitionType.equals(CompetitionTypes.MARRIED_COUPLE))
		{
			isDouble = true;
		}

		columnNames[i] = new String("NrStart");
		types[i++] = new Short((short)0).getClass();

		columnNames[i] = new String("Lokata ogółem");
		types[i++] = new Short((short)0).getClass();
		
		columnNames[i] = new String("Lokata w ślizgu");
		types[i++] = new Short((short)0).getClass();
		
		if (isDouble) {
			/* Jeżeli konkurencja jest "dwójkowa" to zamiast Nazwiska i Imienia wyświetlaj
			 * tylko nazwiska obydwu zaneczkarzy */
			columnNames[i] = new String("Zawodnik na górze");
			types[i++] = new String().getClass();
			columnNames[i] = new String("Zawodnik na dole");
			types[i++] = new String().getClass();
		}
		else {
			columnNames[i] = new String("Imię");
			types[i++] = new String().getClass();
			columnNames[i] = new String("Nazwisko");
			types[i++] = new String().getClass();			
		}
		
		columnNames[i] = new String("Klub");
		types[i++] = new String().getClass();
		
		/* Dodawanie pól dla ślizgów. Najpierw treningowe potem punktowane */
		for (t = 0; t < trainingRuns; t++) {
			/* Wciepywanie ślizgów treningowych */
			columnNames[i] = new String("Trening " + (t + 1));
			types[i++] = new Integer(0).getClass();
		}
		
		for (int j = 0; j < allRuns - trainingRuns; j++) {
			/* Wciepywanie ślizgów punktowanych */
			columnNames[i] = new String("Ślizg " + (j + 1));
			types[i++] = new Integer(0).getClass();
		}
		
		/* */
		return;
		
		
	}
	
	public Vector<Competition> fillWithTestData(Competitions cmps, boolean randomize) {
		
		Competition testCompetition, testCompetition2;
		Vector<Competition> out = new Vector<Competition>();

		/*
		 * Takie przypisanie może być zrobione w tym miejscu znim wektor out będzie miał w sobie jakiekolwiek obiekty, bo w Javie
		 * wszystko jest referencją. Dlatego tu się nie kopiuje całej zawartości wektora tylko kopiuje się referencje.
		 */
		cmps.competitions = out;
		
		LugerCompetitor l1, l2, l3, l4, l5, l6, l7;
		
		LocalDate b = LocalDate.of(1990, 9, 12);
		
		LugersFactory.competitions = cmps;
		
		l1 = LugersFactory.createNewLugerSingleFromName("Im", "Naz", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l2 = LugersFactory.createNewLugerSingleFromName("Imi", "Nąz", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l3 = LugersFactory.createNewLugerSingleFromName("Aąćż", "N", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l4 = LugersFactory.createNewLugerSingleFromName("ěřžšá", "Nazw", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l5 = LugersFactory.createNewLugerSingleFromName("Cazw", "Naz", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l6 = LugersFactory.createNewLugerSingleFromName("baa", "Nk", true, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l7 = LugersFactory.createNewLugerSingleFromName("daa", "kz", true, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));

		
		Vector<LugerCompetitor> vctTst = new Vector<LugerCompetitor>();
		vctTst.add(l5);
		vctTst.add(l4);
		vctTst.add(l3);
		vctTst.add(l2);
		vctTst.add(l1);

		Vector<LugerCompetitor> vctTst2 = new Vector<LugerCompetitor>();
		vctTst2.add(l6);
		vctTst2.add(l7);		
		
		/*
		 * Tworzy pusty wektor czasów przejazdu
		 */
		
		testCompetition2 = new Competition(vctTst2, 4, 0, randomize);		// mniejsza
		testCompetition2.competitionType = CompetitionTypes.WOMAN_SINGLE;
		testCompetition2.startOrder = new SimpleOrder();
		testCompetition2.trainingOrContest = true;
		testCompetition2.id = out.size();
		out.add(testCompetition2);
		
		testCompetition = new Competition(vctTst, 4, 1, randomize);			// większa
		testCompetition.competitionType = CompetitionTypes.MEN_SINGLE;
		testCompetition.startOrder = new SimpleOrder();
		testCompetition.trainingOrContest = true;
		testCompetition.id = out.size();
		out.add(testCompetition);

		
		try {
			updateTableHeading(testCompetition, false);
		} catch (Reserve e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			updateTableData(testCompetition, false);
		} catch (UninitializedCompEx e) {
			e.printStackTrace();
		}
		

		/*
		 * Dodawanie numerów startowych
		 */
		//StartListFactory.generateStartList(testCompetition);
		//StartListFactory.generateStartList(testCompetition2);
		StartListFactory.generateFixedStartList(testCompetition);
		StartListFactory.generateFixedStartList(testCompetition2);
		
		return out;
		
		/*
		this.numberOfLugers = 4;
		this.tableData = new Object[this.numberOfLugers][];
		
		this.tableData[0] = new Object[] {new Short((short)73), new Short((short)4), "Naz", "Am", "kl", new Integer(123010), new Integer(9100), new Integer(670450)};
		this.tableData[1] = new Object[] {new Short((short)9), new Short((short)5), "Nąz", "Imi", "kl", new Integer(124010), new Integer(9220), new Integer(710350)};
		this.tableData[2] = new Object[] {new Short((short)70), new Short((short)8), "Aąćż", "Cm", "kl", new Integer(121010), new Integer(9500), new Integer(690050)};
		this.tableData[3] = new Object[] {new Short((short)61), new Short((short)7), "Cazw", "Im", "kl", new Integer(120010), new Integer(9600), new Integer(780150)};
		 */
	}

}
