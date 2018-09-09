package pl.jeleniagora.mks.scoring;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.DNS;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

public class CalculatePartialRanks {
	
	/**
	 * Ta klasa służy do obliczania lokat cząstkowych w bierzącym ślizgu bez odniesienia do poprzednich.
	 * 
	 * @param comps
	 * @param currentRun
	 * @return
	 */
	public Map<LugerCompetitor, Short> calculatePartialRanksInRun(Competition comps, Run currentRun) {
		
		short rank = 0, lastRank = 1;
		short shift = 0;
		LocalTime m = LocalTime.of(0, 59);
				
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
			
			if (current.getValue().isAfter(m)) {
				// DNS DSQ DNF
				out.put(current.getKey(), new Short((short) 999));
				continue;
			}
			
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
	
	public Map<LugerCompetitor, Short> calculatePartialRanks(Competition comps, Run currentRun)	{
		if (comps.isCurrentRunTraining && comps.trainingOrContest) {
			Map<LugerCompetitor, LocalTime> total = calculateTotalRuntimeInTraining(comps, currentRun);
			Map<LugerCompetitor, Short> out = calculateRanksFromMap(total);

			comps.partialRanks = out;
			return out;

		}
		else {
			Map<LugerCompetitor, LocalTime> total = calculateTotalRuntimeInScored(comps, currentRun);
			Map<LugerCompetitor, Short> out = calculateRanksFromMap(total);
			
			comps.partialRanks = out;
			return out;
		}

	}
	
	/**
	 * Metoda podobna do calculateTotalRuntimeInScored ale operująca wyłącznie na ślizgach / zjazdach
	 * treningowych
	 * @param comp
	 * @param currentRun
	 * @return
	 */
	private Map<LugerCompetitor, LocalTime> calculateTotalRuntimeInTraining(Competition comp, Run currentRun) {

		/*
		 * Map łącząca numer startowy saneczkarza z jego łącznym czasem przejazdu od pierwszego do aktualnego ślizgu 
		 */
		Map<LugerCompetitor, LocalTime> totalTimes = new HashMap<LugerCompetitor, LocalTime>();
		
		LocalTime m = LocalTime.of(0, 59);
		
		for (Run r : comp.runsTimes) {
				
			if (!r.trainingOrScored) {
				/*
				 * Obliczanie czasów tylko dla ślizgów treningowych bez względu na tryb zawodów
				 */
				
				Set<Entry<LugerCompetitor, LocalTime>> set = r.totalTimes.entrySet();	// entry set z czasami aktualnie przetwarzanego ślizgu
				
				Iterator<Entry<LugerCompetitor, LocalTime>> it = set.iterator(); 
								
				do {
					/*
					 * Entry set z mapy nie gwarantuje zachowania kolejności zgodnej z kolejnością
					 * dodawania elementów do mapy ale akurat tutaj nie jest to problem
					 */
					Entry<LugerCompetitor, LocalTime> e = it.next();

					LugerCompetitor k = e.getKey();	// wyciąganie saneczkarza
					LocalTime v = e.getValue();		// i jego czasu przejazdu
										
					if (v == null || v.equals(LocalTime.of(0, 0, 0, 0)) )
						v = DNS.getValue().plusMinutes(1); // jeżeli saneczkarz jeszcze nie jechał
											// to tymczasowo dopisz DNS
					
					if (v.isAfter(m)) {
						// zawodnicy z zapisanymi DNS/DSQ/DNF
						v = v.plusHours(1);
					}

					
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
			
			if (r.equals(currentRun)) {
				// zakończ przetwarzanie na aktualnym ślizgu
				break;
			}
		}
		
		return totalTimes;
		
	}
	
	
	/**
	 * Metoda sumująca czasy ślizgu w punktowanych zdolna to operowania na niepełnych ślizgach. 
	 * Dla zawodników którzy jeszcze nie jechali przyjmuje DNS. Dzięki temu zawodnicy którzy 
	 * jeszcze nie jechali będa mieli "wirtualne czasy" zawsze gorsze od tych, którzy już 
	 * ukończyli zjazd/ślizg co pozwoli zachowac poprawną kolejność
	 * @param comp
	 * @return
	 */
	private Map<LugerCompetitor, LocalTime> calculateTotalRuntimeInScored(Competition comp, Run currentRun) {

		/*
		 * Map łącząca numer startowy saneczkarza z jego łącznym czasem przejazdu od pierwszego do aktualnego ślizgu 
		 */
		Map<LugerCompetitor, LocalTime> totalTimes = new HashMap<LugerCompetitor, LocalTime>();
		
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

					LugerCompetitor k = e.getKey();	// wyciąganie saneczkarza
					LocalTime v = e.getValue();		// i jego czasu przejazdu
					
					if (v.equals(LocalTime.of(0, 0, 0, 0)) || v == null)
						v = DNS.getValue().plusMinutes(1); // jeżeli saneczkarz jeszcze nie jechał
											// to tymczasowo dopisz DNS
						
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
			
			if (r.equals(currentRun)) {
				// zakończ przetwarzanie na aktualnym ślizgu
				break;
			}
		}
		
		return totalTimes;
		
	}
	
	/**
	 * Ta metoda służy do obliczania 
	 * @param rt
	 * @return
	 */
	private Map<LugerCompetitor, Short> calculateRanksFromMap(Map<LugerCompetitor, LocalTime> rt) {
		
		short rank = 1, lastRank = 1;
		short shift = 0;
		
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
			
			if (i > 0)
				previous = list.get(i-1);
			else
				previous = null;
			
			
			if (current.getValue().isAfter(DNS.getValue())) {
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
