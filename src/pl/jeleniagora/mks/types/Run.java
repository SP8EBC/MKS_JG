package pl.jeleniagora.mks.types;

import java.time.LocalTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import pl.jeleniagora.mks.serial.TypesConverters;

/**
 * Klasa używana do przechowywania wyników dla poszczególnych ślizgów. 
 * @author mateusz
 *
 */
public class Run {
	
	/**
	 * Mapa łącząca zawodników z ich wynikami końcowymi dla konkretnego ślizgu w konkurencji
	 */
	Map<LugerCompetitor, LocalTime> run;
	
	/**
	 * Pomocnicza mapa łącząca zawodników z wektorem ich czasów pośrednich. Ostatni element wektora jest to czas ma mecie
	 * czyli łączny czas ślizgu. Zostało to wydzielone do osobnego pola klasy Run aby nieco uprościć konstrukcję metod
	 * aktualizujących główną tabelę wyników w CompManager. Generalnie to nie będą aż tak duże obiekty dlatego zużycie RAM
	 * nie będzie aż tak dramatyczne żeby to był jakiś problem
	 */
	Map<LugerCompetitor, Vector<LocalTime>> intermediateRunTimes;
	
	@SuppressWarnings("unused")
	private Run() {
		;
	}
	
	public Run(Map<LugerCompetitor, Short> startList) {

		Iterator<Entry<LugerCompetitor, Short>> it = startList.entrySet().iterator();

		while (it.hasNext()) {
			Entry<LugerCompetitor, Short> entryFromMap = it.next();
			
			LugerCompetitor key = entryFromMap.getKey();
			
			LocalTime zeroTime = LocalTime.of(0, 0, 0, 0);
			
			run.put(key, zeroTime);
		}

	}
	
	public Run(Vector<LugerCompetitor> lugers) {
		
		Iterator<LugerCompetitor> it = lugers.iterator();
		
		while (it.hasNext()) {
			LugerCompetitor key = it.next();
			
			LocalTime zeroTime = LocalTime.of(0, 0, 0, 0);
			
			run.put(key, zeroTime);
		}
	}
	
	/**
	 * Metoda zwraca końcowy czas ślizgu dla konkretnego zawodnika w konkretnym śligu
	 * @param comptr
	 * @param run
	 * @return
	 */
	public Short getRunTimeForCompetitor(LugerCompetitor comptr) {
		LocalTime t = run.get(comptr);
		
		int nanoTime = t.getNano();
		
		int hndrsMicroTime = nanoTime / (TypesConverters.nanoToMilisecScaling / 10);
		
		return new Short((short) hndrsMicroTime);
	}
	 

}
