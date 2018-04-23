package pl.jeleniagora.mks.types;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Vector;

import pl.jeleniagora.mks.serial.TypesConverters;

/**
 * Klasa używana do przechowywania wyników dla poszczególnych ślizgów. 
 * 
 * TODO: Dopisać pole z jego obsługą ustwiające trening/punktowany ślizg
 * 
 * @author mateusz
 *
 */
public class Run {
	
	/**
	 * Mapa łącząca zawodników z ich wynikami końcowymi dla konkretnego ślizgu w konkurencji
	 */
	public Map<LugerCompetitor, LocalTime> run;
	
	/**
	 * Pomocnicza mapa łącząca zawodników z wektorem ich czasów pośrednich. Ostatni element wektora jest to czas ma mecie
	 * czyli łączny czas ślizgu. Zostało to wydzielone do osobnego pola klasy Run aby nieco uprościć konstrukcję metod
	 * aktualizujących główną tabelę wyników w CompManager. Generalnie to nie będą aż tak duże obiekty dlatego zużycie RAM
	 * nie będzie aż tak dramatyczne żeby to był jakiś problem
	 */
	public Map<LugerCompetitor, Vector<LocalTime>> intermediateRunTimes;
	
	@SuppressWarnings("unused")
	private Run() {
		;
	}
	
	public Run(Map<LugerCompetitor, Short> startList) {
		
		run = new HashMap<LugerCompetitor, LocalTime>();

		Iterator<Entry<LugerCompetitor, Short>> it = startList.entrySet().iterator();

		while (it.hasNext()) {
			Entry<LugerCompetitor, Short> entryFromMap = it.next();
			
			LugerCompetitor key = entryFromMap.getKey();
			
			LocalTime zeroTime = LocalTime.of(0, 0, 0, 0);
			
			run.put(key, zeroTime);
		}

	}
	
	public Run(Vector<LugerCompetitor> lugers) {
		
		run = new HashMap<LugerCompetitor, LocalTime>();
		
		Iterator<LugerCompetitor> it = lugers.iterator();
		
		while (it.hasNext()) {
			LugerCompetitor key = it.next();
			
			LocalTime zeroTime = LocalTime.of(0, 0, 0, 0);
			
			run.put(key, zeroTime);
		}
	}
	
	public Run(Vector<LugerCompetitor> lugers, boolean randmize) {
		
		run = new HashMap<LugerCompetitor, LocalTime>();
		
		Iterator<LugerCompetitor> it = lugers.iterator();
		
		while (it.hasNext()) {
			LugerCompetitor key = it.next();
			
			int m = ThreadLocalRandom.current().nextInt(45, 60);
			int msec = ThreadLocalRandom.current().nextInt(2, 40) * TypesConverters.nanoToMilisecScaling;
			
			LocalTime zeroTime = LocalTime.of(0, 0, m, msec);
			
			run.put(key, zeroTime);
		}
	}
	
	/**
	 * Metoda zwraca końcowy czas ślizgu dla konkretnego zawodnika w konkretnym śligu
	 * @param comptr
	 * @param run
	 * @return
	 */
	public Integer getRunTimeForCompetitor(LugerCompetitor comptr) {
		LocalTime t = run.get(comptr);
		
		int nanoTime = t.getNano();
		
		int hndrsMicroTime = nanoTime / (TypesConverters.nanoToMilisecScaling / 10);
		
		hndrsMicroTime += t.getSecond()*10000;
		
		return new Integer(hndrsMicroTime);
	}
	 

}
