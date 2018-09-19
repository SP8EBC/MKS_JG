package pl.jeleniagora.mks.scoring;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa służy do obliczania pozycji po zakończonej konkurencji
 * @author mateusz
 *
 */
public class CalculateRanksAfterRun {

	public Map<LugerCompetitor, LocalTime> calculateTotalRuntime(Competition comp) {

		/*
		 * Map łącząca numer startowy saneczkarza z jego łącznym czasem przejazdu od pierwszego do aktualnego ślizgu 
		 */
		Map<LugerCompetitor, LocalTime> totalTimes = new HashMap<LugerCompetitor, LocalTime>();
		
		/*
		 * Bool używany do sprawdzania czy w ogóle w tej konkurencji rozegrano jakiś ślizg który powinien być puntkowany.
		 * Jeżeli idą dopiero treningi to metoda powinna zwrócić null. Chodzi o uniknięcie wyświetlania samych lokat '1'
		 * w sytuacji gdy ślizg jest niepunktowany. Co do zasady zakłada się, że w konkurencji idą najpierw ślizgi treningowe
		 * a po pierwszym ślizgu punktowanym wszystkie następne muszą już być puntkowane
		 */
		boolean gotAtLeastOne = false;
		
		for (Run r : comp.runsTimes) {
			if (!comp.trainingOrContest || r.trainingOrScored) {
				/*
				 * Jeżeli cała konkurencja to trening albo ten konkretny ślizg jest punktowany to trzeba go uwzględnić 
				 * w obliczaniu 
				 */
				
				Set<Entry<LugerCompetitor, LocalTime>> set = r.totalTimes.entrySet();	// entry set z czasami aktualnie przetwarzanego ślizgu
				
				Iterator<Entry<LugerCompetitor, LocalTime>> it = set.iterator(); 
								
				do {
					/*
					 * Entry set z mapy nie gwarantuje zachowania kolejności zgodnej z kolejnością
					 * dodawania elementów do mapy ale akurat tutaj nie jest to problem
					 */
					Entry<LugerCompetitor, LocalTime> e = it.next();

					LugerCompetitor k = e.getKey();
					LocalTime v = e.getValue();
					
					if (k == null)
						continue;
					
					if (!v.equals(LocalTime.of(0, 0, 0, 0)))
						gotAtLeastOne = true;
					
					/*
					 * Sprawdzanie czy wyjściowa mapa ma już klucz odpowiadający temu zawodnikowi
					 */
					if (totalTimes.containsKey(k)) {
						LocalTime t = totalTimes.get(k);	// wyciąganie już zapisanego czasu ślizgu
						long toAdd = v.toNanoOfDay();
						LocalTime sum = t.plusNanos(toAdd);			// dodawanie czasu z tego ślizgu
						
						totalTimes.put(k, sum);		// zapisywanie na nowo w wyjściowej mapie
					}
					else {
						totalTimes.put(k, v);
					}
				} while (it.hasNext());
			}
		}
		
		if (gotAtLeastOne)
			return totalTimes;
		else return null;
		
	}
	
	public Map<LugerCompetitor, Short> calculateRanksFromTotalRuntimes(Map<LugerCompetitor, LocalTime> rt) {
		
		short rank = 1, lastRank = 1;
		short shift = 0;
		LocalTime m = LocalTime.of(0, 59);
		
		Map<LugerCompetitor, Short> out = new HashMap<LugerCompetitor, Short>();
		
		/*
		 * Wyciąganie listy czasów łącznych czasów ślizgów
		 */
		List<Entry<LugerCompetitor, LocalTime>> list = new LinkedList<>(rt.entrySet());
		
		/*
		 * Sortowanie
		 */
		list.sort(new EntryLugerCmptrLocalTimeComparator());
		
		for (int i = 0; i < list.size(); i++) {
			Entry<LugerCompetitor, LocalTime> current = list.get(i);
			Entry<LugerCompetitor, LocalTime> previous;
			
			if (current.getKey() == null)
				continue;
			
			if (i > 0)
				previous = list.get(i-1);
			else
				previous = null;
			
			if (current.getValue().isAfter(m)) {
				// DNS DSQ DNF
				out.put(current.getKey(), new Short((short) 999));
				continue;
			}
			
			/*
			 * Sprawdzanie czy następny saneczkarz nie ma takiego samego czasu przejazdu
			 */
			if (previous != null) {
				if (current.getValue().equals(previous.getValue())) {
					LugerCompetitor l = current.getKey();	// jeżeli aktualny ma taki sam czas jak poprzedni 
					out.put(l, (lastRank));
					
					/*
					 * Zwiększ przesunięcie pozycji o jeden żeby wstawić dziurę pomiędzy ostatniego ex-aeqo a kolejnego za nim
					 * 1szy, 2gi, 3ci, 3ci, 5ty, 6ty...
					 */
					shift++;
				}
				else {		// jeżeli ma różny
					lastRank = (short) (++rank+shift);
					LugerCompetitor l = current.getKey();
					out.put(l, lastRank);

				}
			}
			else {
				/*
				 * Wstawianie pierwszego
				 */
				lastRank = 1;
				LugerCompetitor l = current.getKey();
				out.put(l, (short)(1));
			}
			
		}
		
	
		return out;
	}
	
}
