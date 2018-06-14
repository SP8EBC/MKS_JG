package pl.jeleniagora.mks.types;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import pl.jeleniagora.mks.factories.RunsFactory;
import pl.jeleniagora.mks.files.xml.adapters.TrackAdapter;
import pl.jeleniagora.mks.files.xml.adapters.CompetitionTypesAdapter;
import pl.jeleniagora.mks.files.xml.adapters.StartListAdapter;
import pl.jeleniagora.mks.start.order.StartOrderInterface;

/**
 * Klasa przechowywująca wszystkie informację o pojedycznej konkurencji. Jej rodzaj (jedynki M, jedynki K, dwójki, sztafeta), jak
 * również referencje do listy startowej, liczbę ślizgów oraz mapy spinające każdego zawodnika 
 * 
 * @author mateusz
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Competition {
	
	public int id;
	
	public String name;
	
	/**
	 * Ustawienie na true powoduje, że ta konkurencja będzie traktowana jako konkurencja w zawodach - w przeciwnym raze
	 * będzie to jedynie trening. Nastawienie false powoduje, że wszystkie dodane tutaj ślizgi będą z automatu traktowane jako
	 * treningowe bez względu na nastawienie pola trainingOrScored w klasie Run (odpowiadającej pojedynczym ślizgom). Wartość
	 * false zmieni też sposób działania modułu obliczającego lokaty, który będzie obliczał lokaty dla wszystkih ślizgów 
	 * ( w odróżnieniu od nastwienia true gdzie obliczane będzie tylko dla ślizgów punktowanych)
	 */
	public boolean trainingOrContest;
	
	/**
	 *  Egzemplarz enuma określający typ tej konkurencji 
	 *  */
	@XmlElement
	@XmlJavaTypeAdapter(value = CompetitionTypesAdapter.class)
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
	 * Wektor zawiera identyfikatory (pierwszy drugi itp) ukończonych w całości ślizgów punktowanych
	 */
	public Vector<Integer> scoredRunsDone;
	
	/**
	 * Wektor zawiera identyfikatory (pierwszy drugi itp) zakończonych w całości ślizgów treningowych
	 */
	public Vector<Integer> trainingRunsDone;
	
	/**
	 * Mapa łącząca saneczkarzy z numerami startowymi.
	 */
	@XmlJavaTypeAdapter(value = StartListAdapter.class)
	public HashMap<LugerCompetitor, Short> startList;
	
	/**
	 * Kolejność startowa
	 */
	public StartOrderInterface startOrder;
	
	/**
	 * Odwrócona mapa która łączy numery startowe z saneczkarzami.
	 */
	public Map<Short, LugerCompetitor> invertedStartList;
	
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
	
	public Vector<LugerCompetitor> getStartListAsVector() {
		Vector<LugerCompetitor> out = new Vector<LugerCompetitor>();
		
		if (invertedStartList.size() == 0) {
			return null;
		}
		
		for (short i = 0; i < invertedStartList.size(); i++) {
			LugerCompetitor cmptr = invertedStartList.get((short)(i + 1));
			out.add(cmptr);
		}
		
		return out;
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
