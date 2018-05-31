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
		
		for (Run r : comp.runsTimes) {
			if (comp.trainingOrContest || r.trainingOrScored) {
				/*
				 * Jeżeli cała konkurencja to trening albo ten konkretny ślizg jest punktowany to trzeba go uwzględnić 
				 * w obliczaniu 
				 */
				
				Set<Entry<LugerCompetitor, LocalTime>> set = r.run.entrySet();	// entry set z czasami aktualnie przetwarzanego ślizgu
				
				Iterator<Entry<LugerCompetitor, LocalTime>> it = set.iterator(); 
								
				do {
					/*
					 * Entry set z mapy nie gwarantuje zachowania kolejności zgodnej z kolejnością
					 * dodawania elementów do mapy ale akurat tutaj nie jest to problem
					 */
					Entry<LugerCompetitor, LocalTime> e = it.next();

					LugerCompetitor k = e.getKey();
					LocalTime v = e.getValue();
					
					/*
					 * Sprawdzanie czy wyjściowa mapa ma już klucz odpowiadający temu zawodnikowi
					 */
					if (totalTimes.containsKey(k)) {
						LocalTime t = totalTimes.get(k);	// wyciąganie już zapisanego czasu ślizgu
						t.plusNanos(v.getNano());			// dodawanie czasu z tego ślizgu
						
						totalTimes.put(k, t);		// zapisywanie na nowo w wyjściowej mapie
					}
					else {
						totalTimes.put(k, v);
					}
				} while (it.hasNext());
			}
		}
		
		return totalTimes;
		
		
	}
	
	public Map<LugerCompetitor, Short> calculateRanksFromTotalRuntimes(Map<LugerCompetitor, LocalTime> rt) {
		
		short rank = 1;
		short shift = 0;
		
		Map<LugerCompetitor, Short> out = new HashMap<LugerCompetitor, Short>();
		
		List<Entry<LugerCompetitor, LocalTime>> list = new LinkedList<>(rt.entrySet());
		
		list.sort(new EntryLugerCmptrLocalTimeComparator());
		
		for (int i = 0; i < list.size(); i++) {
			Entry<LugerCompetitor, LocalTime> current = list.get(i);
			Entry<LugerCompetitor, LocalTime> previous;
			
			if (i > 0)
				previous = list.get(i-1);
			else
				previous = null;
			
			/*
			 * Sprawdzanie czy następny saneczkarz nie ma takiego samego czasu przejazdu
			 */
			if (previous != null) {
				if (current.getValue().equals(previous.getValue())) {
					LugerCompetitor l = current.getKey();	// jeżeli aktualny ma taki sam czas jak poprzedni 
					out.put(l, (short)(rank));
				}
				else {		// jeżeli ma różny
					LugerCompetitor l = current.getKey();
					out.put(l, (short)(++rank));

				}
			}
			else {
				/*
				 * Wstawianie pierwszego
				 */
				LugerCompetitor l = current.getKey();
				out.put(l, (short)(1));
			}
			
		}
		
	
		return out;
	}
	
}
