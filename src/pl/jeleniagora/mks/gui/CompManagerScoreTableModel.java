package pl.jeleniagora.mks.gui;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionTypes;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerSingle;
import pl.jeleniagora.mks.types.Reserve;
import pl.jeleniagora.mks.types.UninitializedCompEx;

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
		columnNames = new String[5];
		columnNames[2] = new String("Imię");
		columnNames[3] = new String("Nazwisko");
		columnNames[4] = new String("Klub");
		columnNames[0] = new String("NrStartowy");
		columnNames[1] = new String("Miejsce");

		
		types = new Class[5];
		types[0] = new Short((short)0).getClass();
		types[1] = new Short((short)0).getClass();
		types[2] = new String().getClass();
		types[3] = new String().getClass();
		types[4] = new String().getClass();

	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
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
	
	@Override
	public Class getColumnClass(int c) {
		return types[c];
		
	}
	
	@Override
	public String getColumnName(int c) {
		return columnNames[c];
		
	}
	
	/**
	 * Tablica wyników nie jest edytowalna bezpośrednio przez dwuklik na komórce
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	/**
	 * Metoda do aktualizacji głównej tabeli z wynikami. Przepisuje po prostu dane z wejściowej klasy i wszystkich
	 * "podklas" to tabeli Object[][] która jest już bezpośrednim źródłem danych dla JTable
	 * @param competition
	 * @param intermediateTiming
	 * @throws UninitializedCompEx 
	 */
	public void updateTableData(Competition competition, boolean intermediateTiming) throws UninitializedCompEx {
		// !! Ważne !! aktualnie metoda obsługuje tylko konkurencje jedynkowe
		
		if (!intermediateTiming) {
			int allRuns = competition.numberOfAllRuns;
			int trainingRuns = competition.numberOfTrainingRuns;
			
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
				Object[] l = new Object[5 + allRuns];

				Entry<LugerCompetitor, Short> currLuger = lugersIt.next();

				LugerCompetitor luger = currLuger.getKey();
				CompetitionTypes lugerType = luger.getCompetitorType();
				
				switch (lugerType) {
					case DOUBLE:
						break;
					case DOUBLE_MEN_ONLY:
						break;
					case DOUBLE_MIXED:
						break;
					case DOUBLE_WOMAN_ONLY:
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
						String lugeClub = s.single.club;
						int startNum = currLuger.getValue();
						
						l[0] = startNum;
						l[1] = 0; // miejsce
						l[2] = name;
						l[3] = surname;
						l[4] = lugeClub;
						
						j = 5;
					default:
						break;
				}
				
				/*
				 * Czasy ślizgów
				 */
				Vector<Short> times = new Vector<Short>();
				for (int i = 0; i < allRuns; i++) {
					competition.runsTimes.get(i).getRunTimeForCompetitor(luger);
				}
				
				/*
				 * Tworzenie tablicy objektów odpowiadającej jednemu saneczkarzowi
				 */
//				Object[] l = new Object[] {new Short((short)73), new Short((short)4), "Naz", "Am", "kl", new Integer(123010), new Integer(9100), new Integer(670450)};
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
		
		this.columnNames = new String[allRuns + 5];
		this.types = new Class[allRuns + 5];
		
		this.comp = competition;
		
		/* Sprawdzenie czy tablica Stringów podana jako wyjście ma zaalokowane odpowiednio dużo miejsca na elementy */
		if (columnNames.length < allRuns + 2 || types.length < allRuns + 2) 
			throw new Reserve(Reserve.Type.UNSUFFICIENT_SIZE_OF_TABLE);
		
		int i = 0, t = 0;
		
		/* Sprawdzanie czy ta konkurencja to konkurencja dwójkowa*/
		if (competition.competitionType.equals(CompetitionTypes.DOUBLE) ||
				competition.competitionType.equals(CompetitionTypes.DOUBLE_MEN_ONLY) ||
				competition.competitionType.equals(CompetitionTypes.DOUBLE_MIXED) ||
				competition.competitionType.equals(CompetitionTypes.DOUBLE_WOMAN_ONLY))
		{
			isDouble = true;
		}

		columnNames[i] = new String("NrStart");
		types[i++] = new Short((short)0).getClass();

		columnNames[i] = new String("Miejsce");
		types[i++] = new Short((short)0).getClass();
		
		if (isDouble) {
			/* Jeżeli konkurencja jest "dwójkowa" to zamiast Nazwiska i Imienia wyświetlaj
			 * tylko nazwiska obydwu zaneczkarzy */
			columnNames[i] = new String("Nazwisko Góra");
			types[i++] = new String().getClass();
			columnNames[i] = new String("Nazwisko Dół");
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
	
	public void fillWithTestData() {
		this.numberOfLugers = 4;
		this.tableData = new Object[this.numberOfLugers][];
		
		this.tableData[0] = new Object[] {new Short((short)73), new Short((short)4), "Naz", "Am", "kl", new Integer(123010), new Integer(9100), new Integer(670450)};
		this.tableData[1] = new Object[] {new Short((short)9), new Short((short)5), "Nąz", "Imi", "kl", new Integer(124010), new Integer(9220), new Integer(710350)};
		this.tableData[2] = new Object[] {new Short((short)70), new Short((short)8), "Aąćż", "Cm", "kl", new Integer(121010), new Integer(9500), new Integer(690050)};
		this.tableData[3] = new Object[] {new Short((short)61), new Short((short)7), "Cazw", "Im", "kl", new Integer(120010), new Integer(9600), new Integer(780150)};

	}

}
