package pl.jeleniagora.mks.factories;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Fabryka służąca do losowania numerów startowych
 * @author mateusz
 *
 */
public class StartListFactory {


	public static void generateStartList(Competition forCompetition) {
		// TODO: Sprawdzenie czy argumenty nie są nullami
		
		int competitorsCnt = forCompetition.competitorsCount;
		/*
		 * Licznik wygenerowanych numerów startowych. Jeżeli zrówna się z wartością competitorsCnt oznacza to, że
		 * wszyscy saneczkarze mają już wygenerowane numery startowe
		 */
		int generatedStartNumbers = 0;
		
		forCompetition.startList = new HashMap<LugerCompetitor, Short>();
		forCompetition.invertedStartList = new HashMap<Short, LugerCompetitor>();
		
		/*
		 * wyciąganie pierwszego ślizgu z konkurencji. jest potrzebny aby mieć listę
		 * wszystkich saneczkarzy
		 */
		Run run = forCompetition.runsTimes.get(0);
		
		/*
		 * set zawierjący wszystkie klucze, czyli listę saneczkarzy w konkurencji dla której
		 * trzeba wylosować numery startowe
		 */
		Set<LugerCompetitor> lugers = run.run.keySet();
		
		/*
		 * Tworzenie wektora na podstawie wyciągniętego z mapy setu. Chodzi o zapewnienie
		 * dostępu swobodnego (tutaj losowego) to elementów mapy
		 */
		Vector<LugerCompetitor> lugersVct = new Vector<LugerCompetitor>(lugers);
		
		do {
			/*
			 * Wybieranie losowego saneczkarza z wektora
			 */
			LugerCompetitor lugerToAssignNum = lugersVct.get(ThreadLocalRandom.current().nextInt(0, lugersVct.size()));
			
			/*
			 * Sprawdzanie czy na liście startowej występuje już ten saneczkarz, czyli czy ma nadany numer startowy.
			 */
			if (forCompetition.startList.containsKey(lugerToAssignNum))
				continue;	// przerwanie tego obiegu pętli i rozpoczęcie następnego
			
			lugerToAssignNum.setStartNumber((short)(generatedStartNumbers+1));
			
			/*
			 * Dodawanie tego saneczkarza do listy startowej. Numery startowe idą po kolei w górę a tak naprawdę losowany jest
			 * saneczkarz, który będzie to każdego z nich przyporządkowany. Ponieważ numer jest cyfrą dodatnią i większą od zera
			 * to do licznika trzeba dodać jedynkę.
			 */
			forCompetition.startList.put(lugerToAssignNum, (short)(generatedStartNumbers+1));
			
			forCompetition.invertedStartList.put((short)(generatedStartNumbers+1), lugerToAssignNum);
			
			generatedStartNumbers++;
		} while (generatedStartNumbers < competitorsCnt);
		
	}
	
	
	public static void generateFixedStartList(Competition forCompetition) {

		forCompetition.startList = new HashMap<LugerCompetitor, Short>();
		forCompetition.invertedStartList = new HashMap<Short, LugerCompetitor>();
		
		Run run = forCompetition.runsTimes.get(0);
		
		Set<LugerCompetitor> lugers = run.run.keySet();
		
		/*
		 * Tworzenie wektora na podstawie wyciągniętego z mapy setu. Chodzi o zapewnienie
		 * dostępu swobodnego (tutaj losowego) to elementów mapy
		 */
		Vector<LugerCompetitor> lugersVct = new Vector<LugerCompetitor>(lugers);
		
		for (int i = 0; i < lugersVct.size(); i++) {
			LugerCompetitor lugerToAssignNum = lugersVct.get(i);
			
			lugerToAssignNum.setStartNumber((short)(i+1));

			forCompetition.startList.put(lugerToAssignNum, (short)(i+1));
			
			forCompetition.invertedStartList.put((short)(i+1), lugerToAssignNum);

		}

	}
}
