package pl.jeleniagora.mks.types;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import pl.jeleniagora.mks.factories.RunsFactory;
import pl.jeleniagora.mks.files.xml.adapters.TrackAdapter;
import pl.jeleniagora.mks.files.xml.adapters.CompetitionTypesAdapter;
import pl.jeleniagora.mks.files.xml.adapters.InvertedStartListAdapter;
import pl.jeleniagora.mks.files.xml.adapters.RanksAdapter;
import pl.jeleniagora.mks.files.xml.adapters.RunVectorAdapter;
import pl.jeleniagora.mks.files.xml.adapters.StartListAdapter;
import pl.jeleniagora.mks.files.xml.adapters.StartOrderAdapter;
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
	 * Mapa łącząca saneczkarzy z numerami startowymi. Na tej liście moga pojawić się zawodnicy z numerami startowymi równymi zero
	 * co ma miejsce jeszcze przed wygenerowaniem numer startowych, albo po dodaniu nowych zawodników do konkurencji
	 * już po wygenerowniu.
	 */
	@XmlJavaTypeAdapter(value = StartListAdapter.class)
	public HashMap<LugerCompetitor, Short> startList;
	
	/**
	 * Kolejność startowa
	 */
	@XmlJavaTypeAdapter(value = StartOrderAdapter.class)
	@XmlElement
	public StartOrderInterface startOrder;
	
	/**
	 * Odwrócona mapa która łączy numery startowe z saneczkarzami. Tutaj występują tylko i wyłącznie zawodnicy z wygenerowanymi 
	 * numerami startowymi. Nie ma zer
	 */
	@XmlJavaTypeAdapter(value = InvertedStartListAdapter.class)
	@XmlElement
	public Map<Short, LugerCompetitor> invertedStartList;
	
	/**
	 * Mapa łącząca saneczkarzy z miejscami (lokatami) które uzyskali.
	 */
	// TODO
	@XmlJavaTypeAdapter(value = RanksAdapter.class)
	public Map<LugerCompetitor, Short> ranks;
	
	/**
	 * Wektor zawierający czasy w poszczególnych ślizgach, jeden element klasy Run odpowiada jednemu ślizgowi i zawiera
	 * mapę saneczkarzy startujących w tym ślizgu na ich czasy przejazdu. Początkowo czasy są inicjowane zerami. 
	 */
	@XmlJavaTypeAdapter(value = RunVectorAdapter.class)
	@XmlElement(name = "runs")
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
	 * Metoda zwraca ostatni (najwyższy) numer startowy na listach startowych. Jest niezbędna gdyż po usuwaniu sankarzy z konkurencji
	 * najwyższy numer startowy może być dużo większu niż liczba elementów na liście
	 * @return
	 */
	public short getLastStartNumber() {
		short returnVal = 0;
		
		for (Short e : invertedStartList.keySet()) {
			if (e > returnVal)
				returnVal = e;
		}
		return returnVal;
	}
	
	/**
	 * Metoda zwraca pierwszy (najniższy) numer startowy na listach startowych. Najniższy nie oznacza koniecznie pierwszy, bo użytkownik
	 * może po wygenerowaniu numerów startowych usunąć pierwszego z listy i wtedy trzeba zaczać od numeru drugiego.
	 * @return
	 */
	public short getFirstStartNumber() {
		short returnVal = 255; // jest to jednocześnie ograniczenie ilości sankarzy w jednej konkurencji
		
		for (Short e : invertedStartList.keySet()) {
			if (e < returnVal)
				returnVal = e;
		}
		return returnVal;
		
	}
	
	/**
	 * Metoda wykonująca wszystkie czynności potrzebne to dodania jedynki do już zdefiniowanej konkurencji. Metoda
	 * będzie przeciążana osobno dla jedynki, dwójki, drużyny, sztafety itp.
	 * @param comptr
	 */
	public void addToCompetition(LugerCompetitor comptr) {
		// Dodawanie do listy startowej bez numeru startowego
		startList.put(comptr, new Short((short)0));
		
		// Dodawanie do mapy (listy) wyników -> wiadomo że zero bo nie dodaje się po konkurencji
		ranks.put(comptr, new Short((short)0));
		
		competitorsCount++;
		
		for (int i = 0; i < (numberOfAllRuns + numberOfTrainingRuns); i++ ) {
			Run runFromVct = runsTimes.get(i);
			Map<LugerCompetitor, LocalTime> m = runFromVct.totalTimes;
			
			m.put(comptr, LocalTime.of(0, 0, 0, 0));
		}
	}
	
	/**
	 * Metoda 
	 * @param comptr Referencja na zawodnika do usunięcia z tej konkurencji
	 * @return True jeżeli zawodnik został usunięty bądź false jeżeli w ogóle nie występował w tej konkurencji.
	 */
	public boolean removeFromCompetition(LugerCompetitor comptr) {
		int startNumber = comptr.getStartNumber();
		
		
		if (ranks.remove(comptr) == null) {
			return false;	// sprawdzanie czy taki saneczkarz w ogóle został do tej konkurencji dodany
		}
		startList.remove(comptr);
		if (startNumber > 0)
			invertedStartList.remove((short)startNumber);
		
		competitorsCount--;
		
		return true;
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
