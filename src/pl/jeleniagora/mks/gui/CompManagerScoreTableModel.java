package pl.jeleniagora.mks.gui;

import javax.swing.table.AbstractTableModel;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionTypes;
import pl.jeleniagora.mks.types.Reserve;

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
	private short numberOfLugers;
	
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
		return (comp.getNumberOfAllRuns() + 5);
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
	 *  Publiczna metoda służaca do aktualizacji nazw kolumn w głównej tabeli z czasami. Ponieważ dla CompManagera
	 *  wszystkie akcje są obsługiwane przez zewnętrzne klasy jest to wykonywane 
	 * @throws Reserve 
	 */
	public void updateTableHeading(Competition competition) throws Reserve {
		boolean isDouble = false;
		
		int allRuns = competition.getNumberOfAllRuns();
		int trainingRuns = competition.getNumberOfTrainingRuns();
		
		this.columnNames = new String[allRuns + 5];
		this.types = new Class[allRuns + 5];
		
		this.comp = competition;
		
		/* Sprawdzenie czy tablica Stringów podana jako wyjście ma zaalokowane odpowiednio dużo miejsca na elementy */
		if (columnNames.length < allRuns + 2 || types.length < allRuns + 2) 
			throw new Reserve(Reserve.Type.UNSUFFICIENT_SIZE_OF_TABLE);
		
		int i = 0, t = 0;
		
		/* Sprawdzanie czy ta konkurencja to konkurencja dwójkowa*/
		if (competition.getCompetitionType().equals(CompetitionTypes.DOUBLE) ||
				competition.getCompetitionType().equals(CompetitionTypes.DOUBLE_MEN_ONLY) ||
				competition.getCompetitionType().equals(CompetitionTypes.DOUBLE_MIXED) ||
				competition.getCompetitionType().equals(CompetitionTypes.DOUBLE_WOMAN_ONLY))
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
