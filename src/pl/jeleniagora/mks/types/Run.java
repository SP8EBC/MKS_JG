package pl.jeleniagora.mks.types;

import java.time.LocalTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * Klasa używana do przechowywania wyników dla poszczególnych ślizgów. 
 * @author mateusz
 *
 */
public class Run {
	
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
	 * Mapa łącząca zawodników z ich wynikami dla konkretnej konkurencji
	 */
	Map<LugerCompetitor, LocalTime> run;
}
