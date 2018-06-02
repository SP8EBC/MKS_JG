package pl.jeleniagora.mks.start.order;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;

/**
 * Klasa generuje "zwykłą" kolejność startową tj. zgodną z wylosowanymi nrami startowymi rosnąco
 * @author mateusz
 *
 */
public class SimpleOrder implements StartOrderInterface { 

	public SimpleOrder() {
		
	}

	@Override
	public Short nextStartNumber(Short currentStartNumber, Competition currentCompetition) {
		int startlistSize = currentCompetition.invertedStartList.size(); // ilość elementów w liście startowej czyli liczba zaneczkarzy
		
		if (currentStartNumber < (short)startlistSize)
			return currentStartNumber++;
		
		return null;
	}

	@Override
	public Short nextStartNumber(LugerCompetitor currentStartNumber, Competition currentCompetition) {
		return null;
	}
	
}
