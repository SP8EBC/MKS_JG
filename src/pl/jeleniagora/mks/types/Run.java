package pl.jeleniagora.mks.types;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.util.Vector;

import pl.jeleniagora.mks.files.xml.adapters.TotalTimesMapAdapter;
import pl.jeleniagora.mks.serial.TypesConverters;

/**
 * Klasa używana do przechowywania wyników dla poszczególnych ślizgów. 
 * 
 * @author mateusz
 *
 */
@XmlRootElement
public class Run {
	
	/**
	 * Numer kolejny ślizgu w konkurencji. Liczony po programistycznemu, tj. od zera
	 */
	public short number;
	
	/**
	 * Unikatowy, losowo generowany numer seryjny ślizgu
	 */
	@XmlElement(required = true, nillable =  true)
	public long serialNumber;
	
	/**
	 * Numer kolejny ślizgu w ślizgach treningowych i punktowanych. Liczony po programistyczny, czyli np zerowy
	 * ślizg treningowy, pierwszy ślizg treningowy a potem zerowy ślizg punktowny, pierwszy punktowany itp.
	 */
	public short numberInScoredOrTrainingRuns;
	
	/**
	 * Ustawianie na true jeżeli ślizg się zakończył w 100%
	 */
	public boolean done;
	
	/**
	 * Ustawienie na true powoduje traktowanie tego ślizgu jako ślizgu punktowanego a nie treningowego.
	 */
	public boolean trainingOrScored;
	
	/**
	 * Mapa łącząca zawodników z ich wynikami końcowymi dla konkretnego ślizgu w konkurencji
	 */
	@XmlElement(name = "totalTimes")
	@XmlJavaTypeAdapter(value = TotalTimesMapAdapter.class)
	public Map<LugerCompetitor, LocalTime> totalTimes;
	
	/**
	 * Pomocnicza mapa łącząca zawodników z wektorem ich czasów pośrednich. Ostatni element wektora jest to czas ma mecie
	 * czyli łączny czas ślizgu. Zostało to wydzielone do osobnego pola klasy Run aby nieco uprościć konstrukcję metod
	 * aktualizujących główną tabelę wyników w CompManager. Generalnie to nie będą aż tak duże obiekty dlatego zużycie RAM
	 * nie będzie aż tak dramatyczne żeby to był jakiś problem
	 */
	@XmlTransient
	public Map<LugerCompetitor, Vector<LocalTime>> intermediateRunTimes;
	
	@Override
	public String toString() {
		if (trainingOrScored) {
			return "Ślizg punktowny numer " + (numberInScoredOrTrainingRuns + 1);
		}
		else
			return "Ślizg treningowy numer " + (numberInScoredOrTrainingRuns + 1);
	}
	
	@SuppressWarnings("unused")
	private Run() {
		;
	}
	
	public Run(Map<LugerCompetitor, Short> startList, byte trainingOrScored) {
		
		serialNumber = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
		
		totalTimes = new HashMap<LugerCompetitor, LocalTime>();

		Iterator<Entry<LugerCompetitor, Short>> it = startList.entrySet().iterator();
		
		if (trainingOrScored == 1)
			this.trainingOrScored = true;
		else
			this.trainingOrScored = false;
		
		while (it.hasNext()) {
			Entry<LugerCompetitor, Short> entryFromMap = it.next();
			
			LugerCompetitor key = entryFromMap.getKey();
			
			LocalTime zeroTime = LocalTime.of(0, 0, 0, 0);
			
			totalTimes.put(key, zeroTime);
		}

	}
	
	public Run(Vector<LugerCompetitor> lugers, byte trainingOrScored) {
		
		serialNumber = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
		
		totalTimes = new HashMap<LugerCompetitor, LocalTime>();
		
		Iterator<LugerCompetitor> it = lugers.iterator();
		
		if (trainingOrScored == 1)
			this.trainingOrScored = true;
		else
			this.trainingOrScored = false;
		
		while (it.hasNext()) {
			LugerCompetitor key = it.next();
			
			LocalTime zeroTime = LocalTime.of(0, 0, 0, 0);
			
			totalTimes.put(key, zeroTime);
		}
	}
	
	public Run(Vector<LugerCompetitor> lugers,  boolean randmize, byte trainingOrScored) {
		
		serialNumber = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
		
		totalTimes = new HashMap<LugerCompetitor, LocalTime>();
		
		Iterator<LugerCompetitor> it = lugers.iterator();
		
		if (trainingOrScored == 1)
			this.trainingOrScored = true;
		else
			this.trainingOrScored = false;
		
		while (it.hasNext()) {
			LugerCompetitor key = it.next();
			
			int m = ThreadLocalRandom.current().nextInt(45, 60);
			int msec = ThreadLocalRandom.current().nextInt(2, 40) * TypesConverters.nanoToMilisecScaling;
			
			LocalTime zeroTime = LocalTime.of(0, 0, m, msec);
			
			totalTimes.put(key, zeroTime);
		}
	}
	
	/**
	 * Metoda zwraca końcowy czas ślizgu dla konkretnego zawodnika w konkretnym śligu
	 * @param comptr
	 * @param totalTimes
	 * @return
	 */
	public Integer getRunTimeForCompetitor(LugerCompetitor comptr) {
		LocalTime t = totalTimes.get(comptr);
		
		long nanoTime = t.getNano();			/// error here
		
		long hndrsMicroTime = nanoTime / (TypesConverters.nanoToMilisecScaling / 10);
		
		hndrsMicroTime += t.getSecond()*10000;
		hndrsMicroTime += t.getMinute()*600000;
		hndrsMicroTime += t.getHour()*36000000;

		
		return new Integer((int) hndrsMicroTime);
	}
	
	/**
	 * Metoda dokonuje konwersji hashmapy przechowywujących wyniki saneczkarzy na wektor posortowany względem numerów startowych.
	 * @return
	 */
	public Vector<LocalTime> getVectorWithRuntimes(Map<Short, LugerCompetitor> list) {
		
		if (list == null)
			return null;
		
		Vector<LocalTime> out = new Vector<LocalTime>();
		
		/*
		 *	Tu należy pamiętać że numery startowe idą nie po programistycznemu!!!! 
		 */
		int numberOfCmptrs = totalTimes.size();
		
		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		
		for (int i = 1; i <= numberOfCmptrs; i++ ) {
			LocalTime time = this.totalTimes.get(list.get((short)i));
			if (time == null) {
				out.add(zero);
			}
			else {
				out.add(time);
			}
		}
		
		return out;
	}
	 

}
