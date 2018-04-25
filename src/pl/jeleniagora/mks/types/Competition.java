package pl.jeleniagora.mks.types;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import pl.jeleniagora.mks.factories.RunsFactory;

/**
 * Klasa przechowywująca wszystkie informację o pojedycznej konkurencji. Jej rodzaj (jedynki M, jedynki K, dwójki, sztafeta), jak
 * również referencje do listy startowej, liczbę ślizgów oraz mapy spinające każdego zawodnika 
 * 
 * @author mateusz
 *
 */

public class Competition {
	
	public int id;
	
	public String name;
	
	/**
	 *  Egzemplarz enuma określający typ tej konkurencji 
	 *  */
	public CompetitionTypes competitionType;

	/**
	 *  Liczba wszystkich (łącznie z treningowymi) ślizgów w tej konkurencji.
	 *   */ 
	public int numberOfAllRuns;
	
	/** 
	 * Ilość ślizgów treningowych w konkurencji 
	 * */
	public int numberOfTrainingRuns;
	
	/**
	 * Mapa łącząca saneczkarzy z numerami startowymi.
	 */
	public Map<LugerCompetitor, Short> startList;
	
	/**
	 * Mapa łącząca saneczkarzy z miejscami (lokatami) które uzyskali.
	 */
	public Map<LugerCompetitor, Short> ranks;
	
	/**
	 * Wektor zawierający czasy w poszczególnych ślizgach, jeden element klasy Run odpowiada jednemu ślizgowi i zawiera
	 * mapę saneczkarzy startujących w tym ślizgu na ich czasy przejazdu. Początkowo czasy są inicjowane zerami. 
	 */
	public Vector<Run> runsTimes;
	
	/**
	 * Liczba saneczkarzy/dwójek/drużyn biorących udział w tej konkurencji
	 */
	public int competitorsCount;
	
	public int getLugersCnt() {
		if (startList != null)
			return startList.values().size();
		
		return 0;
	}
	
	/**
	 * Metoda wykonująca wszystkie czynności potrzebne to dodania jedynki do już zdefiniowanej konkurencji. Metoda
	 * będzie przeciążana osobno dla jedynki, dwójki, drużyny, sztafety itp.
	 * @param comptr
	 */
	public void addToCompetition(LugerSingle comptr) {
		// Dodawanie do listy startowej bez numeru startowego
		startList.put(comptr, new Short((short)0));
		
		// Dodawanie do mapy (listy) wyników -> wiadomo że zero bo nie dodaje się po konkurencji
		startList.put(comptr, new Short((short)0));
		
		
		for (int i = 0; i < (numberOfAllRuns + numberOfTrainingRuns); i++ ) {
			Run runFromVct = runsTimes.get(i);
			Map<LugerCompetitor, LocalTime> m = runFromVct.run;
			
			m.put(comptr, LocalTime.of(0, 0, 0, 0));
		}
	}
	
	public Competition() {
		this.competitionType = CompetitionTypes.UNINITIALIZED_COMP;
		this.numberOfAllRuns = 0;
		this.numberOfTrainingRuns = 0;
	}
	
	/**
	 * Konstruktor przyjmujący na wejście wektor klas implementujących interfejs 'LugerCompetitor'. Warto zapamiętać że tutaj 
	 * operuje się cały czas na ogólnym typie a w zasadzie na tym interfejsie. Interfejsu nie można zintancjalizować, czyli
	 * stworzyć obiektu jego klasy (bo nie jest klasą). Do tych wszystkich wektorów, map itp. będzie można ciepać jedynie
	 * klasy go implementujące.
	 * @param in
	 * @param allRuns
	 * @param trainingRuns
	 */
	public Competition(Vector<LugerCompetitor> in, int allRuns, int trainingRuns, boolean simul) {
		
		this.numberOfAllRuns = allRuns;
		this.numberOfTrainingRuns = trainingRuns;
		
		this.competitorsCount = in.size();
		
		/*
		 * W tym momencie saneczkarze nie mają przypisanych numerów startowych, dlatego konstruktor
		 * dociepuje jedynie elementy do HashMapy z zerami
		 */
		startList = new HashMap<LugerCompetitor, Short>();
		
		int lugersAtInputLn = in.size();
		
		for (int i = 0; i < lugersAtInputLn; i++) {
			startList.put(in.get(i), new Short((short)0));
		}
		
		/*
		 * tak samo nie mają oczywiście jeszcze żadnych wyników
		 */
		ranks = new HashMap<LugerCompetitor, Short>();
		for (int i = 0; i < lugersAtInputLn; i++) {
			ranks.put(in.get(i), new Short((short)0));
		}		
		
		if (!simul)
			runsTimes = RunsFactory.createNewRunsFromLugersVct(in, allRuns, trainingRuns);
		else 
			runsTimes = RunsFactory.createNewRunsFromLugersVct(in, allRuns, trainingRuns, true);
			
		
	}
	
	/**
	 * Metoda wymagana przez JComboBox<Competition> to uzyskiwania stringa, który ma się pojawiać się jako
	 * pozycja na liście rozwijanej
	 */
	public String toString() {
		if (name == null || name.equals(new String(""))) {
			return competitionType.toString();
		}
		else {
			return name;
		}
	}
	
	/* Tu ma być reszta rzeczy typu lista startowa itp.	 */
}
