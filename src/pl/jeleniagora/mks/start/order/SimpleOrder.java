package pl.jeleniagora.mks.start.order;

import java.time.LocalTime;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa generuje "zwykłą" kolejność startową tj. zgodną z wylosowanymi nrami startowymi rosnąco
 * @author mateusz
 *
 */
public class SimpleOrder implements StartOrderInterface { 

	public SimpleOrder() {
		
	}

	@Override
	public Short nextStartNumber(short currentStartNumber, Competition currentCompetition) {
		int startlistSize = currentCompetition.invertedStartList.size(); // ilość elementów w liście startowej czyli liczba zaneczkarzy
		
		if (currentStartNumber < (short)startlistSize)
			/* Jeżeli na liście startowej jest jeszcze jakis sankarz to po prostu zwróć kolejny numer startowy */
			return ++currentStartNumber;
		else
			/* W przeciwnym razie zwróc null */
			return null;
	}

	@Override
	public Short nextStartNumber(LugerCompetitor currentStartNumber, Competition currentCompetition) {
		int startlistSize = currentCompetition.invertedStartList.size(); // ilość elementów w liście startowej czyli liczba zaneczkarzy
		short currentSN = currentStartNumber.getStartNumber();
		
		if (currentSN < (short)startlistSize)
			/* Jeżeli na liście startowej jest jeszcze jakis sankarz to po prostu zwróć kolejny numer startowy */
			return ++currentSN;
		else
			/* W przeciwnym razie zwróc null */
			return null;
	}

	@Override
	public LugerCompetitor nextStartLuger(LugerCompetitor currentStartNumber, Competition currentCompetition) {
		int startlistSize = currentCompetition.invertedStartList.size(); // ilość elementów w liście startowej czyli liczba zaneczkarzy
		short currentSN = currentStartNumber.getStartNumber();
		
		LugerCompetitor returnValue = null;
		
		if (currentSN < (short)startlistSize) {
			returnValue = currentCompetition.invertedStartList.get((short)(currentSN +1));
		}
		else {
			returnValue = null;
		}
		
		return returnValue;
	}

	@Override
	public LugerCompetitor getFirst(Competition currentCompetition) {
		return currentCompetition.invertedStartList.get((short)(1));
	}

	@Override
	public LugerCompetitor getSecond(Competition currentCompetition) {
		return currentCompetition.invertedStartList.get((short)(2));
	}

	@Override
	public boolean checkIfLastInRun(LugerCompetitor in, Competition currentCompetition, Run currentRun) {
		int numberOfComp = currentCompetition.startList.size();
		short startNumber = in.getStartNumber();	// numer startowy sankarza do sprawdzenia czy jest ostatni
		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		
		boolean output = true;
		
		for (int i = startNumber; i < numberOfComp; i++) {
			//  
			LocalTime v = currentRun.run.get(in);	// wyciąganie czasów ślizgu dla każdego kolejnego za tym to sprawdzenia
			
			if (v.equals(zero)) {
				output = false;
				break;
			}
		}
		
		return output;
	}
	
}
