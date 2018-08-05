package pl.jeleniagora.mks.scoring;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

public class CalculatePartialRanks {
	
	public Map<LugerCompetitor, Short> calculatePartialRanks(Competition comps, Run currentRun) {
		
		short rank = 0, lastRank = 1;
		short shift = 0;
				
		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		
		Map<LugerCompetitor, Short> out = new HashMap<LugerCompetitor, Short>();

		/*
		 * Wyciąganie listy zwierajcej czasy zjazdu / ślizgu każdego zawodnika
		 */
		List<Entry<LugerCompetitor, LocalTime>> list = new LinkedList<>(currentRun.totalTimes.entrySet());

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
				
				/*
				 * Sprawdzanie czy aktualny zawodnik nie ma zapisanego czasu zero albo nie ma zapisanego żadnego
				 * chodzi o omijanie tych którzy jeszcze nie jechali
				 */
				if (current.getValue().equals(zero) ||
						current.getValue() == null)
					continue;
				
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
				if (current.getValue().equals(zero) ||
						current.getValue() == null)
					continue;
				
				/*
				 * Wstawianie pierwszego
				 */
				lastRank = 1;
				LugerCompetitor l = current.getKey();
				out.put(l, (short)(1));
			}
			
		}
		
		comps.partialRanks = out;
		return out;
		
	}
}
